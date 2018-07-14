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
}