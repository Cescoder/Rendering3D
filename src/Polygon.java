
public class Polygon {
    public Matrix[] vertices;

    public Polygon(Matrix[] vertices) {
        this.vertices = vertices;
    }

    public Polygon deepCopy() {
        Matrix[] newVertices = new Matrix[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            newVertices[i] = vertices[i].deepCopy();
        }
        return new Polygon(newVertices);
    }
}