package com.spaceuptech.kraft.data;

import java.io.Serializable;

/**
 * Created by ubuntu on 19/9/17.
 */

public class Post implements Serializable {
    public String postId, content;
    public User author;
    public int likes;
    public long time;
    public boolean liked;
    public Post(String postId, User author, String content, int likes, long time) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.time = time;
    }
    public Post(String postId, User author, String content, int likes, long time, boolean liked) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.liked = liked;
    }
}
