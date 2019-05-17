package com.dimitriongoua.smsrouter.model;

public class Mess {

    public String number;
    public String body;
    public long timestamp;
    public String device;

    public Mess(String number, String body) {
        this.number = number;
        this.body = body;
    }

    public Mess() {
    }


}
