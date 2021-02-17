package sk.kosickaakademia.martinek.chat.graphicuserinterface;

import javafx.scene.control.MenuItem;

public class ChatMenu {


public javafx.scene.control.Menu chatMenu(String card){
    javafx.scene.control.Menu firstOptionMenu = new javafx.scene.control.Menu();
    firstOptionMenu.setText(card);


    firstOptionMenu.getItems().add(new MenuItem("New message"));





    return firstOptionMenu;
}



}
