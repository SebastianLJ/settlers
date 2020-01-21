package controller;

import javafx.application.Platform;
import view.View;

public class viewUpdater implements Runnable {

    private Controller controller;
    private View view;

    viewUpdater(Controller controller, View view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void run() {

        Runnable updater = () -> {
            view.update(controller.getMap());
            view.updatePlayerInfo(controller.getOwnPlayerInfo(), controller.getOtherPlayerInfo());
            view.updateChat(controller.getListView());
            view.updateDiceRoll(controller.getDiceRollLabel());
        };

        while (true) {
            Platform.runLater(updater);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
