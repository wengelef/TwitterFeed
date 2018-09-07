package test.wengelef.twitterfeed.locationtrends.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.gson.annotations.SerializedName
import test.wengelef.twitterfeed.locationtrends.GsonRequest

class AuthenticationServiceImpl(private val context: Context) : AuthenticationService {

    override fun authenticate(clientId: String, clientSecret: String, state: (AuthenticationState) -> Unit) {
        val requestUrl = "https://api.twitter.com/oauth2/token?grant_type=client_credentials&client_id=$clientId&client_secret=$clientSecret"

        Volley.newRequestQueue(context)
                .add(GsonRequest(
                        requestUrl,
                        AuthResponse::class.java,
                        null,
                        Response.Listener { authResponse ->
                            state.invoke(AuthenticationState.Authenticated(authResponse.token))
                        },
                        Response.ErrorListener { error -> state.invoke(AuthenticationState.Error) },
                        Request.Method.POST
                ))
    }

    data class AuthResponse(@SerializedName("access_token") val token: String)
}