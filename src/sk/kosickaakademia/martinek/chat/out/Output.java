package sk.kosickaakademia.martinek.chat.out;

import sk.kosickaakademia.martinek.chat.entity.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Output {
    public String printMyMessages(List<Message> list){
        System.out.println("Moje nové správy: ");
        String spravy = "";
        for(Message ms : list){
            System.out.println(" - OD: " + ms.getFrom() +": \""+ms.getText()+"\" "+ ms.getDt() );
            spravy = spravy + " - OD: " + ms.getFrom() +": \""+ms.getText()+"\" "+ ms.getDt() + "\n";
        }
        return spravy;
    }


    public String actualDateTime(){
        String pattern = "EEEEE dd MMMMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, new Locale("en", "US"));

        String date = simpleDateFormat.format(new Date());
        return "It is now: "+ date;
    }

}
