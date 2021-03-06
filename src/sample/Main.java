package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sk.kosickaakademia.martinek.chat.database.Database;
import sk.kosickaakademia.martinek.chat.entity.Message;
import sk.kosickaakademia.martinek.chat.entity.User;
import sk.kosickaakademia.martinek.chat.out.Output;



public class Main extends Application {
    Stage window;
    Scene chatRoomWindow, loginWindow, newMessageScene;
    Database db = new Database();
    Output out = new Output();
    TableView<Message> tableOfMessages;

    @Override
    public void start(Stage window) throws Exception {



        Text login = new Text("Login");
        Text password = new Text("Password");

        TextField loginInputText = new TextField();
        loginInputText.setPromptText("Type your login name here");
        loginInputText.setPrefWidth(200);
        PasswordField passwordInputText = new PasswordField();
        passwordInputText.setPromptText("Type your password here");

        Label errorLabel = new Label();
        errorLabel.setText("Incorrect login or password.");
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
                        tableOfMessages.setItems(db.getMyMessages(loginInputText.getText().trim()));
                        window.setScene(chatRoomWindow);
                    }
                }
            });

        Button button2 = new Button("Clear");
        button2.setOnAction(event -> {
            loginInputText.setText("");
            passwordInputText.setText("");
            errorLabel.setVisible(false);
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

        gridPane.add(errorLabel,1,2);







        // 2. scéna:
        //elementy na 2. scénu:
        Label nazovAplikacie = new Label("DATABASE MESSENGER 1N 2021");
        nazovAplikacie.setStyle("-fx-font: 20px helvetica;");
        nazovAplikacie.setStyle(" -fx-font-size: 30;" +
                "-fx-font-style: italic;");

        Label loginUserName = new Label("DANKO");
        loginUserName.textProperty().bind(loginInputText.textProperty());
        Label dateTimeActual = new Label(out.actualDateTime());


        Button newMessageButton = new Button("NEW message");
            newMessageButton.setStyle("");
            newMessageButton.setOnAction(event -> {
                    window.setTitle("NEW MESSAGE");
                    window.setScene(newMessageScene);
                });
        Button refreshButton = new Button("Refresh");
        //  refreshButton.setOnAction(event ->  tableOfMessages.setItems(getMes()));
            refreshButton.setOnAction(event ->  tableOfMessages.setItems(db.getMyMessages(loginInputText.getText().trim())));
        Button logoutButton = new Button("Log-out");
            logoutButton.setOnAction(event -> {
                window.setTitle("Chat ROOM 2021 1N/ LOGIN");
                window.setScene(loginWindow);
                loginInputText.setText("");
                passwordInputText.setText("");
                errorLabel.setVisible(false);
            });

            // TABLUKA SPRAV
            // stlpec primatela
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
          //  tableOfMessages.setItems(getMes());

        tableOfMessages.setItems(db.getMyMessages(loginInputText.getText().trim()));


        // BOTTOM BAR ;)
        Button deleteButton = new Button("Delete all my messages");
        deleteButton.setOnAction(event -> {
            ObservableList<Message> messageSelected, allMessages;
            allMessages = tableOfMessages.getItems();
            messageSelected = tableOfMessages.getSelectionModel().getSelectedItems();

            messageSelected.forEach(allMessages::remove);
            db.deleteAllMyMessages(loginInputText.getText());
            tableOfMessages.setItems(db.getMyMessages(loginInputText.getText().trim()));
        });

        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10,10,10,10));
        bottomBar.setSpacing(10);
        bottomBar.getChildren().addAll(dateTimeActual,deleteButton,newMessageButton,refreshButton,logoutButton);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

//stále 2. scéna :D
//Menu menu = new ChatMenu().chatMenu(loginUserName.getText());
Menu firstOptionMenu = new Menu("_",loginUserName);
firstOptionMenu.setText(loginInputText.getText().trim());

MenuItem newMessItem = new MenuItem("New message to...");
    newMessItem.setOnAction(event -> {
        window.setTitle("NEW MESSAGE");
        window.setScene(newMessageScene);
    });
firstOptionMenu.getItems().add(newMessItem);

MenuItem changePassOption = new MenuItem("Change my password...");
    changePassOption.setOnAction(event -> {
        window.setTitle("CHANGING THE PASSWORD");
        GridPane zmenaHeslaGridPane = new GridPane();
        zmenaHeslaGridPane.setStyle("-fx-background-color: BEIGE;");

        zmenaHeslaGridPane.setAlignment(Pos.CENTER);
        Label oldPass = new Label("Old password");
        Label newPass = new Label("New password");
        Label againNewPass = new Label("Repeat new password");
        TextField oldF = new TextField();
        oldF.setPromptText("old password");
        TextField newF =  new TextField();
        newF.setPromptText("new password");
        TextField newAgain = new TextField();
        newAgain.setPromptText("new password");
        Label errorChange = new Label("Incorret DATA");
        errorChange.setVisible(false);
        Button confirmation = new Button("Confirm changing the password");

        zmenaHeslaGridPane.setPadding(new Insets(10, 10, 10, 10));
        zmenaHeslaGridPane.setVgap(10);
        zmenaHeslaGridPane.setHgap(5);
        zmenaHeslaGridPane.setMinSize(400, 200);

        zmenaHeslaGridPane.add(oldPass,0,0);
        zmenaHeslaGridPane.add(newPass,0,1);
        zmenaHeslaGridPane.add(againNewPass,0,2);

        zmenaHeslaGridPane.add(oldF,1,0);
        zmenaHeslaGridPane.add(newF,1,1);
        zmenaHeslaGridPane.add(newAgain,1,2);

        zmenaHeslaGridPane.add(confirmation,1,3);
        zmenaHeslaGridPane.add(errorChange,0,3);
        Scene changingPassScene = new Scene(zmenaHeslaGridPane);
        window.setScene(changingPassScene);
    });
