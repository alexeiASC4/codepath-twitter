package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    EditText mTweetText;
    Button mSendButton;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        mTweetText = findViewById(R.id.etTweetText);
        mSendButton = findViewById(R.id.btnSendTweet);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = mTweetText.getText().toString();
                sendTweet(tweet);
            }
        });


    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
    }

    //store entered text
    public void storeTextEntry(){
        EditText simpleEditText = (EditText) findViewById(R.id.etTweetText);
        String strValue = simpleEditText.getText().toString();
    }



    public void sendTweet(String text){
            client.sendTweet(text, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try{
                        Tweet tweet = Tweet.fromJSON(response);
                        Intent i = new Intent();
                        i.putExtra("text", Parcels.wrap(tweet));
                        setResult(RESULT_OK,i);
                        finish();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
