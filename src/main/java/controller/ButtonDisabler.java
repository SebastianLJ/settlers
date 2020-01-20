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
        if (!controller.isInitialState()) {
            controller.getBuildRoad().setDisable(!(game.canBuildRoad()));
            controller.getBuildSettlement().setDisable(!(game.canBuildCity()));
            controller.disableBuyCity(!game.canBuildCity());
            controller.getBuildDevCard().setDisable(!game.canBuildDevelopmentCard());
            controller.getTradeWithBank().setDisable(!game.canTradeWithBank());
        }
    }
}
