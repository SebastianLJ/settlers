package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Game;
import model.board.*;

import java.util.Arrays;

public class NewView {

    private static final int PLAYER_ONE = 0;
    private static final int PLAYER_TWO = 1;
    private static final int PLAYER_THREE = 2;
    private static final int PLAYER_FOUR = 3;

    private Game game;
    private Hex[][] hexes;

    public NewView(Game game) {
        this.game = game;
        hexes = game.getBoard().getHexes();
    }

    public void update(Group root) {
        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {
                    // Draw vertices
                    drawVertices(root, hex);
                    drawPaths(root, hex);
                    drawRobber(root);


                }
            }
        }
    }

    private void drawRobber(Group root) {
        Hex robberHex = game.getBoard().getCurrentRobberPosHex();
        Circle circle = new Circle(robberHex.getRealX(), robberHex.getRealY(), robberHex.getSize()/4.);
        root.getChildren().add(circle);
    }

    private void drawPaths(Group root, Hex hex) {
        Point[] points = hex.getAdjacentVerticesLocation();
        Edge[] edges = game.getBoard().getAdjacentEdges(hex);

        for (int k = 0; k < edges.length; k++) {
            if (edges[k].getId() == -1) {
                continue;
            }
            Line line = new Line(points[k].getX(), points[k].getY(), points[(k+5)%6].getX(), points[(k+5)%6].getY());
            line.setStrokeWidth(hex.getSize() / 10.);
            root.getChildren().add(line);

            Paint paint = getColorFromPlayerID(edges[k].getId());
            line.setStroke(paint);
        }
    }

    private void drawVertices(Group root, Hex hex) {
        Point[] points = hex.getAdjacentVerticesLocation();
        Vertex[] vertices = game.getBoard().getAdjacentVertices(hex);

        for (int k = 0; k < vertices.length; k++) {
            if (vertices[k].getId() == -1) {
                continue;
            }

            Circle circle = new Circle(points[k].getX(), points[k].getY(), hex.getSize() / 8.);


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
}
