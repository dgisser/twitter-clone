package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        switch (getIntent().getIntExtra("method",0)) {
            case 1:
                if (savedInstanceState == null) {
                    UserTimelineFragment fragmentUserTimeline = new UserTimelineFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, fragmentUserTimeline);
                    ft.commit();
                }
                client.getSelfInfo(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user = User.fromJSON(response);
                        getSupportActionBar().setTitle("@" + user.getScreenName());
                        populateProfileHeader(user);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                        Log.d("profileactivity",obj.toString());
                    }
                });
                break;
            case 2:
                Log.d("profileactivity","case 2");
                long uid = getIntent().getLongExtra("uid", -1);
                if (savedInstanceState == null) {
                    UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(uid);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, fragmentUserTimeline);
                    ft.commit();
                }
                client.getUserInfo(uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (response.length() <= 0)
                            Log.d("profileactivity","no user found");
                        try {
                            user = User.fromJSON(response.getJSONObject(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getSupportActionBar().setTitle("@" + user.getScreenName());
                        populateProfileHeader(user);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                        Log.d("profileactivity",obj.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("profileactivity",responseString.toString());
                    }
                });
                break;
            default:
                Log.d("profileactivity","nothing hit");

        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvFullName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
}
