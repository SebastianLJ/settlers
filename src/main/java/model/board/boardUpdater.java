package model.board;

import model.Game;
import model.Templates;
import org.jspace.*;

public class boardUpdater implements Runnable {
    private Game game;
    private RemoteSpace gameSpace;

    public boardUpdater(Game game, RemoteSpace gameSpace) {
        this.game = game;
        this.gameSpace = gameSpace;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if (!game.yourTurn()) {
                    Board board = (Board) gameSpace.query(Templates.board())[1];
                    game.setBoard(board);
                }
                //board.printVertexIds();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
