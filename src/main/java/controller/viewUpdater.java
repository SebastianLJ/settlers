package controller;

import javafx.application.Platform;
import view.NewView;

public class viewUpdater implements Runnable {

    private NewController controller;
    private NewView view;

    viewUpdater(NewController controller, NewView view) {
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
