package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ComposeTweetFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvReverseCharCounter) TextView reverseCharCounter;
    @BindView(R.id.etTweet) EditText etTweet;
    @BindView(R.id.btTweet) Button btTweet;
    private TwitterClient client;
    private String userName;
    private Tweet tweet;
    final int MAX_TWEET_SIZE = 140;

    public interface ComposeTweetListener {
        void onSubmitTweet(Parcelable tweet);
    }

    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ComposeTweetFragment.
     */
    public static ComposeTweetFragment newInstance() {
        return new ComposeTweetFragment();
    }

    public static ComposeTweetFragment newInstance(String userName) {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            userName = getArguments().getString("userName");
        client = TwitterApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        ButterKnife.bind(this, view);
        if(userName != null && !userName.isEmpty()) {
            etTweet.setText(String.format("@%s", userName));
            reverseCharCounter.setText(String.valueOf(MAX_TWEET_SIZE - userName.length() - 1));
        }
        etTweet.addTextChangedListener(mTextEditorWatcher);
        btTweet.setOnClickListener(this);
        return view;
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            reverseCharCounter.setText(String.valueOf(MAX_TWEET_SIZE - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == btTweet.getId()) {
            client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = new Tweet(response.getString("text"),
                                User.fromJSON(response.getJSONObject("user")),
                                response.getString("created_at"),
                                response.getLong("id"));
                        ComposeTweetListener listener = (ComposeTweetListener) getActivity();
                        listener.onSubmitTweet(Parcels.wrap(tweet));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                    Log.d("DEBUG",response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    Log.d("DEBUG",response.toString());
                }
            });
        }
    }
}
