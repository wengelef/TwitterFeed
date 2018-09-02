package test.wengelef.twitterfeed.locationtrends.domain

import test.wengelef.twitterfeed.locationtrends.data.*

class TwitterFeedInteractorImpl(
        private val woedService: WoedService,
        private val twitterTrendsService: TwitterTrendsService,
        private val authenticationRepository: AuthenticationRepository
) : TwitterFeedInteractor {

    override fun getTrendsForLocation(lat: String, lng: String, result: (TrendsResult) -> Unit) {
        woedService.getWoedForLatLng(lat, lng) { woedId ->
            authenticationRepository.authenticate { authenticationState ->
                when (authenticationState) {
                    is AuthenticationState.Authenticated -> {
                        twitterTrendsService.getTrendsForWoed(woedId, authenticationState.token) { items ->
                            result.invoke(TrendsResult.Success(items))
                        }
                    }
                    AuthenticationState.Error -> result.invoke(TrendsResult.AuthError)
                }
            }
        }
    }
}

sealed class TrendsResult {
    class Success(val trends: List<TrendItem>) : TrendsResult()
    object Error : TrendsResult()
    object AuthError : TrendsResult()
}