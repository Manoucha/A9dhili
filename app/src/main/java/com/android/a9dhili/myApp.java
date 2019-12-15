package com.android.a9dhili;

import android.app.Application;

import com.android.a9dhili.Models.User;

public class myApp extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
