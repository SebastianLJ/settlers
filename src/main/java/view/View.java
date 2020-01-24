package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.Game;
import model.board.*;

public class View {

    private static final int PLAYER_ONE = 0;
    private static final int PLAYER_TWO = 1;
    private static final int PLAYER_THREE = 2;
    private static final int PLAYER_FOUR = 3;

    private Game game;
    private Hex[][] hexes;
    private Circle circle;

    private int thisPlayer;

    public View(Game game) {
        this.game = game;
        hexes = game.getBoard().getHexes();
    }

    public void initializeRobber(Group map) {
        circle = new Circle();
        map.getChildren().add(circle);
    }

    public void update(Group map) {
        Hex hex;
        for (Hex[] value : hexes) {
            for (int j = 0; j < hexes.length; j++) {
                hex = value[j];
                if (hex != null) {
                    // Draw vertices
                    drawVertices(map, hex);
                    drawPaths(map, hex);
                    drawRobber();
                }
            }
        }
    }

    public void updateDiceRoll(Label diceRoll) {
        diceRoll.setText(game.getDiceRoll());
    }

    public void updatePlayerInfo(GridPane ownPlayerInfo, GridPane[] otherPlayerInfo) {
        thisPlayer = updateOwnPlayerInfo(ownPlayerInfo);
        updateOtherPlayerInfo(otherPlayerInfo);
    }

    private void updateOtherPlayerInfo(GridPane[] otherPlayerInfo) {
        int c = 0;
        for (int i = 0; i < 4; i++) {
            if (i != thisPlayer) {
                GridPane info = otherPlayerInfo[c];

                colorIndicateForTurn(i, info);

                if (i < game.getPlayerCount()) {
                    ((Label) info.getChildren().get(4)).setText(game.queryPlayer(i).getName());
                    ((Label) info.getChildren().get(5)).setText(Integer.toString(game.getVictoryPoints(i)));
                    ((Label) info.getChildren().get(6)).setText((Integer.toString(game.queryPlayer(i).getDevelopmentCards().size())));
                    ((Label) info.getChildren().get(7)).setText(Integer.toString(game.getResources(i).size()));
                }
                c++;
            }
        }
    }

    public int updateOwnPlayerInfo(GridPane ownPlayerInfo) {
        int thisPlayer = game.getPlayerId();
        colorIndicateForTurn(thisPlayer, ownPlayerInfo);

        ((Label) ownPlayerInfo.getChildren().get(5)).setText(Integer.toString(game.getVictoryPoints(thisPlayer)));

        // TODO Get dev cards
        //((Label) getNodeFromGridPane(ownPlayerInfo, 1,2)).setText();

        GridPane resources =  (GridPane) ownPlayerInfo.getChildren().get(7);

        String[] amountOfPlayerResources = {Integer.toString(game.queryPlayer(thisPlayer).getResourceAmount(Resource.Ore)),
                Integer.toString(game.queryPlayer(thisPlayer).getResourceAmount(Resource.Brick)),
                Integer.toString(game.queryPlayer(thisPlayer).getResourceAmount(Resource.Wool)),
                Integer.toString(game.queryPlayer(thisPlayer).getResourceAmount(Resource.Grain)),
                Integer.toString(game.queryPlayer(thisPlayer).getResourceAmount(Resource.Lumber))};

        for (int i = 0; i < 5; i++) {
            ((Label) resources.getChildren().get(5 + i)).setText(amountOfPlayerResources[i]);
        }
        return thisPlayer;
    }

    private void colorIndicateForTurn(int i, GridPane info) {
        if (i == game.getTurnId()) {
            info.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
            info.setStyle("-fx-border-color: black");
        } else {
            info.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            info.setStyle("-fx-border-color: none");
        }
    }

    private void drawRobber() {
        Hex robberHex = game.getBoard().getCurrentRobberPosHex();
        circle.setCenterX(robberHex.getRealX());
        circle.setCenterY(robberHex.getRealY());
        circle.setRadius(robberHex.getSize() / 4);
        circle.setStroke(Color.WHITE);
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
            circle.setStroke(Color.BLACK);

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
                paint = Color.DEEPPINK;
                break;
            case PLAYER_FOUR:
                paint = Color.YELLOW;
                break;
        }
        return paint;
    }

    public void updateChat(ListView<String> listView) {
        ObservableList<String> chatEvents = FXCollections.observableArrayList();
        listView.setItems(chatEvents);
        chatEvents.addAll(game.getChat());
    }
}
