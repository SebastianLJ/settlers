package model;

import controller.Controller;
import model.board.*;
import org.jspace.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class newGame {
    private Scanner scanner = new Scanner(System.in);
    private Random dice = new Random();
    //private final RemoteSpace playerSpace;
    private int playerId = 1;
    private PlayerState player;
    private Board board;
    private final String hostURI;
    private int playerCount = 1;
    private int turn = 1;
    private int turnId = turn % playerCount;

    public newGame(String hostURI) throws IOException {
        this.hostURI = hostURI;
        board = new Board();
        player = new PlayerState(1);
        /*playerSpace = new RemoteSpace(hostURI);

        try {
            playerCount = getPlayerCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public int roll() {
        int roll = dice.nextInt(6) + dice.nextInt(6) + 2;
        System.out.println("Rolled " + roll);

        if (roll == 7) {
            boolean success = false;
            // TODO Fix with new controller
                /*while (!success) {
                    int[] coords = controller.getHexCoordinates();
                    success = board.updateRobber(coords[0],coords[1]);
                }*/
        } else {
            setResources(playerId, roll);
        }
        return roll;
    }

    public void trade() {
        String action = scanner.next();

        if (action.equals("trade")) {
            action = scanner.next();

            if (action.equals("player")) {
                //todo
            } else if (action.equals("bank") ||action.equals("harbor")) {
                System.out.println("Please select a resource to trade ");
                String resource = scanner.next();
                if (harborTrades(player).contains(stringToResource(resource))
                        && player.getResourceAmount(stringToResource(resource)) >= 2) {
                    for (int i = 0; i < 2; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else if (hasGenericHarbor(player)
                        && player.getResourceAmount(stringToResource(resource)) >= 3) {
                    for (int i = 0; i < 3; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else if (player.getResourceAmount(stringToResource(resource)) >= 4) {
                    for (int i = 0; i < 4; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else {
                    System.out.println("you don't have enough " + resource + " to trade");
                }
            }
        }
    }

    /**
     * Adds player id to edge, and checks if location is valid
     * @param edge
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildRoad(Edge edge) {
        if (player.getResources().containsAll(Price.Road.getPrice())) {
            if (isRoadValid(edge)) {
                player.getResources().removeAll(Price.Road.getPrice());
                edge.setId(turnId);
                System.out.println("Successfully built road");
                return 1;
            } else {
                System.out.println("Invalid road location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    public int buildStartingRoad(Edge edge) {
        if (isRoadValid(edge)) {
            edge.setId(turnId);
            System.out.println("Successfully built road");
            return 1;
        } else {
            System.out.println("Invalid road location");
            return -2;
        }
    }

    /**
     * Adds player id to vertex and boolean hasSettlement, and checks if location is valid
     * @param vertex
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildSettlement(Vertex vertex) {
        if (player.getResources().containsAll(Price.Settlement.getPrice())) {
            if (isSettlementValid(vertex)) {
                player.getResources().removeAll(Price.Settlement.getPrice());
                vertex.buildSettlement(turnId);
                System.out.println("Successfully built settlement");
                return 1;
            } else {
                System.out.println("Invalid settlement location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     * Initial placement of settlement
     * @param vertex
     * @return -2 invalid location, 1 successfully built
     */
    public int buildStartingSettlement(Vertex vertex) {
        if (isSettlementValidLength(vertex)) {
            vertex.buildSettlement(turnId);
            System.out.println("Successfully built settlement");
            return 1;
        } else {
            System.out.println("Invalid settlement location");
            return -2;
        }
    }

    /**
     * Adds player id to vertex and boolean hasSettlement, and checks if location is valid
     * @param vertex
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildCity(Vertex vertex) {
        if (player.getResources().containsAll(Price.City.getPrice())) {
            if (isCityValid(vertex)) {
                player.getResources().removeAll(Price.City.getPrice());
                vertex.buildCity(turnId);
                System.out.println("Successfully built city");
                return 1;
            } else {
                System.out.println("Invalid city location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     *
     * @return -1 insufficient resources, 1 successfully bought
     */
    public int buyDevelopmentCard() {
        if (player.getResources().containsAll(Price.DevelopmentCard.getPrice())) {
            player.getResources().removeAll(Price.DevelopmentCard.getPrice());
            DevelopmentCard devCard = board.buyDevelopmentCard();
            player.getDevelopmentCards().add(devCard);
            System.out.println("You received: " + devCard);
            return 1;
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     *
     * @param developmentCard
     * @param hex
     * @return -1 invalid location, 1 successfully played development card
     */
    public int playDevelopmentCard(DevelopmentCard developmentCard, Hex hex) {
        if (developmentCard == DevelopmentCard.Knight) {
            return board.updateRobber(hex.getX(), hex.getY());

        } else if (developmentCard == DevelopmentCard.RoadBuilding) {
            int roadsPlaced = 0;
            // TODO Fix with new controller
                        /*while (roadsPlaced < 2) {
                            boolean success = false;
                            while(!success) {
                                Edge edge = controller.getEdgeCoordinates();
                                if (isRoadValid(edge)) {
                                    edge.setId(turnId);
                                    roadsPlaced++;
                                    success = true;
                                } else {
                                    System.out.println("Invalid road location");
                                }
                            }
                        }*/
        } else if (developmentCard == DevelopmentCard.YearOfPlenty) {
            int resourcesSelected = 0;
            while (resourcesSelected < 2) {
                System.out.println("Please select a resource: ");
                String resourceType = scanner.next();
                if (stringToResource(resourceType) != null) {
                    player.getResources().add(stringToResource(resourceType));
                }
                resourcesSelected++;
            }
        } else if (developmentCard == DevelopmentCard.Monopoly) {
            //todo
        }
        return -1;
    }

    public int endTurn() {
        turn++;
        turnId = turn % playerCount;
        player = getPlayer(turnId);
        return turn;
    }

    public int endInitTurn() {
        if (turn == playerCount) {
            //dont update turnId
        } else if (turn > playerCount) {
            turnId--;
        } else {
            turnId++;
        }
        return turn++;
    }

    private PlayerState getPlayer(int turnId) {
        //todo
        return player;
    }

    //todo check victory points

    private boolean isRoadValid(Edge edge) {
        if (edge == null) {
            return false;
        }
        Vertex[] vertices = board.getAdjacentVertices(edge);
        for (Vertex vertex : vertices) {
            if (vertex.getId() == playerId && vertex.isCity() || vertex.isSettlement()) {
                return true;
            }
            for (Edge nextEdge : board.getAdjacentEdges(vertex)) {
                if (nextEdge.getId() == playerId) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Edge> getValidRoads() {
        Edge[][] edges = board.getEdges();
        ArrayList<Edge> res = new ArrayList<Edge>();
        for (Edge[] edgeList : edges) {
            for (Edge edge : edgeList) {
                if (isRoadValid(edge)) {
                    res.add(edge);
                }
            }
        }
        return res;
    }

    private boolean isSettlementValidLength(Vertex vertex) {
        Edge[] edges = board.getAdjacentEdges(vertex);
        for (Edge edge : edges) {
            if (edge != null) {
                Vertex[] possibleOccupations = board.getAdjacentVertices(edge);
                for (Vertex possibleOccupation : possibleOccupations) {
                    if (possibleOccupation.isSettlement() || possibleOccupation.isCity()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isSettlementConnected(Vertex vertex) {
        Edge[] edges = board.getAdjacentEdges(vertex);
        for (Edge edge : edges) {
            if (edge.getId() == playerId) {
                return true;
            }
        }
        return false;
    }

    private boolean isSettlementValid(Vertex vertex) {
        //return isSettlementConnected(vertex) && isSettlementValidLength(vertex);
        return isSettlementValidLength(vertex);
    }

    private boolean isCityValid(Vertex vertex) {
        return vertex.isSettlement();
    }

    /*private int getPlayerCount() throws InterruptedException {
        return playerSpace.getAll(Templates.Player.getTemplateFields()).size();
    }*/

    private int getVictoryPoints(PlayerState player) {
        int vp = getSettlements(player.getPlayerId()).size() + getCities(player.getPlayerId()).size();
        for (DevelopmentCard developmentCard : player.getDevelopmentCards()) {
            if (developmentCard.equals(DevelopmentCard.VictoryPoint)) {
                vp++;
            }
        }
        if (player.hasLargestArmy()) vp++;
        if (player.hasLongestRoad()) vp++;

        return vp;

    }

    private void setResources(int id, int roll) {
        ArrayList<Vertex> settlements = getSettlements(id);
        ArrayList<Vertex> cities = getCities(id);

        player.getResources().addAll(getResources(settlements,roll));
        player.getResources().addAll(getResources(cities, roll));



    }

    private ArrayList<Resource> getResources(ArrayList<Vertex> vertices, int roll) {
        ArrayList<Resource> res = new ArrayList<>();
        for (Vertex vertex : vertices) {
            for (Hex hex : board.getAdjacentHexes(vertex)) {
                if (hex.getNumberToken() == roll) {
                    res.add(hex.getTerrain().getProduct());
                }
            }
        }
        return res;
    }

    private ArrayList<Vertex> getSettlements(int id) {
        ArrayList<Vertex> settlements = new ArrayList<Vertex>();
        for (Vertex[] vertexList : board.getVertices()) {
            for (Vertex vertex : vertexList) {
                if (vertex.getId() == playerId && vertex.isSettlement()) {
                    settlements.add(vertex);
                }
            }
        }
        return settlements;
    }

    private ArrayList<Vertex> getCities(int id) {
        ArrayList<Vertex> cities = new ArrayList<Vertex>();
        for (Vertex[] vertexList : board.getVertices()) {
            for (Vertex vertex : vertexList) {
                if (vertex.getId() == playerId && vertex.isCity()) {
                    cities.add(vertex);
                }
            }
        }
        return cities;
    }

    private int getLongestRoad(int id, Edge edge) {
        //todo
        return 0;
    }

    private ArrayList<PlayerState> getPlayers() {
        ArrayList<PlayerState> players = new ArrayList<PlayerState>();
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    private ArrayList<Resource> harborTrades(PlayerState player) {
        ArrayList<Resource> res = new ArrayList<>();
        for (Vertex settlement : getSettlements(player.getPlayerId())) {
            if (settlement.getHarbor() != null) {
                res.add(settlement.getHarbor().getResource());
            }
        }
        for (Vertex city : getCities(player.getPlayerId())) {
            if (city.getHarbor() != null) {
                res.add(city.getHarbor().getResource());
            }
        }
        return res;
    }

    private boolean hasGenericHarbor(PlayerState player) {
        for (Vertex settlement : getSettlements(player.getPlayerId())) {
            if (settlement.getHarbor() == Harbor.Generic) {
                return true;
            }
        }
        for (Vertex city : getCities(player.getPlayerId())) {
            if (city.getHarbor() == Harbor.Generic) {
                return true;
            }
        }
        return false;
    }

    public Resource stringToResource(String resource) {
        switch (resource) {
            case "brick":
                return Resource.Brick;
            case "lumber":
                return Resource.Lumber;
            case "ore":
                return Resource.Ore;
            case "grain":
                return Resource.Grain;
            case "wool":
                return Resource.Wool;
            default:
                return null;
        }
    }
}
