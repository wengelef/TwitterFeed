package test.wengelef.twitterfeed.locationtrends.data

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.gson.annotations.SerializedName
import test.wengelef.twitterfeed.locationtrends.GsonRequest

class WoedServiceImpl(private val context: Context) : WoedService {

    override fun getWoedForLatLng(lat: String, lng: String, onSuccess: (Int) -> Unit) {
        val requestUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places%20where%20text%3D%22($lat,$lng)%22%20limit%201&diagnostics=false&format=json"
        Volley.newRequestQueue(context)
                .add(GsonRequest(
                        requestUrl,
                        WoedResponse::class.java,
                        null,
                        Response.Listener { woedResponse ->
                            onSuccess.invoke(woedResponse.query.results.place.id.toInt())
                        },
                        Response.ErrorListener { error ->

                        }
                ))
    }
}

data class WoedResponse(
        @SerializedName("query") val query: WoedQuery
)

data class WoedQuery(
        @SerializedName("results") val results: WoedResults
)

data class WoedResults(
        @SerializedName("place") val place: WoedPlace
)

data class WoedPlace(@SerializedName("woeid") val id: String)
