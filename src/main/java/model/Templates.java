package model;

import model.board.Board;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Template;
import org.jspace.TemplateField;

import java.util.ArrayList;

public class Templates {
    /*player id, player state
    Player(new Template(new FormalField(String.class), new FormalField(Integer.class), new FormalField(PlayerState.class))),

    //turn, game
    Turn(new Template(new FormalField(Integer.class), new FormalField(Game.class)))
    ;
    */
    private Template player = new Template(new FormalField(String.class), new FormalField(Integer.class),
            new FormalField(PlayerState.class));


    public static TemplateField[] player(int id) {
        Template template = new Template(new FormalField(String.class), new ActualField(id),
                new FormalField(PlayerState.class));
        return template.getFields();
    }

    public static TemplateField[] player(String name) {
        Template template = new Template(new ActualField(name), new FormalField(Integer.class),
                new FormalField(PlayerState.class));
        return template.getFields();
    }

    public static TemplateField[] player() {
        Template template = new Template(new FormalField(String.class), new FormalField(Integer.class),
                new FormalField(PlayerState.class));
        return template.getFields();
    }

    public static TemplateField[] turn() {
        Template template = new Template(new ActualField("turn_count"), new FormalField(Integer.class));
        return template.getFields();
    }

    public static TemplateField[] board() {
        Template template = new Template(new ActualField("board"), new FormalField(Board.class));
        return template.getFields();
    }

    public static TemplateField[] lock() {
        Template template = new Template(new ActualField("lock"));
        return template.getFields();
    }


}
