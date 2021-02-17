package sk.kosickaakademia.martinek.chat.database;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sk.kosickaakademia.martinek.chat.entity.Message;
import sk.kosickaakademia.martinek.chat.entity.User;
import sk.kosickaakademia.martinek.chat.util.Util;

import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Database {

    tajnosti tj = new tajnosti();

    // QUERIES VŠETKY POTREBNE NAJDEME TUTO
    private final String insertNewUser = "INSERT INTO user (login, password) VALUES (?,?)";
    private final String loginUser = "SELECT * FROM user WHERE login LIKE ? AND password LIKE ?";
    private final String newMessage = "INSERT INTO message(fromUser, toUser, text) VALUES (?,?,?)";
    private final String getLoginID = "SELECT id FROM chat2021.user WHERE login LIKE (?) ";
    private final String getMyMESSAGES = "SELECT user.login AS fromWHO, text AS what,dt AS timeWHEN FROM message INNER JOIN chat2021.user ON user.id = message.fromUser WHERE toUser = (?)";
    private final String deleteMyMessages = "DELETE FROM message WHERE toUser = (?)";
    private final String updateUserPassword = "UPDATE user SET password = (?) WHERE login LIKE (?) ";


    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn = DriverManager.getConnection(tj.getUrl(), tj.getUsername(), tj.getPassword());
        Class.forName("com.mysql.cj.jdbc.Driver");
        return conn;
    }
    private Connection getConnectionToChat1n() throws SQLException, ClassNotFoundException {
        Connection conn = DriverManager.getConnection(tj.getUrlChat1n(), tj.getUsername(), tj.getPassword());
        Class.forName("com.mysql.cj.jdbc.Driver");
        return conn;
    }

    // test metoda na testovanie connections :D
    public void test() {
        try{
            Connection con = getConnection();
            if (con == null) System.out.println("PROBLEMOS");
            else System.out.println("SAXESSS");
            con.close();
        }catch (Exception e) {
            System.out.println("dajaky problem s otestovanim pripojenia");
            e.printStackTrace();
        }
    }

    public boolean insertNewUser(String login, String password){
            if(login==null  ||  login.equals("") || password==null || password.length()<6 ) return false;

            String hashPassword = new Util().getMd5(password);
            try{
                Connection con = getConnection();
                if(con == null){
                    System.out.println("con = error!!!");
                    return false;
                }

                PreparedStatement ps = con.prepareStatement(insertNewUser);
                ps.setString(1, login);
                ps.setString(2, hashPassword);
                int result = ps.executeUpdate();
                con.close();
                if (result == 0)
                    return false;
                else {
                    System.out.println("User " + login + " has been added to the database !");
                    return true;
                }

            }catch (Exception e){
                System.out.println("už existuje taký user");
                e.printStackTrace();
            }
            return true;
    }

    public User loginUser(String login, String password){
            //overenie či user je validný
        if (login==null || login.equals("") || password==null || password.length()<6) return null;

            String hashPassword = new Util().getMd5(password);
            try{
                    Connection con = getConnection();
                    if(con == null){
                        System.out.println("con = error!!!");
                        return null;
                    }

                    PreparedStatement ps = con.prepareStatement(loginUser);
                    ps.setString(1, login);
                    ps.setString(2,hashPassword);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next())
                    {
                        System.out.println("SAXESS");
                        int id =  rs.getInt("id");
                        User user = new User(id,login,hashPassword);
                        con.close();
                        return user;
                    }else {
                        con.close();
                        System.out.println("incorect credintials!!!!!");
                        return null;
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
    }

    public boolean sendMessage(int from, String toUser, String text){
            if(text==null || text.equals("")) return false;

            int to=getUserId(toUser);
            if(to==-1) return false;


            try {
                    Connection con = getConnection();
                    if(con==null){
                        System.out.println("ajajaj");
                        return false;
                    }
                    PreparedStatement ps=con.prepareStatement(newMessage);
                    ps.setInt(1,from);
                    ps.setInt(2,to);
                    ps.setString(3,text);
                    int result = ps.executeUpdate();
                    con.close();

                    if(result<1){
                        System.out.println("SPRAVA NEBOLA ODOSLANA");
                        return false;
                    }
                    else{
                        System.out.println("SPRAVA ODOSLANA");
                        return true;
                    }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return false;
    }

    public int getUserId(String login){
            //validation ;)

            //connection
        try{
            Connection con = getConnection();
            if(con == null){
                System.out.println("con = error!!!");
                return -1;
            }

            PreparedStatement ps = con.prepareStatement(getLoginID);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                int id = rs.getInt("id");
                con.close();
                System.out.println("SAXESS you reached the ID");
                return (int) id;

            }else {
                con.close();
                System.out.println("The user with this LOGIN doesnt EXIST");
                return -1;
            }

        }catch (Exception e){
            System.out.println("dajaký problem S GETuserID metodou");
            e.printStackTrace();
        }

            return -1;
    }

    public ObservableList<Message> getMyMessages(String login){

        int Idecko = getUserId(login);
        if(Idecko==-1){
            System.out.println("login nejestvuje problem je na zaciatku metody getmymessages :D");
            return null;
        }

        //ArrayList<Message> messeges = new ArrayList<>();
        ObservableList<Message> messeges2 = FXCollections.observableArrayList();

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement( getMyMESSAGES );
            ps.setInt(1,Idecko);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String odKOHO = rs.getString("fromWHO");
                String textSPRAVY = rs.getString("what");

                  //  String pattern = "EEEEE dd MMMMM yyyy HH:mm:ss";
         /*           String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));

                //DateTimeFormatter = rs.getDate("timeWHEN");
                //Time casOdoslania  = rs.getDate("timeWHEN");
                Date casOdoslania = rs.getDate("timeWHEN");

          */
             //   String date = simpleDateFormat.format(casOdoslania);
                String date = rs.getString("timeWhen");



                // System.out.println(odKOHO +  " \"" + textSPRAVY + "\" " + casOdoslania);
                Message sprava = new Message(odKOHO,textSPRAVY,date);
              //  messeges.add(sprava);
                messeges2.add(sprava);

              //  deleteAllMyMessages("DANKO");
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //return messeges;
        return messeges2;
    }

    public void deleteAllMyMessages(String login){

        int Idecko = getUserId(login);

        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement( deleteMyMessages );
            ps.setInt(1,Idecko);
            // ResultSet rs = ps.executeQuery();
            ps.executeUpdate();  // vyhodí číslo kolko riadkov zmazalo ;)

            System.out.println("schránka je prázdna");
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean changePassword(String login , String oldPassword, String newPassword){

        User onlineUser = loginUser(login,oldPassword);
        if (onlineUser==null){
            System.out.println("nesprávne staré_heslo alebo meno");
            return false;
        }else{
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter new password, one more time, please:");
            String reallyNewPassword = sc.nextLine();
            if (reallyNewPassword.equals(newPassword)){
               // System.out.println("Vaše nové heslo je uložené.");


                if(login==null  ||  login.equals("") || oldPassword==null || oldPassword.length()<6 ) return false;

                String hashPassword = new Util().getMd5(newPassword);
                try{
                    Connection con = getConnection();
                    if(con == null){
                        System.out.println("con = error!!!");
                        return false;
                    }


                    PreparedStatement ps = con.prepareStatement(updateUserPassword);
                    ps.setString(1, hashPassword);
                    ps.setString(2, login);
                    int result = ps.executeUpdate();
                    con.close();
                    if (result == 0)
                        return false;
                    else {
                        System.out.println("YOu changed your password SAXESFULLY!! ");
                        return true;
                    }

                }catch (Exception e){
                    System.out.println("heslo sa nepodarilo zmeniť");
                    e.printStackTrace();
                }

            }else {
                System.out.println("Vaše nové heslo sa nezhoduje so zadaným...");
                //while //3 pokusy a potom neúspešná zmena hesla
                return false;
            }
        }





            // už už pracujem na tom :D
            return false;
    }





}
