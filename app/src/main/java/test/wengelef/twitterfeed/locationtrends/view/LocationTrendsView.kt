package test.wengelef.twitterfeed.locationtrends.view

import test.wengelef.twitterfeed.locationtrends.data.TrendItem

interface LocationTrendsView : Presenter.View {
    fun showLocationError()
    fun showTrends(trends: List<TrendItem>)
    fun showError()
    fun showAuthError()
}