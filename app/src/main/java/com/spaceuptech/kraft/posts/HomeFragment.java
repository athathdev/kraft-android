package com.spaceuptech.kraft.posts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceuptech.kraft.DatabaseHelper;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Post;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private List<Post> posts;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        generatePosts();

        RecyclerView recyclerViewPosts = (RecyclerView) v.findViewById(R.id.recyclerViewPosts);
        RecyclerView.LayoutManager layoutManagerPosts = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManagerPosts);
        RecyclerView.Adapter adapterPosts = new PostAdapter(getContext(), databaseHelper, posts);
        recyclerViewPosts.setAdapter(adapterPosts);
        return v;

    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }


    private void generatePosts(){

        posts = new ArrayList<>();
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));
        posts.add(new Post("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","jayeshC", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg", getString(R.string.lorem_ipsum), 21));


    }

}
