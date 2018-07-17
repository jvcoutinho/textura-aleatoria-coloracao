import javafx.geometry.Point3D;

public class Point {

    private double x;
    private double y;
    private double z;
    private double[] normal;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.normal = new double[] { 0, 0, 0 };
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /* Ortogonalização de Gram-Schmidt: u' = u - (<u.v>/<v.v>)v */
    public Point orthogonalize(Point v) {
        double uv = this.scalarProduct(v);
        double vv = v.scalarProduct(v);
        return this.subtract(v.multiply(uv/vv));
    }

    public double scalarProduct(Point v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Point multiply(double scalar) {
        return new Point(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Point multiply(double[][] matrix) {
        double x = matrix[0][0] * this.x + matrix[0][1] * this.y + matrix[0][2] * this.z;
        double y = matrix[1][0] * this.x + matrix[1][1] * this.y + matrix[1][2] * this.z;
        double z = matrix[2][0] * this.x + matrix[2][1] * this.y + matrix[2][2] * this.z;
        return new Point(x, y, z);
    }

    public Point add(Point v) {
        return new Point(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public Point subtract(Point v) {
        return new Point(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public Point normalize() {
        double norm = this.norm();
        return new Point(this.x / norm, this.y / norm, this.z / norm);
    }

    public double norm() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public Point vectorProduct(Point v) {
        return new Point(
            this.y * v.z - this.z * v.y, 
            this.z * v.x - this.x * v.z,
            this.x * v.y - this.y * v.x);
    }

    public void addNormal(Point normal) {
        this.normal[0] += normal.x;
        this.normal[1] += normal.y;
        this.normal[2] += normal.z;
    }

    public void normalizeNormal() {
        Point normal = new Point(this.normal[0], this.normal[1], this.normal[2]);
        normal = normal.normalize();

        this.normal[0] = normal.x;
        this.normal[1] = normal.y;
        this.normal[2] = normal.z;
    }

    public double[] getNormal() {
        return this.normal;
    }

    public void printPoint() {
        System.out.println(this.x + " " + this.y + " " + this.z);
    }
    
}