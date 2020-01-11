package model.board;



public class Vertex {
    private Hex[] adjHexes = new Hex[3];
    private Harbor harbor;
    private boolean settlement = false;
    private boolean city = false;
    private int id = -1;

    public Vertex(Hex[] hexes) {
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

    public boolean equals(Hex[] adjHexes) {
        int matches = 0;
        for (int i = 0; i < this.adjHexes.length; i++) {
            for (int j = 0; j < adjHexes.length; j++) {
                if (this.adjHexes[i].equals(adjHexes[j])) {
                    matches++;
                }
            }
        }
        return matches == 3;
    }
}
