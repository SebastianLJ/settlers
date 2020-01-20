package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;
import model.GameState;
import model.board.*;
import view.NewView;

import java.io.IOException;
import java.util.List;

import static java.lang.Math.atan2;
import static java.lang.Math.floor;
import static model.GameState.*;

public class NewController extends Application {
    private double offsetX, offsetY;
    private double HEX_SIZE;
    private double HEX_HEIGHT;
    private double HEX_WIDTH;

    private Game game;
    private NewView view;
    private GameState gameState = GameState.BuildRoad;
    private boolean initialState = true;
    private int endedInitTurnCount = 0;

    @FXML
    private Button buildRoad, buildSettlement, buildCity, buildDevCard,
            tradeWithBank, tradeWithPlayer, playDevCard, viewDevCard,
            rollDices, endTurnButton;

    private Button createGameButton;
    private TextField textField;

    public Label playerNameLabel;
    public Label victoryPointsLabel;
    public Label diceRollLabel;

    private Group map;
    private String playerName;

    private int diceRoll;
    public GridPane ownPlayerInfo;
    private GridPane[] otherPlayerInfo = new GridPane[3];
    @FXML
    private ListView<String> chatView;

    @FXML
    private TextArea chatTextField;
    @FXML
    private Button chatButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Settlers of Catan - Lobby");
        FXMLLoader loader = new FXMLLoader(NewController.class.getClassLoader().getResource("LobbyView.fxml"));
        AnchorPane lobby = loader.load();

        textField = (TextField) loader.getNamespace().get("nameField");

