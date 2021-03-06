import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

import javafx.application.Application;

public class Main extends Application {

    private static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private static GraphicsContext context;

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, Color.BLACK);
            primaryStage = definePrimaryStage(primaryStage, scene);

            final Canvas canvas = defineCanvas(primaryStage.getWidth(), primaryStage.getHeight());
            context = canvas.getGraphicsContext2D();
            
            root.getChildren().add(canvas);
            primaryStage.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public Stage definePrimaryStage(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Pressione ESC para sair.");
        primaryStage.setTitle("Textura Aleat\u00F3ria via Colora\u00E7\u00E3o");
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if(KeyCode.ESCAPE == event.getCode())
                primaryStage.close();
        });
        return primaryStage;
    }

    public Canvas defineCanvas(double width, double height) {
        Canvas canvas = new Canvas(1000, 500);
        canvas.setWidth(width);
        canvas.setHeight(height);
        return canvas;
    }

    public static void main(String[] args) {
        Illumination scene = loadScene();
        Camera camera = loadCamera();
        SceneObject object = loadObject();
        
        // Matriz de mudança de base: coordenadas de mundo para coordenadas de vista.
        double[][] worldToView = worldToViewMatrix(camera.getU(), camera.getV(), camera.getN());
        scene.toViewCoordinates(worldToView, camera.getCoordinates());
        object.toViewCoordinates(worldToView, camera.getCoordinates());

        // Normalização dos pontos.
        object.normalize();



       
        // Rasterização e coloração.
        double[][] zBuffer = initializeZBuffer();
        object.rasterize(zBuffer, camera.getD(), camera.getHx(), camera.getHy(), screenWidth, screenHeight, scene, context);
            
        launch(args);

        System.out.println("oiss");
        // // Projeção para coordenadas de tela.
        // object.toScreenCoordinates(camera.getD(), camera.getHx(), camera.getHy(), screenWidth, screenHeight);

  
    }

    public static double[][] initializeZBuffer() {
        double[][] buffer = new double[(int) screenWidth][(int) screenHeight];
        for (int i = 0; i < buffer.length; i++) 
            for (int j = 0; j < buffer[i].length; j++) 
                buffer[i][j] = Double.POSITIVE_INFINITY;
        return buffer;
    }

    public static Illumination loadScene() {
        Illumination scene = null;

        try {
            Scanner fileLoader = new Scanner(new File("../iluminacao.txt")).useLocale(Locale.US);
            
            Point coordinates = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            double ka = fileLoader.nextDouble();
            Point Ia = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            double kd = fileLoader.nextDouble();
            Point Od = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            double ks = fileLoader.nextFloat();
            Point Il = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            double n = fileLoader.nextDouble();

            scene = new Illumination(coordinates, Il, ka, Ia, kd, Od, ks, n);
            fileLoader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Valores de cena nao carregados!");
            System.exit(1);
        }

        System.out.println("Valores de cena carregados!");
        return scene;
    }
    
    public static Camera loadCamera() {
        Camera camera = null;

        try {
            Scanner fileLoader = new Scanner(new File("../camera.cfg")).useLocale(Locale.US);
            
            Point coordinates = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            Point N = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            Point V = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            double d = fileLoader.nextDouble();
            double hx = fileLoader.nextDouble();
            double hy = fileLoader.nextDouble();

            camera = new Camera(coordinates, N, V, d, hx, hy);
            fileLoader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Camera nao carregada!");
            System.exit(1);
        }

        System.out.println("Camera carregada!");
        return camera;
    }

    public static SceneObject loadObject() {
        SceneObject object = null;

        try {
            Scanner fileLoader = new Scanner(new File("../tui.byu")).useLocale(Locale.US);
            
            int numPoints, numTriangles;
            numPoints = fileLoader.nextInt();
            numTriangles = fileLoader.nextInt();

            Point[] points = new Point[numPoints]; 
            for (int i = 0; i < numPoints; i++) 
                points[i] = new Point(fileLoader.nextDouble(), fileLoader.nextDouble(), fileLoader.nextDouble());
            
            Triangle[] triangles = new Triangle[numTriangles];
            for (int i = 0; i < numTriangles; i++) {
                int v0 = fileLoader.nextInt() - 1;
                int v1 = fileLoader.nextInt() - 1;
                int v2 = fileLoader.nextInt() - 1;
                triangles[i] = new Triangle(v0, v1, v2);
            }

            object = new SceneObject(points, triangles);
            fileLoader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Objeto não carregado!");
            System.exit(1);
        }

        System.out.println("Objeto carregado!");
        return object;
    }

    public static double[][] worldToViewMatrix(Point u, Point v, Point n) {
        double[][] worldToView = new double[3][3];
        
        // Vetor U.
        worldToView[0][0] = u.getX();
        worldToView[0][1] = u.getY();
        worldToView[0][2] = u.getZ();

        // Vetor V.
        worldToView[1][0] = v.getX();
        worldToView[1][1] = v.getY();
        worldToView[1][2] = v.getZ();

        // Vetor N.
        worldToView[2][0] = n.getX();
        worldToView[2][1] = n.getY();
        worldToView[2][2] = n.getZ();

        System.out.println("Matriz de mudanca de coordenadas pronta!");
        return worldToView;
    }

}