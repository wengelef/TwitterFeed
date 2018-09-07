package test.wengelef.twitterfeed

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import test.wengelef.twitterfeed.locationtrends.view.LocationTrendsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LocationTrendsFragment())
                .commit()
    }
}