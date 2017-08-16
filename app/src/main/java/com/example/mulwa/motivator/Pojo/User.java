package com.example.mulwa.motivator.Pojo;

import android.net.Uri;

/**
 * Created by mulwa on 8/13/17.
 */

public class User {
    private String name;
    private String email;
    private String mobile;


    public User(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;


    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }


}
