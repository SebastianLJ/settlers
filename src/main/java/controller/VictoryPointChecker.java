package controller;

import controller.NewController;
import model.Game;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class VictoryPointChecker implements Runnable {
    Game game;
    NewController controller;
    RemoteSpace space;

    public VictoryPointChecker(NewController controller, Game game) {
        this.game = game;
        this.space = game.getGameSpace();
        this.controller = controller;
    }

    @Override
    public void run() {
        Integer winnerId = null;
        while(true) {
            try {
                winnerId = (Integer) space.getp(new ActualField("game_won_by"),
                        new FormalField(Integer.class))[1];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (winnerId != null) {
                break;
            } else if (game.getVictoryPoints(game.getPlayerId()) >= 10) {
                winnerId = game.getPlayerId();
                try {
                    space.put("game_won_by", winnerId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        controller.gameWon(winnerId);
    }
}
