import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, Color.BLACK);
            primaryStage = definePrimaryStage(primaryStage, scene);

            final Canvas canvas = defineCanvas(primaryStage.getWidth(), primaryStage.getHeight());
            
            root.getChildren().add(canvas);
            primaryStage.show();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public Stage definePrimaryStage(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight());
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
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
		launch(args);
	}

}