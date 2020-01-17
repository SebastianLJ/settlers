package model;

import controller.NewController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jspace.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class Lobby {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose 'HOST' or 'JOIN': ");
        String input = scanner.next();
        if (input.equals("HOST")) {
            SequentialSpace game = new SequentialSpace();
            SequentialSpace chat = new SequentialSpace();
            SpaceRepository repository = new SpaceRepository();

            // Setting up URI
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ip = inetAddress.getHostAddress();
            int port = 9001;

            String URI = "tcp://" + ip + ":" + port + "?keep";
            System.out.println("A game is hosted on IP:Port: " + ip + ":" + port);

            // Opening gate at given URI
            repository.addGate(URI);
            repository.add("game", game);
            repository.add("chat", chat);

            playGame(game, true);

        } else if (input.equals("JOIN")) {
            System.out.println("Enter game ip and port... Format: ip:port");
            input = scanner.next();

            String hostUri = "tcp://" + input;

            RemoteSpace game = new RemoteSpace(hostUri + "/game?keep");
            RemoteSpace chat = new RemoteSpace(hostUri + "/chat?keep");

            playGame(game, false);

        }
    }

    private static void playGame(Space game, boolean sender) throws InterruptedException {
        if (sender) {

        } else {

        }
    }
}
