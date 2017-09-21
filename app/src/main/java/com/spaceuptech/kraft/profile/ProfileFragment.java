package com.spaceuptech.kraft.profile;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private String name, email, img, bio, userId;
    private CircleImageView circleImageView;
    private TextView textViewName, textViewEmail, textViewBio, textViewPostsCounter, textViewProfileViewsCounter;
    private Gson gson = new Gson();
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverProfile, new IntentFilter(DataService.REQUEST_GET_PROFILE));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverPosts, new IntentFilter(DataService.REQUEST_PROFILE_POSTS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverViews, new IntentFilter(DataService.REQUEST_PROFILE_VIEWS));


        circleImageView = (CircleImageView) v.findViewById(R.id.imgProfile);
        textViewName = (TextView) v.findViewById(R.id.lblNameProfile);
        textViewEmail = (TextView) v.findViewById(R.id.lblEmailProfile);
        textViewBio = (TextView) v.findViewById(R.id.lblBioProfile);
        textViewPostsCounter = (TextView) v.findViewById(R.id.lblPostsCounterProfile);
        textViewProfileViewsCounter = (TextView) v.findViewById(R.id.lblProfileViewsCounterProfile);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_user), Context.MODE_PRIVATE);
        name = sharedPref.getString("name", "user");
        email = sharedPref.getString("email", "user@gmail.com");
        img = sharedPref.getString("img", "img");
        bio = sharedPref.getString("bio", "Hey There, I'm using Kraft");
        userId = sharedPref.getString("userId", "userId");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sessionId", DataService.SESSION_ID);
        jsonObject.addProperty("userId", userId);
        ClientApi.call(getContext(), DataService.ENGINE_PROFILE, DataService.REQUEST_GET_PROFILE, gson.toJson(jsonObject).getBytes());

        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewBio.setText(bio);
        Glide.with(getContext()).load(img).into(circleImageView);

        return v;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverProfile);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverPosts);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverViews);
        super.onDestroy();
    }


    private BroadcastReceiver broadcastReceiverProfile = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();

            boolean ack = jsonObject.get("ack").getAsBoolean();

            if (ack) {
                String ResponseUserId = jsonObject.get("userId").getAsString();
                String ResponseEmail = jsonObject.get("email").getAsString();
                String ResponseBio = jsonObject.get("bio").getAsString();
                String ResponseAccType = jsonObject.get("accType").getAsString();
                String ResponseName = jsonObject.get("name").getAsString();
                String ResponseImg = jsonObject.get("img").getAsString();
                if (!ResponseUserId.equals(userId)) return;
                textViewName.setText(ResponseName);
                textViewEmail.setText(ResponseEmail);
                textViewBio.setText(ResponseBio);
                Glide.with(getContext()).load(ResponseImg).into(circleImageView);
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
