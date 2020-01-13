package model.board;



public class Vertex {
    int x, y;
    private Harbor harbor;
    private boolean settlement = false;
    private boolean city = false;
    private int id = -1;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Harbor getHarbor() {
        return harbor;
    }

    public boolean isSettlement() {
        return settlement;
    }

    public boolean isCity() {
        return city;
    }

    public int getId() {
        return id;
    }

    public void setHarbor(Harbor harbor) {
        this.harbor = harbor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Vertex vertex) {
        return this.x == vertex.getX() && this.y == vertex.getY();
    }
}
