package com.example.mulwa.motivator.Pojo;

import android.net.Uri;

/**
 * Created by mulwa on 8/14/17.
 */

public class Message {
    private String message;
    private String ownerId;
    private String  date;
    private String userName;
    private String photoUrl;

    public Message(String message, String ownerId, String date,String userName, String photoUrl) {
        this.message = message;
        this.ownerId = ownerId;
        this.date = date;
        this.userName = userName;
        this.photoUrl = photoUrl;
    }


    public Message() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getDate() {
        return date;
    }
}
