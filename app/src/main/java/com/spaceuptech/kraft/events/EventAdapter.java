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

import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.spaceuptech.kraft.MainActivity.colorList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    Context context;
    List<Event> events;
    EventAdapter(Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
        return new EventAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Event event = events.get(position);
        holder.textViewEventName.setText(event.name);
        holder.textViewOrganizationName.setText(event.organizationName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
        holder.textViewDate.setText(simpleDateFormat.format(new Date(event.date)));
        holder.textViewContent.setText(event.content);
        if (android.os.Build.VERSION.SDK_INT > 15){
            Drawable drawable = context.getResources().getDrawable(R.drawable.circle_shape);
            int color = (event.name.hashCode())%colorList.length;
            if (color < 0) color = color*-1;
            drawable.setColorFilter(Color.parseColor(colorList[color]), PorterDuff.Mode.SRC_ATOP);
            holder.textViewEventIcon.setBackground(drawable);
        }
        holder.textViewEventIcon.setText(event.name.substring(0,1).toUpperCase());
        holder.imageButtonActionInterested.setImageResource((event.interested) ? R.drawable.ic_mood_yellow : R.drawable.ic_mood);
        holder.linearLayoutActionInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.get(position).interested = !events.get(position).interested;
                holder.onClickAnimate(holder.imageButtonActionInterested);
                holder.imageButtonActionInterested.setImageResource((events.get(position).interested) ? R.drawable.ic_mood_yellow : R.drawable.ic_mood);
                //TODO Animations and sound effect on clicking like
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
