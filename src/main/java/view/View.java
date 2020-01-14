package view;

import controller.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.board.Hex;
import model.board.Preset;
import model.board.Terrain;
import model.board.Vertex;

import java.io.IOException;

import static java.lang.Math.atan2;
import static java.lang.Math.floor;

public class View extends Application {

    private final int screenSize = 400;

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
        Polygon polygon = new Polygon(0, 0, screenSize, 0, screenSize, screenSize, 0, screenSize);
        polygon.setFill(Color.DEEPSKYBLUE);
        root.getChildren().add(polygon);


        // TODO Make game depending on users choices in lobby UI
        controller.createGame("");

        //Hex[][] hexes = controller.getGame().getBoard().getHexes();

        Hex[][] hexes = new Hex[5][5];

        // ONLY FOR UI TESTING PURPOSES
        //setup hexes
        int c = 0;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                if (j > hexes.length/2 - 1 - i && j < hexes.length + hexes.length/2 - i) {
                    hexes[i][j] = new Hex(j, i, Preset.getTerrain()[c], Preset.getNumberTokens()[c]);
                    c++;
                } else hexes[i][j] = null;
            }
        }


        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {
                    polygon = createPolygonFromHex(hex);
                    polygon.setId(i + " " + j);

                    //
                    Polygon finalPolygon = polygon;
                    Hex finalHex = hex;


                    polygon.setOnMouseClicked(mouseEvent ->
                            handleMouseEvent(mouseEvent, finalHex, finalPolygon));
                    //

                    root.getChildren().add(polygon);
                }
            }
        }

        //controller.getGame().getBoard().printHexes();


        Scene scene = new Scene(root,screenSize,screenSize);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMouseEvent(MouseEvent mouseEvent, Hex hex, Polygon polygon) {
        int i = Integer.parseInt(getId(polygon)[0]);
        int j = Integer.parseInt(getId(polygon)[1]);

        double touchAngle = getAngleFromTouch(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex));

        System.out.println(getChosenIntersection(i, j, touchAngle));

        /*System.out.println("This is hex [" + getId(polygon)[0] + "][" + getId(polygon)[1] + "] " +
                "at coordinates (" + getHexCenterX(hex) + ", " + getHexCenterY(hex) + "). The screen was touched in an angle of "
                + getAngleFromTouch(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex)));*/
    }

    private String getChosenIntersection(int i, int j, double angle) {
        Vertex[] vertices = controller.getGame().getBoard().getAdjacentVertices(new Hex(j,i, Terrain.None, -1));
        System.out.println("angle: " + angle + " index: " + (int) floor(angle)/60);
        Vertex chosenVertex = vertices[0];
        return chosenVertex.getId() + "";
    }

    private double getAngleFromTouch(double touch_x, double touch_y, double center_x, double center_y) {
        double delta_x = touch_x - center_x;
        double delta_y = touch_y - center_y;
        double theta_radians = atan2(delta_y, delta_x);

        double theta_degrees = theta_radians*180/Math.PI;

        if (theta_degrees < 0) {
            return theta_degrees + 360;
        } else {
            return theta_degrees;
        }
    }


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

    private double getHexCenterY(Hex hex) {
        return 3*HEX_HEIGHT/4*hex.getY() + offsetY;
    }

    private double getHexCenterX(Hex hex) {
        return HEX_WIDTH*hex.getX() + 0.5*HEX_WIDTH*hex.getY() + offsetX;
    }

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

    private String[] getId(Polygon polygon) {
        return polygon.getId().split(" ");
    }
}
