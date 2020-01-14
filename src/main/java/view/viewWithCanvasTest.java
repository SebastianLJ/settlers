package view;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class viewWithCanvasTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene s = new Scene(root, 300, 300, Color.BLACK);

        final Canvas canvas = new Canvas(250,250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLUE);
        gc.fillRect(75,75,100,100);

        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("hej", 125, 125);

        root.getChildren().add(canvas);
        stage.setScene(s);
        stage.show();
    }
}
