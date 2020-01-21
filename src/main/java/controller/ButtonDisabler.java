package controller;

import model.Game;

public class ButtonDisabler implements Runnable {

    private Controller controller;
    private Game game;

    public ButtonDisabler(Controller controller, Game game) {
        this.controller = controller;
        this.game = game;
    }

    @Override
    public void run() {
        while(true) {
            controller.setBuildRoad(!(controller.isInitialState() || game.canBuildRoad()));
            controller.setBuildSettlement(!(controller.isInitialState() || game.canBuildSettlement()));
            controller.setBuildCity(!game.canBuildCity());
            controller.setBuildDevCard(!game.canBuildDevelopmentCard());
            controller.setTradeWithBank(!game.canTradeWithBank());
            controller.setTradeWithPlayer(!game.canTradeWithPlayer());
            controller.setPlayDevCard(!game.canPlayDevCard());
            controller.setViewDevCard(!game.canViewDevCard());
        }
    }
}
