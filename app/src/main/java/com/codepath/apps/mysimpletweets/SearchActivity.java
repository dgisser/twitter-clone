package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.codepath.apps.mysimpletweets.Activities.BasicActivity;
import com.codepath.apps.mysimpletweets.fragments.SearchTweetsFragment;

public class SearchActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SearchTweetsFragment searchFrag = (SearchTweetsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.search_tweet_fragment);
        }
        setContentView(R.layout.activity_search);
        SearchTweetsFragment searchTweetFragment =
                SearchTweetsFragment.newInstance(getIntent().getStringExtra("queryString"));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flstuff, searchTweetFragment);
        ft.commit();
    }
}
