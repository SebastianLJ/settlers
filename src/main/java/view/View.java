package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.board.*;

import java.io.IOException;

import static java.lang.Math.*;

public class View extends Application {

    private final int screenSize = 1000;

    private final int HEX_SIZE = screenSize/10;
    private final double HEX_WIDTH = Math.sqrt(3)*HEX_SIZE;
    private final double HEX_HEIGHT = 2*HEX_SIZE;

    // Coordinates for center tiles
    private final int centerTileX = 2, centerTileY = 2;

    // Offset calculated from the center tile
    private final double centerCoordX = HEX_WIDTH*centerTileX + 0.5*HEX_WIDTH*centerTileY, centerCoordY = 3*HEX_HEIGHT/4*centerTileY;

    private final double offsetX = screenSize/2.0 - centerCoordX, offsetY = screenSize/2.0 - centerCoordY;

    private Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        controller = new Controller(this);

        Group root = new Group();
        primaryStage.setTitle("Settlers of Catan");

        // TODO Make game depending on users choices in lobby UI
        controller.createGame("");

        Hex[][] hexes = controller.getGame().getBoard().getHexes();
        setupHexUI(root, hexes);









        Scene scene = new Scene(root,screenSize,screenSize, Color.DEEPSKYBLUE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupHexUI(Group root, Hex[][] hexes) {
        Polygon polygon;
        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {
                    polygon = createPolygonFromHex(hex);
                    polygon.setId(i + " " + j);

                    Hex finalHex = hex;

                    polygon.setOnMouseClicked(mouseEvent ->
                            handleMouseEvent(mouseEvent, finalHex));

                    StackPane stack = new StackPane();
                    stack.getChildren().add(polygon);
                    Label label = new Label("NUM");
                    stack.getChildren().add(label);
                    root.getChildren().add(polygon);
                }
            }
        }
    }

    /**
     * Finds the appropriate game object (ie. Path, Intersection etc).
     * @param mouseEvent from listener connected to polygon
     * @param hex connected to polygon
     */
    private void handleMouseEvent(MouseEvent mouseEvent, Hex hex) {
        int i = hex.getY();
        int j = hex.getX();

        double touchAngle = getAngleFromScreenClick(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex));

        System.out.println(getChosenIntersection(i, j, touchAngle));
        System.out.println(getChosenPath(i, j, touchAngle));

        /*System.out.println("This is hex [" + getId(polygon)[0] + "][" + getId(polygon)[1] + "] " +
                "at coordinates (" + getHexCenterX(hex) + ", " + getHexCenterY(hex) + "). The screen was touched in an angle of "
                + getAngleFromScreenClick(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex)));*/
    }

    /**
     * Get the path chosen by the user via a screen click
     * @param i i index in hexes[i][j]
     * @param j j index in hexes[i][j]
     * @param angle angle calculated by @getAngleFromScreenClick
     * @return // TODO TBD.
     */
    private String getChosenPath(int i, int j, double angle) {
        int edgeIndex = (int) (floor(angle) + 30) % 360 / 60;
        //Edge[] edges = controller.getGame().getBoard().getAdjacentEdges(new Hex(j, i, Terrain.None, -1));

        //Edge chosenEdge = edges[edgeIndex];
        return "EDGE INFO: i-value: " + i + " j-value: " + j + " angle: " + angle + " index: " + edgeIndex;
    }

    /**
     * Get the intersection chosen by the user via a screen click
     * @param i i index in hexes[i][j]
     * @param j j index in hexes[i][j]
     * @param angle angle calculated by @getAngleFromScreenClick
     * @return // TODO TBD.
     */
    private String getChosenIntersection(int i, int j, double angle) {
        int vertexIndex = (int) floor(angle)/60;
        Vertex[] vertices = controller.getGame().getBoard().getAdjacentVertices(new Hex(j,i, Terrain.None, -1));

        // Do some verification of chosen vertex..
        Vertex chosenVertex = vertices[vertexIndex];
        return "VERTEX INFO: i-value: " + i + " j-value: " + j + " angle: " + angle + " index: " + vertexIndex;
    }


    /**
     * Calculates and returns the angle from the center of the clicked hexagon to the actual mouse click.
     * @param click_x x-coordinate of click
     * @param click_y y-coordinate of click
     * @param center_x x-coordinate of hex center
     * @param center_y y-coordinate of hex center
     * @return angle from center to click, as described by https://www.redblobgames.com/grids/hexagons/#basics
     */
    private double getAngleFromScreenClick(double click_x, double click_y, double center_x, double center_y) {
        double delta_x = click_x - center_x;
        double delta_y = click_y - center_y;
        double theta_radians = atan2(delta_y, delta_x);

        double theta_degrees = theta_radians*180/Math.PI;

        if (theta_degrees < 0) {
            return theta_degrees + 360;
        } else {
            return theta_degrees;
        }
    }


    /**
     * Creates a Shape.Polygon in the x,y-basis from hex-basis
     * @param hex input hex containing x,y-hex
     * @return new polygon object with coordinates made from hex
     */
    private Polygon createPolygonFromHex(Hex hex) {
        // HexCoordinates to x,y-coordinates, with center offset
        double x = getHexCenterX(hex);
        double y = getHexCenterY(hex);

        Polygon polygon = new Polygon(x,y + HEX_HEIGHT/2, x + HEX_WIDTH/2, y + HEX_HEIGHT/4, x + HEX_WIDTH/2, y - HEX_HEIGHT/4,
                                x, y - HEX_HEIGHT/2, x - HEX_WIDTH/2, y - HEX_HEIGHT/4, x - HEX_WIDTH/2, y + HEX_HEIGHT/4);

        polygon.setStroke(Color.BLACK);

        // Get the color for the given hex depending on which terrain/resource type it contains
        Paint color = getColorFromTerrain(hex.getTerrain());

        polygon.setFill(color);

        return polygon;
    }

    // Shifts y-coordinate in hex to x,y-basis
    private double getHexCenterY(Hex hex) {
        return 3*HEX_HEIGHT/4*hex.getY() + offsetY;
    }

    // Shifts x-coordinate in hex to x,y-basis
    private double getHexCenterX(Hex hex) {
        return HEX_WIDTH*hex.getX() + 0.5*HEX_WIDTH*hex.getY() + offsetX;
    }

    // Finds the correct color depending on the terrain
    private Paint getColorFromTerrain(Terrain terrain) {
        Paint color;

        switch (terrain) {
            case Fields:
                color = Color.WHEAT;
                break;
            case Forest:
                color = Color.FORESTGREEN;
                break;
            case Pasture:
                color = Color.LAWNGREEN;
                break;
            case Hills:
                color = Color.FIREBRICK;
                break;
            case Mountains:
                color = Color.LIGHTGREY;
                break;
            case Desert:
                color = Color.SANDYBROWN;
                break;
            default:
                color = Color.BLACK;
        }
        return color;
    }
}
