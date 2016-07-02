package com.codepath.apps.mysimpletweets.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;

/**
 * Created by dgisser on 7/1/16.
 */
public abstract class BasicActivity extends AppCompatActivity{
    public MenuItem miActionProgressItem;
    public void showProgressBar() {
        // Show progress item
        if (miActionProgressItem != null)
            miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        if (miActionProgressItem != null)
            miActionProgressItem.setVisible(false);
    }

    public void replyClicked(String userName) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance(userName);
        composeTweetFragment.show(fm, "fragment_compose_tweet");
    }

}
