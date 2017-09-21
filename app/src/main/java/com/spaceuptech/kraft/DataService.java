package com.spaceuptech.kraft;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.data.Event;
import com.spaceuptech.kraft.data.Post;
import com.spaceuptech.kraft.utility.Conversions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class DataService extends Service {
    public static String SESSION_ID = "";

    private ClientApi api;
    private DatabaseHelper dbHelper;

    public static final String ENGINE_LOGIN = "kraft-login";
    public static final String ENGINE_POST = "kraft-post";
    public static final String ENGINE_EVENT = "kraft-event";
    public static final String ENGINE_PROFILE = "kraft-profile";

    public static final String REQUEST_LOGIN = "login";
    public static final String REQUEST_SIGNUP = "signup";

    public static final String REQUEST_SILENT_LOGIN = "silent-login";

    public static final String REQUEST_CREATE_POST = "create";
    public static final String REQUEST_POSTS = "posts"; // To request posts from server also Acknowledgement from server on request of posts
    public static final String REQUEST_POST = "post"; // Single post emitted by server due to request posts
    public static final String REQUEST_GET_POST = "get"; // Request for a specific post
    public static final String REQUEST_LIKE_POST = "like"; // Request for a specific post
    public static final String REQUEST_IMPRESSION_POST = "impression";

    public static final String REQUEST_EVENTS = "events";
    public static final String REQUEST_EVENT = "event";
    public static final String REQUEST_INTEREST_EVENT = "interest";
    public static final String REQUEST_IMPRESSION_EVENT = "impression";

    public static final String REQUEST_GET_PROFILE = "profile";
    public static final String REQUEST_PROFILE_VIEWS = "profile-views";
    public static final String REQUEST_PROFILE_POSTS = "profile-posts";



    private Gson gson = new Gson();

    public static List<Post> posts = new ArrayList<>();
    public static List<Event> events = new ArrayList<>();

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!api.isConnected()) api.connect();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Connected", "Service started");

        dbHelper = new DatabaseHelper(getApplicationContext());

        api = new ClientApi("192.168.3.30", 11100, true, new ClientApi.OnMessageReceived() {
            @Override
            public void messageReceived(ClientApi.Request message) {
                Log.d("Received", message.engine + " : " + message.func + " : " + new String(message.args));
                switch (message.engine) {

                    case ENGINE_LOGIN:
                        switch (message.func) {
                            case REQUEST_LOGIN:
                                Intent intent = new Intent(REQUEST_LOGIN);
                                intent.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                break;
                            case REQUEST_SIGNUP:
                                Intent intent1 = new Intent(REQUEST_SIGNUP);
                                intent1.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                                break;
                        }
                        break;
                    case ENGINE_POST:
                        switch (message.func) {
                            case REQUEST_CREATE_POST:
                                Intent intent = new Intent(REQUEST_CREATE_POST);
                                intent.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                break;
                            case REQUEST_POST:
                                Post post = gson.fromJson(new String(message.args), Post.class);
                                int size = posts.size();
                                if (size >= 10) {
                                    if (Conversions.getTimeFromUUID(UUID.fromString(post.postId)) >= Conversions.getTimeFromUUID(UUID.fromString(posts.get(0).postId))) posts.clear();
                                }
                                boolean exists = false;
                                for (Post temp : posts) {
                                    if (temp.postId.equals(post.postId)) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (exists) return;
                                posts.add(post);
                                Collections.sort(posts, new PostComparator());
                                Intent intent1 = new Intent(REQUEST_POST);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                                break;
                            case REQUEST_POSTS:
                                JsonObject jsonObject = gson.fromJson(new String(message.args), JsonElement.class).getAsJsonObject();
                                if (! jsonObject.get("ack").getAsBoolean()) {
                                    JsonObject jsonObject1 = new JsonObject();
                                    jsonObject1.addProperty("sessionId", DataService.SESSION_ID);
                                    jsonObject1.addProperty("refresh", true);
                                }
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REQUEST_POST));
                                break;
                            case REQUEST_LIKE_POST:
                                Intent intent2 = new Intent(REQUEST_LIKE_POST);
                                intent2.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                                break;
                        }
                        break;
                    case ENGINE_EVENT:
                        switch (message.func) {
                            case REQUEST_EVENTS:
                                JsonObject jsonObject = gson.fromJson(new String(message.args), JsonElement.class).getAsJsonObject();
                                if (!jsonObject.get("ack").getAsBoolean() && !SESSION_ID.equals("")) {
                                    JsonObject jsonObject1 = new JsonObject();
                                    jsonObject1.addProperty("sessionId", DataService.SESSION_ID);
                                    jsonObject1.addProperty("refresh", true);
                                    ClientApi.call(getApplicationContext(), ENGINE_EVENT, REQUEST_EVENTS, gson.toJson(jsonObject1).getBytes());
                                }
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REQUEST_EVENT));
                                break;
                            case REQUEST_EVENT:
                                Event event = gson.fromJson(new String(message.args), Event.class);
                                int size = events.size();
                                if (size > 10) {
                                    if (Conversions.getTimeFromUUID(UUID.fromString(event.eventId)) > Conversions.getTimeFromUUID(UUID.fromString(events.get(0).eventId))) events.clear();
                                }
                                boolean exists = false;
                                for (Event temp : events) {
                                    if (temp.eventId.equals(event.eventId)) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (exists) return;
                                events.add(event);
                                Collections.sort(events, new EventComparator());
                                Intent intent1 = new Intent(REQUEST_EVENT);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                                break;
                            case REQUEST_INTEREST_EVENT:
                                Intent intent2 = new Intent(REQUEST_INTEREST_EVENT);
                                intent2.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                                break;

                        }
                        break;
                    case ENGINE_PROFILE:
                        switch (message.func) {
                            case REQUEST_GET_PROFILE:
                                Intent intent = new Intent(REQUEST_GET_PROFILE);
                                intent.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                break;
                            case REQUEST_PROFILE_POSTS:
                                Intent intent1 = new Intent(REQUEST_PROFILE_POSTS);
                                intent1.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
                                break;
                            case REQUEST_PROFILE_VIEWS:
                                Intent intent2 = new Intent(REQUEST_PROFILE_VIEWS);
                                intent2.putExtra("args", message.args);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                                break;
                        }
                }
            }
        }, new ClientApi.OnConnected() {
            @Override
            public void connected() {
                Log.d("Connected", "Connected to server");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(REQUEST_SILENT_LOGIN));
            }
        }, new ClientApi.OnDisconnected() {
            @Override
            public void disconnected() {
                if (api.isNetworkConnected(getApplicationContext())) api.connect();
            }
        });
        api.setSslAuthenticateHost(false);


        LocalBroadcastManager.getInstance(this).registerReceiver(api.requestReceiver, new IntentFilter(ClientApi.CALL));
        registerReceiver(api.networkChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(api.requestReceiver);
        unregisterReceiver(api.networkChangeReceiver);
        dbHelper.close();
        super.onDestroy();
    }
    public class PostComparator implements Comparator<Post> {
        @Override
        public int compare(Post post1, Post post2) {
            return (int)(Conversions.getTimeFromUUID(UUID.fromString(post2.postId)) - Conversions.getTimeFromUUID(UUID.fromString(post1.postId)));
        }
    }
    public class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            return (int) (Conversions.getTimeFromUUID(UUID.fromString(event2.eventId)) - Conversions.getTimeFromUUID(UUID.fromString(event1.eventId)));
        }
    }
}
