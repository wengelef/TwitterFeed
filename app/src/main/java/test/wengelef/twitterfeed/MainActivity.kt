package test.wengelef.twitterfeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import test.wengelef.twitterfeed.locationtrends.view.LocationTrendsFragment

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_FRAGMENT_TAG = "LocationTrendsFragment"
    }
    private lateinit var locationFragment: LocationTrendsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationFragment = if (savedInstanceState == null) {
            LocationTrendsFragment()
        } else {
            supportFragmentManager.findFragmentByTag(LOCATION_FRAGMENT_TAG) as LocationTrendsFragment
        }


        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, locationFragment, LOCATION_FRAGMENT_TAG)
                .commit()
    }
}