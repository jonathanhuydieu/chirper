package com.codepath.apps.restclienttemplate.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.time.format.DateTimeFormatter

@Parcelize
class Tweet (var body: String = "", var createdAt: String = "", var user: User? = null) :
Parcelable {

    var timestamp = ""

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))

            tweet.timestamp = TimeFormatter.getTimeDifference(jsonObject.getString("created_at"))
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets

        }

        // Returns the correct time for the tweet returning TimeFormatter.getTimeDifference(createdAt)
        fun getFormattedTimeStamp(createdAt: String): String {
            return TimeFormatter.getTimeDifference(createdAt) // The formatted timestamp from the library formatter function
        }
    }
}