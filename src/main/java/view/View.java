package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.board.Hex;
import model.board.Preset;
import model.board.Terrain;

import java.io.IOException;

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
        //controller.createGame("");

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
                    root.getChildren().add(polygon);
                }
            }
        }


        Scene scene = new Scene(root,screenSize,screenSize);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private Polygon createPolygonFromHex(Hex hex) {
        // HexCoordinates to x,y-coordinates, with center offset
        double x = HEX_WIDTH*hex.getX() + 0.5*HEX_WIDTH*hex.getY() + offsetX;
        double y = 3*HEX_HEIGHT/4*hex.getY() + offsetY;

        Polygon polygon = new Polygon(x,y + HEX_HEIGHT/2, x + HEX_WIDTH/2, y + HEX_HEIGHT/4, x + HEX_WIDTH/2, y - HEX_HEIGHT/4,
                                x, y - HEX_HEIGHT/2, x - HEX_WIDTH/2, y - HEX_HEIGHT/4, x - HEX_WIDTH/2, y + HEX_HEIGHT/4);

        polygon.setStroke(Color.BLACK);

        // Get the color for the given hex depending on which terrain/resource type it contains
        Paint color = getColorFromTerrain(hex.getTerrain());

        polygon.setFill(color);

        return polygon;
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
}
