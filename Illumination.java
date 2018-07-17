public class Illumination {

    private Point coordinates; // Pl
    private Point lightColor; // Il
    private double ambientalConstant; // ka
    private Point ambientalVector; // Ia
    private double diffuseConstant; // kd
    private Point diffuseVector; // Od
    private double specularConstant; // ks
    private double rugosityConstant; // n
    
    public Illumination(Point coordinates, Point lightColor, double ambientalConstant, Point ambientalVector, 
        double diffuseConstant, Point diffuseVector, double specularConstant, double rugosityConstant) {
        
        this.coordinates = coordinates;
        this.lightColor = lightColor;
        this.ambientalConstant = ambientalConstant;
        this.ambientalVector = ambientalVector;
        this.diffuseConstant = diffuseConstant;
        this.diffuseVector = diffuseVector;
        this.specularConstant = specularConstant;
        this.rugosityConstant = rugosityConstant;
    }

    public void toViewCoordinates(double[][] worldToView, Point cameraCoordinates) {
        Point v = this.coordinates.subtract(cameraCoordinates);
        this.coordinates = v.multiply(worldToView);
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public Point shading(Point N, Point L, Point R, Point V) {
        return this.ambientalComponent().add(this.diffuseComponent(N, L)).add(this.specularComponent(R, V));
    }

    public Point ambientalComponent() {
        double x = this.ambientalVector.getX() * this.ambientalConstant * this.diffuseVector.getX();
        double y = this.ambientalVector.getY() * this.ambientalConstant * this.diffuseVector.getY();
        double z = this.ambientalVector.getZ() * this.ambientalConstant * this.diffuseVector.getZ();
        return new Point(x, y, z);
    }

    public Point diffuseComponent(Point N, Point L) {
        double scalar = N.scalarProduct(L);
        double x = this.lightColor.getX() * this.diffuseVector.getX() * this.diffuseConstant * scalar;
        double y = this.lightColor.getY() * this.diffuseVector.getY() * this.diffuseConstant * scalar;
        double z = this.lightColor.getZ() * this.diffuseVector.getZ() * this.diffuseConstant * scalar;
        return new Point(x, y, z);
    }

    public Point specularComponent(Point R, Point V) {
        double scalar = Math.pow(R.scalarProduct(V), this.rugosityConstant);
        double x = this.lightColor.getX() * this.specularConstant * scalar;
        double y = this.lightColor.getY() * this.specularConstant * scalar;
        double z = this.lightColor.getZ() * this.specularConstant * scalar;
        return new Point(x, y, z);
    }
 }