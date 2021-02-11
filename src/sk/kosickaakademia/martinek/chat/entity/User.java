package sk.kosickaakademia.martinek.chat.entity;

public class User {
    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    private int id;
    private String login;
    private  String password;


}
