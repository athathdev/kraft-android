package com.spaceuptech.kraft.posts;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.Post;
import com.spaceuptech.kraft.profile.ProfileActivity;
import com.spaceuptech.kraft.utility.TimeStamp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<Post> posts;
    Context context;

    PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        holder.textViewAuthorName.setText(post.author.name);
        holder.textViewAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user-id", post.author.id);
                context.startActivity(intent);
            }
        });
        holder.textViewTime.setText(TimeStamp.getTimeElapsed(post.time));
        holder.textViewContent.setText(post.content);
        if (post.likes < 1) {
            holder.textViewLikesCounter.setVisibility(View.GONE);
        } else {
            holder.textViewLikesCounter.setVisibility(View.VISIBLE);
            holder.textViewLikesCounter.setText(String.valueOf(post.likes));
        }
        Glide.with(context).load(post.author.pic).into(holder.circleImageViewProfileAuthor);
        // TODO If no image then show the background containing first letter of User
        holder.imageButtonActionLikePost.setImageResource((post.liked) ? R.drawable.ic_favourite_red : R.drawable.ic_favourite);
        holder.imageButtonActionLikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posts.get(position).liked = !posts.get(position).liked;
                holder.onClickAnimate(view);
                holder.imageButtonActionLikePost.setImageResource((posts.get(position).liked) ? R.drawable.ic_favourite_red : R.drawable.ic_favourite);
                //TODO Animations and sound effect on clicking like
            }
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthorName, textViewTime, textViewContent, textViewLikesCounter;
        CircleImageView circleImageViewProfileAuthor;
        ImageButton imageButtonActionLikePost;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthorName = (TextView) itemView.findViewById(R.id.lblAuthorNamePost);
            textViewTime = (TextView) itemView.findViewById(R.id.lblTimePost);
            textViewContent = (TextView) itemView.findViewById(R.id.lblContentPost);
            textViewLikesCounter = (TextView) itemView.findViewById(R.id.lblLikeCounterPost);
            circleImageViewProfileAuthor = (CircleImageView) itemView.findViewById(R.id.imgUserPost);
            imageButtonActionLikePost = (ImageButton) itemView.findViewById(R.id.btnLikePost);
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
