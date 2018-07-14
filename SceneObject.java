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

    /* Algoritmo adaptado para "passagem por referência" ser possível. */
    public void toScreenCoordinates(double d, double hx, double hy, double width, double height) {
        ScreenPoint[] screenVertices = new ScreenPoint[3];

        for (int i = 0; i < this.triangles.length; i++) {
            Point[] vertices = this.triangles[i].getVertices();
            int[] pointIndexes = this.triangles[i].getPointIndexes(); 
            
            for(int j = 0; j < vertices.length && j < pointIndexes.length; j++) {

                int pointIndex = pointIndexes[j];
                Point point = this.points[pointIndex];

                double x = (d/hx) * point.getX()/point.getZ();
                double y = (d/hy) * point.getY()/point.getZ();
                ScreenPoint screenPoint = new ScreenPoint((int) ((x + 1) * width / 2), (int) ((1 - y) * height / 2));
                screenVertices[j] = screenPoint;
                this.screenPoints[pointIndex] = screenPoint;
            }

            triangles[i].setScreenVertices(screenVertices);
        }
        System.out.println("Projecao para coordenadas de tela completa.");
    }
}