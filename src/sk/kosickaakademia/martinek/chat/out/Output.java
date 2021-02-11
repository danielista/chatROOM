package sk.kosickaakademia.martinek.chat.out;

import sk.kosickaakademia.martinek.chat.entity.Message;

import java.util.List;

public class Output {
    public void printMyMessages(List<Message> list){
        System.out.println("Moje nové správy: ");
        for(Message ms : list){
            System.out.println(" - OD: " + ms.getFrom() +": \""+ms.getText()+"\" "+ ms.getDt() );
        }
    }
}
