
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class App {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    // Costanti per la rotazione
    public static final double X_ROTATION_S = 0;
    public static final double Y_ROTATION_S = Math.PI / 2;
    public static final double Z_ROTATION_S = 0;

    public static void main(String[] args) throws Exception {

        // Load the polygons from the file
        Polygon[] polygons = loadPolygonsFromFile("src\\data.txt");

        JFrame frame = new JFrame("3D Renderer");
        Panel panel = new Panel(frame);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);

        long lastTime = System.currentTimeMillis();

        while (true) {
            long currentTime = System.currentTimeMillis();
            // deltaTime in Seconds
            double deltaTime = (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            System.out.println(deltaTime + " seconds \t" + 1 / deltaTime + " fps");

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

            // Rotate the polygons
            for (Polygon polygon : polygons) {
                for (int i = 0; i < polygon.vertices.length; i++) {
                    polygon.vertices[i] = rotationMatrix.multiply(polygon.vertices[i]);
                }
            }

            // Set the projected polygons to the panel
            panel.setPolygons(polygons);
            panel.updateDrawingPolygons();
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
