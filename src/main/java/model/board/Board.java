package model.board;

public class Board {
    private Hex[][] hexes = new Hex[5][5];
    private Edge[][] edges = new Edge[11][11];
    private Vertex[][] vertices = new Vertex[11][11];


    public Board() {

        //setup hexes
        int c = 0;
        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes.length; j++) {
                if (j > i + hexes.length/2 - 1 && j < hexes.length + hexes.length/2 - i) {
                    hexes[i][j] = new Hex(i, j, Preset.getTerrain()[c], Preset.getNumberTokens()[c]);
                    c++;
                } else hexes[i][j] = null;
            }
        }

        //setup edges
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                Edge temp = null;
                if (i % 2 == 0 && j % 2 == 0 && j > i + edges.length/2 - 1 && j < edges.length + edges.length/2 - i) {
                    temp = new Edge(i,j);
                } else if (j % 2 == 1 && j > i + edges.length/2 - 1 && j < edges.length + edges.length/2 - i) {
                    temp = new Edge(i, j);
                }
                edges[i][j] = temp;
            }
        }

        //todo setup vertices
    }
}
