package model.board;

public class Edge {
    int x,y;
    private int id = -1;

    public Edge(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
