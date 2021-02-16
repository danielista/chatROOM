package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sk.kosickaakademia.martinek.chat.database.Database;
import sk.kosickaakademia.martinek.chat.entity.Message;
import sk.kosickaakademia.martinek.chat.entity.User;
import sk.kosickaakademia.martinek.chat.out.Output;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;



public class Main extends Application {
    Stage window;
    Scene chatRoomWindow, loginWindow, newMessageScene;
    Database db = new Database();
    Output out = new Output();
    TableView<Message> tableOfMessages;

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




        // 2. scéna:
        //elementy na 2. scénu:
        TextArea messagesArea = new TextArea();
        messagesArea.setMaxWidth(400);
        messagesArea.setMaxHeight(300);
        messagesArea.setStyle("-fx-margin: 20");
        messagesArea.setEditable(false);

        Label loginUser = new Label("logged as: ");
        Label loginUserName = new Label("DANKO");  //dorob prepojenie
        Label dateTimeActual = new Label(out.actualDateTime());
        Button refreshTime = new Button("refresh me");

        Button newMessageButton = new Button("NEW message");
        newMessageButton.setOnAction(event -> {
                window.setTitle("NEW MESSAGE");
                window.setScene(newMessageScene);
            });
        Button refreshButton = new Button("REFRESH");
        refreshButton.setOnAction(event ->  tableOfMessages.setItems(getMes()));
        Button logoutButton = new Button("LOGOUT");

    // TABLUKA SPRAV
        //stlpec primatela
    TableColumn<Message, String> nameColumn = new TableColumn<>("FROM");
    nameColumn.setMinWidth(70);
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

        // stlpec textovych sprav
        TableColumn<Message, String> nameColumn2 = new TableColumn<>("text správy");
        nameColumn2.setMinWidth(400);
        nameColumn2.setCellValueFactory(new PropertyValueFactory<>("text"));

        // stlpec časov
        TableColumn<Message, String> nameColumn3 = new TableColumn<>("DATE");
        nameColumn3.setMinWidth(180);
        nameColumn3.setCellValueFactory(new PropertyValueFactory<>("dt"));

        tableOfMessages = new TableView<>();
        tableOfMessages.getColumns().addAll(nameColumn,nameColumn2,nameColumn3);
        tableOfMessages.setItems(getMes());


        // hore  meno prihlaseneho
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(loginUser,loginUserName,dateTimeActual);
        //Setting the margin to the nodes
        HBox.setMargin(loginUser, new Insets(10, 0, 20, 20));
        HBox.setMargin(loginUserName, new Insets(10, 10, 20, 2));
        HBox.setMargin(dateTimeActual, new Insets(10, 0, 20, 2));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(newMessageButton,refreshButton,logoutButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        BorderPane chatroomPane = new BorderPane();
        chatroomPane.setMinSize(500,500);

        chatroomPane.setTop(hbox);
       // chatroomPane.setCenter(messagesArea); // tu dam tabulku
        chatroomPane.setCenter(tableOfMessages);
        chatroomPane.setRight(vbox);
        chatroomPane.getRight().setRotate(20);
        chatroomPane.getRight().setTranslateX(-30);



        //NEW MESSAGE WINDOW ELEMENTS
        Label komu = new Label("Komu: ");
        Label textSpravy = new Label("Text správy: ");
            ChoiceBox<String> friends = new ChoiceBox<>();
            friends.setValue("Choose from the list of friends");
            friends.getItems().addAll("Brano","kristianS","Simon","DANKO");

        //TextField komuTextField = new TextField();
        TextArea teloSpravy = new TextArea();
            teloSpravy.setPrefHeight(60);
            teloSpravy.setPrefWidth(240);
        Button sendIt = new Button("SEND");
            sendIt.setPrefWidth(60);
            sendIt.setOnAction(event -> {
                int from = 10;
               // String toUser = komu.getText().trim();
                String toUser = friends.getValue();
                String text = teloSpravy.getText().trim();
                db.sendMessage(from,toUser,text);
                teloSpravy.setText("");
                window.setTitle("CHATROOM 2021");
                window.setScene(chatRoomWindow);
            });


        //NEW MESSAGE GRID PANE
        GridPane newMessagePane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        newMessagePane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        newMessagePane.add(komu, 0, 0);
        newMessagePane.add(textSpravy, 0, 1);
      //  newMessagePane.add(komuTextField, 1, 0);
        newMessagePane.add(friends,1,0);
        newMessagePane.add(teloSpravy, 1, 1);
        newMessagePane.add(sendIt,1,3);
        GridPane.setHalignment(sendIt,HPos.RIGHT);


        //Creating a scene object
        loginWindow = new Scene(gridPane);
        chatRoomWindow = new Scene(chatroomPane,700,380);
        newMessageScene = new Scene(newMessagePane,400,200);



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

    public ObservableList<Message> getMes(){
        ObservableList<Message> mess = FXCollections.observableArrayList();
        mess.addAll(db.getMyMessages("DANKO"));
        return mess;
    }


    public static void main(String[] args) {
        launch(args);  // spúšťanie formulára :D

        // tajnosti tj = new tajnosti();
        // new Database().insertNewUser(tj.getLoginChat(),tj.getPasswordChat());

        // posielam spravu :D  // Brano      kristianS
        // new Database().sendMessage(10," ..komu.. "," ..text.. ");

        // prezeram všetky spravy premňa ;)
       // new Output().printMyMessages(new Database().getMyMessages("DANKO"));
         // new Database().deleteAllMyMessages("DANKO");
        //  new Database().changePassword("DANKO","406068e1638b16699da096f61f331111", tj.getPasswordChat());

        System.out.println(new Output().actualDateTime());;
    }
}
