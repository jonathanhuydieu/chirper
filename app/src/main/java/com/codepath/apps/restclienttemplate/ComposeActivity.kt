package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    // Variable to edit text
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    // Declare variable for twitter client
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)


        // Try to implement character count
        etCompose.addTextChangedListener(object: TextWatcher {

            // Fires off right before the text has changed
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Check for emptiness from the getgo
                if (etCompose.text.isEmpty()) {
                    btnTweet.isEnabled = false
                }
            }

            // Fires off as the text is being changed
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val tweetContent = etCompose.text.toString()

                if (tweetContent.isEmpty() || tweetContent.length > 280) {
                    btnTweet.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // Don't do anything
                val tweetContent = etCompose.text.toString()
                if (tweetContent.isNotEmpty() && tweetContent.length < 280) {
                    btnTweet.isEnabled = true
                }
            }

        })

        btnTweet = findViewById(R.id.btnTweet)

        // Init. client
        client = TwitterApplication.getRestClient(this)

        // Set onclick listener that handles the user's click on the tweet button
        btnTweet.setOnClickListener {

            // Grab the content of the edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            // Make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets are n o t allowed!", Toast.LENGTH_SHORT).show()
                    // Look into using a snackbar instead of a toast
            } else

            // Make sure the tweet is under the character count
            if (tweetContent.length > 280) {
                Toast.makeText(this,  "This tweet is too long! The limit is 140 characters.", Toast.LENGTH_SHORT)
                    .show()
            } else {

            // Make an api call to twitter that publishes the tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        // Send the tweet back to TimelineActivity to show
                        // after we successfully tweet, we have to display it

                        val tweet = Tweet.fromJson(json.jsonObject) // We know json will exist if it's successful

                        // Create intent to pass back to timeline activity
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish() // Closes out so we end back up in timelineactivity
                        Log.e(TAG, "Successfully published tweet! >:)")
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet :(", throwable)
                    }


                })

            }


        }

    }

    companion object {
        val TAG = "ComposeActivity"
    }
}