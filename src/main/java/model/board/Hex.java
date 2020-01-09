package model.board;

public class Hex {
    private Terrain terrain;
    private int numberToken;
    private Intersection[] intersections = new Intersection[6];
    private Path[] paths = new Path[6];

    public Hex(Terrain terrain, int numberToken) {
        this.terrain = terrain;
        this.numberToken = numberToken;
    }

    public void setup(Hex[] hexes, int[] adjHexes) {
        int j;
        Hex hex1, hex2;
        for (int i = 0; i < 6; i++) {
            j = i - 1;
            if (j == -1) {
                j = 5;
            }
            if (adjHexes[i] == -1) {
                hex1 = null;
            } else {
                hex1 = hexes[adjHexes[i]];
            }
            if (adjHexes[j] == -1) {
                hex2 = null;
            } else {
                hex2 = hexes[adjHexes[j]];
            }
            Hex[] tempHexes = {this, hex1, hex2};
            Intersection tempIntersection = isIntersectionDefined(tempHexes);

            if (tempIntersection == null) {
                intersections[i] = new Intersection(tempHexes);
            } else {
                intersections[i] = tempIntersection;
            }
        }

        //setup patch pointers
        for (int i = 0; i < 6; i++) {

            if (adjHexes[i] == -1) {
                hex1 = null;
            } else {
                hex1 = hexes[adjHexes[i]];
            }

            Path tempPath = isPathDefined(hex1, i);
            if (tempPath == null) {
                paths[i] = new Path(this, hex1);
            } else {
                paths[i] = tempPath;
            }

        }
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getNumberToken() {
        return numberToken;
    }

    public Intersection[] getIntersections() {
        return intersections;
    }

    public Path[] getPaths() {
        return paths;
    }

    private Path isPathDefined(Hex adjHex, int pos) {
        if (adjHex == null) {
            return null;
        } else {
            return adjHex.getPaths()[(pos + 3) % 6];
        }
    }

    private Intersection isIntersectionDefined(Hex[] adjHexes) {
        if (adjHexes[1] == null && adjHexes[2] == null) {
            return null;
        } else if (adjHexes[1] == null) {
            return adjHexes[2].getMatchingIntersection(adjHexes);
        } else if (adjHexes[2] == null) {
            return adjHexes[1].getMatchingIntersection(adjHexes);
        } else {
            Intersection tempIntersection1 = adjHexes[1].getMatchingIntersection(adjHexes);
            Intersection tempIntersection2 = adjHexes[2].getMatchingIntersection(adjHexes);
            if (tempIntersection1 == null) {
                return tempIntersection2;
            } else return tempIntersection1;
        }
    }

    private Intersection getMatchingIntersection(Hex[] adjHex) {
        for (int i = 0; i < intersections.length; i++) {
            if (intersections[i].equals(adjHex)) {
                return intersections[i];
            }
        }
        return null;
    }
}

