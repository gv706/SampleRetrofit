package com.application.sampleretrofit;

import com.google.gson.annotations.SerializedName;

public class Post {
    Integer id;
    int userId;
    String title;

    @SerializedName("body")
    String text;

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
