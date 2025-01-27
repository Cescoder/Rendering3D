import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Panel extends JPanel {
    private JFrame frame;
    private Polygon[] polygons;

    public Panel(JFrame frame) {
        this.frame = frame;
    }

    public void setPolygons(Polygon[] polygons) {
        this.polygons = polygons;
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

        for (Polygon polygon : polygons) {
            for (int i = 0; i < polygon.vertices.length; i++) {
                Matrix vertex1 = polygon.vertices[i];
                Matrix vertex2 = polygon.vertices[(i + 1) % polygon.vertices.length];
                g.drawLine((int) vertex1.get(0, 0), (int) vertex1.get(1, 0),
                        (int) vertex2.get(0, 0), (int) vertex2.get(1, 0));
            }
        }

    }
}
