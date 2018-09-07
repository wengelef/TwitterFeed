package test.wengelef.twitterfeed.locationtrends.data

import test.wengelef.twitterfeed.locationtrends.Auth

class AuthenticationRepository(private val authService: AuthenticationService) {

    fun authenticate(function: (AuthenticationState) -> Unit) {
        authService.authenticate(Auth.clientId, Auth.clientSecret) { authenticationState ->
            function.invoke(authenticationState)
        }
    }
}