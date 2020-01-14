package model;

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

    public Game(String hostURI) throws IOException {
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


                //todo get new robber coords from view
                int x = 2;
                int y = 2;
                board.updateRobber(x,y);
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

                    } else if (action.equals("bank")) {

                    } else if (action.equals("harbor")) {

                    }

                } else if (action.equals("build")) {
                    action = scanner.next();

                    if (action.equals("road")) {
                        //todo get coordinates from view
                        int x = 0, y = 0;
                        if (isRoadValid(new Edge(x, y))) {
                            if (player.getResources().containsAll(Price.Road.getPrice())) {
                                player.getResources().removeAll(Price.Road.getPrice());
                                board.getEdges()[y][x].setId(turnId);
                            } else {
                                System.out.println("Not enough resources");
                            }
                        } else {
                            System.out.println("Invalid road location");
                        }
                    } else if (action.equals("settlement")) {
                        //todo get coordinates from view
                        int x = 0, y = 0;
                        if (isSettlementValid(new Vertex(x,y))) {
                            if (player.getResources().containsAll(Price.Settlement.getPrice())) {
                                player.getResources().removeAll(Price.Settlement.getPrice());
                                board.getVertices()[y][x].buildSettlement(turnId);
                            } else {
                                System.out.println("Not enough resources");
                            }
                        } else {
                            System.out.println("Invalid settlement location");
                        }
                    } else if (action.equals("city")) {
                        //todo get coordinates from view
                        int x = 0, y = 0;
                        if (isCityValid(new Vertex(x, y))) {
                            if (player.getResources().containsAll(Price.City.getPrice())) {
                                player.getResources().removeAll(Price.City.getPrice());
                                board.getVertices()[y][x].buildCity(turnId);
                            } else {
                                System.out.println("Not enough resources");
                            }
                        } else {
                            System.out.println("Invalid city location");
                        }
                    } else if (action.equals("DevCard")) {
                        if (player.getResources().containsAll(Price.DevelopmentCard.getPrice())) {
                            player.getResources().removeAll(Price.DevelopmentCard.getPrice());
                            DevelopmentCard devCard = board.buyDevelopmentCard();
                            player.getDevelopmentCards().add(devCard);
                            System.out.println("You received: " + devCard);
                        }
                    }
                } else if (action.equals("playDevCard")) {

                } else if (action.equals("endTurn")) {
                    endTurn = true;
                }
            }

            if (getVictoryPoints(turnId) >= 10) {
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

    private int getVictoryPoints(int id) {
        return 0;
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
        return 0;
    }

    private ArrayList<PlayerState> getPlayers() {
        ArrayList<PlayerState> players = new ArrayList<PlayerState>();
        return players;
    }

    public Board getBoard() {
        return board;
    }
}
