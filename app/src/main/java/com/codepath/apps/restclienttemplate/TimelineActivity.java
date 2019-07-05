package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


//This activity shows a user's tweets
public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    FloatingActionButton mButtonCompose;
    public static final int  REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(this);

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the ArrayList (data source)
        tweets = new ArrayList<>();

        //construct the adapter from this data source
        tweetAdapter = new TweetAdapter(tweets);

        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        mButtonCompose = findViewById(R.id.btnCompose);

        populateTimeline();

        setupBtnListener();

    }

    public void populateTimeline (){
        //network request to retrieve data from API
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("TwitterClient", response.toString());
                //iterate through the JSON Array
                //for each entry deserialize the JSON object
                for (int i = 0; i< response.length(); i++){
                    //convert each the object to a Tweet model
                    //add that Tweet model to our data source
                    //notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() -1);

                    }catch (JSONException e ){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    //setup button
    public void setupBtnListener(){
        mButtonCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to ComposeActivity
                launchComposeView();

                //send the contents of et_tweet
                /*Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                i.putExtra("", etName.getText().toString());
                startActivity(i);*/

                //startActivityForResult(i, 123);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //if (resultCode==RESULT_OK && requestCode == REQUEST_CODE){
            Tweet tweet = (Tweet) data.getSerializableExtra("text");
            tweets.add(0,tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        //}
    }

    private void launchComposeView(){
        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i, 100);

    }

}
