package test.wengelef.twitterfeed.locationtrends.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fr_feed_list.*
import test.wengelef.twitterfeed.R
import test.wengelef.twitterfeed.locationtrends.data.AuthenticationRepository
import test.wengelef.twitterfeed.locationtrends.data.AuthenticationServiceImpl
import test.wengelef.twitterfeed.locationtrends.data.TrendItem
import test.wengelef.twitterfeed.locationtrends.data.WoedServiceImpl
import test.wengelef.twitterfeed.locationtrends.domain.TwitterFeedInteractorImpl

class LocationTrendsFragment : Fragment(), LocationTrendsView {

    companion object {
        const val REQUEST_LOCATION_ID = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var presenter: LocationTrendsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fr_feed_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        presenter = LocationTrendsPresenter(
                TwitterFeedInteractorImpl(
                        WoedServiceImpl(context!!.applicationContext),
                        TwitterTrendsServiceImpl(context!!.applicationContext),
                        AuthenticationRepository(AuthenticationServiceImpl(context!!.applicationContext))
                )
        )

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_ID)
            }
        } else {
            onLocationPermissionGranted()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.start(this)
    }

    override fun onStop() {
        presenter.destroy()
        super.onStop()
    }

    @SuppressLint("MissingPermission")
    private fun onLocationPermissionGranted() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        presenter.onLocationError()
                    } else {
                        presenter.onLocation(location.latitude.toString(),
                                location.longitude.toString())
                    }
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onLocationPermissionGranted()
                }
            }
            else -> {
                // dont't know, don't care
            }
        }
    }

    override fun showLocationError() {
        Snackbar.make(root, "Turn on Location", Snackbar.LENGTH_LONG).show()
    }

    override fun showTrends(trends: List<TrendItem>) {
        Snackbar.make(root, "Trends Success!", Snackbar.LENGTH_LONG).show()
    }

    override fun showError() {
        Snackbar.make(root, "Unexpected Error", Snackbar.LENGTH_LONG).show()
    }

    override fun showAuthError() {
        Snackbar.make(root, "Invalid Authentication", Snackbar.LENGTH_LONG).show()
    }
}