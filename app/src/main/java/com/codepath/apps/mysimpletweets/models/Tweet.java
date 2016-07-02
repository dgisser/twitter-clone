package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dgisser on 6/27/16.
 */
@Parcel
public class Tweet {

    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public int retweetCount;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() { return createdAt; }

    //for Parceler
    public Tweet() {
    }

    public Tweet(String body, User user, String createdAt, long uid) {
        this.body = body;
        this.user = user;
        this.createdAt = getTimeDifference(createdAt);
        this.uid = uid;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = getTimeDifference(jsonObject.getString("created_at"));
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getTimeDifference(String rawJsonDate) {
        String time = "";
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        format.setLenient(true);
        try {
            long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
            if (diff < 5)
                time = "Just now";
            else if (diff < 60)
                time = String.format(Locale.ENGLISH, "%ds",diff);
            else if (diff < 60 * 60)
                time = String.format(Locale.ENGLISH, "%dm", diff / 60);
            else if (diff < 60 * 60 * 24)
                time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
            else if (diff < 60 * 60 * 24 * 30)
                time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
            else {
                Calendar now = Calendar.getInstance();
                Calendar then = Calendar.getInstance();
                then.setTime(format.parse(rawJsonDate));
                if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                    time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                            + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                } else {
                    time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                            + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                            + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
                }
            }
        }  catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
