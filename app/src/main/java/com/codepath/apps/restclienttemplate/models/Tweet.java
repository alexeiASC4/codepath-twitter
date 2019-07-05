package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class Tweet implements Serializable {

    //list the attributes
    public String body;
    public long uid; //database iD for the tweet
    public User user;
    public String createdAt;

    //deserialize the JSON data

    public static Tweet fromJSON (JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }
}
