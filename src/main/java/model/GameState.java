package model;

public enum GameState {
    Trade("trade"),
    BuildRoad("buildRoad"),
    BuildSettlement("buildSettlement"),
    BuildCity("buildCity"),
    BuyDevelopmentCard("buyDevelopmentCard"),
    PlayDevelopmentCard("playDevelopmentCard"),
    None("none");

    String type;
    GameState(String type) {
        this.type = type;
    }
}
