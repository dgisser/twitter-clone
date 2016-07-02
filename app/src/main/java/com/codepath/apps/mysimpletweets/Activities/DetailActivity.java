package com.codepath.apps.mysimpletweets.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    private Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        setContentView(R.layout.activity_detail);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvTime = (TextView) findViewById(R.id.tvTime);
        ImageButton btReply = (ImageButton) findViewById(R.id.btReply);
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        Log.d("detailactivity","a");
        tvTime.setText(tweet.getCreatedAt());
        Log.d("detailactivity","b");
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);

        return view;
    }

}
