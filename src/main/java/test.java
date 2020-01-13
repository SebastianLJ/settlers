import model.PlayerState;
import model.board.Board;
import model.board.Hex;
import model.board.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jspace.*;


public class test {
    public static void main(String[] args) {
        /*Board board = new Board();
        board.printHexes();
        System.out.println();
        Hex[] adj = board.getAdjacentHexes(new Vertex(4,0));
        for (Hex anAdj : adj) {
            if (anAdj == null) {
                System.out.print("None, ");
            } else {
                System.out.print(anAdj.getTerrain() + ", ");
            }
        }*/

        Space space = new SequentialSpace();
        PlayerState player1 = new PlayerState(1);
        PlayerState player2 = new PlayerState(2);
        PlayerState[] players = new PlayerState[2];
        players[0] = player1;
        players[1] = player2;
        int[] test = {0, 1, 2};
        try {
            Tuple playerTuple = new Tuple("players", players);

            space.put(playerTuple);
            System.out.println("put players in space");

            /*PlayerState[] p = (PlayerState[]) space.get(new ActualField("players"), new FormalField(PlayerState[].class))[1];
            System.out.println("Got players from space");
            for (int i = 0; i < p.length; i++) {
                System.out.println("Player id: " + p[i].getPlayerId());
            }*/
            PlayerState[] p = (PlayerState[]) space.get(new ActualField("players"), new FormalField(PlayerState[].class));
            System.out.println("player id: " + p[0].getPlayerId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
