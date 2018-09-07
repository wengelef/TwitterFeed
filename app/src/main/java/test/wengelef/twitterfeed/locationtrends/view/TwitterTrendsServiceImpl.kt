package test.wengelef.twitterfeed.locationtrends.view

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import test.wengelef.twitterfeed.locationtrends.GsonRequest
import test.wengelef.twitterfeed.locationtrends.data.TrendItem
import test.wengelef.twitterfeed.locationtrends.data.TwitterTrendsResponse
import test.wengelef.twitterfeed.locationtrends.data.TwitterTrendsService

class TwitterTrendsServiceImpl(private val context: Context) : TwitterTrendsService {

    override fun getTrendsForWoed(woedId: Int, authToken: String, onSuccess: (List<TrendItem>) -> Unit) {
        val requestUrl = "https://api.twitter.com/1.1/trends/place.json?id=$woedId"
        Volley.newRequestQueue(context)
                .add(GsonRequest(
                        requestUrl,
                        Array<TwitterTrendsResponse>::class.java,
                        mutableMapOf("Authorization" to "Bearer $authToken"),
                        Response.Listener { trendsResponse ->
                            if (trendsResponse.isEmpty()) {
                                onSuccess.invoke(emptyList())
                            } else {
                                onSuccess.invoke(trendsResponse.first().trends)
                            }
                        },
                        Response.ErrorListener { error ->
                            Log.e("TwitterService", "response : $error")
                        }
                ))
    }
}