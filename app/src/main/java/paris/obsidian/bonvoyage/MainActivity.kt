package paris.obsidian.bonvoyage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.trips.*

private const val TAG = "MainActivity"

class MainActivity: AppCompatActivity() {

    private val tripsListViewModel by viewModels<TripsListViewModel> {
        TripsListViewModelFactory(this)
    }

    lateinit var tripAdapter : TripAdapter

    override fun onResume() {
        super.onResume()

        tripsListViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tripAdapter = TripAdapter(onClick = {trip -> adapterOnClick(trip)},
            onRemoveClick = {trip -> adapterRemove(trip)})

        val recyclerView: RecyclerView = findViewById(R.id.countryRecycler)
        recyclerView.adapter = tripAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

       tripsListViewModel.tripsLiveData.observe(this) {
            it?.let {
                tripAdapter.submitList(it as MutableList<Trip>)
            }
        }

        Log.v(TAG, "Main activity loaded");

        window.setBackgroundDrawableResource(R.drawable.main_background)
    }

    private fun adapterOnClick(trip: Trip) {
        if (trip.id == 0) {
            Log.v(TAG, "Starting new trip form");
            val intent = Intent(this, TripAddingActivity()::class.java)
            startActivity(intent)
        }
        else {
            Log.v(TAG, "Going to trip: ${trip.name} (id: ${trip.id})");
            val intent = Intent(this, TripDetailActivity()::class.java)
            intent.putExtra("id", trip.id)
            startActivity(intent)
        }
    }

    private fun adapterRemove(trip: Trip) {
        if (trip.id != 0) {

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = DatabaseClient.getInstance(applicationContext)
                    val tripDao : TripDao = db.tripDao()
                    tripDao.removeOne(trip)
                }
            }
            tripsListViewModel.removeTrip(trip)
            Log.v(TAG, "Trip removed: ${trip.name} (id: ${trip.id})");
        }
    }
}