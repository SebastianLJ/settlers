package model;

import org.jspace.FormalField;
import org.jspace.Template;
import org.jspace.TemplateField;

import java.util.ArrayList;

public enum Templates {
    Player(new Template(new FormalField(Integer.class), new FormalField(PlayerState.class)))
    ;

    private final Template template;

    Templates(Template template) {
        this.template = template;
    }

    public TemplateField[] getTemplateFields() {
        return template.getFields();
    }
}
