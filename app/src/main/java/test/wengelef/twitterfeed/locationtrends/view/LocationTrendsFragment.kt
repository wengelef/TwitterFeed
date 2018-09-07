package test.wengelef.twitterfeed.locationtrends.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fr_feed_list.*
import kotlinx.android.synthetic.main.item_trend.view.*
import test.wengelef.twitterfeed.R
import test.wengelef.twitterfeed.locationtrends.data.AuthenticationRepository
import test.wengelef.twitterfeed.locationtrends.data.AuthenticationServiceImpl
import test.wengelef.twitterfeed.locationtrends.data.TrendItem
import test.wengelef.twitterfeed.locationtrends.data.WoedServiceImpl
import test.wengelef.twitterfeed.locationtrends.domain.TwitterFeedInteractorImpl

class LocationTrendsFragment : Fragment(), LocationTrendsView {

    companion object {
        const val REQUEST_LOCATION_ID = 1
        const val KEY_ITEMS_STATE = "key_items_state"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var presenter: LocationTrendsPresenter

    private lateinit var adapter: TrendsForLocationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fr_feed_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.apply {
            val items = getSerializable(KEY_ITEMS_STATE)
            adapter.items = items as List<TrendItem>
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_ITEMS_STATE, adapter.items as ArrayList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrendsForLocationAdapter()

        feed_recycler.layoutManager = LinearLayoutManager(context)
        feed_recycler.adapter = adapter
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
        adapter.items = trends
    }

    override fun showError() {
        Snackbar.make(root, "Unexpected Error", Snackbar.LENGTH_LONG).show()
    }

    override fun showAuthError() {
        Snackbar.make(root, "Invalid Authentication", Snackbar.LENGTH_LONG).show()
    }
}

class TrendsForLocationAdapter : RecyclerView.Adapter<TrendsForLocationAdapter.TrendsForLocationViewHolder>() {

    var items: List<TrendItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendsForLocationViewHolder {
        return TrendsForLocationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_trend, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrendsForLocationViewHolder, position: Int) {
        holder.itemView.apply {
            name.text = items[position].name
            url.text = items[position].url
            volume.text = items[position].volume.toString()
        }
    }

    inner class TrendsForLocationViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
