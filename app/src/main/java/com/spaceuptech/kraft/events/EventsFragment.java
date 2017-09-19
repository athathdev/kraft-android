package com.spaceuptech.kraft.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Event;
import com.spaceuptech.kraft.data.Post;
import com.spaceuptech.kraft.data.User;
import com.spaceuptech.kraft.posts.PostAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    List<Event> events;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        generateEvents();

        RecyclerView recyclerViewEvents = (RecyclerView) v.findViewById(R.id.recyclerViewEvents);
        RecyclerView.LayoutManager layoutManagerEvents = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManagerEvents);
        RecyclerView.Adapter adapterEvents = new EventAdapter(getContext(), events);
        recyclerViewEvents.setAdapter(adapterEvents);

        return v;
    }

    private void generateEvents(){

        events = new ArrayList<>();
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, true));
        events.add(new Event("1","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L, false));

    }

}
