
public class Polygon{
    public Matrix[] vertices;

    public Polygon(Matrix[] vertices){
        this.vertices = vertices;
    }
    
    public String toString(){
        String result = "";
        for (int i = 0; i < vertices.length; i++) {
            result += vertices[i] + "\n";
        }
        return result;
    }
}