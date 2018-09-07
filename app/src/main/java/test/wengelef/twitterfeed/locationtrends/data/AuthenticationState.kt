package test.wengelef.twitterfeed.locationtrends.data

sealed class AuthenticationState {
    data class Authenticated(val token: String): AuthenticationState()
    object Error : AuthenticationState()
}