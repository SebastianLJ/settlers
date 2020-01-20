package model;

import javafx.collections.ObservableList;
import model.board.*;
import org.jspace.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Random dice = new Random();
    private RemoteSpace gameSpace;
    private RemoteSpace chatSpace;
    private Board board;
    private String hostURI;
    private PlayerState player;
    private int startingSettlementsBuiltThisTurn = 0;
    private int startingRoadsBuiltThisTurn = 0;
    private Chat chat;

    public Game(String hostURI, boolean isHost, String playerName) throws IOException {
        this.hostURI = hostURI;

        if (isHost) {
            SequentialSpace gameTemp = new SequentialSpace();
            SequentialSpace chatTemp = new SequentialSpace();
            SpaceRepository repository = new SpaceRepository();

            // Setting up URI
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ip = inetAddress.getHostAddress();
            int port = 9001;

            System.out.println("A game is hosted on IP:Port: " + ip + ":" + port);

            String URI = "tcp://" + ip + ":" + port;

            // Opening gate at given URI
            repository.addGate(URI + "?keep");
            repository.add("game", gameTemp);
            repository.add("chatSpace", chatTemp);

            gameSpace = new RemoteSpace(URI + "/game?keep");
            chatSpace = new RemoteSpace(URI + "/chatSpace?keep");


            board = new Board();
            this.player = new PlayerState(0, playerName);
            try {
                gameSpace.put(playerName, player.getId(), player);
                gameSpace.put("turn_count", 0);
                gameSpace.put("board", board);
                gameSpace.put("player_count", 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            gameSpace = new RemoteSpace(this.hostURI + "/game?keep");
            chatSpace = new RemoteSpace(this.hostURI + "/chatSpace?keep");

            this.player = new PlayerState(getPlayerCount(), playerName);
            try {
                gameSpace.put(playerName, player.getId(), player);
                board = (Board) gameSpace.query(Templates.board())[1];
                int playerCount = (int) gameSpace.get(Templates.playerCount())[1];
                gameSpace.put("player_count", playerCount + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        chat = new Chat(chatSpace);
        new Thread(chat).start();
        new Thread(new boardUpdater(this,gameSpace)).start();
    }

    public int roll() {

        try {
            gameSpace.getp(Templates.dices());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int dice_one = dice.nextInt(6) + 1;
        int dice_two = dice.nextInt(6) + 1;

        try {
            gameSpace.put("dices", dice_one, dice_two);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int roll = dice_one + dice_two;
        System.out.println(player.getName() + " rolled " + roll);

        if (roll == 7) {
            boolean success = false;
            // TODO Fix with new controller
                /*while (!success) {
                    int[] coords = controller.getHexCoordinates();
                    success = board.updateRobber(coords[0],coords[1]);
                }*/
        } else {
            distributeResources(roll);
        }
        return roll;
    }

    public void trade() {
        String action = scanner.next();

        if (action.equals("trade")) {
            action = scanner.next();

            if (action.equals("player")) {
                //todo
            } else if (action.equals("bank") ||action.equals("harbor")) {
                System.out.println("Please select a resource to trade ");
                String resource = scanner.next();
                if (harborTrades(player).contains(stringToResource(resource))
                        && player.getResourceAmount(stringToResource(resource)) >= 2) {
                    for (int i = 0; i < 2; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else if (hasGenericHarbor(player)
                        && player.getResourceAmount(stringToResource(resource)) >= 3) {
                    for (int i = 0; i < 3; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else if (player.getResourceAmount(stringToResource(resource)) >= 4) {
                    for (int i = 0; i < 4; i++) {
                        player.getResources().remove(stringToResource(resource));
                    }
                    System.out.println("Please select a single resource to receive: ");
                    resource = scanner.next();
                    player.getResources().add(stringToResource(resource));
                } else {
                    System.out.println("you don't have enough " + resource + " to trade");
                }
            }
        }
    }

    /**
     * Adds player id to edge, and checks if location is valid
     * @param edge
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildRoad(Edge edge) {
        if (player.getResources().containsAll(Price.Road.getPrice())) {
            if (isRoadValid(edge)) {
                player.getResources().removeAll(Price.Road.getPrice());
                edge.setId(player.getId());
                System.out.println("Successfully built road");
                return 1;
            } else {
                System.out.println("Invalid road location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    public int buildStartingRoad(Edge edge) {
        if (startingRoadsBuiltThisTurn > 0) {
            System.out.println("Can't build anymore roads this turn");
            return -1;
        }
        if (isRoadValid(edge)) {
            edge.setId(player.getId());
            System.out.println("Successfully built road");
            startingRoadsBuiltThisTurn++;
            return 1;
        } else {
            System.out.println("Invalid road location");
            return -2;
        }
    }

    /**
     * Adds player id to vertex and boolean hasSettlement, and checks if location is valid
     * @param vertex
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildSettlement(Vertex vertex) {
        if (player.getResources().containsAll(Price.Settlement.getPrice())) {
            if (isSettlementValid(vertex)) {
                player.getResources().removeAll(Price.Settlement.getPrice());
                vertex.buildSettlement(player.getId());
                System.out.println("Successfully built settlement");
                return 1;
            } else {
                System.out.println("Invalid settlement location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     * Initial placement of settlement
     * @param vertex
     * @return -2 invalid location, -1 already built starting settlement this turn, 1 successfully built
     */
    public int buildStartingSettlement(Vertex vertex) {
        if (startingSettlementsBuiltThisTurn > 0) {
            System.out.println("Can't build anymore settlements this turn");
            return -1;
        } else if (isSettlementValidLength(vertex)) {
            vertex.buildSettlement(player.getId());
            System.out.println("Successfully built settlement");
            startingSettlementsBuiltThisTurn++;
            return 1;
        } else {
            System.out.println("Invalid settlement location");
            return -2;
        }

    }

    /**
     * Adds player id to vertex and boolean hasSettlement, and checks if location is valid
     * @param vertex
     * @return -2 invalid location, -1 insufficient resources, 1 successfully built
     */
    public int buildCity(Vertex vertex) {
        if (player.getResources().containsAll(Price.City.getPrice())) {
            if (isCityValid(vertex)) {
                player.getResources().removeAll(Price.City.getPrice());
                vertex.buildCity(player.getId());
                System.out.println("Successfully built city");
                return 1;
            } else {
                System.out.println("Invalid city location");
                return -2;
            }
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     *
     * @return -1 insufficient resources, 1 successfully bought
     */
    public int buyDevelopmentCard() {
        if (player.getResources().containsAll(Price.DevelopmentCard.getPrice())) {
            player.getResources().removeAll(Price.DevelopmentCard.getPrice());
            DevelopmentCard devCard = board.buyDevelopmentCard();
            player.getDevelopmentCards().add(devCard);
            System.out.println("You received: " + devCard);
            return 1;
        } else {
            System.out.println("Not enough resources");
            return -1;
        }
    }

    /**
     *
     * @param developmentCard
     * @param hex
     * @return -1 invalid location, 1 successfully played development card
     */
    public int playDevelopmentCard(DevelopmentCard developmentCard, Hex hex) {
        if (developmentCard == DevelopmentCard.Knight) {
            return board.updateRobber(hex.getX(), hex.getY());

        } else if (developmentCard == DevelopmentCard.RoadBuilding) {
            int roadsPlaced = 0;
            // TODO Fix with new controller
                        /*while (roadsPlaced < 2) {
                            boolean success = false;
                            while(!success) {
                                Edge edge = controller.getEdgeCoordinates();
                                if (isRoadValid(edge)) {
                                    edge.setId(turnId);
                                    roadsPlaced++;
                                    success = true;
                                } else {
                                    System.out.println("Invalid road location");
                                }
                            }
                        }*/
        } else if (developmentCard == DevelopmentCard.YearOfPlenty) {
            int resourcesSelected = 0;
            while (resourcesSelected < 2) {
                System.out.println("Please select a resource: ");
                String resourceType = scanner.next();
                if (stringToResource(resourceType) != null) {
                    player.getResources().add(stringToResource(resourceType));
                }
                resourcesSelected++;
            }
        } else if (developmentCard == DevelopmentCard.Monopoly) {
            //todo
        }
        return -1;
    }

    public int endTurn() {
        updateBoard();
        int turn = -1;
        try {
            turn = (int) gameSpace.get(Templates.turn())[1];
            gameSpace.put("turn_count", turn + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return turn + 1;
    }

    public int endInitTurn() {
        int playerCount = getPlayerCount();
        int turn = 0;
        updateBoard();
        try {
            turn = (int) gameSpace.get(Templates.turn())[1];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (turn == playerCount) {
            setInitStageOne(true);
            turn--;
        } else if (getInitStageOne()) {
            turn--;
        } else {
            turn++;
        }

        try {
            gameSpace.put("turn_count", turn);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startingSettlementsBuiltThisTurn = 0;
        startingRoadsBuiltThisTurn = 0;
        return turn;
    }

    private void setInitStageOne(boolean b) {
        try {
            gameSpace.put("init_stage_one", b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean getInitStageOne() {
        boolean initStageOne = false;
        try {
            Object[] temp = gameSpace.queryp(new ActualField("init_stage_one")
                    , new FormalField(Boolean.class));
            if (temp != null) {
                initStageOne = (boolean) temp[1];
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return initStageOne;
    }

    //todo check victory points

    private boolean isRoadValid(Edge edge) {
        if (edge == null) {
            return false;
        }
        Vertex[] vertices = board.getAdjacentVertices(edge);
        for (Vertex vertex : vertices) {
            if (vertex.getId() == player.getId() && vertex.isCity() || vertex.isSettlement()) {
                return true;
            }
            for (Edge nextEdge : board.getAdjacentEdges(vertex)) {
                if (nextEdge.getId() == player.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Edge> getValidRoads() {
        Edge[][] edges = board.getEdges();
        ArrayList<Edge> res = new ArrayList<>();
        for (Edge[] edgeList : edges) {
            for (Edge edge : edgeList) {
                if (isRoadValid(edge)) {
                    res.add(edge);
                }
            }
        }
        return res;
    }

    private boolean isSettlementValidLength(Vertex vertex) {
        Edge[] edges = board.getAdjacentEdges(vertex);
        for (Edge edge : edges) {
            if (edge != null) {
                Vertex[] possibleOccupations = board.getAdjacentVertices(edge);
                for (Vertex possibleOccupation : possibleOccupations) {
                    if (possibleOccupation.isSettlement() || possibleOccupation.isCity()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isSettlementConnected(Vertex vertex) {
        Edge[] edges = board.getAdjacentEdges(vertex);
        for (Edge edge : edges) {
            if (edge.getId() == player.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSettlementValid(Vertex vertex) {
        //return isSettlementConnected(vertex) && isSettlementValidLength(vertex);
        return isSettlementValidLength(vertex);
    }

    private boolean isCityValid(Vertex vertex) {
        return vertex.isSettlement();
    }

    private int getPlayerCount() {
        try {
            return (int) gameSpace.query(Templates.playerCount())[1];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean yourTurn() {
        return player.getId() == getTurnId();
    }

    public int getVictoryPoints(int playerId) {
        PlayerState player = null;
        try {
            player = (PlayerState) gameSpace.query(Templates.player(playerId))[2];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (player != null) {
            int vp = getSettlements(player.getId()).size() + getCities(player.getId()).size();
            for (DevelopmentCard developmentCard : player.getDevelopmentCards()) {
                if (developmentCard.equals(DevelopmentCard.VictoryPoint)) {
                    vp++;
                }
            }
            if (player.hasLargestArmy()) vp++;
            if (player.hasLongestRoad()) vp++;

            return vp;
        }
        return 0;

    }

    private void distributeResources(int roll) {
        List<Object[]> players = new ArrayList<>();
        try {
            players = gameSpace.getAll(new FormalField(String.class), new FormalField(Integer.class),
                    new FormalField(PlayerState.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Object[] playerTuple : players) {
            PlayerState tPlayer = (PlayerState) playerTuple[2];
            ArrayList<Vertex> settlements = getSettlements(tPlayer.getId());
            ArrayList<Vertex> cities = getCities(tPlayer.getId());

            ArrayList<Resource> resources = new ArrayList<>();
            resources.addAll(getResources(settlements, roll));
            resources.addAll(getResources(cities, roll));

            tPlayer.getResources().addAll(resources);

            if (!resources.isEmpty()) {
                System.out.println(tPlayer.getName() + " received " + resources.toString());
            }
            try {
                gameSpace.put(tPlayer.getName(), tPlayer.getId(), tPlayer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Resource> getResources(int playerId) {
        try {
            PlayerState tempPlayer = (PlayerState) gameSpace.query(Templates.player(playerId))[2];
            return tempPlayer.getResources();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Resource> getResources(ArrayList<Vertex> vertices, int roll) {
        ArrayList<Resource> res = new ArrayList<>();
        for (Vertex vertex : vertices) {
            for (Hex hex : board.getAdjacentHexes(vertex)) {
                if (hex.getNumberToken() == roll) {
                    res.add(hex.getTerrain().getProduct());
                }
            }
        }
        return res;
    }

    private ArrayList<Vertex> getSettlements(int id) {
        ArrayList<Vertex> settlements = new ArrayList<>();
        for (Vertex[] vertexList : board.getVertices()) {
            for (Vertex vertex : vertexList) {
                if (vertex != null && vertex.getId() == id && vertex.isSettlement()) {
                    settlements.add(vertex);
                }
            }
        }
        return settlements;
    }

    private ArrayList<Vertex> getCities(int id) {
        ArrayList<Vertex> cities = new ArrayList<>();
        for (Vertex[] vertexList : board.getVertices()) {
            for (Vertex vertex : vertexList) {
                if (vertex != null && vertex.getId() == id && vertex.isCity()) {
                    cities.add(vertex);
                }
            }
        }
        return cities;
    }

    private int getLongestRoad(int id, Edge edge) {
        //todo
        return 0;
    }

    private List<Object[]> getPlayers() {
        try {
            return gameSpace.queryAll(Templates.player());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }


    private ArrayList<Resource> harborTrades(PlayerState player) {
        ArrayList<Resource> res = new ArrayList<>();
        for (Vertex settlement : getSettlements(player.getId())) {
            if (settlement.getHarbor() != null) {
                res.add(settlement.getHarbor().getResource());
            }
        }
        for (Vertex city : getCities(player.getId())) {
            if (city.getHarbor() != null) {
                res.add(city.getHarbor().getResource());
            }
        }
        return res;
    }

    private boolean hasGenericHarbor(PlayerState player) {
        for (Vertex settlement : getSettlements(player.getId())) {
            if (settlement.getHarbor() == Harbor.Generic) {
                return true;
            }
        }
        for (Vertex city : getCities(player.getId())) {
            if (city.getHarbor() == Harbor.Generic) {
                return true;
            }
        }
        return false;
    }

    private Resource stringToResource(String resource) {
        switch (resource) {
            case "brick":
                return Resource.Brick;
            case "lumber":
                return Resource.Lumber;
            case "ore":
                return Resource.Ore;
            case "grain":
                return Resource.Grain;
            case "wool":
                return Resource.Wool;
            default:
                return null;
        }
    }

    public int getStartingSettlementsBuiltThisTurn() {
        return startingSettlementsBuiltThisTurn;
    }

    public int getStartingRoadsBuiltThisTurn() {
        return startingRoadsBuiltThisTurn;
    }

    public void setPlayer(int id) {
        try {
            player = (PlayerState) gameSpace.get(Templates.player(id))[2];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer() {
        try {
            gameSpace.put(player.getName(), player.getId(), player);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateBoard() {
        try {
            Board tempBoard = (Board) gameSpace.get(Templates.board())[1];
            gameSpace.put("board", board);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTurnId() {
        int turn = getTurn();
        return turn % getPlayerCount();
    }

    private int getTurn() {
        int turn = -1;
        try {
            turn = (int) gameSpace.query(Templates.turn())[1];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return turn;
    }

    public PlayerState getPlayer() {
        return player;
    }

    public PlayerState getPlayer(int playerId) {
        try {
            Object[] t = gameSpace.queryp(Templates.player(playerId));
            if (t != null) {
                return (PlayerState) t[2];
            } else
                return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<String> getChat() {
        return chat.getNewChat();
    }

    public void sendChat(String chatMessage) throws InterruptedException {
        if (!chatMessage.equals("")) {
            chatSpace.put("chat", player.getName(), "says " + chatMessage);
        }
    }
}
