package com.spaceuptech.kraft.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String name, email, img, bio, userId;
    private CircleImageView circleImageView;
    private TextView textViewName, textViewEmail, textViewBio, textViewPostsCounter, textViewProfileViewsCounter;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverProfile, new IntentFilter(DataService.REQUEST_GET_PROFILE));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverPosts, new IntentFilter(DataService.REQUEST_PROFILE_POSTS));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverViews, new IntentFilter(DataService.REQUEST_PROFILE_VIEWS));


        circleImageView = (CircleImageView) findViewById(R.id.imgProfile);
        textViewName = (TextView) findViewById(R.id.lblNameProfile);
        textViewEmail = (TextView) findViewById(R.id.lblEmailProfile);
        textViewBio = (TextView) findViewById(R.id.lblBioProfile);
        textViewPostsCounter = (TextView) findViewById(R.id.lblPostsCounterProfile);
        textViewProfileViewsCounter = (TextView) findViewById(R.id.lblProfileViewsCounterProfile);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user-id");
        img = intent.getStringExtra("user-img");
        name = intent.getStringExtra("user-name");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sessionId", DataService.SESSION_ID);
        jsonObject.addProperty("userId", userId);
        ClientApi.call(this, DataService.ENGINE_PROFILE, DataService.REQUEST_GET_PROFILE, gson.toJson(jsonObject).getBytes());

        textViewName.setText(name);
        Glide.with(this).load(img).into(circleImageView);
        
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverProfile);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverPosts);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverViews);
        super.onDestroy();
    }


    private BroadcastReceiver broadcastReceiverProfile = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();

            boolean ack = jsonObject.get("ack").getAsBoolean();
            String ResponseUserId = jsonObject.get("userId").getAsString();
            String ResponseEmail = jsonObject.get("email").getAsString();
            String ResponseBio = jsonObject.get("bio").getAsString();
            String ResponseAccType = jsonObject.get("accType").getAsString();
            String ResponseName = jsonObject.get("name").getAsString();
            String ResponseImg = jsonObject.get("img").getAsString();

            if (ack && ResponseUserId.equals(userId)) {
                textViewName.setText(ResponseName);
                textViewEmail.setText(ResponseEmail);
                textViewBio.setText(ResponseBio);
                Glide.with(getApplicationContext()).load(ResponseImg).into(circleImageView);
            }
        }
    };

    private BroadcastReceiver broadcastReceiverPosts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();

            String ResponseUserId = jsonObject.get("userId").getAsString();
            int ResponseCount = jsonObject.get("count").getAsInt();

            if (ResponseUserId.equals(userId)) {
                textViewPostsCounter.setText(String.valueOf(ResponseCount));
            }
        }
    };

    private BroadcastReceiver broadcastReceiverViews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();

            String ResponseUserId = jsonObject.get("userId").getAsString();
            int ResponseCount = jsonObject.get("count").getAsInt();

            if (ResponseUserId.equals(userId)) {
                textViewProfileViewsCounter.setText(String.valueOf(ResponseCount));
            }
        }
    };
    
}
