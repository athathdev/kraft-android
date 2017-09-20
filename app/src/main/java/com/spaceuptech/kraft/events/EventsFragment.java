package com.spaceuptech.kraft.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceuptech.kraft.DatabaseHelper;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Event;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private List<Event> events;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        generateEvents();

        RecyclerView recyclerViewEvents = (RecyclerView) v.findViewById(R.id.recyclerViewEvents);
        RecyclerView.LayoutManager layoutManagerEvents = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManagerEvents);
        RecyclerView.Adapter adapterEvents = new EventAdapter(getContext(), databaseHelper, events);
        recyclerViewEvents.setAdapter(adapterEvents);

        return v;
    }

    @Override
    public void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    private void generateEvents(){

        events = new ArrayList<>();
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));
        events.add(new Event("776e4d5e-9dc9-11e7-abc4-cec278b6b50a","StartUp Netcon", "E-Cell VJTI", getString(R.string.lorem_ipsum), 1508046480000L));

    }

}
