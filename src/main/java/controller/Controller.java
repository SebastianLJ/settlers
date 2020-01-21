package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
import view.View;

import java.io.IOException;

import static java.lang.Math.atan2;
import static java.lang.Math.floor;
import static model.GameState.*;

public class Controller extends Application {
    private final int INSUFFICIENT_RESOURCES_ERROR = 0;
    private final int NOT_ENOUGH_RESOURCES_ERROR = 0;
    public ToggleGroup toggleGroupGive;
    public ToggleGroup toggleGroupGet;

    public RadioButton giveOreRadioButton;
    public RadioButton giveBrickRadioButton;
    public RadioButton giveWoolRadioButton;
    public RadioButton giveGrainRadioButton;
    public RadioButton giveLumberRadioButton;
    public RadioButton getOreRadioButton;
    public RadioButton getBrickRadioButton;
    public RadioButton getWoolRadioButton;
    public RadioButton getGrainRadioButton;
    public RadioButton getLumberRadioButton;

    private Game game;
    private View view;
    private GameState gameState = GameState.BuildRoad;
    private boolean initialState = true;
    private int endedInitTurnCount = 0;

    @FXML
    private Button buildRoad, buildSettlement, buildCity, buildDevCard,
            tradeWithBank, tradeWithPlayer, playDevCard, viewDevCard,
            rollDices, endTurnButton;

    private boolean buildRoadLock, buildSettlementLock, buildCityLock, buildDevCardLock,
                    tradeWithBankLock, tradeWithPlayerLock, playDevCardLock, viewDevCardLock,
                    rollDicesLock, endTurnButtonLock;

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

    @FXML
    private ScrollPane scrollPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Settlers of Catan - Lobby");
        FXMLLoader loader = new FXMLLoader(Controller.class.getClassLoader().getResource("LobbyView.fxml"));
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
        FXMLLoader loader = new FXMLLoader(Controller.class.getClassLoader().getResource("AdvancedGameView.fxml"));
        StackPane root = loader.load();

        map = new Group();

        VBox gameView = (VBox) loader.getNamespace().get("gameView");
        HBox diceView = (HBox) gameView.getChildren().get(0);

        AnchorPane anchorPane = (AnchorPane) gameView.getChildren().get(1);
        anchorPane.getChildren().set(0,map);

        Scene scene = new Scene(root);

