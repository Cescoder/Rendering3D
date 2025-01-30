
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class App {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    // Rotation speed
    public static final double X_ROTATION_S = 0;
    public static final double Y_ROTATION_S = 0;
    public static final double Z_ROTATION_S = 0;

    public static final double X_SCALING_S = 0;
    public static final double Y_SCALING_S = 0;
    public static final double Z_SCALING_S = 0;

    public static void main(String[] args) throws Exception {

        // Load the polygons from the file
        Polygon[] polygons = loadPolygonsFromFile("src/data.txt");
        Polygon[] polygons2 = loadPolygonsFromFile("src/data2.txt");
        Polygon[] polygons3 = loadPolygonsFromFile("src/data3.txt");

        JFrame frame = new JFrame("3D Renderer");

        Scene scene = new Scene();
        scene.setCenter(new Matrix(new double[][] { { 100 }, { 200 }, { 0 } }));

        Object3D object = new Object3D(polygons);
        Object3D object2 = new Object3D(polygons2);
        // Object3D object3 = new Object3D(polygons3);
        object.setCenter(new Matrix(new double[][] { { -2 }, { -2 }, { 0 } }));
        // object2.setCenter(new Matrix(new double[][] { { 1 }, { 1 }, { 1 } }));
        // object3.setCenter(new Matrix(new double[][] { { 0 }, { 0 }, { 0 } }));

        scene.addChild(object);
        // scene.addChild(object2);
        // object.addChild(object2);
        // object.addChild(object2);
        // object2.addChild(object3);
        Panel panel = new Panel();

        panel.addComponentListener(
                new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        panel.updateDrawingAxis();
                    }
                });

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);

        panel.setNormalProjectionVector(new Matrix(new double[][] { { 0 }, { -1 }, { 0 } }));

        long lastTime = System.currentTimeMillis();

        while (true) {
            long currentTime = System.currentTimeMillis();
            // deltaTime in Seconds
            double deltaTime = (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            // System.out.println(deltaTime + " seconds \t" + 1 / deltaTime + " fps");

            double X_ROTATION = X_ROTATION_S * deltaTime;
            double Y_ROTATION = Y_ROTATION_S * deltaTime;
            double Z_ROTATION = Z_ROTATION_S * deltaTime;

            // Create the rotation matrices
            double[][] y_rotation = {
                    { Math.cos(Y_ROTATION), 0, Math.sin(Y_ROTATION) },
                    { 0, 1, 0 },
                    { -Math.sin(Y_ROTATION), 0, Math.cos(Y_ROTATION) }
            };
            Matrix y_rotationMatrix = new Matrix(y_rotation);

            double[][] x_rotation = {
                    { 1, 0, 0 },
                    { 0, Math.cos(X_ROTATION), -Math.sin(X_ROTATION) },
                    { 0, Math.sin(X_ROTATION), Math.cos(X_ROTATION) }
            };
            Matrix x_rotationMatrix = new Matrix(x_rotation);

            double[][] z_rotation = {
                    { Math.cos(Z_ROTATION), -Math.sin(Z_ROTATION), 0 },
                    { Math.sin(Z_ROTATION), Math.cos(Z_ROTATION), 0 },
                    { 0, 0, 1 }
            };
            Matrix z_rotationMatrix = new Matrix(z_rotation);

            Matrix rotationMatrix = x_rotationMatrix.multiply(y_rotationMatrix).multiply(z_rotationMatrix);

            Matrix scalingMatrix = new Matrix(new double[][] {
                    { 1 + X_SCALING_S * deltaTime, 0, 0 },
                    { 0, 1 + Y_SCALING_S * deltaTime, 0 },
                    { 0, 0, 1 + Z_SCALING_S * deltaTime }
            });

            // Rotate the object
            scene.multiply(rotationMatrix);
            scene.multiply(scalingMatrix);
            Polygon[] scenePoly = scene.deepGetPolygons(0).toArray(new Polygon[0]);
            panel.updateDrawingPolygons(scenePoly);
            panel.setDeltaTime(deltaTime);
            panel.repaint();

        }

    }

    private static Polygon[] loadPolygonsFromFile(String filename) throws IOException {
        List<Polygon> polygons = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("[;#]");
            Matrix[] vertices = new Matrix[parts.length / 3];
            for (int i = 0; i < parts.length; i += 3) {
                double x = Double.parseDouble(parts[i]);
                double y = Double.parseDouble(parts[i + 1]);
                double z = Double.parseDouble(parts[i + 2]);
                vertices[i / 3] = new Matrix(new double[][] { { x }, { y }, { z } });
            }
            polygons.add(new Polygon(vertices));
        }
        reader.close();
        return polygons.toArray(new Polygon[0]);
    }
}
