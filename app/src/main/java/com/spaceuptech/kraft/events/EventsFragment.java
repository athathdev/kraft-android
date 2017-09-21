package com.spaceuptech.kraft.events;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.DatabaseHelper;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Event;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private Gson gson = new Gson();

    private RecyclerView.Adapter adapterEvents;
    private RecyclerView recyclerViewEvents;
    private RecyclerView.LayoutManager layoutManagerEvents;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverEvent, new IntentFilter(DataService.REQUEST_EVENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverInterest, new IntentFilter(DataService.REQUEST_INTEREST_EVENT));

        getEvents(true);

        recyclerViewEvents = (RecyclerView) v.findViewById(R.id.recyclerViewEvents);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeEvents);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorMaterialBlue, R.color.colorMaterialRed, R.color.colorMaterialAmber, R.color.colorMaterialGreen);


        layoutManagerEvents = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManagerEvents);
        adapterEvents = new EventAdapter(getContext(), databaseHelper, DataService.events);
        recyclerViewEvents.setAdapter(adapterEvents);

        recyclerViewEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) userScrolled = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManagerEvents.getChildCount();
                totalItemCount = layoutManagerEvents.getItemCount();
                pastVisiblesItems = ((LinearLayoutManager)recyclerViewEvents.getLayoutManager()).findFirstVisibleItemPosition();

                if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    if (dy > 0) {
                        getEvents(false);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverEvent);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverInterest);
        super.onDestroy();
    }

    private void getEvents(boolean refresh) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sessionId", DataService.SESSION_ID);
        jsonObject.addProperty("refresh", refresh);
        ClientApi.call(getContext(), DataService.ENGINE_EVENT, DataService.REQUEST_EVENTS, gson.toJson(jsonObject).getBytes());
    }


    private BroadcastReceiver broadcastReceiverEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            adapterEvents.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver broadcastReceiverInterest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();
            boolean ack = jsonObject.get("ack").getAsBoolean();
            boolean interest = jsonObject.get("interest").getAsBoolean();
            String eventId = jsonObject.get("eventId").getAsString();
            if (ack) {
                if (interest) databaseHelper.setEventAsInterested(eventId);
                else databaseHelper.setEventAsNotInterested(eventId);
            }
        }
    };


}
