public class SceneObject {

    private Point[] points;
    private ScreenPoint[] screenPoints;
    private Triangle[] triangles;

    public SceneObject(Point[] points, Triangle[] triangles) {
        this.points = points;
        this.triangles = triangles;
        this.screenPoints = new ScreenPoint[this.points.length];
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

    public void toScreenCoordinates(double d, double hx, double hy, double width, double height) {
        for (int i = 0; i < this.points.length; i++) {
            double x = (d/hx) * this.points[i].getX()/this.points[i].getZ();
            double y = (d/hy) * this.points[i].getY()/this.points[i].getZ();
            this.screenPoints[i] = new ScreenPoint((int) ((x + 1) * width / 2), (int) ((1 - y) * height / 2));
        }

        System.out.println("Projecao para coordenadas de tela completa.");
    }
}