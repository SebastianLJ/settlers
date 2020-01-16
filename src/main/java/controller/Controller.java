package controller;

import model.Game;
import view.View;

import java.io.IOException;

public class Controller {

    private View view;
    private Game game;

    public Controller(View view) {
        this.view = view;
    }

    public void createGame(String URIForGame) throws IOException {
        game = new Game(URIForGame);
    }

    public Game getGame() {
        return game;
    }

}
