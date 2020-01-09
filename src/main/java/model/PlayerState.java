package model;

import model.board.Resource;

import java.util.ArrayList;

public class PlayerState {
    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private ArrayList<DevelopmentCard> developmentCards = new ArrayList<DevelopmentCard>();
    private int[][] settlements = new int[5][2];
    private int[][] cities = new int[5][2];
    private int[][] roads = new int[16][2];
    private int longestRoad;
    private int armySize;

}
