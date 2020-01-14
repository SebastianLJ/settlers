package model;

import model.board.DevelopmentCard;
import model.board.Resource;

import java.util.ArrayList;

public class PlayerState {
    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
    private int playerId;
    private boolean hasLongestRoad = false;
    private boolean hasLargetArmy = false;
    private int longestRoad= 1;
    private int armySize = 0;

    public PlayerState(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(int longestRoad) {
        this.longestRoad = longestRoad;
    }

    public int getArmySize() {
        return armySize;
    }

    public void setArmySize(int armySize) {
        this.armySize = armySize;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public boolean hasLongestRoad() {
        return hasLongestRoad;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public boolean hasLargetArmy() {
        return hasLargetArmy;
    }

    public void setHasLargetArmy(boolean hasLargetArmy) {
        this.hasLargetArmy = hasLargetArmy;
    }
}
