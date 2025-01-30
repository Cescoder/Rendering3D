import java.util.ArrayList;
import java.util.List;

public class Object3D {
    private Polygon[] polygons;

    // relative to the parent
    private Matrix center = new Matrix(new double[][] { { 0 }, { 0 }, { 0 } });

    protected ArrayList<Object3D> children = new ArrayList<>();
    private Object3D parent;

    public Object3D() {
        this.polygons = new Polygon[0];
    }

    public Object3D(Polygon[] polygons) {
        this.polygons = polygons;
    }

    public void setParent(Object3D parent) {
        this.parent = parent;
    }

    public void addChild(Object3D child) {
        children.add(child);
        child.setParent(this);
    }

    public void setCenter(Matrix center) {
        this.center = center;
    }

    /*
     * public void multiply(Matrix matrix) {
     * multiply(matrix, this.center);
     * }
     */

    /*
     * public Polygon[] getPolygons(int deepness) {
     * Matrix translationVector = new Matrix(new double[][] {
     * { this.center.get(0, 0) - center.get(0, 0) },
     * { this.center.get(1, 0) - center.get(1, 0) },
     * { this.center.get(2, 0) - center.get(2, 0) }
     * });
     * 
     * ArrayList<Polygon> allPolygons = new ArrayList<>();
     * for (Polygon polygon : polygons) {
     * Matrix[] vertices = new Matrix[polygon.vertices.length];
     * for (int i = 0; i < polygon.vertices.length; i++) {
     * vertices[i] = polygon.vertices[i].add(translationVector);
     * }
     * allPolygons.add(new Polygon(vertices));
     * }
     * 
     * return allPolygons.toArray(new Polygon[0]);
     * }
     */

    protected Polygon[] getPolygons(int deepness) {
        Matrix sum = new Matrix(new double[][] { { 0 }, { 0 }, { 0 } });
        Polygon[] r = new Polygon[this.polygons.length];
        Object3D current = this;
        for (int i = 0; i < deepness; i++) {
            sum = sum.add(current.center);
            current = current.parent;
        }

        for (int i = 0; i < this.polygons.length; i++) {
            Matrix[] vertices = new Matrix[this.polygons[i].vertices.length];
            for (int j = 0; j < this.polygons[i].vertices.length; j++) {
                vertices[j] = this.polygons[i].vertices[j].add(sum);
            }
            r[i] = new Polygon(vertices);
        }

        return r;
    }

    // this method will return the polygons of the object and all of its children
    // respecting the given center
    public List<Polygon> deepGetPolygons(int deepness) {
        ArrayList<Polygon> allPolygons = new ArrayList<>();
        allPolygons.addAll(List.of(this.getPolygons(deepness)));
        for (Object3D child : children) {
            allPolygons.addAll(child.deepGetPolygons(deepness + 1));
        }

        return allPolygons;
    }

    public void multiply(Matrix matrix) {
        multiply(matrix, 0);
    }

    public void multiply(Matrix matrix, int deepness) {
        if (deepness == 0) {
            for (Polygon polygon : polygons) {
                for (int i = 0; i < polygon.vertices.length; i++) {
                    polygon.vertices[i] = matrix.multiply(polygon.vertices[i]);
                }
            }

            for (Object3D child : children) {
                child.multiply(matrix, deepness + 1);
            }
            return;
        }

        for (Object3D child : children) {
            child.multiply(matrix, deepness + 1);
        }

        Matrix sum = new Matrix(new double[][] { { 0 }, { 0 }, { 0 } });
        Object3D current = this;
        for (int i = 0; i < deepness; i++) {
            sum = sum.add(current.center);
            current = current.parent;
        }

        Polygon[] r = getPolygons(deepness);
        // rotate the r polygons
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[i].vertices.length; j++) {
                r[i].vertices[j] = matrix.multiply(r[i].vertices[j]);
                r[i].vertices[j] = r[i].vertices[j].subtract(sum);
                // set the new vertices
                polygons[i].vertices[j] = r[i].vertices[j];
            }
        }

    }

}
