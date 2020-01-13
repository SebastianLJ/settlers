package model;

import model.board.Resource;

import java.util.ArrayList;

public class PlayerState {
    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
    private int playerId;
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

    public void removeResources(ArrayList<Resource> usedResources) {
        for (Resource resource : usedResources) {
            resources.remove(resource);
        }
    }

    public void removeDevelopmentCards(ArrayList<DevelopmentCard> usedDevCards) {
        for (DevelopmentCard developmentCard : usedDevCards) {
            developmentCards.remove(developmentCard);
        }
    }
}
