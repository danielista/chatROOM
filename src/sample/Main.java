package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sk.kosickaakademia.martinek.chat.database.Database;
import sk.kosickaakademia.martinek.chat.database.tajnosti;
import sk.kosickaakademia.martinek.chat.out.Output;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);  // spúšťanie formulára :D

        tajnosti tj = new tajnosti();
       // new Database().insertNewUser(tj.getLoginChat(),tj.getPasswordChat());

        // posielam spravu :D
        // new Database().sendMessage(10,"Brano","Sewas Braňo, konečne mi to ide :D");

        // prezeram všetky spravy premňa ;)
        new Output().printMyMessages(new Database().getMyMessages("DANKO"));

    }
}
