public class Triangle {

    private Point[] vertices;
    private int[] pointIndexes;
    private ScreenPoint[] screenVertices;
    private Point normal;

    public Triangle(Point v0, Point v1, Point v2) {
        this.vertices = new Point[3];
        this.vertices[0] = v0;
        this.vertices[1] = v1;
        this.vertices[2] = v2;
   }

    public void setPointIndexes(int[] pointIndexes) {
        this.pointIndexes = pointIndexes;
    }

    public void calculateNormal() {
        Point p1p2 = vertices[1].subtract(vertices[0]);
        Point p1p3 = vertices[2].subtract(vertices[0]);
        this.normal = p1p2.vectorProduct(p1p3).normalize();
    }

    public void sumNormalToVertices() {
        this.vertices[0].addNormal(this.normal); 
        this.vertices[1].addNormal(this.normal); 
        this.vertices[2].addNormal(this.normal); 
    }

    public Point[] getVertices() {
        return vertices;
    }

    public void setScreenVertices(ScreenPoint[] screenVertices) {
        this.screenVertices = screenVertices;
    }

    public int[] getPointIndexes() {
        return pointIndexes;
    }


}