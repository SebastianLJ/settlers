package model.board;

public enum Harbor {
    Generic("generic", null, 0),
    Brick("special", Resource.Brick, 1),
    Lumber("special", Resource.Lumber, 2),
    Ore("special", Resource.Ore, 3),
    Grain("special", Resource.Grain, 4),
    Wool("special", Resource.Wool, 5),
    None("none", null, 6);

    private String type;
    private Resource resource;
    private int id;

    Harbor(String type, Resource resource, int id) {
        this.type = type;
        this.resource = resource;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Resource getResource() {
        return resource;
    }
}
