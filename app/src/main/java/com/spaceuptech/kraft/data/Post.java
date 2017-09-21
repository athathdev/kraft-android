package com.spaceuptech.kraft.data;

import java.io.Serializable;
import java.util.Comparator;


public class Post implements Serializable {
    public String postId, content, userId, userName, userImg;
    public int likes;

    public Post(String postId, String userId, String userName, String userImg, String content, int likes) {
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.userImg = userImg;
        this.content = content;
        this.likes = likes;
    }


}
