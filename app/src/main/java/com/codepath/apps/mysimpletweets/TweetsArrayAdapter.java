package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dgisser on 6/27/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{
    private long uid;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets, long uid) {
        super(context, android.R.layout.simple_list_item_1, tweets);
        Log.d("tweetsarrayadapter",String.format("uid %d", uid));
        this.uid = uid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        //TODO butterknife
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageButton btReply = (ImageButton) convertView.findViewById(R.id.btReply);
        final View finalConvertView = convertView;
        btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BasicActivity) finalConvertView.getContext()).replyClicked(tweet.getUser().getScreenName());
            }
        });
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getCreatedAt());
        ivProfileImage.setImageResource(android.R.color.transparent);
        //check if id is equal to context id
        if (tweet.getUser().getUid() != uid) {
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ProfileActivity.class);
                    i.putExtra("method",2);
                    i.putExtra("uid",tweet.getUser().getUid());
                    v.getContext().startActivity(i);
                }
            });
        }
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        return convertView;
    }
}
