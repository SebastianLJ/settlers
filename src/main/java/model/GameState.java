package model;

public enum GameState {
    TradeBank("tradeBank"),
    TradePlayer("tradePlayer"),
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
