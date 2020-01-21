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
                if (gameSpace.getp(new ActualField("board_updated")) != null && !game.yourTurn()) {
                    Board board = (Board) gameSpace.query(Templates.board())[1];
                    game.setBoard(board);
                } else {
                    Board tempBoard = (Board) gameSpace.query(Templates.board())[1];

                    if (!tempBoard.equals(game.getBoard())) {
                        gameSpace.get(Templates.board());
                        gameSpace.put("board", game.getBoard());
                        for (int i = 0; i < game.getPlayerCount() - 1; i++) {
                            gameSpace.put("board_updated");
                        }
                        //System.out.println("board update sent");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}