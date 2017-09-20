package com.spaceuptech.kraft.data;

public class LoginResponse {
    public boolean ack, exists;
    public String sessionId, err;
    public User user;

    public LoginResponse(boolean ack, boolean exists, String sessionId, User user) {
        this.ack = ack;
        this.exists = exists;
        this.user = user;
        this.sessionId = sessionId;
    }
}
