package pino3d;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;

@SuppressWarnings("serial")
public class Pino3DSimple extends GLJPanel implements GLEventListener, KeyListener {
    private static String TITLE = "🎄 Pino Navideño 3D Giratorio 🎄";
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int FPS = 60;
    
    private float rotationY = 0.0f;
    private float rotationSpeed = 1.0f;
    private GLU glu;
    private boolean rotating = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GLJPanel canvas = new Pino3DSimple();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                final JFrame frame = new JFrame();
                frame.setLayout(new BorderLayout());
                frame.getContentPane().add(canvas, BorderLayout.CENTER);
                frame.addKeyListener((KeyListener) canvas);
                
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) {
                                    animator.stop();
                                }
                                System.exit(0);
                            }
                        }.start();
                    }
                });

                frame.setTitle(TITLE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                animator.start();
            }
        });
    }

    public Pino3DSimple() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        
        gl.glClearColor(0.1f, 0.1f, 0.3f, 1.0f); // Fondo azul noche
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        // Configurar iluminación
        float[] ambientLight = {0.3f, 0.3f, 0.3f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        
        float[] diffuseLight = {0.9f, 0.9f, 0.9f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        
        float[] specularLight = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        
        // Material especular para hacerlo más brillante
        float[] mat_specular = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] high_shininess = {100.0f};
        gl.glMaterialfv(GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL_FRONT, GL2.GL_SHININESS, high_shininess, 0);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 0.1f, 100.0f);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Configurar posición de la luz
        float[] lightPos = {5.0f, 10.0f, 5.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        gl.glLoadIdentity();
        
        // Posicionar cámara
        glu.gluLookAt(0.0f, 3.0f, 8.0f,  // posición cámara
                      0.0f, 0.0f, 0.0f,  // punto de mira
                      0.0f, 1.0f, 0.0f); // vector arriba

        // Rotación automática
        if (rotating) {
            gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
            rotationY += rotationSpeed;
            if (rotationY >= 360) rotationY = 0;
        }

        // Dibujar el pino navideño completo
        dibujarPinoNavideno(gl);
        
        gl.glFlush();
    }

    private void dibujarPinoNavideno(GL2 gl) {
        //  ESTRELLA en la punta
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 0.0f); // Amarillo
        gl.glTranslatef(0.0f, 3.5f, 0.0f);
        dibujarEstrella(gl);
        gl.glPopMatrix();

        //  CAPAS DEL PINO (de arriba hacia abajo)
        
        // Capa 1 - Superior (más pequeña)
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.6f, 0.1f); // Verde oscuro
        gl.glTranslatef(0.0f, 2.5f, 0.0f);
        dibujarCapaPino(gl, 0.1f, 1.2f, 1.0f);
        gl.glPopMatrix();

        // Capa 2 - Media
        gl.glPushMatrix();
        gl.glColor3f(0.1f, 0.7f, 0.2f); // Verde medio
        gl.glTranslatef(0.0f, 1.5f, 0.0f);
        dibujarCapaPino(gl, 0.1f, 1.6f, 1.2f);
        gl.glPopMatrix();

        // Capa 3 - Inferior (más grande)
        gl.glPushMatrix();
        gl.glColor3f(0.2f, 0.8f, 0.3f); // Verde claro
        gl.glTranslatef(0.0f, 0.3f, 0.0f);
        dibujarCapaPino(gl, 0.1f, 2.0f, 1.5f);
        gl.glPopMatrix();

        //  TRONCO
        gl.glPushMatrix();
        gl.glColor3f(0.4f, 0.2f, 0.1f); // Marrón
        gl.glTranslatef(0.0f, -1.0f, 0.0f);
        dibujarTronco(gl, 0.3f, 0.4f, 1.2f);
        gl.glPopMatrix();

        //  REGALOS en la base
        dibujarRegalos(gl);
        
        //  ESFERAS DECORATIVAS en el árbol
        dibujarEsferasDecorativas(gl);
    }

    private void dibujarCapaPino(GL2 gl, float base, float top, float height) {
        int slices = 16;
        int stacks = 8;
        
        gl.glBegin(GL_TRIANGLE_FAN);
        // Punta del cono
        gl.glNormal3f(0, 1, 0);
        gl.glVertex3f(0, height/2, 0);
        
        // Base del cono
        for (int i = 0; i <= slices; i++) {
            double angle = 2 * Math.PI * i / slices;
            float x = (float) Math.cos(angle) * top;
            float z = (float) Math.sin(angle) * top;
            gl.glNormal3f(x, 0, z);
            gl.glVertex3f(x, -height/2, z);
        }
        gl.glEnd();
        
        // Base del cono (círculo inferior)
        gl.glBegin(GL_TRIANGLE_FAN);
        gl.glNormal3f(0, -1, 0);
        gl.glVertex3f(0, -height/2, 0);
        for (int i = slices; i >= 0; i--) {
            double angle = 2 * Math.PI * i / slices;
            float x = (float) Math.cos(angle) * top;
            float z = (float) Math.sin(angle) * top;
            gl.glVertex3f(x, -height/2, z);
        }
        gl.glEnd();
    }

    private void dibujarTronco(GL2 gl, float base, float top, float height) {
        int slices = 12;
        
        // Lados del cilindro
        gl.glBegin(GL_QUAD_STRIP);
        for (int i = 0; i <= slices; i++) {
            double angle = 2 * Math.PI * i / slices;
            float x = (float) Math.cos(angle);
            float z = (float) Math.sin(angle);
            
            gl.glNormal3f(x, 0, z);
            gl.glVertex3f(x * top, height/2, z * top);
            gl.glVertex3f(x * base, -height/2, z * base);
        }
        gl.glEnd();
        
        // Tapas del cilindro
        // Tapa superior
        gl.glBegin(GL_TRIANGLE_FAN);
        gl.glNormal3f(0, 1, 0);
        gl.glVertex3f(0, height/2, 0);
        for (int i = 0; i <= slices; i++) {
            double angle = 2 * Math.PI * i / slices;
            float x = (float) Math.cos(angle) * top;
            float z = (float) Math.sin(angle) * top;
            gl.glVertex3f(x, height/2, z);
        }
        gl.glEnd();
        
        // Tapa inferior
        gl.glBegin(GL_TRIANGLE_FAN);
        gl.glNormal3f(0, -1, 0);
        gl.glVertex3f(0, -height/2, 0);
        for (int i = slices; i >= 0; i--) {
            double angle = 2 * Math.PI * i / slices;
            float x = (float) Math.cos(angle) * base;
            float z = (float) Math.sin(angle) * base;
            gl.glVertex3f(x, -height/2, z);
        }
        gl.glEnd();
    }

    private void dibujarEstrella(GL2 gl) {
        float size = 0.3f;
        
        gl.glBegin(GL_TRIANGLES);
        // Puntas de la estrella
        for (int i = 0; i < 5; i++) {
            double angle1 = 2 * Math.PI * i / 5;
            double angle2 = 2 * Math.PI * (i + 0.5) / 5;
            double angle3 = 2 * Math.PI * (i + 1) / 5;
            
            float x1 = (float) Math.sin(angle1) * size;
            float z1 = (float) Math.cos(angle1) * size;
            float x2 = (float) Math.sin(angle2) * size * 0.4f;
            float z2 = (float) Math.cos(angle2) * size * 0.4f;
            float x3 = (float) Math.sin(angle3) * size;
            float z3 = (float) Math.cos(angle3) * size;
            
            gl.glNormal3f(0, 1, 0);
            gl.glVertex3f(x1, 0, z1);
            gl.glVertex3f(x2, 0, z2);
            gl.glVertex3f(x3, 0, z3);
        }
        gl.glEnd();
    }

    private void dibujarRegalos(GL2 gl) {
        // Regalo 1 - Rojo
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glTranslatef(1.5f, -1.0f, 1.0f);
        dibujarCubo(gl, 0.4f);
        gl.glPopMatrix();

        // Regalo 2 - Azul
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glTranslatef(-1.2f, -1.0f, -1.0f);
        dibujarCubo(gl, 0.3f);
        gl.glPopMatrix();

        // Regalo 3 - Dorado
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 0.8f, 0.0f);
        gl.glTranslatef(0.8f, -1.0f, -1.5f);
        dibujarCubo(gl, 0.35f);
        gl.glPopMatrix();
    }

    private void dibujarCubo(GL2 gl, float size) {
        float s = size / 2;
        
        gl.glBegin(GL_QUADS);
        
        // Cara frontal
        gl.glNormal3f(0, 0, 1);
        gl.glVertex3f(-s, -s, s);
        gl.glVertex3f(s, -s, s);
        gl.glVertex3f(s, s, s);
        gl.glVertex3f(-s, s, s);
        
        // Cara trasera
        gl.glNormal3f(0, 0, -1);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(-s, s, -s);
        gl.glVertex3f(s, s, -s);
        gl.glVertex3f(s, -s, -s);
        
        // Cara superior
        gl.glNormal3f(0, 1, 0);
        gl.glVertex3f(-s, s, -s);
        gl.glVertex3f(-s, s, s);
        gl.glVertex3f(s, s, s);
        gl.glVertex3f(s, s, -s);
        
        // Cara inferior
        gl.glNormal3f(0, -1, 0);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(s, -s, -s);
        gl.glVertex3f(s, -s, s);
        gl.glVertex3f(-s, -s, s);
        
        // Cara derecha
        gl.glNormal3f(1, 0, 0);
        gl.glVertex3f(s, -s, -s);
        gl.glVertex3f(s, s, -s);
        gl.glVertex3f(s, s, s);
        gl.glVertex3f(s, -s, s);
        
        // Cara izquierda
        gl.glNormal3f(-1, 0, 0);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(-s, -s, s);
        gl.glVertex3f(-s, s, s);
        gl.glVertex3f(-s, s, -s);
        
        gl.glEnd();
    }

    private void dibujarEsferasDecorativas(GL2 gl) {
        float[][] posiciones = {
            {0.8f, 2.0f, 0.6f},   // Roja
            {-0.7f, 1.2f, 1.1f},  // Azul
            {1.1f, 0.5f, -0.8f},  // Amarilla
            {-1.3f, 1.8f, -0.5f}, // Magenta
            {0.5f, 0.8f, 1.4f}    // Cian
        };
        
        float[][] colores = {
            {1.0f, 0.0f, 0.0f}, // Rojo
            {0.0f, 0.0f, 1.0f}, // Azul
            {1.0f, 1.0f, 0.0f}, // Amarillo
            {1.0f, 0.0f, 1.0f}, // Magenta
            {0.0f, 1.0f, 1.0f}  // Cian
        };

        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            gl.glColor3f(colores[i][0], colores[i][1], colores[i][2]);
            gl.glTranslatef(posiciones[i][0], posiciones[i][1], posiciones[i][2]);
            dibujarEsfera(gl, 0.1f, 8, 8);
            gl.glPopMatrix();
        }
    }

    private void dibujarEsfera(GL2 gl, float radius, int slices, int stacks) {
        for (int i = 0; i < stacks; i++) {
            double lat0 = Math.PI * (-0.5 + (double) i / stacks);
            double z0 = Math.sin(lat0);
            double zr0 = Math.cos(lat0);
            
            double lat1 = Math.PI * (-0.5 + (double) (i + 1) / stacks);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);
            
            gl.glBegin(GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                double lng = 2 * Math.PI * j / slices;
                double x = Math.cos(lng);
                double y = Math.sin(lng);
                
                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(radius * x * zr0, radius * y * zr0, radius * z0);
                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(radius * x * zr1, radius * y * zr1, radius * z1);
            }
            gl.glEnd();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_SPACE:
                rotating = !rotating; // Pausar/reanudar rotación
                break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_ADD:
                rotationSpeed += 0.5f; // Aumentar velocidad
                break;
            case KeyEvent.VK_MINUS:
            case KeyEvent.VK_SUBTRACT:
                rotationSpeed = Math.max(0.1f, rotationSpeed - 0.5f); // Reducir velocidad
                break;
            case KeyEvent.VK_R:
                rotationY = 0; // Reiniciar rotación
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
