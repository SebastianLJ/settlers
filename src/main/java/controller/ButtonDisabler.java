package controller;

import model.Game;

public class ButtonDisabler implements Runnable {

    private NewController controller;
    private Game game;

    public ButtonDisabler(NewController controller, Game game) {
        this.controller = controller;
        this.game = game;
    }

    @Override
    public void run() {
        while(true) {
            if (controller.getBuildRoad() != null && game.canBuildRoad()) {
                System.out.println("can build road");
                controller.getBuildRoad().setDisable(false);
            } else if (controller.getBuildRoad() != null) {
                System.out.println("can't build road");
                controller.getBuildRoad().setDisable(true);
            }
        }
    }
}
