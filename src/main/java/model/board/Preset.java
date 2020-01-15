package model.board;

import java.util.ArrayList;

public class Preset {
    private static int[][] adjacencyList = {{-1,1,4,3,-1,-1},{-1,2,5,4,0,-1}, {-1,-1,6,5,1,-1},
            {0,4,8,7,-1,-1}, {1,5,9,8,3,0}, {2,6,10,9,4,1}, {-1,-1,11,10,5,2},
            {3,8,12,-1,-1,-1}, {4,9,13,12,7,3}, {5,10,14,13,8,4}, {6,11,15,14,9,5}, {-1,-1,-1,15,10,6},
            {8,13,16,-1,-1,7}, {9,14,17,16,12,8}, {10,15,18,17,13,9}, {11,-1,-1,18,14,10},
            {13,17,-1,-1,-1,12}, {14,18,-1,-1,16,13}, {15,-1,-1,-1,17,14}};

    private static Terrain[] terrain = {Terrain.Mountains, Terrain.Pasture, Terrain.Forest,
            Terrain.Fields, Terrain.Hills, Terrain.Pasture, Terrain.Hills,
            Terrain.Fields, Terrain.Forest, Terrain.Desert, Terrain.Forest, Terrain.Mountains,
            Terrain.Forest, Terrain.Mountains, Terrain.Fields, Terrain.Pasture,
            Terrain.Hills, Terrain.Fields, Terrain.Pasture};

    private static int[] numberTokens = {10, 2, 9,
            12, 6, 4, 10,
            9, 11, 7, 3, 8,
            8, 3, 4, 5,
            5, 6, 11};

    //[tile, intersection, intersection]
    private static int[][] harbors = {{4, 0, 5}, {5, 0, 5}, {7, 0, 3}, {8, 0, 3},
            {3, 1, 1}, {2, 2, 1}, {10, 1, 2}, {10, 2, 2},
            {1, 3, 0}, {0, 4, 0}, {11, 2, 5}, {10, 3, 5}, {0, 5, 5}, {1, 5, 5},
            {3, 5, 5}, {4, 5, 5}, {7, 4, 4}, {8, 4, 4}};

    public static int[][] getAdjacencyList() {
        return adjacencyList;
    }

    public static Terrain[] getTerrain() {
        return terrain;
    }

    public static int[] getNumberTokens() {
        return numberTokens;
    }

    public static int[][] getHarbors() {
        return harbors;
    }
}
