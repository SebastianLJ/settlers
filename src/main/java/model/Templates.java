package model;

import org.jspace.FormalField;
import org.jspace.Template;
import org.jspace.TemplateField;

public enum Templates {
    Player(new Template(new FormalField(String.class), new FormalField(Integer.class)))
    ;

    private final Template template;

    Templates(Template template) {
        this.template = template;
    }

    public TemplateField[] getTemplateFields() {
        return template.getFields();
    }
}
