package model;

public enum Terrain {
    Hills("hills", Resource.Brick),
    Forest("forest", Resource.Lumber),
    Mountains("mountains", Resource.Ore),
    Fields("fields", Resource.Grain),
    Pasture("pasture", Resource.Wool);


    private final String type;
    private final Resource product;

    Terrain(String type, Resource product) {
        this.type = type;
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public Resource getProduct() {
        return product;
    }
}
