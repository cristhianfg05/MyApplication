package com.example.myapplication;

import android.app.Activity;
import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.media.MediaPlayer;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 1;


    private GLSurfaceView glView;   // Use GLSurfaceView
    private MyGLRenderer myGLRenderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 720;
    private float previousX;
    private float previousY;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private float[] angularSpeed = new float[3]; // Velocidades angulares en los ejes X, Y, Z
    float[] rotationMatrix = new float[9]; // Matriz de rotación
    float[] gravity = new float[]{0f, 9.80665f, 0f};
    float[] magnetometerReading = new float[3];
    private float[] orientationValues = new float[3]; // Orientación en los ejes azimut, inclinación, y balanceo



    // Call back when the activity is started, to initialize the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        Toast.makeText(MainActivity.this, "pulsa H para comenzar", Toast.LENGTH_SHORT).show();
        glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
        glView.setRenderer(myGLRenderer=new MyGLRenderer(this)); // Use a custom renderer
        this.setContentView(glView);                // This activity sets to GLSurfaceView

        // Inicializar el SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Verificar si el dispositivo tiene giroscopio
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor == null) {
            // El dispositivo no tiene giroscopio, muestra un mensaje o toma alguna acción
            Toast.makeText(this, "El dispositivo no tiene giroscopio", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad
        }

    }

    // Call back when the activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
        sensorManager.unregisterListener(this);
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                System.out.println("toca");

                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation above the mid-line
                if (y >  myGLRenderer.getHeight() / 2) {
                    dx = dx * -1;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < myGLRenderer.getWidth() / 2) {
                    dy = dy * -1;
                }

                myGLRenderer.setzCam(myGLRenderer.getzCam()+(dx + dy) * TOUCH_SCALE_FACTOR);
        }

        previousX = x;
        previousY = y;
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch (keyCode) {
            case KeyEvent.KEYCODE_W:
                myGLRenderer.movePOVForward();
                return true;
            case KeyEvent.KEYCODE_S:
                myGLRenderer.movePOVBackward();
                return true;
            case KeyEvent.KEYCODE_A:

                myGLRenderer.movePOVLeft();
                return true;
            case KeyEvent.KEYCODE_D:
                myGLRenderer.movePOVRight();
                return true;
            case KeyEvent.KEYCODE_C:
                myGLRenderer.cam = !myGLRenderer.cam;
                return true;
            case KeyEvent.KEYCODE_F:
                myGLRenderer.cajaMaderaExiste = false;
                return true;
            case KeyEvent.KEYCODE_H:
                myGLRenderer.menu = false;
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                myGLRenderer.moveCameraUp();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                myGLRenderer.moveCameraDown();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                myGLRenderer.moveCameraLeft();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                myGLRenderer.moveCameraRight();
                return true;
            case KeyEvent.KEYCODE_SPACE:
                myGLRenderer.jump();
                return true;
            case KeyEvent.KEYCODE_N:
                Log.d("tecla", "onKeyDown: N");
                myGLRenderer.niebla = !myGLRenderer.niebla;
                Log.d("tecla", myGLRenderer.niebla+"");
                return true;
            case KeyEvent.KEYCODE_P:
                if(myGLRenderer.doomBG.isPlaying()){
                    myGLRenderer.doomBG.pause();
                    myGLRenderer.currentPos = myGLRenderer.doomBG.getCurrentPosition();
                } else {
                    myGLRenderer.doomBG.seekTo(myGLRenderer.currentPos);
                    myGLRenderer.doomBG.start();
                }
                return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, angularSpeed,
                    0, angularSpeed.length);

            updateOrientationAngles();

            //myGLRenderer.updateCamera(angularSpeed);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                angularSpeed, magnetometerReading);

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, angularSpeed);

        Log.d("Gyroscope", "Valores : "+ Arrays.toString(angularSpeed));

        // "orientationAngles" now has up-to-date information.
    }
}