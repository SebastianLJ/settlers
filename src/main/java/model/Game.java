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
    private final RemoteSpace playerSpace;
    private int playerId;
    private Board board;
    private final String hostURI;
    private int playerCount;

    public Game(String hostURI) throws IOException {
        this.hostURI = hostURI;
        playerSpace = new RemoteSpace(hostURI);

        try {
            playerCount = getPlayerCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        boolean victory = false;
        int roll;

        //roll for starting player
        int turn = dice.nextInt(playerCount);
        int turnId;

        while(!victory) {
            turnId = turn % playerCount;
            roll = dice.nextInt(6) + dice.nextInt(6) + 2;

            if (roll == 7) {


                //todo get new robber coords
                int x = 0;
                int y = 0;
                board.updateRobber(x,y);
            } else {
                //todo distribute Resource Cards
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

                    } else if (action.equals("settlement")) {

                    } else if (action.equals("city")) {

                    } else if (action.equals("DevCard")) {

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

    private int getPlayerCount() throws InterruptedException {
        return playerSpace.getAll(Templates.Player.getTemplateFields()).size();
    }

    private int getVictoryPoints(int id) {
        return 0;
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
