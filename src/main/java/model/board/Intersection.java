package model.board;

public class Intersection {
    private Hex[] adjHexes = new Hex[3];
    private Harbor harbor;
    private boolean settlement = false;
    private boolean city = false;
    private int id = -1;

    public Intersection(Hex[] hexes) {
        adjHexes[0] = hexes[0];
        adjHexes[1] = hexes[1];
        adjHexes[2] = hexes[2];
    }

    public Hex[] getAdjHexes() {
        return adjHexes;
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
}
