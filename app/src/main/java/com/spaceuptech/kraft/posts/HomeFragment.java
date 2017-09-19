package com.spaceuptech.kraft.posts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Post;
import com.spaceuptech.kraft.data.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Post> posts;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        generatePosts();

        RecyclerView recyclerViewPosts = (RecyclerView) v.findViewById(R.id.recyclerViewPosts);
        RecyclerView.LayoutManager layoutManagerPosts = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(layoutManagerPosts);
        RecyclerView.Adapter adapterPosts = new PostAdapter(getContext(), posts);
        recyclerViewPosts.setAdapter(adapterPosts);
        return v;

    }

    private void generatePosts(){
        User user1 = new User("user1", "Jayesh Choudhary", "https://image-store.slidesharecdn.com/6ad94721-e556-42ee-92c7-db66e3535e86-original.jpeg");
        User user2 = new User("user2", "Noorain Panjwani", "https://qph.ec.quoracdn.net/main-thumb-87314876-200-otbyewbjtwtmseavcjdzppytgtjoraqo.jpeg");

        posts = new ArrayList<>();
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 0, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, false));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, true));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, true));
        posts.add(new Post("1",user1, getString(R.string.lorem_ipsum), 21, 1505800080000L, true));
        posts.add(new Post("1",user2, getString(R.string.lorem_ipsum), 10, 1505800080000L, true));

    }

}
