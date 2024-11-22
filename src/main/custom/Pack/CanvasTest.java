package Pack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CanvasTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Canvas Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 800, 600);

        gc.setFill(Color.BLUE);
        gc.fillRect(100, 100, 200, 150);

        gc.setFill(Color.RED);
        gc.fillOval(400, 200, 100, 100);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(50, 50, 750, 550);

        gc.setFill(Color.GREEN);
        gc.fillText("Canvas Test", 350, 50);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
