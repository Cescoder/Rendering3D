import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {
    /* private Polygon[] polygons; */
    private double deltaTime = -1;

    private Polygon[] axis = new Polygon[] {
            new Polygon(new Matrix[] { new Matrix(new double[][] { { 0 }, { 0 }, { 0 } }),
                    new Matrix(new double[][] { { 1 }, { 0 }, { 0 } }) }),
            new Polygon(new Matrix[] { new Matrix(new double[][] { { 0 }, { 0 }, { 0 } }),
                    new Matrix(new double[][] { { 0 }, { 1 }, { 0 } }) }),
            new Polygon(new Matrix[] { new Matrix(new double[][] { { 0 }, { 0 }, { 0 } }),
                    new Matrix(new double[][] { { 0 }, { 0 }, { 1 } }) })
    };

    private Matrix normalProjectionVector = new Matrix(new double[][] {
            { 0 },
            { 0 },
            { 1 }
    });

    private Matrix projectionMatrix = createProjectionMatrix(normalProjectionVector);

    private Polygon[] drawingPolygons;

    private Polygon[] drawingAxis = createDrawingAxis(axis, projectionMatrix, this.getWidth(), this.getHeight());

    public void updateDrawingPolygons(Polygon[] polygons) {
        // Polygon[] polygons = scene.deepGetPolygons(0);
        // from list to array
        if (polygons == null) {
            return;
        }

        // TODO: Probably an optimization can be done here
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

        // Find two orthonormal basis vectors that span the plane orthogonal to the
        // normal vector
        Matrix basis1 = new Matrix(3, 1);
        Matrix basis2 = new Matrix(3, 1);

        // Create the first basis vector
        if (Math.abs(normalized.get(2, 0)) < 1.0 - 1e-10) {
            // If normal is not parallel to the Z-axis
            basis1.set(0, 0, -normalized.get(1, 0));
            basis1.set(1, 0, normalized.get(0, 0));
            basis1.set(2, 0, 0);
        } else {
            // If normal is parallel to the Z-axis, choose a vector in the XY-plane
            basis1.set(0, 0, 1);
            basis1.set(1, 0, 0);
            basis1.set(2, 0, 0);
        }

        // Normalize the first basis vector
        norm = 0;
        for (int i = 0; i < 3; i++) {
            norm += basis1.get(i, 0) * basis1.get(i, 0);
        }
        norm = Math.sqrt(norm);
        for (int i = 0; i < 3; i++) {
            basis1.set(i, 0, basis1.get(i, 0) / norm);
        }

        // Create the second basis vector using the cross product of the normal and
        // basis1
        basis2.set(0, 0, normalized.get(1, 0) * basis1.get(2, 0) - normalized.get(2, 0) * basis1.get(1, 0));
        basis2.set(1, 0, normalized.get(2, 0) * basis1.get(0, 0) - normalized.get(0, 0) * basis1.get(2, 0));
        basis2.set(2, 0, normalized.get(0, 0) * basis1.get(1, 0) - normalized.get(1, 0) * basis1.get(0, 0));

        // Normalize the second basis vector
        norm = 0;
        for (int i = 0; i < 3; i++) {
            norm += basis2.get(i, 0) * basis2.get(i, 0);
        }
        norm = Math.sqrt(norm);
        for (int i = 0; i < 3; i++) {
            basis2.set(i, 0, basis2.get(i, 0) / norm);
        }

        // Construct the projection matrix
        Matrix projectionMatrix = new Matrix(2, 3);
        for (int i = 0; i < 3; i++) {
            projectionMatrix.set(0, i, basis1.get(i, 0));
            projectionMatrix.set(1, i, basis2.get(i, 0));
        }

        return projectionMatrix;
    }

    private static Polygon[] createDrawingAxis(Polygon[] axis, Matrix projectionMatrix, int width, int height) {
        Polygon[] drawingAxis = new Polygon[axis.length];

        for (int i = 0; i < axis.length; i++) {
            Matrix[] vertices = new Matrix[axis[i].vertices.length];

            for (int j = 0; j < vertices.length; j++) {
                vertices[j] = projectionMatrix.multiply(axis[i].vertices[j]);
                vertices[j].set(0, 0, vertices[j].get(0, 0) * 100 + width / 2);
                vertices[j].set(1, 0, -vertices[j].get(1, 0) * 100 + height / 2);
            }

            drawingAxis[i] = new Polygon(vertices);
        }

        return drawingAxis;
    }

    public void setNormalProjectionVector(Matrix normalProjectionVector) {
        this.normalProjectionVector = normalProjectionVector;
        this.projectionMatrix = createProjectionMatrix(normalProjectionVector);
        this.drawingAxis = createDrawingAxis(axis, projectionMatrix, this.getWidth(), this.getHeight());
        System.out.println(this.projectionMatrix);
    }

    private void drawAxis(Graphics g) {
        if (drawingAxis == null) {
            return;
        }

        // draw the x, y, z axes
        g.setColor(Color.RED);
        // draw the x-axis using the first polygon
        for (int i = 0; i < drawingAxis[0].vertices.length; i++) {
            Matrix vertex1 = drawingAxis[0].vertices[i];
            Matrix vertex2 = drawingAxis[0].vertices[(i + 1) % drawingAxis[0].vertices.length];
            g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                    (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
        }

        g.setColor(Color.GREEN);
        // draw the y-axis using the second polygon
        for (int i = 0; i < drawingAxis[1].vertices.length; i++) {
            Matrix vertex1 = drawingAxis[1].vertices[i];
            Matrix vertex2 = drawingAxis[1].vertices[(i + 1) % drawingAxis[1].vertices.length];
            g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                    (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
        }

        g.setColor(Color.BLUE);
        // draw the z-axis using the third polygon
        for (int i = 0; i < drawingAxis[2].vertices.length; i++) {
            Matrix vertex1 = drawingAxis[2].vertices[i];
            Matrix vertex2 = drawingAxis[2].vertices[(i + 1) % drawingAxis[2].vertices.length];
            g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                    (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        drawAxis(g);

        g.setColor(Color.WHITE);

        if (drawingPolygons == null) {
            return;
        }

        for (Polygon polygon : drawingPolygons) {
            if (polygon == null) {
                continue;
            }
            for (int i = 0; i < polygon.vertices.length; i++) {
                Matrix vertex1 = polygon.vertices[i];
                Matrix vertex2 = polygon.vertices[(i + 1) % polygon.vertices.length];
                g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                        (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("FPS: " + (deltaTime == -1 ? "N/A" : String.format("%.2f", 1 / deltaTime)), 10, 20);

    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void updateDrawingAxis() {
        drawingAxis = createDrawingAxis(axis, projectionMatrix, this.getWidth(), this.getHeight());
    }

}
