package model.board;

public enum Harbor {
    Generic("generic", null),
    Brick("special", Resource.Brick),
    Lumber("special", Resource.Lumber),
    Ore("special", Resource.Ore),
    Grain("special", Resource.Grain),
    Wool("special", Resource.Wool),
    None("none", null);
    ;

    private String type;
    private Resource resource;


    Harbor(String type, Resource resource) {
        this.type = type;
        this.resource = resource;
    }

    public String getType() {
        return type;
    }

    public Resource getResource() {
        return resource;
    }
}
