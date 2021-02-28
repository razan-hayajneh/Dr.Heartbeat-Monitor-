package com.example.drheartbeatmonitor;

import java.util.ArrayList;

public class messages {
    private String message;
    private String s_r;

    public messages(String message, String s_r) {
        this.message = message;
        this.s_r = s_r;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getS_r() {
        return s_r;
    }

    public void setS_r(String s_r) {
        this.s_r = s_r;
    }

    public static int getLastContactId() {
        return lastContactId;
    }

    public static void setLastContactId(int lastContactId) {
        messages.lastContactId = lastContactId;
    }

    private static int lastContactId = 0;

    public static ArrayList<messages> create_message_list(String[] message, String[] s_r) {
        ArrayList<messages> new_messages = new ArrayList<>();
        for (int i=0;i<s_r.length;i++) {
            new_messages.add(new messages( message[i], s_r[i]));
            lastContactId++;
        }
        return new_messages;
    }
}
