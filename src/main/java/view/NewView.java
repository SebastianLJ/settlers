package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Game;
import model.board.*;

public class NewView {

    public static final int PLAYER_ONE = 0;
    public static final int PLAYER_TWO = 1;
    public static final int PLAYER_THREE = 2;
    public static final int PLAYER_FOUR = 3;
    private final int screenSize = 1000;

    private final int HEX_SIZE = screenSize/10;
    private final double HEX_WIDTH = Math.sqrt(3)*HEX_SIZE;
    private final double HEX_HEIGHT = 2*HEX_SIZE;

    // Coordinates for center tiles
    private final int centerTileX = 2, centerTileY = 2;

    // Offset calculated from the center tile
    private final double centerCoordX = HEX_WIDTH*centerTileX + 0.5*HEX_WIDTH*centerTileY, centerCoordY = 3*HEX_HEIGHT/4*centerTileY;

    private final double offsetX = screenSize/2.0 - centerCoordX, offsetY = screenSize/2.0 - centerCoordY;


    private Game game;
    private Hex[][] hexes;

    public NewView(Game game) {
        this.game = game;
        hexes = game.getBoard().getHexes();

    }

    public void update(Group root) {
        // Draw vertices
        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {
                    drawVertices(root, hex);
                    drawPaths(root, hex);


                }


            }
        }




    }

    private void drawPaths(Group root, Hex hex) {
        Point[] points = getAdjacentVerticesLocation(hex);
        Edge[] edges = game.getBoard().getAdjacentEdges(hex);

        for (int k = 0; k < edges.length; k++) {
            if (edges[k].getId() == -1) {
                continue;
            }
            Line line = new Line(points[k].getX(), points[k].getY(), points[(k+1)%6].getX(), points[(k+1)%6].getY());
            line.setStrokeWidth(HEX_SIZE/10.);
            root.getChildren().add(line);

            Paint paint = getColorFromPlayerID(edges[k].getId());
            line.setStroke(paint);
        }
    }

    private void drawVertices(Group root, Hex hex) {
        Point[] points = getAdjacentVerticesLocation(hex);
        Vertex[] vertices = game.getBoard().getAdjacentVertices(hex);

        for (int k = 0; k < vertices.length; k++) {
            if (vertices[k].getId() == -1) {
                continue;
            }

            Circle circle = new Circle(points[k].getX(), points[k].getY(), hex.getSize()/8.);

            Paint playerColor = getColorFromPlayerID(vertices[k].getId());
            circle.setFill(playerColor);

            if (vertices[k].isCity()) {
                circle.setRadius(hex.getSize()/4.);
            }
            root.getChildren().add(circle);
        }
    }

    private Paint getColorFromPlayerID(int playerID) {
        Paint paint = Color.TRANSPARENT;
        switch (playerID) {
            case PLAYER_ONE:
                paint = Color.RED;
                break;
            case PLAYER_TWO:
                paint = Color.BLUE;
                break;
            case PLAYER_THREE:
                paint = Color.GREEN;
                break;
            case PLAYER_FOUR:
                paint = Color.YELLOW;
                break;
        }
        return paint;
    }

    private Point[] getAdjacentVerticesLocation(Hex hex) {
        return hex.getAdjacentVerticesLocation();
    }

    // Shifts y-coordinate in hex to x,y-basis
    private double getHexCenterY(Hex hex) {
        return hex.getRealY();
    }

    // Shifts x-coordinate in hex to x,y-basis
    private double getHexCenterX(Hex hex) {
        return hex.getRealX();
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
