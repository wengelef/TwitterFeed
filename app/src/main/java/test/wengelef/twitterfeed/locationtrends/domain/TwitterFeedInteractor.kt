package test.wengelef.twitterfeed.locationtrends.domain

interface TwitterFeedInteractor {
    fun getTrendsForLocation(lat: String, lng: String, result: (TrendsResult) -> Unit)
}