package test.wengelef.twitterfeed.locationtrends.data

import com.google.gson.annotations.SerializedName

interface TwitterTrendsService {
    fun getTrendsForWoed(woedId: Int = 1, authToken: String, onSuccess: (List<TrendItem>) -> Unit)
}

data class TwitterTrendsResponse(
        @SerializedName("trends") val trends: List<TrendItem>
)

data class TrendItem(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String,
        @SerializedName("query") val query: String,
        @SerializedName("tweet_volume") val volume: Int
)