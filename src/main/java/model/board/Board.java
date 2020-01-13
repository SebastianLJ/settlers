package model.board;

public class Board {
    private Hex[][] hexes = new Hex[5][5];
    private Edge[][] edges = new Edge[11][11];
    private Vertex[][] vertices = new Vertex[6][12];
    private final int FX_HEX_SIZE = 30;


    public Board() {

        //setup hexes
        int c = 0;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                if (j > hexes.length/2 - 1 - i && j < hexes.length + hexes.length/2 - i) {
                    hexes[i][j] = new Hex(j, i, Preset.getTerrain()[c], Preset.getNumberTokens()[c]);
                    c++;
                } else hexes[i][j] = null;
            }
        }

        //setup edges
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                if ((i % 2 == 0 && j % 2 == 0 || j % 2 == 1) && j > i + edges.length/2 - 1 && j < edges.length + edges.length/2 - i) {
                    edges[i][j] = new Edge(j, i);
                } else {
                    edges[i][j] = null;
                }
            }
        }

        //setup vertices
        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < vertices[0].length; j++) {
                if (j > 10 || i == 0 && j < 4 || i == 1 && j < 3 || i == 2 && j < 1 || i == 3 && j > 9 ||
                        i == 4 && j > 8 || i == 5 && j > 6) {
                    vertices[i][j] = null;
                } else {
                    vertices[i][j] = new Vertex(j,i);
                }
            }
        }
    }

    public Hex[] getAdjacentHexes(Vertex v) {
        int x = v.getX();
        int y = v.getY();
        Hex[] res = new Hex[3];
        if (x % 2 == 0) {
            if (x == 2) {
                res[0] = hexes[y-1][0];
                res[1] = hexes[y-1][x/2];
                res[2] = hexes[y][0];
            } else if (x == 0) {
                res[0] = hexes[0][y-1];
                res[1] = new Hex(-1, -1, Terrain.None, -1);
                res[2] = new Hex(-1, -1, Terrain.None, -1);
            } else if (y == 0) {
                res[0] = hexes[y][x/2 - 1];
                res[1] = hexes[y][x/2];
                res[2] = new Hex(-1, -1, Terrain.None, -1);
            } else {
                res[0] = hexes[y-1][x/2 - 1];
                res[1] = hexes[y-1][x/2];
                res[2] = hexes[y][x/2 - 1];
            }
        } else {
            if (x == 1) {
                res[0] = hexes[0][y-1];
                res[1] = hexes[0][y];
                res[2] = new Hex(-1, -1, Terrain.None, -1);;
            } else if (y == 0) {
                res[0] = hexes[y][x/2];
                res[1] = hexes[y][x/2 - 1];
                res[2] = res[2] = new Hex(-1, -1, Terrain.None, -1);
            } else {
                res[0] = hexes[y - 1][x/2];
                res[1] = hexes[y][x/2];
                res[2] = hexes[y][x/2 - 1];
            }
        }
        return res;
    }

    public Vertex[] getAdjacentVertices(Edge edge) {
        int x = edge.getX();
        int y = edge.getY();
        Vertex[] res = new Vertex[2];
        if (edge.y % 2 == 0) {
            res[0] = vertices[y-1][x];
            res[1] = vertices[y][x];
        } else {
            res[0] = vertices[y][x-1];
            res[1] = vertices[y][x];
        }
        return res;
    }

    public Vertex[] getAdjacentVertices(Hex hex) {
        int x = hex.getX();
        int y = hex.getY();
        Vertex[] res = new Vertex[6];
        if (y == 0) {
            res[0] = vertices[x*2+2][y+1];
            res[1] = vertices[x*2+1][y+1];
            res[2] = vertices[x*2][y+1];
            res[3] = vertices[x*2][y];
            res[4] = vertices[x*2+1][y];
            res[5] = vertices[x*2+2][y];
        } else {
            res[0] = vertices[x*2+2][y+1];
            res[1] = vertices[x*2+1][y+1];
            res[2] = vertices[x*2][y+1];
            res[3] = vertices[x*2+1][y];
            res[4] = vertices[x*2+2][y];
            res[5] = vertices[x*2+3][y];
        }
        return res;
    }

    public Edge[] getAdjacentEdges(Vertex vertex) {
        int x = vertex.getX();
        int y = vertex.getY();
        Edge[] res = new Edge[3];
        if (x % 2 == 0) {
            if (y == 0) {
                res[0] = edges[y][x];
                res[1] = edges[y][x+1];
                res[2] = edges[y+1][x];
            } else {
                res[0] = edges[y*2-1][x];
                res[1] = edges[y*2][x-1];
                res[2] = edges[y*2][x];
            }
        } else {
            if (y == 0) {
                res[0] = edges[y][x];
                res[1] = edges[y][x+1];
                res[2] = null;
            } else {
                res[1] = edges[y*2][x-1];
                res[2] = edges[y*2][x];
                res[0] = edges[y*2+1][x];
            }


        }
        return res;
    }

    public double[] getVertexTransformedCoordinates(Vertex vertex, int size, int centerX, int centerY,
                                                    int centerXTransformed, int centerYTransformed) {
        double[] res = new double[2];
        int angle;
        double angle_rad;
        Hex hex = hexes[centerY][centerX];

        Vertex[] temp = getAdjacentVertices(hex);

        int c = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals(vertex)) {
                c = i;
            }
        }

        angle = 60 * c * - 30;
        angle_rad = Math.PI / 180 * angle;
        res[0] = centerXTransformed + size * Math.cos(angle_rad);
        res[1] = centerYTransformed + size + Math.sin(angle_rad);

        return res;
    }

    public double[][] getEdgeTransformedCoordinates(Edge edge, int size, int centerX, int centerY,
                                              int centerXTransformed, int centerYTransformed) {
        double[][] res = new double[2][2];
        Vertex[] temp = getAdjacentVertices(edge);
        res[0] = getVertexTransformedCoordinates(temp[0], size, centerX, centerY, centerXTransformed, centerYTransformed);
        res[0] = getVertexTransformedCoordinates(temp[1], size, centerX, centerY, centerXTransformed, centerYTransformed);

        return res;
    }

    public void printHexes() {
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                if (hexes[i][j] == null) {
                    System.out.print("null ");
                } else {
                    System.out.print(hexes[i][j].getTerrain() + " ");
                }
            }
            System.out.println();
        }
    }

    public Edge[][] getEdges() {
        return edges;
    }
}
