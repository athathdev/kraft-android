package com.spaceuptech.kraft.data;


public class Event {
    public String eventId, eventName, content, userId, userName, eventImg;
    public long date;
    public int interests;

    public Event(String eventId, String eventName, String eventImg, String userId, String userName,  String content, long date, int interests){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventImg = eventImg;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.interests = interests;
    }
}
