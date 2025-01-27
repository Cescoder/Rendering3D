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
