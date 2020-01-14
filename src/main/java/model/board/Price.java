package model.board;

import java.util.ArrayList;

public enum Price {
    Road(new ArrayList<Resource>() {{   add(Resource.Brick);
                                        add(Resource.Lumber);}}),
    Settlement(new ArrayList<Resource>() {{ add(Resource.Brick);
                                            add(Resource.Lumber);
                                            add(Resource.Grain);
                                            add(Resource.Wool);}}),

    City(new ArrayList<Resource>() {{ add(Resource.Grain);
        add(Resource.Grain);
        add(Resource.Ore);
        add(Resource.Ore);
        add(Resource.Ore);}}),

    DevelopmentCard(new ArrayList<Resource>() {{ add(Resource.Grain);
        add(Resource.Wool);
        add(Resource.Ore);}})
    ;

    private ArrayList<Resource> price;

    Price(ArrayList<Resource> price) {
        this.price = price;
    }

    public ArrayList<Resource> getPrice() {
        return price;
    }
}
