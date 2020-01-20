package model.board;

import java.util.ArrayList;

public enum Resource {
    Brick("brick"),
    Lumber("lumber"),
    Ore("ore"),
    Grain("grain"),
    Wool("wool");


    private final String type;

    Resource(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
