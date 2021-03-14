package com.application.sampleretrofit;

import com.google.gson.annotations.SerializedName;

public class Comment {
    int id;
    int postId;
    String name;
    String email;
    @SerializedName("body")
    String text;
}
