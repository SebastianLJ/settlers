package model;

import controller.Controller;
import model.board.*;
import org.jspace.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Random dice = new Random();
    //private final RemoteSpace playerSpace;
    private int playerId = 1;
    private PlayerState player;
    private Board board;
    private final String hostURI;
    private int playerCount = 1;
    private Controller controller;

    public Game(String hostURI, Controller controller) throws IOException {
        this.hostURI = hostURI;
        this.controller = controller;
        board = new Board();
        player = new PlayerState(1);
        /*playerSpace = new RemoteSpace(hostURI);

        try {
            playerCount = getPlayerCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }


    public void start() {
        boolean victory = false;
        int roll;

        //roll for starting player
        //int turn = dice.nextInt(playerCount);
        int turn = 1;
        int turnId;

        while(!victory) {
            turnId = turn % playerCount;
            roll = dice.nextInt(6) + dice.nextInt(6) + 2;
            System.out.println("Rolled " + roll);

            if (roll == 7) {
                boolean success = false;
                while (!success) {
                    int[] coords = controller.getHexCoordinates();
                    success = board.updateRobber(coords[0],coords[1]);
                }
            } else {
                setResources(playerId, roll);
            }

            boolean endTurn = false;
            String action;
            while(!endTurn) {
                action = scanner.next();

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
                } else if (action.equals("build")) {
                    action = scanner.next();

                    if (action.equals("road")) {
                        if (player.getResources().containsAll(Price.Road.getPrice())) {
                            boolean success = false;
                            while(!success) {
                                int[] coords = controller.getEdgeCoordinates();
                                //break if player cancels
                                if (coords[0] == -1 && coords[1] == -1) {
                                    break;
                                }
                                if (isRoadValid(new Edge(coords[0], coords[1]))) {
                                    player.getResources().removeAll(Price.Road.getPrice());
                                    board.getEdges()[coords[1]][coords[0]].setId(turnId);
                                    success = true;
                                } else {
                                    System.out.println("Invalid road location");
                                }
                            }
                        } else {
                            System.out.println("Not enough resources");
                        }

                    } else if (action.equals("settlement")) {

                        if (player.getResources().containsAll(Price.Settlement.getPrice())) {
                            boolean success = false;
                            while (!success) {
                                int[] coords = controller.getVertexCoordinates();
                                //break if player cancels
                                if (coords[0] == -1 && coords[1] == -1) {
                                    break;
                                }
                                if (isSettlementValid(new Vertex(coords[0], coords[1]))) {
                                    player.getResources().removeAll(Price.Settlement.getPrice());
                                    board.getVertices()[coords[1]][coords[0]].buildSettlement(turnId);
                                    success = true;
                                } else {
                                    System.out.println("Invalid settlement location");
                                }
                            }
                        } else {
                            System.out.println("Not enough resources");
                        }
                    } else if (action.equals("city")) {
                        if (player.getResources().containsAll(Price.City.getPrice())) {
                            boolean success = false;
                            while (!success) {
                                int[] coords = controller.getVertexCoordinates();
                                //break if player cancels
                                if (coords[0] == -1 && coords[1] == -1) {
                                    break;
                                }
                                if (isCityValid(new Vertex(coords[0], coords[1]))) {
                                    player.getResources().removeAll(Price.City.getPrice());
                                    board.getVertices()[coords[1]][coords[0]].buildCity(turnId);
                                    success = true;
                                } else {
                                    System.out.println("Invalid city location");
                                }
                            }
                        } else {
                            System.out.println("Not enough resources");
                        }
                    } else if (action.equals("DevCard")) {
                        if (player.getResources().containsAll(Price.DevelopmentCard.getPrice())) {
                            player.getResources().removeAll(Price.DevelopmentCard.getPrice());
                            DevelopmentCard devCard = board.buyDevelopmentCard();
                            player.getDevelopmentCards().add(devCard);
                            System.out.println("You received: " + devCard);
                        } else {
                            System.out.println("Not enough resources");
                        }
                    }
                } else if (action.equals("playDevCard")) {
                    System.out.println("please select a development card to play: ");
                    action = scanner.next();
                    if (action.equals(DevelopmentCard.Knight.getType())) {
                        boolean success = false;
                        while (!success) {
                            int[] coords = controller.getHexCoordinates();
                            success = board.updateRobber(coords[0],coords[1]);
                        }
                    } else if (action.equals(DevelopmentCard.RoadBuilding.getType())) {
                        int roadsPlaced = 0;
                        while (roadsPlaced < 2) {
                            boolean success = false;
                            while(!success) {
                                int[] coords = controller.getEdgeCoordinates();
                                if (isRoadValid(new Edge(coords[0], coords[1]))) {
                                    board.getEdges()[coords[1]][coords[0]].setId(turnId);
                                    roadsPlaced++;
                                    success = true;
                                } else {
                                    System.out.println("Invalid road location");
                                }
                            }
                        }
                    } else if (action.equals(DevelopmentCard.YearOfPlenty.getType())) {
                        int resourcesSelected = 0;
                        while (resourcesSelected < 2) {
                            System.out.println("Please select a resource: ");
                            String resourceType = scanner.next();
                            if (stringToResource(resourceType) != null) {
                                player.getResources().add(stringToResource(resourceType));
                            }
                            resourcesSelected++;
                        }
                    } else if (action.equals(DevelopmentCard.Monopoly.getType())) {
                        //todo
                    }

                } else if (action.equals("endTurn")) {
                    endTurn = true;
                }
            }

            if (getVictoryPoints(player) >= 10) {
                victory = true;
            }

            turn++;
        }
    }

    private boolean isRoadValid(Edge edge) {
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
            Vertex[] possibleOccupations = board.getAdjacentVertices(edge);
            for (Vertex possibleOccupation : possibleOccupations) {
                if (possibleOccupation.isSettlement() || possibleOccupation.isCity() ) {
                    return false;
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
        return isSettlementConnected(vertex) && isSettlementValidLength(vertex);
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
        if (player.hasLargetArmy()) vp++;
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
