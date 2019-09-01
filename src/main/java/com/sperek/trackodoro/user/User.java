package com.sperek.trackodoro.user;

import java.util.UUID;

public class User {

    private String userMail;
    private String password;
    private UUID userId;

    public User(String userMail, String password, UUID userId) {
        this.userId = userId;
        this.password = password;
        this.userMail = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getPassword() {
        return password;
    }

    public UUID getUserId() {
        return userId;
    }
}
