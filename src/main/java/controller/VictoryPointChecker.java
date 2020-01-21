package controller;

import model.Game;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class VictoryPointChecker implements Runnable {
    private Game game;
    private Controller controller;
    private RemoteSpace space;

    public VictoryPointChecker(Controller controller, Game game) {
        this.game = game;
        if (game == null) {
            System.out.println("check");
        }
        this.space = game.getGameSpace();
        this.controller = controller;
    }

    @Override
    public void run() {
        Integer winnerId = null;
        Object[] obj = null;
        while(true) {
            try {
                obj = space.getp(new ActualField("game_won_by"),
                        new FormalField(Integer.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                winnerId = (Integer) obj[1];
                break;
            } else if (game.getVictoryPoints(game.getPlayerId()) >= 10) {
                winnerId = game.getPlayerId();
                try {
                    space.put("game_won_by", winnerId);
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        controller.gameWon(winnerId);
    }
}
