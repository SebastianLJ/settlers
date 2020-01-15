package controller;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Game;
import model.board.Edge;
import model.board.Hex;
import model.board.Terrain;
import model.board.Vertex;
import view.NewView;
import view.View;

import static java.lang.Math.atan2;
import static java.lang.Math.floor;

public class NewController extends Application {

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
    private NewView view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Settlers of Catan");
        Group root = new Group();
        Scene scene = new Scene(root,screenSize,screenSize, Color.DEEPSKYBLUE);

        // Opens Lobby first

        // From Lobby create or join game TODO Implement

        // TODO Game shouldn't have a reference to controller
        game = new Game("");
        view = new NewView(game);

        Hex[][] hexes = game.getBoard().getHexes();
        setupHexUI(root, hexes);

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


        // TODO implements states


        getChosenIntersection(i, j, touchAngle);
        getChosenEdge(i, j, touchAngle);

        /*System.out.println("This is hex [" + getId(polygon)[0] + "][" + getId(polygon)[1] + "] " +
                "at coordinates (" + getHexCenterX(hex) + ", " + getHexCenterY(hex) + "). The screen was touched in an angle of "
                + getAngleFromScreenClick(mouseEvent.getSceneX(), mouseEvent.getSceneY(), getHexCenterX(hex), getHexCenterY(hex)));*/
    }

    public Edge getChosenEdge(int i, int j, double angle) {
        int edgeIndex = (int) (floor(angle) + 30) % 360 / 60;
        Edge[] edges = game.getBoard().getAdjacentEdges(new Hex(j, i, Terrain.None, -1));

        Edge chosenEdge = edges[edgeIndex];
        System.out.println("You clicked on hex at position: (" + i + ", " + j + ") in an angle of " + angle + " chosen edge index is: " + edgeIndex);
        return chosenEdge;
    }

    public Vertex getChosenIntersection(int i, int j, double angle) {
        int vertexIndex = (int) floor(angle)/60;
        Vertex[] vertices = game.getBoard().getAdjacentVertices(new Hex(j,i, Terrain.None, -1));

        // Do some verification of chosen vertex..
        Vertex chosenVertex = vertices[vertexIndex];

        System.out.println("You clicked on hex at position: (" + i + ", " + j + ") in an angle of " + angle + " chosen vertex index is: " + vertexIndex);

        return chosenVertex;
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
