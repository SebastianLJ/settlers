package model;

import org.jspace.*;

import java.io.IOException;
import java.util.Random;

public class Game {
    private final String hostURI;
    private final RemoteSpace players;
    private int playerCount;
    private Random roll = new Random();

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

        //roll for starting player
        int turn = roll.nextInt(playerCount);

        while(!victory) {
            turn = turn % playerCount;
            




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
