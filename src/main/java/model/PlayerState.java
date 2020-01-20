package model;

import model.board.DevelopmentCard;
import model.board.Resource;
import org.jspace.Tuple;

import java.util.ArrayList;

public class PlayerState {
    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
    private int playerId;
    private boolean hasLongestRoad = false;
    private boolean hasLargestArmy = false;
    private int longestRoad= 1;
    private int armySize = 0;
    private String name;

    public PlayerState(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public int getId() {
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

    public boolean hasLargestArmy() {
        return hasLargestArmy;
    }

    public void setHasLargestArmy(boolean hasLargestArmy) {
        this.hasLargestArmy = hasLargestArmy;
    }

    public int getResourceAmount(Resource resourceMatch) {
        int count = 0;
        for (Resource resource : resources) {
            if (resource == resourceMatch) count++;
        }
        return count;
    }

    public String resourcesToString() {
        StringBuilder res = new StringBuilder();
        for (Resource resource : resources) {
            res.append(resource.getType()).append(", ");
        }
        return res.toString();
    }

    public void useResources(ArrayList<Resource> usedResources) {
        for (Resource resource : usedResources) {
            resources.remove(resource);
        }
    }

    public String getName() {
        return name;
    }
}
