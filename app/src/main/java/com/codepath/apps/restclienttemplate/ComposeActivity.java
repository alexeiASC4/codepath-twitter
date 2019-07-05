package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.TimelineActivity.REQUEST_CODE;

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

        client = TwitterApp.getRestClient(this);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweet = mTweetText.getText().toString();
                sendTweet(tweet);
                finish();
            }
        });


    }


    public void sendTweet(String text){
            client.sendTweet(text, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == REQUEST_CODE) {
                        try {
                            JSONObject responseJson = new JSONObject(new String(responseBody));
                            Tweet tweet = Tweet.fromJSON(responseJson);
                            Intent i = new Intent();
                            i.putExtra("text", tweet);
                            setResult(RESULT_OK, i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();

                }
            });

    }
}


