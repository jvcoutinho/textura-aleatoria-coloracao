public class Triangle {

    private Point[] vertices;
    private int[] pointIndexes;
    private Pixel[] screenVertices;
    private Point normal;

    public Triangle(int v0, int v1, int v2) {
        this.pointIndexes = new int[3];
        this.pointIndexes[0] = v0;
        this.pointIndexes[1] = v1;
        this.pointIndexes[2] = v2;
   }

   public void setVertices(Point[] vertices) {
       this.vertices = vertices;
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

    public void setScreenVertices(Pixel[] screenVertices) {
        this.screenVertices = screenVertices;
    }

    public int[] getPointIndexes() {
        return pointIndexes;
    }
    

}