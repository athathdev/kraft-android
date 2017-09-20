package com.spaceuptech.kraft.data;

import java.io.Serializable;


public class Post implements Serializable {
    public String id, content, authorId, authorName, authorPic;
    public int likes;

    public Post(String id, String authorId, String authorName, String authorPic, String content, int likes) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorPic = authorPic;
        this.content = content;
        this.likes = likes;
    }
}
