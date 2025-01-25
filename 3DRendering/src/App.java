
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class App {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    //Costanti per la rotazione
    public static final double X_ROTATION = 0.01;
    public static final double Y_ROTATION = 0.01;
    public static final double Z_ROTATION = 0.01;


    public static void main(String[] args) throws Exception {
    
        // Carica i poligoni dal file
        List<Polygon> polygons = loadPolygonsFromFile("src\\data.txt");


        // Matrici di rotazione e proiezione (come nel tuo codice originale)
        double[][] y_rotation = {
            {Math.cos(Y_ROTATION), 0, Math.sin(Y_ROTATION)},
            {0, 1, 0},
            {-Math.sin(Y_ROTATION), 0,Math.cos(Y_ROTATION)}
        };
        Matrix y_rotationMatrix = new Matrix(y_rotation);

        double[][] x_rotation = {
            {1, 0, 0},
            {0, Math.cos(X_ROTATION), -Math.sin(X_ROTATION)},
            {0, Math.sin(X_ROTATION), Math.cos(X_ROTATION)}
        };
        Matrix x_rotationMatrix = new Matrix(x_rotation);

        double[][] z_rotation = {
            {Math.cos(Z_ROTATION), -Math.sin(Z_ROTATION), 0},
            {Math.sin(Z_ROTATION), Math.cos(Z_ROTATION), 0},
            {0, 0, 1}
        };
        Matrix z_rotationMatrix = new Matrix(z_rotation);

        double[][] projection = {
            {1, 0, 0},
            {0, 1, 0}
        };
        Matrix projectionMatrix = new Matrix(projection);

        // Combine the rotation matrices
        Matrix rotationMatrix = x_rotationMatrix.multiply(y_rotationMatrix).multiply(z_rotationMatrix);
        
        Polygon[] projectedPolygons;

        JFrame frame = new JFrame("3D Rotating Cube");
        Panel panel = new Panel(frame);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);

        while (true) {
            // Rotate the polygons
            for (Polygon polygon : polygons) {
                for (int i = 0; i < polygon.vertices.length; i++) {
                    polygon.vertices[i] = rotationMatrix.multiply(polygon.vertices[i]);
                }
            }

            // Project the polygons and scale them
            projectedPolygons = new Polygon[polygons.size()];
            for (int i = 0; i < polygons.size(); i++) {
                Matrix[] vertices = new Matrix[polygons.get(i).vertices.length];
                for (int j = 0; j < vertices.length; j++) {
                    vertices[j] = projectionMatrix.multiply(polygons.get(i).vertices[j]);
                    vertices[j].getMatrix()[0][0] = vertices[j].getMatrix()[0][0] * 100 + frame.getWidth() / 2;
                    vertices[j].getMatrix()[1][0] = vertices[j].getMatrix()[1][0] * 100 + frame.getHeight() / 2;
                }
                projectedPolygons[i] = new Polygon(vertices);
            }

            // Set the projected polygons to the panel
            panel.setPolygons(projectedPolygons);
            panel.repaint();

            // Sleep for a while
            Thread.sleep(16);


        }
        
    }

    private static List<Polygon> loadPolygonsFromFile(String filename) throws IOException {
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
                vertices[i / 3] = new Matrix(new double[][]{{x}, {y}, {z}});
            }
            polygons.add(new Polygon(vertices));
        }
        reader.close();
        return polygons;
    }
}
