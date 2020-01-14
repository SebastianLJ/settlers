package model.board;

public enum DevelopmentCard {
    Knight("knight"),
    VictoryPoint("victortPoint"),
    RoadBuilding("roadBuilding"),
    YearOfPlenty("yearOfPlenty"),
    Monopoly("monopoly");

    private String type;

    DevelopmentCard(String type) {
        this.type = type;
    }
}
