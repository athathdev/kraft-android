package com.spaceuptech.kraft.events;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.DatabaseHelper;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.spaceuptech.kraft.MainActivity.colorList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private List<Event> events;
    private DatabaseHelper databaseHelper;
    private Gson gson = new Gson();
    EventAdapter(Context context, DatabaseHelper databaseHelper, List<Event> events){
        this.context = context;
        this.events = events;
        this.databaseHelper = databaseHelper;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Event event = events.get(position);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sessionId", DataService.SESSION_ID);
        jsonObject.addProperty("eventId", event.eventId);
        ClientApi.call(context, DataService.ENGINE_EVENT, DataService.REQUEST_IMPRESSION_EVENT, gson.toJson(jsonObject).getBytes());

        holder.textViewEventName.setText(event.eventName);
        holder.textViewOrganizationName.setText(event.userName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
        holder.textViewDate.setText(simpleDateFormat.format(new Date(event.date)));
        holder.textViewContent.setText(event.content);
        if (android.os.Build.VERSION.SDK_INT > 15){
            Drawable drawable = context.getResources().getDrawable(R.drawable.circle_shape);
            int color = (event.eventName.hashCode())%colorList.length;
            if (color < 0) color = color*-1;
            drawable.setColorFilter(Color.parseColor(colorList[color]), PorterDuff.Mode.SRC_ATOP);
            holder.textViewEventIcon.setBackground(drawable);
        }
        holder.textViewEventIcon.setText(event.eventName.substring(0,1).toUpperCase());
        holder.imageButtonActionInterested.setImageResource( databaseHelper.checkIfInterestedEvent(event.eventId) ? R.drawable.ic_mood_yellow : R.drawable.ic_mood);
        holder.linearLayoutActionInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean interested = databaseHelper.checkIfInterestedEvent(event.eventId);
                holder.onClickAnimate(holder.imageButtonActionInterested);
                holder.imageButtonActionInterested.setImageResource( !interested ? R.drawable.ic_mood_yellow : R.drawable.ic_mood);
                //TODO Animations and sound effect on clicking like
                if (interested) {
                    event.interests--;
                } else {
                    event.interests++;
                }

                //TODO Animations and sound effect on clicking like
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sessionId", DataService.SESSION_ID);
                jsonObject.addProperty("eventId", event.eventId);
                jsonObject.addProperty("interest", !interested);
                ClientApi.call(context, DataService.ENGINE_EVENT, DataService.REQUEST_INTEREST_EVENT, gson.toJson(jsonObject).getBytes());
            }
        });
        holder.imageButtonActionInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean interested = databaseHelper.checkIfInterestedEvent(event.eventId);
                holder.onClickAnimate(holder.imageButtonActionInterested);
                holder.imageButtonActionInterested.setImageResource( !interested ? R.drawable.ic_mood_yellow : R.drawable.ic_mood);
                //TODO Animations and sound effect on clicking like
                if (interested) {
                    event.interests--;
                } else {
                    event.interests++;
                }

                //TODO Animations and sound effect on clicking like
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sessionId", DataService.SESSION_ID);
                jsonObject.addProperty("eventId", event.eventId);
                jsonObject.addProperty("interest", !interested);
                ClientApi.call(context, DataService.ENGINE_EVENT, DataService.REQUEST_INTEREST_EVENT, gson.toJson(jsonObject).getBytes());
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventName, textViewDate, textViewContent, textViewOrganizationName, textViewEventIcon;
        ImageButton imageButtonActionInterested;
        LinearLayout linearLayoutActionInterested;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewEventName = (TextView) itemView.findViewById(R.id.lblNameEvent);
            textViewOrganizationName = (TextView) itemView.findViewById(R.id.lblOrganizationNameEvent);
            textViewDate = (TextView) itemView.findViewById(R.id.lblDateEvent);
            textViewEventIcon = (TextView) itemView.findViewById(R.id.lblIconEvent);
            textViewContent = (TextView) itemView.findViewById(R.id.lblContentEvent);
            imageButtonActionInterested = (ImageButton) itemView.findViewById(R.id.btnActionInterestedEvent);
            linearLayoutActionInterested = (LinearLayout) itemView.findViewById(R.id.layoutActionInterestedEvent);
        }
        public void onClickAnimate(View view){
            ImageButton img = (ImageButton) view;
            PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f);
            PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f);
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(img, scalex, scaley);
            anim.setRepeatCount(1);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setDuration(100);
            anim.start();
        }
    }
}
