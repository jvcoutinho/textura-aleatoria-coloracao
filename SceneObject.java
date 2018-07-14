public class SceneObject {

    private Point[] points;
    private Triangle[] triangles;

    public SceneObject(Point[] points, Triangle[] triangles) {
        this.points = points;
        this.triangles = triangles;
    }

    public void toViewCoordinates(double[][] worldToView, Point cameraCoordinates) {
        for (int i = 0; i < this.points.length; i++) {
            Point v = this.points[i].subtract(cameraCoordinates);
            this.points[i] = v.multiply(worldToView);
        }       
    }

    public void normalize() {
        for (int i = 0; i < this.triangles.length; i++) {
            this.triangles[i].calculateNormal();
            this.triangles[i].sumNormalToVertices();
        }

        for (int i = 0; i < this.points.length; i++)
            this.points[i].normalizeNormal();

        System.out.println("Todas as normais normalizadas.");
    }
}