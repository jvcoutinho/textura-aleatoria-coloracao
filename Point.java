import javafx.geometry.Point3D;

public class Point {

    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        // Point3D oi = new Point3D(x, y, z);
        // oi.normalize()
        // oi.
        // oi.su
        // oi.magnitude()
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
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Point subtract(Point v) {
        this.x -= v.x;
        this.x -= v.y;
        this.z -= v.z;
        return this;
    }

    public Point normalize() {
        double norm = this.norm();
        this.x /= norm;
        this.y /= norm;
        this.z /= norm;
        return this;
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

    // public Point normalize() {
    //     double norm = this.calculateNorm();
    //     System.out.println(norm);
    //     this.x = x / norm;
    //     this.y = y / norm;
    //     this.z = y / norm;
    //     System.out.println(x + " " + y + " " + z);
    //     System.out.println("NORMAL: " + this.calculateNorm()+'\n');
    //     return this;
    // }

    // public double calculateNorm() {
    //     System.out.println("NORMA n raiz: " + Math.pow(this.x, 2) + " " + Math.pow(this.y, 2) + " " + Math.pow(this.z, 2));
    //     return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    // }

    // public Point orthogonalize(Point v) {
    //     double a = this.scalarProduct(v);
    //     double b = v.scalarProduct(v);
    //     return this.sum(v.multiply(a / b));
    // }

    // public Point sum(Point u) {
    //     this.x += u.getX();
    //     this.y += u.getY();
    //     this.z += u.getZ();
    //     return this;
    // }

    // public Point multiply(double scalar) {
    //     this.x *= scalar;
    //     this.y *= scalar;
    //     this.z *= scalar;
    //     return this;
    // }

    // public double scalarProduct(Point v) {
    //     Point u = this;
    //     return u.x * v.getX() + u.y * v.getY() + u.z * v.getZ(); 
    // }

    // public Point vectorProduct(Point v) {
    //     Point u = this;
    //     double x = u.y * v.getZ() - u.z * v.getY();
    //     double y = u.z * v.getX() - u.x * v.getZ();
    //     double z = u.x * v.getY() - u.y * v.getX();  
    //     return new Point(x, y, z);
    // }

    public void printPoint() {
        System.out.println(this.x + " " + this.y + " " + this.z);
    }
    
}