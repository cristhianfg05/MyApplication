package com.example.myapplication;

import static android.app.ProgressDialog.show;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.Console;

/**
 * OpenGL Custom renderer used with GLSurfaceView
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {


    private float width, height;


    Context context;   // Application's context

    private Light light;
    boolean cam = true;
    boolean cajaMaderaExiste = true;

    //MENU
    private TextureCube menuHUD;
    private TextureCube hud;
    private TextureCube caraDoom;
    int frame = 0;
    int k = 0;

    /**
     * listaObjetos[0] --> altar_obj
     * listaObjetos[1] --> caja_madera_obj
     * listaObjetos[2] --> caja_secreto_obj
     * listaObjetos[3] --> caja1_obj
     * listaObjetos[4] --> caja2_obj
     * listaObjetos[5] --> caja3_obj
     * listaObjetos[6] --> caja4_obj
     * listaObjetos[7] --> caja5_obj
     * listaObjetos[8] --> caja6_obj
     * listaObjetos[9] --> caja7_obj
     * listaObjetos[10] --> caja8_objsss
     * listaObjetos[11] --> cartel_a1_obj
     * listaObjetos[12] --> cartel_a2_obj
     * listaObjetos[13] --> cartel_h1_obj
     * listaObjetos[14] --> cartel_h2_obj
     * listaObjetos[15] --> cartel_h3_obj
     * listaObjetos[16] --> cartel_h4_obj
     * listaObjetos[17] --> cartel_obj
     * listaObjetos[18] --> cuadro_h_obj
     * listaObjetos[19] --> cuadro_h2_obj
     * listaObjetos[20] --> diente_obj
     * listaObjetos[21] --> fondo_obj
     * listaObjetos[22] --> guante_obj
     * listaObjetos[23] --> lava_obj
     * listaObjetos[24] --> ojo_obj
     * listaObjetos[25] --> pato_obj
     * listaObjetos[26] --> piel_obj
     * listaObjetos[27] --> pistola_obj
     * listaObjetos[28] --> planeta1_obj
     * listaObjetos[29] --> planeta2_obj
     * listaObjetos[30] --> planeta3_obj
     * listaObjetos[31] --> planeta4_obj
     * listaObjetos[32] --> planeta5_obj
     * listaObjetos[33] --> puerta_falsa1_obj
     * listaObjetos[34] --> puerta_falsa2_obj
     * listaObjetos[35] --> puerta_falsa3_obj
     * listaObjetos[36] --> puerta1_obj
     * listaObjetos[37] --> puerta2_obj
     * listaObjetos[38] --> puerta3_obj
     * listaObjetos[39] --> puerta4_obj
     * listaObjetos[40] --> puerta5_obj
     * listaObjetos[41] --> sala1_obj
     * listaObjetos[42] --> sala2_obj
     * listaObjetos[43] --> sala3_obj
     * listaObjetos[44] --> sala4_obj
     * listaObjetos[45] --> sala5_obj
     * listaObjetos[46] --> sala6_obj
     * listaObjetos[47] --> suelo1_obj
     * listaObjetos[48] --> suelo2_obj
     * listaObjetos[49] --> suelo3_obj
     * listaObjetos[50] --> suelo4_obj
     */
    Object3D[] listaObjetos;

    boolean menu = true;


    public float zCam = 0;
    int currentPos = 0;

    public float xCamPos = 0;
    public float yCamPos = 0.9f;
    public float zCamPos = 3;
    public float xCamFront = 0;
    public float yCamFront = 0;
    public float zCamFront = -1; // Ejemplo de posición inicial de la cámara

    private float cameraSpeed = 0.4f;
    float verticalMovement = 0.0f;

    public float pitch = 0.0f;
    public float yaw = -90.0f;

    private static boolean isJumping = false;
    private static float jumpHeight = 1.0f;
    private static float jumpVelocity = 0.0f;
    private static float gravity = 0.005f;
    float[] fogColor = {0f, 0f, 0f, 0.7f};

    MediaPlayer doomBG;
    boolean niebla = true;


    // Constructor with global application context
    public MyGLRenderer(Context context) {
        this.context = context;
        this.listaObjetos = new Object3D[100];
        this.listaObjetos[0] = new Object3D(context, R.raw.altar_obj);
        this.listaObjetos[1] = new Object3D(context, R.raw.caja_madera_obj);
        this.listaObjetos[2] = new Object3D(context, R.raw.caja_secreto_obj);
        this.listaObjetos[3] = new Object3D(context, R.raw.caja1_obj);
        this.listaObjetos[4] = new Object3D(context, R.raw.caja2_obj);
        this.listaObjetos[5] = new Object3D(context, R.raw.caja3_obj);
        this.listaObjetos[6] = new Object3D(context, R.raw.caja4_obj);
        this.listaObjetos[7] = new Object3D(context, R.raw.caja5_obj);
        this.listaObjetos[8] = new Object3D(context, R.raw.caja6_obj);
        this.listaObjetos[9] = new Object3D(context, R.raw.caja7_obj);
        this.listaObjetos[10] = new Object3D(context, R.raw.caja8_obj);
        this.listaObjetos[11] = new Object3D(context, R.raw.cartel_a1_obj);
        this.listaObjetos[12] = new Object3D(context, R.raw.cartel_a2_obj);
        this.listaObjetos[13] = new Object3D(context, R.raw.cartel_h1_obj);
        this.listaObjetos[14] = new Object3D(context, R.raw.cartel_h2_obj);
        this.listaObjetos[15] = new Object3D(context, R.raw.cartel_h3_obj);
        this.listaObjetos[16] = new Object3D(context, R.raw.cartel_h4_obj);
        this.listaObjetos[17] = new Object3D(context, R.raw.cartel_obj);
        this.listaObjetos[18] = new Object3D(context, R.raw.cuadro_h_obj);
        this.listaObjetos[19] = new Object3D(context, R.raw.cuadro_h2_obj);
        this.listaObjetos[20] = new Object3D(context, R.raw.diente_obj);
        this.listaObjetos[21] = new Object3D(context, R.raw.fondo_obj);
        this.listaObjetos[22] = new Object3D(context, R.raw.guante_obj);
        this.listaObjetos[23] = new Object3D(context, R.raw.lava_obj);
        this.listaObjetos[24] = new Object3D(context, R.raw.ojo_obj);
        this.listaObjetos[25] = new Object3D(context, R.raw.pato_obj);
        this.listaObjetos[26] = new Object3D(context, R.raw.piel_obj);
        this.listaObjetos[27] = new Object3D(context, R.raw.pistola_obj);
        this.listaObjetos[28] = new Object3D(context, R.raw.planeta1_obj);
        this.listaObjetos[29] = new Object3D(context, R.raw.planeta2_obj);
        this.listaObjetos[30] = new Object3D(context, R.raw.planeta3_obj);
        this.listaObjetos[31] = new Object3D(context, R.raw.planeta4_obj);
        this.listaObjetos[32] = new Object3D(context, R.raw.planeta5_obj);
        this.listaObjetos[33] = new Object3D(context, R.raw.puerta_falsa1_obj);
        this.listaObjetos[34] = new Object3D(context, R.raw.puerta_falsa2_obj);
        this.listaObjetos[35] = new Object3D(context, R.raw.puerta_falsa3_obj);
        this.listaObjetos[36] = new Object3D(context, R.raw.puerta1_obj);
        this.listaObjetos[37] = new Object3D(context, R.raw.puerta2_obj);
        this.listaObjetos[38] = new Object3D(context, R.raw.puerta3_obj);
        this.listaObjetos[39] = new Object3D(context, R.raw.puerta4_obj);
        this.listaObjetos[40] = new Object3D(context, R.raw.puerta5_obj);
        this.listaObjetos[41] = new Object3D(context, R.raw.sala1_obj);
        this.listaObjetos[42] = new Object3D(context, R.raw.sala2_obj);
        this.listaObjetos[43] = new Object3D(context, R.raw.sala3_obj);
        this.listaObjetos[44] = new Object3D(context, R.raw.sala4_obj);
        this.listaObjetos[45] = new Object3D(context, R.raw.sala5_obj);
        this.listaObjetos[46] = new Object3D(context, R.raw.sala6_obj);
        this.listaObjetos[47] = new Object3D(context, R.raw.suelo1_obj);
        this.listaObjetos[48] = new Object3D(context, R.raw.suelo2_obj);
        this.listaObjetos[49] = new Object3D(context, R.raw.suelo3_obj);
        this.listaObjetos[50] = new Object3D(context, R.raw.suelo4_obj);
        this.listaObjetos[51] = new Object3D(context, R.raw.controles_obj);
        doomBG = MediaPlayer.create(context, R.raw.doom);

    }


    // Call back when the surface is first created or re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        gl.glEnable(GL10.GL_LIGHTING);

        // Enable Normalize
        gl.glEnable(GL10.GL_NORMALIZE);


        light = new Light(gl, GL10.GL_LIGHT0);
        light.setPosition(new float[]{0.0f, 0f, 1, 0.0f});

        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f});
        light.setDiffuseColor(new float[]{1, 1, 1});

        //TEXTURAS
        gl.glEnable(GL10.GL_TEXTURE_2D);
        this.listaObjetos[0].loadTexture(gl, context, R.raw.altar);
        this.listaObjetos[1].loadTexture(gl, context, R.raw.caja_madera);
        this.listaObjetos[2].loadTexture(gl, context, R.raw.caja_secreto);
        this.listaObjetos[3].loadTexture(gl, context, R.raw.caja);
        this.listaObjetos[4].loadTexture(gl, context, R.raw.caja2);
        this.listaObjetos[5].loadTexture(gl, context, R.raw.caja3);
        this.listaObjetos[6].loadTexture(gl, context, R.raw.caja4);
        this.listaObjetos[7].loadTexture(gl, context, R.raw.caja5);
        this.listaObjetos[8].loadTexture(gl, context, R.raw.caja6);
        this.listaObjetos[9].loadTexture(gl, context, R.raw.caja7);
        this.listaObjetos[10].loadTexture(gl, context, R.raw.caja8);
        this.listaObjetos[11].loadTexture(gl, context, R.raw.cartel_a);
        this.listaObjetos[12].loadTexture(gl, context, R.raw.cartel_a);
        this.listaObjetos[13].loadTexture(gl, context, R.raw.cartel_h);
        this.listaObjetos[14].loadTexture(gl, context, R.raw.cartel_h);
        this.listaObjetos[15].loadTexture(gl, context, R.raw.cartel_h);
        this.listaObjetos[16].loadTexture(gl, context, R.raw.cartel_h);
        this.listaObjetos[17].loadTexture(gl, context, R.raw.cartel);
        this.listaObjetos[18].loadTexture(gl, context, R.raw.cuadro_h);
        this.listaObjetos[19].loadTexture(gl, context, R.raw.cuadro_h2);
        this.listaObjetos[20].loadTexture(gl, context, R.raw.diente);
        this.listaObjetos[21].loadTexture(gl, context, R.raw.fondo);
        this.listaObjetos[22].loadTexture(gl, context, R.raw.guante_tex);
        this.listaObjetos[23].loadTexture(gl, context, R.raw.lava);
        this.listaObjetos[24].loadTexture(gl, context, R.raw.ojo);
        this.listaObjetos[25].loadTexture(gl, context, R.raw.pato);
        this.listaObjetos[26].loadTexture(gl, context, R.raw.piel);
        this.listaObjetos[27].loadTexture(gl, context, R.raw.pistola);
        this.listaObjetos[28].loadTexture(gl, context, R.raw.planeta);
        this.listaObjetos[29].loadTexture(gl, context, R.raw.planeta2);
        this.listaObjetos[30].loadTexture(gl, context, R.raw.planeta3);
        this.listaObjetos[31].loadTexture(gl, context, R.raw.planeta4);
        this.listaObjetos[32].loadTexture(gl, context, R.raw.planeta5);
        this.listaObjetos[33].loadTexture(gl, context, R.raw.puerta_falsa);
        this.listaObjetos[34].loadTexture(gl, context, R.raw.puerta_falsa);
        this.listaObjetos[35].loadTexture(gl, context, R.raw.puerta_falsa);
        this.listaObjetos[36].loadTexture(gl, context, R.raw.puerta);
        this.listaObjetos[37].loadTexture(gl, context, R.raw.puerta);
        this.listaObjetos[38].loadTexture(gl, context, R.raw.puerta);
        this.listaObjetos[39].loadTexture(gl, context, R.raw.puerta);
        this.listaObjetos[40].loadTexture(gl, context, R.raw.puerta);
        this.listaObjetos[41].loadTexture(gl, context, R.raw.sala1);
        this.listaObjetos[42].loadTexture(gl, context, R.raw.sala2);
        this.listaObjetos[43].loadTexture(gl, context, R.raw.sala3);
        this.listaObjetos[44].loadTexture(gl, context, R.raw.sala4);
        this.listaObjetos[45].loadTexture(gl, context, R.raw.sala5);
        this.listaObjetos[46].loadTexture(gl, context, R.raw.sala5);
        this.listaObjetos[47].loadTexture(gl, context, R.raw.suelo1);
        this.listaObjetos[48].loadTexture(gl, context, R.raw.suelo2);
        this.listaObjetos[49].loadTexture(gl, context, R.raw.suelo3);
        this.listaObjetos[50].loadTexture(gl, context, R.raw.suelo4);
        this.listaObjetos[51].loadTexture(gl, context, R.raw.controles);

        caraDoom = new TextureCube();
        menuHUD = new TextureCube();
        hud = new TextureCube();
        menuHUD.loadTexture(gl, context, R.raw.menu_hud);


        doomBG.setLooping(true);
        doomBG.start();
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float) width / height;

        this.width = width;
        this.height = height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        //gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        //gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection

        float near = 1.0f; // Distancia cercana
        float far = 4.0f; // Distancia lejana

        // Configura la matriz de proyección
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-1.0f, 1.0f, -1.0f, 1.0f, near, far);
        GLU.gluPerspective(gl, 60, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {


        setPerspectiveProjection(gl);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        if (cam) {
            GLU.gluLookAt(gl, xCamPos, yCamPos, zCamPos, xCamPos + xCamFront, yCamPos + yCamFront, zCamPos + zCamFront, 0f, 1f, 0f);
        } else {
            setCameraPositionAndLookAt(0f, 10f, 5f, gl);
        }


        updateCameraVectors();
        if (menu) {
            setOrthographicProjection(gl);
            gl.glPushMatrix();
            gl.glScalef(4f, 4f, 4f);
            menuHUD.draw(gl);
            gl.glPopMatrix();
        } else {


            if (!niebla) {
                gl.glEnable(GL10.GL_FOG);
                gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
                gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
                gl.glFogf(GL10.GL_FOG_START, 7.0f);  // Ajusta la distancia inicial de la niebla
                gl.glFogf(GL10.GL_FOG_END, 10.0f);
            } else {
                gl.glDisable(GL10.GL_FOG);
            }
            frame = (frame+1)%46;
            if(frame %7 == 0){
                k = (k+1)%5;
            }
            for (int i = 0; i < 52; i++) {
                gl.glPushMatrix();
                if (i >= 36 && i <= 40) {
                    if (frame < 23) {
                        verticalMovement += 0.01f;
                    } else {
                        verticalMovement -= 0.01f;
                    }
                    gl.glTranslatef(0.0f, -verticalMovement, 0.0f);
                    this.listaObjetos[i].draw(gl);
                } else if (i == 23 || i == 44 || (i >= 28 && i <= 32)) {
                    this.listaObjetos[i].shiftUVsH(0.01f);
                    this.listaObjetos[i].draw(gl);
                }else if(i == 21){
                    this.listaObjetos[i].shiftUVsH(0.0005f);
                    this.listaObjetos[i].draw(gl);
                } else if (i == 1) {
                    if (cajaMaderaExiste)
                        this.listaObjetos[i].draw(gl);
                } else {
                    this.listaObjetos[i].draw(gl);
                }
                gl.glPopMatrix();
            }

            //Cargar textura del hud

            hud.loadTexture(gl, context, R.raw.doom_hud);

            gl.glDisable(GL10.GL_FOG);
            // Dibujar el HUD
            setOrthographicProjection(gl);
            gl.glPushMatrix();
            gl.glScalef(3.5f, 0.5f, 1f);
            gl.glTranslatef(0f, -7f, 0f);
            hud.draw(gl);
            gl.glPopMatrix();



            cargarCaraDoom(gl);


            gl.glPushMatrix();
            gl.glScalef(0.4f, 0.5f, 1f);
            gl.glTranslatef(0f, -7f, 0f);
            caraDoom.draw(gl);
            gl.glPopMatrix();

        }
    }

    public void cargarCaraDoom(GL10 gl) {
        if (k == 0) {
            caraDoom.loadTexture(gl, context,R.raw.d1);
        } else if (k == 1) {
            caraDoom.loadTexture(gl, context,R.raw.d2);
        } else if (k == 2) {
            caraDoom.loadTexture(gl, context,R.raw.d3);
        } else if (k == 3) {
            caraDoom.loadTexture(gl, context,R.raw.d4);
        }else {
            caraDoom.loadTexture(gl, context,R.raw.d5);
        }

    }

    public void setCameraPositionAndLookAt(float xObjeto, float yObjeto, float zObjeto, GL10 gl) {
        // Ajusta estas constantes según tus preferencias
        float alturaCamara = 2.0f;  // Altura adicional sobre el objeto
        float distanciaMirada = 5.0f;  // Distancia de la cámara al objeto

        // Calcula las nuevas coordenadas de la cámara
        float xCamara = xObjeto;
        float yCamara = yObjeto + alturaCamara;
        float zCamara = zObjeto + distanciaMirada;

        // Establece la posición de la cámara
        GLU.gluLookAt(gl, xCamara, yCamara, zCamara, xObjeto, yObjeto, zObjeto, 0f, 1f, 0f);

        // También puedes ajustar otros parámetros de la cámara según tus necesidades
        // como la orientación, el ángulo de visión, etc.
    }

    public void destruyeCaja() {
        cajaMaderaExiste = false;
    }


    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public float getzCam() {
        return 0;
    }

    public void setzCam(float zCam) {
        this.zCam = zCam;
    }


    public void movePOVForward() {
        xCamPos += cameraSpeed * xCamFront;
        zCamPos += cameraSpeed * zCamFront;
    }

    public void movePOVBackward() {
        xCamPos -= cameraSpeed * xCamFront;
        zCamPos -= cameraSpeed * zCamFront;
    }

    public void movePOVLeft() {
        float strafeX = (float) Math.cos(Math.toRadians(yaw - 90.0f));
        float strafeZ = (float) Math.sin(Math.toRadians(yaw - 90.0f));

        xCamPos -= strafeX * cameraSpeed;
        zCamPos -= strafeZ * cameraSpeed;
    }

    public void movePOVRight() {// Calcula la dirección perpendicular a la vista de la cámara
        float strafeX = (float) Math.cos(Math.toRadians(yaw - 90.0f));
        float strafeZ = (float) Math.sin(Math.toRadians(yaw - 90.0f));

        xCamPos += strafeX * cameraSpeed;
        zCamPos += strafeZ * cameraSpeed;
    }

    public void moveCameraUp() {
        pitch += 1.0f;
        if (pitch > 89.0f) pitch = 89.0f;
    }

    public void moveCameraDown() {
        pitch -= 1.0f;
        if (pitch < -89.0f) pitch = -89.0f;
    }

    public void moveCameraLeft() {
        yaw -= 1.0f;
    }

    public void moveCameraRight() {
        yaw += 1.0f;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpVelocity = 0.15f;
        }
    }

    private void updateCameraVectors() {
        float newFrontx = (float) (cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));
        float newFronty = (float) sin(Math.toRadians(pitch));
        float newFrontz = (float) (sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));

        float length = (float) sqrt(newFrontx * newFrontx + newFronty * newFronty + newFrontz * newFrontz);

        // Normalizar manualmente dividiendo cada componente por la longitud
        xCamFront = newFrontx / length;
        yCamFront = newFronty / length;
        zCamFront = newFrontz / length;


        yCamPos += jumpVelocity;
        if (isJumping) {
            jumpVelocity -= gravity; // Aplicar gravedad durante el salto

            // Verificar si ha alcanzado el suelo
            if (yCamPos <= 0.9f) {
                yCamPos = 0.9f;
                isJumping = false;
                jumpVelocity = 0.0f;
            }
        }
    }

    private void setPerspectiveProjection(GL10 gl) {
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        gl.glDepthMask(true);  // disable writes to Z-Buffer

        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix

        // Use perspective projection
        GLU.gluPerspective(gl, 60, (float) width / height, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset
    }

    private void setOrthographicProjection(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-5, 5, -4, 4, -5, 5);
        gl.glDepthMask(false);  // disable writes to Z-Buffer
        gl.glDisable(GL10.GL_DEPTH_TEST);  // disable depth-testing

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}