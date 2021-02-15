package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sk.kosickaakademia.martinek.chat.database.Database;
import sk.kosickaakademia.martinek.chat.database.tajnosti;
import sk.kosickaakademia.martinek.chat.entity.User;
import sk.kosickaakademia.martinek.chat.out.Output;


public class Main extends Application {
    Stage window;
    Scene chatRoomWindow;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;


        Text login = new Text("Login");
        Text password = new Text("Password");

        TextField loginInputText = new TextField();
        TextField passwordInputText = new TextField();

        Label errorLabel = new Label();
        errorLabel.setText("Nič tu nefunguje!!!");
        errorLabel.setTextFill(Color.web("#FF0000"));
        errorLabel.setStyle("-fx-font: normal italic 12px 'calibri' ");
        errorLabel.setVisible(false);

        //Creating Buttons
        Button button1 = new Button();
        button1.setText("Login me");
        button1.setPrefWidth(70);
        button1.setStyle("-fx-background-color: green; \n" +
                         "-fx-text-fill: white; ");
        button1.setOnAction(event -> {
            System.out.println("OOOOOOOOO yeah");
            String login1 = loginInputText.getText().trim();
            String password1 = passwordInputText.getText().trim();
            if(login1.length()>0 && password1.length()>0){
                Database db = new Database();
                User user = db.loginUser(login1,password1);
                if(user==null){
                    errorLabel.setVisible(true);
                }else{
                    System.out.println("Success! You are logged!");
                    window.setScene(chatRoomWindow);
                }
            }
        });

        Button button2 = new Button("Clear");
        button2.setOnAction(event -> {
            loginInputText.setText("");
            passwordInputText.setText("");
        });

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(10);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(login, 0, 0);
        gridPane.add(loginInputText, 1, 0);
        gridPane.add(password, 0, 1);
        gridPane.add(passwordInputText, 1, 1);
        gridPane.add(button1, 0, 3);
        gridPane.add(button2, 1, 3);
        gridPane.add(errorLabel,0,2);

        //Creating a scene object
        Scene loginWindow = new Scene(gridPane);

        Pane chatroomPane = new Pane();
        chatroomPane.setMinSize(500,500);


        chatRoomWindow = new Scene(chatroomPane,600,600);


        //Setting title to the Stage
        window.setTitle("Chat ROOM 2021 1N/ LOGIN");

        //Adding scene to the stage
        window.setScene(loginWindow);

        //Displaying the contents of the stage
        window.show();



/*
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Chat room 1N 2021");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
        */

    }


    public static void main(String[] args) {
        launch(args);  // spúšťanie formulára :D

        // tajnosti tj = new tajnosti();
        // new Database().insertNewUser(tj.getLoginChat(),tj.getPasswordChat());

        // posielam spravu :D  // Brano      kristianS
        // new Database().sendMessage(10," ..komu.. "," ..text.. ");

        // prezeram všetky spravy premňa ;)
        new Output().printMyMessages(new Database().getMyMessages("DANKO"));
         // new Database().deleteAllMyMessages("DANKO");
        //  new Database().changePassword("DANKO","406068e1638b16699da096f61f331111", tj.getPasswordChat());

    }
}
