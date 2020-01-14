package model.board;

public class Hex {
    private int x, y;
    private Terrain terrain;
    private int numberToken;
    private boolean robber = false;

    public Hex(int x, int y, Terrain terrain, int numberToken) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
        this.numberToken = numberToken;
    }

    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean isRobber() {
        return robber;
    }

    public void setRobber(boolean robber) {
        this.robber = robber;
    }
}

