package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.codepath.apps.mysimpletweets.Activities.BasicActivity;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SearchTweetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTweetsFragment extends TweetsListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String QUERYSTRING = "query";
    private TwitterClient client;

    // TODO: Rename and change types of parameters
    private String query;

    public SearchTweetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param query Parameter 1.
     * @return A new instance of fragment SearchTweetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchTweetsFragment newInstance(String query) {
        SearchTweetsFragment fragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString(QUERYSTRING, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(QUERYSTRING);
        }
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    @Override
    public void populateTimeline() {
        ((BasicActivity) getActivity()).showProgressBar();
        client.getSearchTimeline(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
                ((BasicActivity) getActivity()).hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("searchtweetsfragment", response.toString());
                try {
                    addAll(Tweet.fromJSONArray(response.getJSONArray("statuses")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        ((BasicActivity) getActivity()).showProgressBar();
        client.getSearchTimeline(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
                ((BasicActivity) getActivity()).hideProgressBar();
                layout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("searchtweetsfragment", response.toString());
                try {
                    addAll(Tweet.fromJSONArray(response.getJSONArray("statuses")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
