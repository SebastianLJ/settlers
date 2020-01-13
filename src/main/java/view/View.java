package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class View extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Polygon Example");


        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(0.0, 0.0,
                200.0, 200.0,
                200.0, 100.0);

        root.getChildren().add(polygon);
        Scene scene = new Scene(root,300,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
