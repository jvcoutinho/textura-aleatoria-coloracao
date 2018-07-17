import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SceneObject {

    private Point[] points;
    private Pixel[] pixels;
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

        for (int i = 0; i < this.triangles.length; i++) {
            int[] indexes = this.triangles[i].getPointIndexes();
            Point v0 = this.points[indexes[0]];
            Point v1 = this.points[indexes[1]];
            Point v2 = this.points[indexes[2]];
            this.triangles[i].setVertices(new Point[] { v0, v1, v2 });
        }
    }

    public void normalize() {
        for (int i = 0; i < this.triangles.length; i++) {
            this.triangles[i].calculateNormal();
            this.triangles[i].sumNormalToVertices();
        }

        for (int i = 0; i < this.points.length; i++) {
            this.points[i].normalizeNormal();
        }

        System.out.println("Todas as normais normalizadas.");
    }

    public void rasterize(double[][] zBuffer, double d, double hx, double hy, double width, double height, Illumination scene,
        GraphicsContext context) {
        for (int i = 0; i < 1; i++) {
            Point[] vertices = triangles[i].getVertices();
           
            Pixel[] screenVertices = projectVertices(vertices, d, hx, hy, width, height);
            Vector<Pixel> internPixels = scanlineConversion(screenVertices);

            for (int j = 0; j < internPixels.size(); j++) {
                double[] barycentricCoordinates = resolve(internPixels.get(j), screenVertices);
                Point pointApproximation = approximatePoint(vertices, barycentricCoordinates);
                checkzBuffer(zBuffer, vertices, pointApproximation, internPixels.get(j), barycentricCoordinates,
                    width, height, scene, context);
               // System.out.println("hehe");
            }
        }
    }

    public Pixel[] projectVertices(Point[] vertices, double d, double hx, double hy, double width, double height) {
        Pixel[] screenVertices = new Pixel[3];
        for (int i = 0; i < vertices.length && i < screenVertices.length; i++) {
            double x = (d/hx) * vertices[i].getX()/vertices[i].getZ();
            double y = (d/hy) * vertices[i].getY()/vertices[i].getZ();
            screenVertices[i] = new Pixel((int) Math.round((x + 1) * width / 2), (int) Math.round((1 - y) * height / 2));
        }

        return screenVertices;
    }

    public Vector<Pixel> scanlineConversion(Pixel[] screenVertices) {
        
        Vector<Pixel> pixels = new Vector<>();
        sort(screenVertices);
        Vector<Edge>[] edgeTable = initializeEdgeTable(screenVertices);
        
        System.out.println(screenVertices[0].getY() + " " + screenVertices[2].getY());
        Vector<Edge> activeEdgeTable = new Vector<>();
        for (int i = screenVertices[0].getY(); i < screenVertices[0].getY() + 100; i++) {
            update(activeEdgeTable, i);
            append(activeEdgeTable, edgeTable[i - screenVertices[0].getY()]);
            sort(activeEdgeTable);
            joinLines(activeEdgeTable, i, pixels);
            //System.out.println(activeEdgeTable.size());
        }

        return pixels;
    } 

    public void update(Vector<Edge> AET, int scanline) {
        for (int i = 0; i < AET.size(); i++) {
            AET.get(i).increaseX();
            if(AET.get(i).getY() == scanline) 
                AET.remove(i);
        }
       
    }

    public void append(Vector<Edge> AET, Vector<Edge> ET) {
        if(ET != null)
            AET.addAll(ET);
    }

    public void sort(Vector<Edge> AET) {
        AET.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge a, Edge b) {
                if(a.getX() < b.getX())
                    return -1;
                else if(a.getX() > b.getX())
                    return 1;
                else
                    return 0;
            }
        });
    }

    public void joinLines(Vector<Edge> AET, int scanline, Vector<Pixel> pixels) {
        if(AET.size() >= 2) {
            int Xmin = Math.min(AET.get(0).getX(), AET.get(1).getX());
            int Xmax = Math.max(AET.get(0).getX(), AET.get(1).getX());

            for (int i = Xmin; i <= Xmax; i++) {
                pixels.add(new Pixel(i, scanline));
            }
            
        }
    }
    
    public Vector<Edge>[] initializeEdgeTable(Pixel[] screenVertices) {
        Vector<Edge>[] ET = new Vector[screenVertices[2].getY() - screenVertices[0].getY()];
        
        // Aresta v1v0:
        Edge v1v0 = new Edge(screenVertices[0].getX(), screenVertices[1].getY(), 
            (screenVertices[1].getX() - screenVertices[0].getX()) / (screenVertices[1].getY() - screenVertices[0].getY()));

        // Aresta v2v1:
        Edge v2v1 = new Edge(screenVertices[1].getX(), screenVertices[2].getY(), 
            (screenVertices[2].getX() - screenVertices[1].getX()) / (screenVertices[2].getY() - screenVertices[1].getY()));

        // Aresta v2v0:
        Edge v2v0 = new Edge(screenVertices[0].getX(), screenVertices[2].getY(), 
            (screenVertices[2].getX() - screenVertices[0].getX()) / (screenVertices[2].getY() - screenVertices[0].getY()));

        int index = screenVertices[0].getY();
        ET[screenVertices[0].getY() - index] = new Vector<Edge>();
        ET[screenVertices[0].getY() - index].add(v1v0);
        ET[screenVertices[0].getY() - index].add(v2v0);
        ET[screenVertices[1].getY() - index] = new Vector<Edge>();
        ET[screenVertices[1].getY() - index].add(v2v1);
        return ET;
    }

    public void sort(Pixel[] screenVertices) {
            Arrays.sort(screenVertices, new Comparator<Pixel>() {
                @Override
                public int compare(Pixel a, Pixel b) {
                    if(a.getY() < b.getY())
                        return -1;
                    else if(a.getY() > b.getY())
                        return 1;
                    else
                        return 0;
                }
            });
    }

    public double[] resolve(Pixel pixel, Pixel[] screenVertices) {
        int x1 = pixel.getX() - screenVertices[2].getX();
        int y1 = pixel.getX() - screenVertices[2].getY(); 
        int x2 = screenVertices[0].getX() - screenVertices[2].getX();
        int y2 = screenVertices[0].getY() - screenVertices[2].getY();
        int x3 = screenVertices[1].getX() - screenVertices[2].getX();
        int y3 = screenVertices[1].getY() - screenVertices[2].getY();

        double a2 = (x1*y1 - y2*x1 + y1*x3) / y3*x2;
        double a1 = ((x1 - x3) / x2) * a2;
        double a3 = 1 - a1 - a2;
        return new double[] {a1, a2, a3};
    }

    public Point approximatePoint(Point[] vertices, double[] barycentricCoordinates) {
        double x = 0;
        double y = 0;
        double z = 0;
        for (int i = 0; i < 3; i++) {
            x += vertices[i].getX() * barycentricCoordinates[i];
            y += vertices[i].getY() * barycentricCoordinates[i];
            z += vertices[i].getZ() * barycentricCoordinates[i];
        }
        return new Point(x, y, z);
    }

    public void checkzBuffer(double[][] zBuffer, Point[] vertices, Point point, Pixel pixel, 
        double[] barycentricCoordinates, double width, double height, Illumination scene, GraphicsContext context) {

        boolean hasDiffuseComponent = true;
        boolean hasSpecularComponent = true;
        Point N, L, V, R = null;

        int x = pixel.getX();
        int y = pixel.getY();
        if(x < width && x >= 0 && y >= 0 && y < height) {
            System.out.println("oiss");
            if(point.getZ() < zBuffer[x][y])
                zBuffer[x][y] = point.getZ();
                N = calculateNormal(vertices, barycentricCoordinates).normalize();
                L = scene.getCoordinates().subtract(point).normalize();
                V = point.multiply(-1).normalize(); 
                if(V.scalarProduct(N) < 0)
                    N = N.multiply(-1);
                if(N.scalarProduct(L) < 0) {
                    hasDiffuseComponent = false;
                    hasSpecularComponent = false;
                } else {
                    R = N.multiply(2 * N.scalarProduct(L)).subtract(L).normalize();
                    if(R.scalarProduct(V) < 0)
                        hasSpecularComponent = false;
                }
                shading(pixel, scene, context, N, L, R, V);
        }
    }
    
    public Point calculateNormal(Point[] vertices, double[] barycentricCoordinates) {
        double[] N0 = vertices[0].getNormal();
        double[] N1 = vertices[1].getNormal();
        double[] N2 = vertices[2].getNormal();
        double x = barycentricCoordinates[0] * N0[0] + barycentricCoordinates[1] * N1[0] + barycentricCoordinates[2] * N2[0];
        double y = barycentricCoordinates[0] * N0[1] + barycentricCoordinates[1] * N1[1] + barycentricCoordinates[2] * N2[1];
        double z = barycentricCoordinates[0] * N0[2] + barycentricCoordinates[1] * N1[2] + barycentricCoordinates[2] * N2[2];          
        return new Point(x, y, z);
    }

    public void shading(Pixel pixel, Illumination scene, GraphicsContext context, Point N, Point L, Point R, Point V) {
        System.out.println("oie");
        Point light = scene.shading(N, L, R, V);
        context.setFill(Color.rgb((int) light.getX(), (int) light.getY(), (int) light.getZ()));
        context.fillRect(pixel.getX(), pixel.getY(), 1, 1);
    }
}