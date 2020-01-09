package model.board;

public class Path {
    private Hex[] adjHexes = new Hex[2];
    private int id = -1;

    public Path(Hex hex1 ,Hex hex2) {
        adjHexes[0] = hex1;
        adjHexes[1] = hex2;
    }

    public Hex[] getAdjHexes() {
        return adjHexes;
    }

    public int getId() {
        return id;
    }
}
