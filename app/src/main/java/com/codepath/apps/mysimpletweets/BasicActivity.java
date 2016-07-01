package com.codepath.apps.mysimpletweets;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

}
