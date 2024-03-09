package flashcard;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/flashcard/view/flascard.fxml"));
         //Parent root = FXMLLoader.load(getClass().getResource("/sample/view/list.fxml"));
        primaryStage.setTitle("Flashcards");
        primaryStage.setScene(new Scene(root, 1055, 574));

        primaryStage.setResizable(false);
        primaryStage.show();

    }


}
