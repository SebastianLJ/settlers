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

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        Edge tEdge = (Edge) obj;
        return  tEdge.getId() == this.getId() && tEdge.getX() == this.getX() &&
                tEdge.getY() == this.getY();
    }
}
