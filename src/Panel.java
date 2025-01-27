import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {
    private JFrame frame;
    private Polygon[] polygons;
    private Polygon[] drawingPolygons;
    private Matrix projectionMatrix = new Matrix(new double[][] {
            { 1, 0, 0 },
            { 0, 1, 0 }
    });
    private Matrix normalProjectionVector = new Matrix(new double[][] {
            { 0 },
            { 0 },
            { 1 }
    });

    public Panel(JFrame frame) {
        this.frame = frame;
    }

    public void setPolygons(Polygon[] polygons) {
        this.polygons = polygons;
    }

    public void updateDrawingPolygons() {
        if (polygons == null) {
            return;
        }

        drawingPolygons = new Polygon[polygons.length];

        for (int i = 0; i < polygons.length; i++) {
            Matrix[] vertices = new Matrix[polygons[i].vertices.length];

            for (int j = 0; j < vertices.length; j++) {
                vertices[j] = projectionMatrix.multiply(polygons[i].vertices[j]);
                vertices[j].set(0, 0, vertices[j].get(0, 0) * 100 + this.getWidth() / 2);
                vertices[j].set(1, 0, -vertices[j].get(1, 0) * 100 + this.getHeight() / 2);

            }
            // TODO: Probably an optimization can be done here
            // creating a new Polygon object for each frame is not efficient
            drawingPolygons[i] = new Polygon(vertices);
        }
    }

    private static Matrix createProjectionMatrix(Matrix normalProjectionVector) {
        // Ensure the vector is a column matrix with 3 rows and 1 column
        if (normalProjectionVector.getRowCount() != 3 || normalProjectionVector.getColCount() != 1) {
            throw new IllegalArgumentException("The normal vector must be a 3x1 column matrix.");
        }

        // Calculate the norm of the normal vector
        double norm = 0;
        for (int i = 0; i < 3; i++) {
            double value = normalProjectionVector.get(i, 0);
            norm += value * value;
        }
        norm = Math.sqrt(norm);

        if (norm == 0) {
            throw new IllegalArgumentException("The normal vector cannot be the zero vector.");
        }

        // Normalize the normal vector
        Matrix normalized = new Matrix(3, 1);
        for (int i = 0; i < 3; i++) {
            normalized.set(i, 0, normalProjectionVector.get(i, 0) / norm);
        }

        // Compute the outer product of the normalized vector with itself
        Matrix outerProduct = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                outerProduct.set(i, j, normalized.get(i, 0) * normalized.get(j, 0));
            }
        }

        // Create the identity matrix
        Matrix identity = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            identity.set(i, i, 1.0);
        }

        // Compute the projection matrix as I - outerProduct
        Matrix projectionMatrix = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                projectionMatrix.set(i, j, identity.get(i, j) - outerProduct.get(i, j));
            }
        }

        return projectionMatrix;
    }

    public void setNormalProjectionVector(Matrix normalProjectionVector) {
        this.normalProjectionVector = normalProjectionVector;
        this.projectionMatrix = createProjectionMatrix(normalProjectionVector);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        if (polygons == null) {
            return;
        }

        g.setColor(Color.WHITE);

        for (Polygon polygon : drawingPolygons) {
            for (int i = 0; i < polygon.vertices.length; i++) {
                Matrix vertex1 = polygon.vertices[i];
                Matrix vertex2 = polygon.vertices[(i + 1) % polygon.vertices.length];
                g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                        (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
            }
        }

    }
}
