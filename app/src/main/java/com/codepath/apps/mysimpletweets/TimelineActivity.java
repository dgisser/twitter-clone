package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.Activities.BasicActivity;
import com.codepath.apps.mysimpletweets.Activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.Activities.SearchActivity;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends BasicActivity
        implements ComposeTweetFragment.ComposeTweetListener {
    @BindView(R.id.viewpager) ViewPager vpPager;
    //BindView(R.id.miActionProgress) MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }

    //send an API request to get timeline JSON
    // fill listview by creating tweet objects from json


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(searchView.getContext(), SearchActivity.class);
                i.putExtra("queryString",query);
                startActivity(i);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("method", 1);
        startActivity(i);
    }

    public void onCreateTweet(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm, "fragment_compose_tweet");
    }

    @Override
    public void onSubmitTweet(Parcelable tweet) {
        Tweet newTweet = (Tweet) Parcels.unwrap(tweet);
        ((TweetsPagerAdapter)vpPager.getAdapter()).htFrag.tweets.add(0,newTweet);
        ((TweetsPagerAdapter)vpPager.getAdapter()).htFrag.aTweets.notifyDataSetChanged();
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("method", 1);
        startActivity(i);
    }

    /*@Override
    public void onFinishEditDialog(String inputText) {
        //add tweet
    }*/

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private HomeTimelineFragment htFrag;
        private String tabTitles [] = {"home", "mentions"};

        public TweetsPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                htFrag = new HomeTimelineFragment();
                return htFrag;
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