        initiateButtonListeners(loader, root);

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
                game.sendMsg(chatTextField.getText())
        );

        scrollPane = (ScrollPane) loader.getNamespace().get("scrollPane");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);

        double mapSize = gameView.getHeight() - diceView.getHeight();
        gameView.setMaxWidth(mapSize);

        ((SplitPane) root.getChildren().get(0)).setDividerPositions(mapSize / root.getWidth());

        //createStartGameButton(root, mapSize);

        return mapSize;
    }

    private void createStartGameButton(StackPane root, double mapSize) {
        Button startGameButton = new Button();
        startGameButton.setPrefSize(mapSize/2, mapSize/4);
        startGameButton.setText("START GAME");
        startGameButton.setStyle("-fx-background-color: \n" +
                "        linear-gradient(#ffd65b, #e68400),\n" +
                "        linear-gradient(#ffef84, #f2ba44),\n" +
                "        linear-gradient(#ffea6a, #efaa22),\n" +
                "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
                "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,2,3,0;\n" +
                "    -fx-text-fill: #654b00;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 22px;\n" +
                "    -fx-padding: 10 20 10 20;");
        startGameButton.setTranslateX(mapSize/2-startGameButton.getPrefWidth()/2);
        startGameButton.setTranslateY(mapSize/2-startGameButton.getPrefHeight()/2);

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black; -fx-opacity: 0.2");

        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        startGameButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> startGameButton.setEffect(shadow));
        //Removing the shadow when the mouse cursor is off
        startGameButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> startGameButton.setEffect(null));
        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    if (game.getPlayerCount() > 2) {
                        root.getChildren().removeAll(startGameButton, pane);
                    }
                });
        root.getChildren().addAll(pane, startGameButton);
    }

    private void initiateButtonListeners(FXMLLoader loader, StackPane root) {
        buildRoad = (Button) loader.getNamespace().get("buildRoad");
        buildRoad.setOnAction(actionEvent -> gameState = GameState.BuildRoad);

        buildSettlement = (Button) loader.getNamespace().get("buildSettlement");
        buildSettlement.setOnAction(actionEvent -> gameState = GameState.BuildSettlement);

        buildCity = (Button) loader.getNamespace().get("buildCity");
        buildCity.setOnAction(actionEvent -> gameState = GameState.BuildCity);

        buildDevCard = (Button) loader.getNamespace().get("buildDevCard");
        buildDevCard.setOnAction(actionEvent -> gameState = GameState.BuyDevelopmentCard);

        tradeWithBank = (Button) loader.getNamespace().get("tradeWithBank");
        tradeWithBank.setOnAction(actionEvent -> {
            gameState = GameState.TradeBank;
            try {
                openTradeWithBankDialog(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tradeWithPlayer = (Button) loader.getNamespace().get("tradeWithPlayer");
        tradeWithPlayer.setOnAction(actionEvent -> gameState = GameState.TradePlayer);

        playDevCard = (Button) loader.getNamespace().get("playDevCard");
        //todo play dev card

        viewDevCard = (Button) loader.getNamespace().get("viewDevCard");
        //todo

        rollDices = (Button) loader.getNamespace().get("rollDices");
        rollDices.setOnAction(actionEvent -> {

            if (game.yourTurn()) {

                rollDices.setDisable(true);
                endTurnButton.setDisable(false);
                setLocks(false);

                diceRoll = game.roll();
                diceRollLabel.setText(Integer.toString(diceRoll));
            }
        });

        endTurnButton = (Button) loader.getNamespace().get("endTurn");
        endTurnButton.setOnAction(actionEvent -> {

            endTurn();
            if (!initialState) {
                setLocks(true);
                rollDices.setDisable(false);
            } else {
                buildCityLock = true;
                buildDevCardLock = true;
                tradeWithBankLock = true;
                tradeWithPlayerLock = true;
                playDevCardLock = true;
            }
            endTurnButton.setDisable(true);

        });
        endTurnButton.setDisable(true);
        rollDices.setDisable(true);
    }

    private void openTradeWithBankDialog(StackPane root) throws IOException {
        FXMLLoader loader = new FXMLLoader(Controller.class.getClassLoader().getResource("TradeWithBankDialog.fxml"));
        BorderPane tradeDialog = loader.load();

        toggleGroupGive = new ToggleGroup();
        toggleGroupGet = new ToggleGroup();

        giveOreRadioButton = (RadioButton) loader.getNamespace().get("giveOreRadioButton");
        giveBrickRadioButton = (RadioButton) loader.getNamespace().get("giveBrickRadioButton");
        giveWoolRadioButton = (RadioButton) loader.getNamespace().get("giveWoolRadioButton");
        giveGrainRadioButton = (RadioButton) loader.getNamespace().get("giveGrainRadioButton");
        giveLumberRadioButton = (RadioButton) loader.getNamespace().get("giveLumberRadioButton");
        giveOreRadioButton.setToggleGroup(toggleGroupGive);
        giveBrickRadioButton.setToggleGroup(toggleGroupGive);
        giveWoolRadioButton.setToggleGroup(toggleGroupGive);
        giveGrainRadioButton.setToggleGroup(toggleGroupGive);
        giveLumberRadioButton.setToggleGroup(toggleGroupGive);

        getOreRadioButton = (RadioButton) loader.getNamespace().get("getOreRadioButton");
        getBrickRadioButton = (RadioButton) loader.getNamespace().get("getBrickRadioButton");
        getWoolRadioButton = (RadioButton) loader.getNamespace().get("getWoolRadioButton");
        getGrainRadioButton = (RadioButton) loader.getNamespace().get("getGrainRadioButton");
        getLumberRadioButton = (RadioButton) loader.getNamespace().get("getLumberRadioButton");
        getOreRadioButton.setToggleGroup(toggleGroupGet);
        getBrickRadioButton.setToggleGroup(toggleGroupGet);
        getWoolRadioButton.setToggleGroup(toggleGroupGet);
        getGrainRadioButton.setToggleGroup(toggleGroupGet);
        getLumberRadioButton.setToggleGroup(toggleGroupGet);

        final String[] userGivesResource = new String[1];
        final String[] userGetsResource = new String[1];

        toggleGroupGive.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton)toggleGroupGive.getSelectedToggle();

            if (rb != null) {
                userGivesResource[0] = rb.getText().split(" ")[1];
            }
        });

        toggleGroupGet.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton)toggleGroupGet.getSelectedToggle();

            if (rb != null) {
                userGetsResource[0] = rb.getText().split(" ")[1];

            }
        });

        giveOreRadioButton.fire();
        getOreRadioButton.fire();

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black; -fx-opacity: 0.2");

        root.getChildren().addAll(pane, tradeDialog);

        Button tradeButton = (Button) loader.getNamespace().get("tradeButton");
        Button cancelButton = (Button) loader.getNamespace().get("cancelButton");

        tradeButton.setOnMouseClicked(e -> tradeResourcesWithBank(userGivesResource[0], userGetsResource[0]));
        cancelButton.setOnMouseClicked(e -> root.getChildren().removeAll(tradeDialog, pane));
    }

    private void tradeResourcesWithBank(String userGivesResources, String userGetsResource) {
        game.tradeWithBank(userGivesResources, userGetsResource);
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

    private void setLocks(boolean val) {
        buildRoadLock = val;
        buildSettlementLock = val;
        buildCityLock = val;
        buildDevCardLock = val;
        tradeWithBankLock = val;
        tradeWithPlayerLock = val;
        playDevCardLock = val;
    }

    private void setupHexUI(Group map, Hex[][] hexes) {
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

                    // Add number token
                    Text label = new Text(hex.getNumberToken() + "");

                    // Text size
                    label.setFont(Font.font(hex.getSize() / 4.));

                    // Center text
                    double halfLabelHeight = label.getLayoutBounds().getHeight() / 2;
                    double halfLabelWidth = label.getLayoutBounds().getWidth() / 2;
                    label.relocate(hex.getRealX() - halfLabelWidth, hex.getRealY() - halfLabelHeight);

                    // White circle around text
                    Circle circle = new Circle(hex.getRealX(), hex.getRealY(), halfLabelHeight * 1.5);
                    circle.setFill(Color.WHITE);
                    map.getChildren().addAll(polygon, circle, label);
                }
            }
        }
    }

    /**
     * Finds the appropriate game object (ie. Path, Intersection etc).
     *
     * @param mouseEvent from listener connected to polygon
     * @param hex        connected to polygon
     */
    private void handleMouseEvent(MouseEvent mouseEvent, Hex hex) {
        int i = hex.getY();
        int j = hex.getX();

        double touchAngle = getAngleFromScreenClick(mouseEvent.getX(), mouseEvent.getY(), hex.getRealX(), hex.getRealY());

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
                            endTurnButton.setDisable(false);
                        }
                    } else {
                        success = game.buildRoad(getChosenEdge(i, j, touchAngle));
                    }
                    switch (success) {
                        case 0:
                    }
                    break;
                case BuildSettlement:
                    if (initialState && game.getStartingSettlementsBuiltThisTurn() == 0) {
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
    }

    public void endTurn() {
        gameState = None;
        if (endedInitTurnCount < 2) {
            game.endInitTurn();
            endedInitTurnCount++;
            if (endedInitTurnCount >= 2) {
                initialState = false;
            }
        } else {
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
        int vertexIndex = (int) floor(angle) / 60;
        Vertex[] vertices = game.getBoard().getAdjacentVertices(new Hex(j, i, Terrain.None, -1));

        // Do some verification of chosen vertex..

        //System.out.println("You clicked on hex at position: (" + i + ", " + j + ") in an angle of " + angle + " chosen vertex index is: " + vertexIndex);
        //System.out.println("x: " + chosenVertex.getX() + ", y: " + chosenVertex.getY());
        return vertices[vertexIndex];
    }

    /**
     * Calculates and returns the angle from the center of the clicked hexagon to the actual mouse click.
     *
     * @param click_x  x-coordinate of click
     * @param click_y  y-coordinate of click
     * @param center_x x-coordinate of hex center
     * @param center_y y-coordinate of hex center
     * @return angle from center to click, as described by https://www.redblobgames.com/grids/hexagons/#basics
     */
    private double getAngleFromScreenClick(double click_x, double click_y, double center_x, double center_y) {
        double delta_x = click_x - center_x;
        double delta_y = click_y - center_y;
        double theta_radians = atan2(delta_y, delta_x);

        double theta_degrees = theta_radians * 180 / Math.PI;

        if (theta_degrees < 0) {
            return theta_degrees + 360;
        } else {
            return theta_degrees;
        }
    }

    /**
     * Creates a Shape.Polygon in the x,y-basis from hex-basis
     *
     * @param hex input hex containing x,y-hex
     * @return new polygon object with coordinates made from hex
     */
    private Polygon createPolygonFromHex(Hex hex) {
        // HexCoordinates to x,y-coordinates, with center offset
        double x = hex.getRealX();
        double y = hex.getRealY();

        Polygon polygon = new Polygon(x, y + hex.getHeight() / 2, x + hex.getWidth() / 2, y + hex.getHeight() / 4, x + hex.getWidth() / 2, y - hex.getHeight() / 4,
                x, y - hex.getHeight() / 2, x - hex.getWidth() / 2, y - hex.getHeight() / 4, x - hex.getWidth() / 2, y + hex.getHeight() / 4);

        polygon.setStroke(Color.BLACK);

        // Get the color for the given hex depending on which terrain/resource type it contains
        Paint color = getColorFromTerrain(hex.getTerrain());

        polygon.setFill(color);

        return polygon;
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
        game = new Game(hostUri, isHost, playerName, mapSize);
        view = new View(game);

        Hex[][] hexes = game.getBoard().getHexes();
        setupHexUI(map, hexes);
        view.initializeRobber(map);
        Thread thread = new Thread(new viewUpdater(this, view));
        thread.setDaemon(true);
        thread.start();

        //new Thread(new ButtonDisabler(this, game)).start();
        //new Thread(new VictoryPointChecker(this, game)).start();
    }

    private void showJoinGameDialog(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Controller.class.getClassLoader().getResource("LobbyViewWithIPPortInput.fxml"));
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

    public void setBuildRoad(boolean val) {
        buildRoad.setDisable(buildRoadLock || val);
    }

    public void setBuildSettlement(boolean val) {
        buildSettlement.setDisable(buildSettlementLock || val);
    }

    public void setBuildCity(boolean val) {
        buildCity.setDisable(buildCityLock || val);
    }

    public void setBuildDevCard(boolean val) {
        buildDevCard.setDisable(buildDevCardLock || val);
    }

    public void setTradeWithBank(boolean val) {
        tradeWithBank.setDisable(tradeWithBankLock || val);
    }

    public void setTradeWithPlayer(boolean val) {
        tradeWithPlayer.setDisable(tradeWithPlayerLock || val);
    }

    public void setPlayDevCard(boolean val) {
        playDevCard.setDisable(playDevCardLock || val);
    }

    public void setViewDevCard(boolean val) {
        viewDevCard.setDisable(viewDevCardLock || val);
    }

    public void setRollDices(boolean val) {
        rollDices.setDisable(rollDicesLock || val);
    }

    public boolean isInitialState() {
        return initialState;
    }

    public Label getDiceRollLabel() {
        return diceRollLabel;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void gameWon(int playerId) {

    }
}
