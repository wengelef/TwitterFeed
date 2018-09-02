package test.wengelef.twitterfeed.locationtrends.data

interface AuthenticationService {
    fun authenticate(clientId: String, clientSecret: String, state: (AuthenticationState) -> Unit)
}