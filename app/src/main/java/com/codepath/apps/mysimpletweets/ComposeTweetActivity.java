package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetActivity extends AppCompatActivity {
    private EditText etTweet;
    private TextView reverseCharCounter;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        etTweet = (EditText) findViewById(R.id.etTweet);
        reverseCharCounter = (TextView) findViewById(R.id.tvReverseCharCounter);
        etTweet.addTextChangedListener(mTextEditorWatcher);
        client = TwitterApplication.getRestClient();
    }

    public void onSubmit(View view) {
        client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                finish();
                Log.d("DEBUG",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("DEBUG",response.toString());
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            final int MAX_TWEET_SIZE = 140;
            reverseCharCounter.setText(String.valueOf(MAX_TWEET_SIZE - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
