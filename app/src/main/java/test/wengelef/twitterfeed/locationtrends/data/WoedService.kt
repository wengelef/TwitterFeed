package test.wengelef.twitterfeed.locationtrends.data

interface WoedService {
    fun getWoedForLatLng(lat: String, lng: String, onSuccess: (Int) -> Unit)
}