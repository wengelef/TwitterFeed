package test.wengelef.twitterfeed.locationtrends.view

import test.wengelef.twitterfeed.locationtrends.domain.TrendsResult
import test.wengelef.twitterfeed.locationtrends.domain.TwitterFeedInteractor

class LocationTrendsPresenter(private val twitterFeedInteractor: TwitterFeedInteractor) :
        Presenter<LocationTrendsView> {

    private lateinit var view: LocationTrendsView

    override fun start(view: LocationTrendsView) {
        this.view = view
    }

    override fun destroy() {
        // clean up
    }

    fun onLocation(lat: String, lng: String) {
        twitterFeedInteractor.getTrendsForLocation(lat, lng) { trendsResult ->
            when (trendsResult) {
                is TrendsResult.Success -> view.showTrends(trendsResult.trends)
                is TrendsResult.Error -> view.showError()
                is TrendsResult.AuthError -> view.showAuthError()
            }
        }
    }

    fun onLocationError() {
        view.showLocationError()
    }
}