firstOptionMenu.getItems().add(changePassOption);

firstOptionMenu.getItems().add(new SeparatorMenuItem());
firstOptionMenu.getItems().add(new MenuItem("Exit"));


        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setDisable(true);
        about.setOnAction(event -> {
            window.setTitle("ABOUT");
            // dokončiť window s info o aplikácii :D
        });
        help.getItems().add(about);

MenuBar menuBar = new MenuBar();
menuBar.getMenus().addAll(firstOptionMenu,help);



        // hore  meno prihlaseneho
        HBox hbox = new HBox();
        hbox.setSpacing(10);
       // hbox.getChildren().addAll(nazovAplikacie,loginUserName,dateTimeActual);
        hbox.getChildren().addAll(nazovAplikacie,loginUserName);
        //Setting the margin to the nodes
        HBox.setMargin(nazovAplikacie, new Insets(10, 0, 20, 20));
        HBox.setMargin(loginUserName, new Insets(10, 10, 20, 2));
      //  HBox.setMargin(dateTimeActual, new Insets(10, 0, 20, 2));

        VBox horeMenuAprvyRiadok = new VBox();
        horeMenuAprvyRiadok.getChildren().addAll(menuBar,hbox);
        hbox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();

        //vbox.getChildren().addAll(newMessageButton,refreshButton,logoutButton);
        vbox.setAlignment(Pos.TOP_CENTER);

        vbox.setSpacing(10);

        BorderPane chatroomPane = new BorderPane();
        chatroomPane.setMinSize(400,400);

        chatroomPane.setTop(horeMenuAprvyRiadok);

        chatroomPane.setCenter(tableOfMessages);
        chatroomPane.getCenter().cursorProperty().set(Cursor.CLOSED_HAND);

        chatroomPane.setRight(vbox);

        chatroomPane.setBottom(bottomBar);

        chatroomPane.getBoundsInParent();



        //NEW MESSAGE WINDOW ELEMENTS
        Label komu = new Label("Komu: ");
        Label textSpravy = new Label("Text správy: ");
        ComboBox<String> listOfFriends = new ComboBox<>();
        listOfFriends.setEditable(true);
        listOfFriends.getItems().addAll("Brano","kristianS","Simon","DANKO","CaptainUkraine","Roman","Kubo");
        listOfFriends.setPromptText("Choose or type new friend");
        listOfFriends.setPrefWidth(240);

    //    ChoiceBox<String> friends = new ChoiceBox<>();
      //      friends.setValue("Choose from the list of friends");
        //    friends.getItems().addAll("Brano","kristianS","Simon","DANKO","CaptainUkraine","Roman","Kubo");

        //TextField komuTextField = new TextField();
        TextArea teloSpravy = new TextArea();
            teloSpravy.setPrefHeight(60);
            teloSpravy.setPrefWidth(240);
            teloSpravy.setPromptText("type your message here...");
        Button sendIt = new Button("SEND");
            sendIt.setPrefWidth(60);
            sendIt.setOnAction(event -> {
                int from = 10;
                from = db.getUserId(loginUserName.getText());
               // String toUser = komu.getText().trim();
                //String toUser = friends.getValue();
                String toUser = listOfFriends.getValue();
                String text = teloSpravy.getText().trim();
               if (db.sendMessage(from,toUser,text)==true){
                   listOfFriends.getItems().add(listOfFriends.getValue());
                   listOfFriends.setAccessibleText("DANKO"); //neviem načo je ten accesibletext:D
               }
                teloSpravy.setText("");
                window.setTitle("CHATROOM 2021");
                window.setScene(chatRoomWindow);
            });


        //NEW MESSAGE GRID PANE
        GridPane newMessagePane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        newMessagePane.setAlignment(Pos.CENTER);

        // Arranging all the nodes in the grid
        newMessagePane.add(komu, 0, 0);
        newMessagePane.add(textSpravy, 0, 1);
        // newMessagePane.add(komuTextField, 1, 0);
        //newMessagePane.add(friends,1,0);
        newMessagePane.add(listOfFriends,1,0);
        newMessagePane.add(teloSpravy, 1, 1);
        newMessagePane.add(sendIt,1,3);
        GridPane.setHalignment(sendIt,HPos.RIGHT);


        //Creating a scene object
        loginWindow = new Scene(gridPane);
        gridPane.setStyle("-fx-background-color: BEIGE;");
        chatRoomWindow = new Scene(chatroomPane,680,500);
        chatroomPane.setStyle("-fx-background-color: BEIGE;");
        newMessageScene = new Scene(newMessagePane,400,200);
        newMessagePane.setStyle("-fx-background-color: BEIGE;");


        //Setting title to the Stage
        window.setTitle("Chat ROOM 2021 1N/ LOGIN");
        window.setScene(loginWindow);

        window.show();

    }


    public Text nazov(){
        Text t = new Text();
        t.setX(10.0f);
        t.setY(50.0f);
        t.setCache(true);
        t.setText("Reflections on JavaFX...");
        t.setFill(Color.RED);
        t.setFont(Font.font(null, FontWeight.BOLD, 30));

        Reflection r = new Reflection();
        r.setFraction(0.7f);

        t.setEffect(r);

        t.setTranslateY(400);
        return t;
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

        System.out.println(new Output().actualDateTime() + "bye bye ;)");;
    }
}