        Button joinGameButton = (Button) loader.getNamespace().get("JoinGameButton");
        joinGameButton.setOnMouseClicked(mouseEvent -> {
            try {
                playerName = textField.getText();
                showJoinGameDialog(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createGameButton = (Button) loader.getNamespace().get("CreateGameButton");
        createGameButton.setOnMouseClicked(mouseEvent -> {
            try {
                createGame(primaryStage, true, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private double initializeScene(Stage primaryStage) throws java.io.IOException {
        primaryStage.setTitle("Settlers of Catan");
        FXMLLoader loader = new FXMLLoader(NewController.class.getClassLoader().getResource("AdvancedGameView.fxml"));
        SplitPane root = loader.load();

        map = new Group();

        VBox gameView = (VBox) loader.getNamespace().get("gameView");
        HBox diceView = (HBox) gameView.getChildren().get(0);

        gameView.getChildren().add(map);
        Scene scene = new Scene(root);

        initiateButtonListeners(loader);

        playerNameLabel = (Label) loader.getNamespace().get("playerNameLabel");
        playerNameLabel.setText(playerName);

        diceRollLabel = (Label) loader.getNamespace().get("diceRollLabel");

        ownPlayerInfo = (GridPane) loader.getNamespace().get("ownPlayerInfo");

        for (int i = 0; i < 3; i++) {
            otherPlayerInfo[i] = (GridPane) loader.getNamespace().get("otherPlayerInfo" + i);
        }

        chatView = (ListView<String>) loader.getNamespace().get("chatView");
        chatTextField = (TextArea) loader.getNamespace().get("chatTextField");
        chatButton = (Button) loader.getNamespace().get("chatButton");
        chatButton.setOnMouseClicked(mouseEvent ->
                {
                    try {
                        game.sendChat(chatTextField.getText());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chatTextField.clear();
                }
        );

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);

        double mapSize = gameView.getHeight() - diceView.getHeight();
        gameView.setMaxWidth(mapSize);

        root.setDividerPositions(mapSize/root.getWidth());
        return mapSize;
    }

    private void initiateButtonListeners(FXMLLoader loader) {
        buildRoad = (Button) loader.getNamespace().get("buildRoad");
        buildRoad.setOnAction(actionEvent -> gameState = GameState.BuildRoad);

        buildSettlement = (Button) loader.getNamespace().get("buildSettlement");
        buildSettlement.setOnAction(actionEvent -> gameState = GameState.BuildSettlement);

        buildCity = (Button) loader.getNamespace().get("buildCity");
        buildCity.setOnAction(actionEvent -> gameState = GameState.BuildCity);

        buildDevCard = (Button) loader.getNamespace().get("buildDevCard");
        buildDevCard.setOnAction(actionEvent -> gameState = GameState.BuyDevelopmentCard);

        tradeWithBank = (Button) loader.getNamespace().get("tradeWithBank");
        tradeWithBank.setOnAction(actionEvent -> gameState = GameState.TradeBank);

        tradeWithPlayer = (Button) loader.getNamespace().get("tradeWithPlayer");
        tradeWithPlayer.setOnAction(actionEvent -> gameState = GameState.TradePlayer);

        playDevCard = (Button) loader.getNamespace().get("playDevCard");
        //todo play dev card

        viewDevCard = (Button) loader.getNamespace().get("viewDevCard");
        //todo

        rollDices = (Button) loader.getNamespace().get("rollDices");
        rollDices.setOnAction(actionEvent -> {
            if (game.yourTurn()) {
                diceRoll = game.roll();
                diceRollLabel.setText(Integer.toString(diceRoll));
                //setButtonsDisable(false);
                //rollDices.setDisable(true);
            }
        });

        endTurnButton = (Button) loader.getNamespace().get("endTurn");
        endTurnButton.setOnAction(actionEvent -> {
            //endTurnButton.setDisable(true);
            //setButtonsDisable(true);
            endTurn();
        });
    }

    private void setButtonsDisable(boolean bool) {
        buildRoad.setDisable(bool);
        buildSettlement.setDisable(bool);
        buildCity.setDisable(bool);
        buildDevCard.setDisable(bool);
        tradeWithBank.setDisable(bool);
        tradeWithPlayer.setDisable(bool);
        playDevCard.setDisable(bool);
        endTurnButton.setDisable(bool);
    }

    private void initializeOffsets(double mapSize, Hex[][] hexes) {
        HEX_SIZE = mapSize / 10;
        HEX_WIDTH = Math.sqrt(3)*HEX_SIZE;
        HEX_HEIGHT = 2*HEX_SIZE;

        // Coordinates for center tiles
        int centerTileX = hexes.length/2;
        int centerTileY = hexes.length/2;

        // Offset calculated from the center tile
        double centerCoordX = HEX_WIDTH*centerTileX + 0.5*HEX_WIDTH*centerTileY;
        double centerCoordY = 3*HEX_HEIGHT/4*centerTileY;

        // offset for xy coordinates
        offsetX = mapSize /2.0 - centerCoordX;
        offsetY = mapSize /2.0 - centerCoordY;
    }

    private void setupHexUI(Group root, Hex[][] hexes) {
        Polygon polygon;
        Hex hex;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                hex = hexes[i][j];
                if (hex != null) {
                    hex.setSize(HEX_SIZE);
                    hex.setOffsetX(offsetX);
                    hex.setOffsetY(offsetY);

                    //System.out.println("Creating hex at (" + hex.getRealX() + ", " + hex.getRealY() + ")");

                    polygon = createPolygonFromHex(hex);
                    polygon.setId(i + " " + j);

                    Hex finalHex = hex;

                    polygon.setOnMouseClicked(mouseEvent ->
                            handleMouseEvent(mouseEvent, finalHex));

                    // Add number token
                    Text label = new Text(hex.getNumberToken() + "");

                    // Text size
                    label.setFont(Font.font(HEX_SIZE/4.));

                    // Center text
                    double halfLabelHeight = label.getLayoutBounds().getHeight() / 2;
                    double halfLabelWidth = label.getLayoutBounds().getWidth() / 2;
                    label.relocate(hex.getRealX() - halfLabelWidth, hex.getRealY() - halfLabelHeight);

                    // White circle around text
                    Circle circle = new Circle(hex.getRealX(),hex.getRealY(), halfLabelHeight*1.5);
                    circle.setFill(Color.WHITE);
                    root.getChildren().addAll(polygon, circle, label);
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

        //double touchAngle = getAngleFromScreenClick(mouseEvent.getX(), mouseEvent.getY(), getHexCenterX(hex), getHexCenterY(hex));

        double touchAngle = getAngleFromScreenClick(mouseEvent.getX(), mouseEvent.getY(), getHexCenterX(hex), getHexCenterY(hex));


        // TODO implements states

        //System.out.println(game.yourTurn());
        if (game.yourTurn()) {
            int success = 0;
            switch (gameState) {
                case TradeBank:
                    //todo
                    break;
                case BuildRoad:
                    if (initialState && game.getStartingRoadsBuiltThisTurn() == 0) {
                        success = game.buildStartingRoad(getChosenEdge(i, j, touchAngle));
                        if (success == 1) {
                            endTurn();
                        }
                    } else {
                        success = game.buildRoad(getChosenEdge(i, j, touchAngle));
                    }
                    break;
                case BuildSettlement:
                    if (initialState &&game.getStartingSettlementsBuiltThisTurn() == 0) {
                        success = game.buildStartingSettlement(getChosenIntersection(i, j, touchAngle));
                    } else {
                        success = game.buildSettlement(getChosenIntersection(i, j, touchAngle));
                    }
                    break;
                case BuildCity:
                    success = game.buildCity(getChosenIntersection(i, j, touchAngle));
                    break;
                case BuyDevelopmentCard:
                    success = game.buyDevelopmentCard();
                    break;
                case PlayDevelopmentCard:
                    //todo
                    break;
                case None:
                    System.out.println("No action selected");
            }
            //todo notify user according to return value
            if (success == 1) {

            } else if (success == -1) {

            } else if (success == -2) {

            }
        }

        //getChosenIntersection(i, j, touchAngle);
        //getChosenEdge(i, j, touchAngle);
        /*System.out.println("This is hex [" + getId(polygon)[0] + "][" + getId(polygon)[1] + "] " +
                "at coordinates (" + getHexCenterX(hex) + ", " + getHexCenterY(hex) + "). The screen was touched in an angle of "
                + getAngleFromScreenClick(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex)));*/
    }

    public void endTurn() {
        System.out.println("End of turn");
        gameState = None;
        if (endedInitTurnCount < 2) {
            game.endInitTurn();
            endedInitTurnCount++;
        } else {
            initialState = false;
            game.endTurn();
        }
    }

    private Edge getChosenEdge(int i, int j, double angle) {
        int edgeIndex = (int) (floor(angle) + 30) % 360 / 60;
        Edge[] edges = game.getBoard().getAdjacentEdges(new Hex(j, i, Terrain.None, -1));

        //System.out.println("You clicked on hex at position: (" + i + ", " + j + ") in an angle of " + angle + " chosen edge index is: " + edgeIndex);
        return edges[edgeIndex];
    }

    private Vertex getChosenIntersection(int i, int j, double angle) {
        int vertexIndex = (int) floor(angle)/60;
        Vertex[] vertices = game.getBoard().getAdjacentVertices(new Hex(j,i, Terrain.None, -1));

        // Do some verification of chosen vertex..

        //System.out.println("You clicked on hex at position: (" + i + ", " + j + ") in an angle of " + angle + " chosen vertex index is: " + vertexIndex);
        //System.out.println("x: " + chosenVertex.getX() + ", y: " + chosenVertex.getY());
        return vertices[vertexIndex];
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

    public void onButtonClick(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        String buttonSource = button.getId();

        // TODO
        switch (buttonSource) {
            case "BuildRoad":
                System.out.println("Selected Build Road");
                gameState = GameState.BuildRoad;
                break;
            case "BuildSettlement":
                System.out.println("Selected Build Settlement");
                gameState = GameState.BuildSettlement;
                System.out.println(gameState.toString());
                break;
        }

    }

    public void onMouseEntered(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setEffect(new DropShadow());
    }

    public void onMouseExited(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setEffect(null);
    }

    private void createGame(Stage primaryStage, boolean isHost, String hostUri) throws IOException {
        playerName = textField.getText();
        double mapSize = initializeScene(primaryStage);
        game = new Game(hostUri, isHost, playerName);
        view = new NewView(game);

        Hex[][] hexes = game.getBoard().getHexes();
        initializeOffsets(mapSize, hexes);
        setupHexUI(map, hexes);
        Thread thread = new Thread(new viewUpdater(this, view));
        thread.setDaemon(true);
        thread.start();
    }

    private void showJoinGameDialog(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(NewController.class.getClassLoader().getResource("LobbyViewWithIPPortInput.fxml"));
        Scene scene = new Scene(loader.load());

        textField = (TextField) loader.getNamespace().get("enterNameTextField");
        textField.setText(playerName);

        TextField ipTextField = (TextField) loader.getNamespace().get("ipText");
        TextField portTextField = (TextField) loader.getNamespace().get("portText");
        
        Button joinButton = (Button) loader.getNamespace().get("join");
        joinButton.setOnMouseClicked(mouseEvent -> {
            try {
                playerName = textField.getText();
                String uri = "tcp://" + ipTextField.getText() + ":" + portTextField.getText();
                System.out.println("uri: " + uri);
                createGame(primaryStage, false, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button createGameButton = (Button) loader.getNamespace().get("CreateGameButton");
        createGameButton.setOnMouseClicked(mouseEvent -> {
            try {
                createGame(primaryStage, true, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    Group getMap() {
        return map;
    }

    GridPane getOwnPlayerInfo() {
        return ownPlayerInfo;
    }

    GridPane[] getOtherPlayerInfo() {
        return otherPlayerInfo;
    }

    ListView<String> getListView() {
        return chatView;
    }
}
