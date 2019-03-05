package com.example.apoorva.pictonification;

public class FeedData {

    String message;
    String title;
    String name;
    String date;
    String type;
    boolean flag;
    String answer;

    public FeedData(){}

    public FeedData(String message, String title, String name, String time,String type) {
        this.message = message;
        this.title = title;
        this.name = name;
        this.date = time;
        this.type=type;
    }

    public FeedData(String message, String name, String time, Boolean flag, String answer) {
        this.message = message;
        this.date = time;
        this.name = name;
        this.flag = flag;
        this.answer = answer;
    }

    public FeedData(String answer) {
        this.message = answer;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public boolean getFlag() {
        return flag;
    }

    public String getAnswer() {
        return answer;
    }
}
