package model.board;

public class Hex {
    private int x, y;
    private Terrain terrain;
    private int numberToken;

    public Hex(int x, int y, Terrain terrain, int numberToken) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
        this.numberToken = numberToken;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getNumberToken() {
        return numberToken;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

