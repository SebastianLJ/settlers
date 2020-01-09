package model.board;

public class Hex {
    private Terrain terrain;
    private int numberToken;
    private Intersection[] intersections = new Intersection[6];
    private Path[] paths = new Path[6];

    public Hex(Terrain terrain, int numberToken) {

    }

    public void setup(Hex[] adjHexes) {
        int j;
        for (int i = 0; i < 6; i++) {
            j = i - 1;
            if (j == -1) {
                j = 5;
            }
            Hex[] temp = {this, adjHexes[i], adjHexes[j]};
            intersections[i] = new Intersection(temp);
        }

        for (int i = 0; i < 6; i++) {
            paths[i] = new Path(this, adjHexes[0]);
        }
    }
}

