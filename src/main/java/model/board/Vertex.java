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

    public void buildSettlement(int id) {
        this.id = id;
        settlement = true;
        city = false;
    }

    public void buildCity(int id) {
        this.id = id;
        settlement = false;
        city = true;
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

    public void setHarbor(int harborType) {
        switch (harborType) {
            case 0:
                harbor = Harbor.Brick;
                break;
            case 1:
                harbor = Harbor.Lumber;
                break;
            case 2:
                harbor = Harbor.Ore;
                break;
            case 3:
                harbor = Harbor.Grain;
                break;
            case 4:
                harbor = Harbor.Wool;
            case 5:
                harbor = Harbor.Generic;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        Vertex tVertex = (Vertex) obj;
        return tVertex.isCity() == this.isCity() && tVertex.isSettlement() == this.isSettlement() &&
                tVertex.getId() == this.getId() && tVertex.getX() == this.getX() &&
                tVertex.getY() == this.getY();
    }
}
