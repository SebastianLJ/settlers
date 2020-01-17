import controller.NewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LobbyController extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Settlers of Catan - Lobby");

        FXMLLoader loader = new FXMLLoader(NewController.class.getClassLoader().getResource("LobbyController.fxml"));
        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void createGame() {
        // TODO
    }

    public void joinGame() {
        // TODO

    }

    public void onMouseEntered(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setEffect(new DropShadow());
    }

    public void onMouseExited(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setEffect(null);
    }

}
