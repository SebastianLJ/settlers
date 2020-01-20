package model.board;


public class Hex {
    private int x, y;
    private Terrain terrain;
    private int numberToken;
    private boolean robber = false;
    private double size;
    private double HEX_HEIGHT;
    private double HEX_WIDTH;
    private double offsetX;
    private double offsetY;

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
        return HEX_WIDTH*x + 0.5*HEX_WIDTH*y + offsetX;
    }

    public double getRealY() {
        return 3*HEX_HEIGHT/4.0*y + offsetY;
    }

    public Point[] getAdjacentVerticesLocation() {
        return new Point[]{new Point(getRealX() + HEX_WIDTH/2, getRealY() + HEX_HEIGHT/4), new Point(getRealX(), getRealY() + HEX_HEIGHT/2),
                new Point(getRealX() - HEX_WIDTH/2, getRealY() + HEX_HEIGHT/4), new Point(getRealX() - HEX_WIDTH/2, getRealY() - HEX_HEIGHT/4),
                new Point(getRealX(),getRealY() - HEX_HEIGHT/2), new Point(getRealX() + HEX_WIDTH/2, getRealY() - HEX_HEIGHT/4)};
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
        HEX_HEIGHT = 2 * size;
        HEX_WIDTH = Math.sqrt(3) * size;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getHeight() {
        return HEX_HEIGHT;
    }

    public double getWidth() {
        return HEX_WIDTH;
    }
}

