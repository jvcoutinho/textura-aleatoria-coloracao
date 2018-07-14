public class Triangle {

    private Point[] vertices;

    public Triangle(Point v0, Point v1, Point v2) {
        this.vertices = new Point[3];
        this.vertices[0] = v0;
        this.vertices[1] = v1;
        this.vertices[2] = v2;
    }

}