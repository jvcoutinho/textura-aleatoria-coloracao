public class Illumination {

    private Point coordinates; // Pl
    private Point lightColor; // Il
    private double ambientalConstant; // ka
    private Point ambientalVector; // Ia
    private double diffuseConstant; // kd
    private Point diffuseVector; // Id
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

    
}