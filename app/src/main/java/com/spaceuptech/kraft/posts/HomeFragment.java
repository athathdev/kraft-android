package com.spaceuptech.kraft.posts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.DatabaseHelper;
import com.spaceuptech.kraft.MainActivity;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Post;
import com.spaceuptech.kraft.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private Gson gson = new Gson();

    private RecyclerView.Adapter adapterPosts;
    private RecyclerView recyclerViewPosts;
    private RecyclerView.LayoutManager layoutManagerPosts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;

    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverPost, new IntentFilter(DataService.REQUEST_POST));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiverLike, new IntentFilter(DataService.REQUEST_LIKE_POST));

        databaseHelper = new DatabaseHelper(getContext());

        getPosts(true);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipePosts);
        recyclerViewPosts = (RecyclerView) v.findViewById(R.id.recyclerViewPosts);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fabCreatePost);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorMaterialBlue, R.color.colorMaterialRed, R.color.colorMaterialAmber, R.color.colorMaterialGreen);

        layoutManagerPosts = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManagerPosts);
        adapterPosts = new PostAdapter(getContext(), databaseHelper,DataService.posts);
        recyclerViewPosts.setAdapter(adapterPosts);

        recyclerViewPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) userScrolled = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManagerPosts.getChildCount();
                totalItemCount = layoutManagerPosts.getItemCount();
                pastVisiblesItems = ((LinearLayoutManager)recyclerViewPosts.getLayoutManager()).findFirstVisibleItemPosition();

                if (userScrolled ) {
                    userScrolled = false;
                    if (dy > 0) {
                        floatingActionButton.hide();
                        if ((visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            if (dy > 0) {
                                getPosts(false);
                            }
                        }
                    } else floatingActionButton.show();

                }
            }
        });

        return v;

    }

    private void getPosts(boolean refresh) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sessionId", DataService.SESSION_ID);
        jsonObject.addProperty("refresh", refresh);
        ClientApi.call(getContext(), DataService.ENGINE_POST, DataService.REQUEST_POSTS, gson.toJson(jsonObject).getBytes());
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverPost);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiverLike);

        super.onDestroy();
    }


    private BroadcastReceiver broadcastReceiverPost = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapterPosts.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        }
    };

    private BroadcastReceiver broadcastReceiverLike = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();
            boolean ack = jsonObject.get("ack").getAsBoolean();
            boolean like = jsonObject.get("like").getAsBoolean();
            String postId = jsonObject.get("postId").getAsString();
            if (ack) {
                if (like) databaseHelper.setPostAsLiked(postId);
                else databaseHelper.setPostAsNotLiked(postId);
            }
        }
    };


}
