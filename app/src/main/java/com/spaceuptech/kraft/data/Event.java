package com.spaceuptech.kraft.data;

import java.util.Date;

/**
 * Created by ubuntu on 19/9/17.
 */

public class Event {
    public String id, name, content, organizationName;
    public long date;

    public Event(String id, String name, String organizationName, String content, long date){
        this.id = id;
        this.name = name;
        this.organizationName = organizationName;
        this.content = content;
        this.date = date;
    }
}
