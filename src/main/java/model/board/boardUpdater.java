package model.board;

import model.Templates;
import org.jspace.*;

public class boardUpdater implements Runnable {
    private Board board;
    private RemoteSpace gameSpace;

    public boardUpdater(Board board, RemoteSpace gameSpace) {
        this.board = board;
        this.gameSpace = gameSpace;
    }

    @Override
    public void run() {
        while(true) {
            try {
                board = (Board) gameSpace.query(Templates.board())[1];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
