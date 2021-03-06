package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.codepath.apps.mysimpletweets.Activities.BasicActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by dgisser on 6/28/16.
 */
public class UserTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() == null)
            super.onCreate(savedInstanceState);
        else
            super.onCreate(savedInstanceState, getArguments().getLong("uid"));
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    public static UserTimelineFragment newInstance() {
        return new UserTimelineFragment();
    }

    public static UserTimelineFragment newInstance(long uid) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("uid", uid);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline() {
        long uid;
        if (getArguments() == null)
            uid = -1;
        else
            uid = getArguments().getLong("uid");
        ((BasicActivity) getActivity()).showProgressBar();
        client.getUserTimeline(uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
                ((BasicActivity) getActivity()).hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                ((BasicActivity) getActivity()).hideProgressBar();
            }
        });
    }

    @Override
    public void populateTimeLine(final SwipeRefreshLayout layout) {
        long uid;
        if (getArguments() == null)
            uid = -1;
        else
            uid = getArguments().getLong("uid");
        ((BasicActivity) getActivity()).showProgressBar();
        client.getUserTimeline(uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
                ((BasicActivity) getActivity()).hideProgressBar();
                layout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                ((BasicActivity) getActivity()).hideProgressBar();
                layout.setRefreshing(false);
            }
        });
    }
}
