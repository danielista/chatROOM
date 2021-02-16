package sk.kosickaakademia.martinek.chat.entity;

import java.util.Date;

public class Message {
    private int id;

    // na výpis mojich nových prijatých správ
    public Message(String from, String text, String dt) {
        this.from = from;
        this.dt = dt;
        this.text = text;
    }

    private String from;
    private String to;
    //private Date dt;
    private String dt;
    private String text;

    public Message(int id, String from, String to, String dt, String text) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.dt = dt;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDt() {
        return dt;
    }

    public String getText() {
        return text;
    }
}