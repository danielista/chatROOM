package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import static javafx.scene.layout.GridPane.*;


public class Main extends Application {
    Stage window;
    Scene chatRoomWindow, loginWindow, newMessageScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;


        Text login = new Text("Login");
        Text password = new Text("Password");

        TextField loginInputText = new TextField();
        loginInputText.setPromptText("Type your login name here");
        loginInputText.setPrefWidth(200);
        PasswordField passwordInputText = new PasswordField();
        passwordInputText.setPromptText("Type your password here");

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
                    window.setTitle("CHATROOM 2021");
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
        gridPane.add(button1, 1, 3);
        gridPane.add(button2, 1, 3);
        GridPane.setHalignment(button2,HPos.RIGHT);

        gridPane.add(errorLabel,0,2);




        //elementy na 2. scénu:
        Label loginUser = new Label("logged as: ");
        Label loginUserName = new Label("DANKO");  //dorob prepojenie

        Button newMessageButton = new Button("NEW message");
        newMessageButton.setOnAction(event -> {
            window.setTitle("NEW MESSAGE");
            window.setScene(newMessageScene);
        });
        Button refreshButton = new Button("REFRESH");
        Button logoutButton = new Button("LOGOUT");

        TextArea messagesArea = new TextArea();
        messagesArea.setMaxWidth(400);
        messagesArea.setMaxHeight(300);
        messagesArea.setStyle("-fx-margin: 20");
        messagesArea.setEditable(false);

        // 2. scéna:

        //Instantiating the HBox class
        HBox hbox = new HBox();

        //Setting the space between the nodes of a HBox pane
        hbox.setSpacing(10);
        hbox.getChildren().addAll(loginUser,loginUserName);
        //Setting the margin to the nodes
        HBox.setMargin(loginUser, new Insets(10, 0, 0, 20));
        HBox.setMargin(loginUserName, new Insets(10, 0, 0, 2));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(newMessageButton,refreshButton,logoutButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        BorderPane chatroomPane = new BorderPane();
        chatroomPane.setMinSize(500,500);

        chatroomPane.setTop(hbox);
        chatroomPane.setCenter(messagesArea);
        chatroomPane.setRight(vbox);
        chatroomPane.getRight().setRotate(20);
        chatroomPane.getRight().setTranslateX(-30);

        //NEW MESSAGE WINDOW ELEMENTS
        Label komu = new Label("TO: ");
        Label textSpravy = new Label("Text správy: ");
        TextField komuTextField = new TextField();
        TextField teloSpravy = new TextField();

        //NEW MESSAGE GRID PANE
        GridPane newMessagePane = new GridPane();
        newMessagePane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        newMessagePane.add(komu, 0, 0);
        newMessagePane.add(textSpravy, 0, 1);
        newMessagePane.add(komuTextField, 1, 0);
        newMessagePane.add(teloSpravy, 1, 1);


        //Creating a scene object
        loginWindow = new Scene(gridPane);
        chatRoomWindow = new Scene(chatroomPane,600,500);

        newMessageScene = new Scene(newMessagePane);



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
