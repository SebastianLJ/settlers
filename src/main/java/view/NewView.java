package view;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import model.Game;
import model.board.Hex;
import model.board.Terrain;
import model.newGame;

public class NewView {

    private final int screenSize = 1000;

    private final int HEX_SIZE = screenSize/10;
    private final double HEX_WIDTH = Math.sqrt(3)*HEX_SIZE;
    private final double HEX_HEIGHT = 2*HEX_SIZE;

    // Coordinates for center tiles
    private final int centerTileX = 2, centerTileY = 2;

    // Offset calculated from the center tile
    private final double centerCoordX = HEX_WIDTH*centerTileX + 0.5*HEX_WIDTH*centerTileY, centerCoordY = 3*HEX_HEIGHT/4*centerTileY;

    private final double offsetX = screenSize/2.0 - centerCoordX, offsetY = screenSize/2.0 - centerCoordY;


    private newGame game;
    private Hex[][] hexes;

    public NewView(newGame game) {
        this.game = game;
        hexes = game.getBoard().getHexes();

    }

    public void update() {
        // TODO Find out if needed
        /*Polygon polygon;
        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {


                    double x = getHexCenterX(hex);
                    double y = getHexCenterY(hex);

                    polygon = new Polygon(x, y + HEX_HEIGHT / 2, x + HEX_WIDTH / 2, y + HEX_HEIGHT / 4, x + HEX_WIDTH / 2, y - HEX_HEIGHT / 4,
                            x, y - HEX_HEIGHT / 2, x - HEX_WIDTH / 2, y - HEX_HEIGHT / 4, x - HEX_WIDTH / 2, y + HEX_HEIGHT / 4);

                    polygon.setId(i + " " + j);

                    polygon.setStroke(Color.BLACK);

                    // Get the color for the given hex depending on which terrain/resource type it contains
                    Paint color = getColorFromTerrain(hex.getTerrain());

                    polygon.setFill(color);
                }
            }
        }*/
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
