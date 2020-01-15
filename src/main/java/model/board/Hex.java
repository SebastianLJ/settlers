package model.board;

public class Hex {
    private int x, y;
    private Terrain terrain;
    private int numberToken;
    private boolean robber = false;
    private double sqrt3 = Math.sqrt(3);

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

    public double getRealX() {
        return sqrt3*x + 0.5*sqrt3*y;
    }

    public double getRealY() {
        return 6.0/4*y;
    }

}

