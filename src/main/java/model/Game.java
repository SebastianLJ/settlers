package model;

import org.jspace.*;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final String hostURI;
    private final RemoteSpace players;
    private int playerCount;
    private Random dice = new Random();
    private Scanner scanner = new Scanner(System.in);

    public Game(String hostURI) throws IOException {
        this.hostURI = hostURI;
        players = new RemoteSpace(hostURI);

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
                //todo add robber
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

    private boolean isRoadValid() {
        return false;
    }

    private boolean isSettlementValid() {
        return false;
    }

    private boolean isCityValid() {
        return false;
    }

    private int getPlayerCount() throws InterruptedException {
        return players.getAll(Templates.Player.getTemplateFields()).size();
    }

    private int getVictoryPoints(int id) {
        return 0;
    }

    private int getSettlements(int id) {
        return 0;
    }

    private int getCities(int id) {
        return 0;
    }

    private int getLongestRoad(int id) {
        return 0;
    }
}
