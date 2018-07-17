import javafx.geometry.Point3D;

public class Camera {

    private Point coordinates;
    private Point N;
    private Point U;
    private Point V;
    private double d;
    private double hx;
    private double hy;
    
    public Camera(Point coordinates, Point N, Point V, double d, double hx, double hy) {
        this.coordinates = coordinates;
        this.N = N;
        this.V = V;
        this.d = d;
        this.hx = hx;
        this.hy = hy;
        this.U = calculateUVector(N, V);
        this.U.printPoint();
    }

    public Point calculateUVector(Point N, Point V) {
        V = V.orthogonalize(N).normalize();
        N = N.normalize();
        return N.vectorProduct(V);
    }

    public Point getU() {
        return U;
    }

    public Point getN() {
        return N;
    }

    public Point getV() {
        return V;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public double getD() {
        return d;
    }

    public double getHx() {
        return hx;
    }

    public double getHy() {
        return hy;
    }
}