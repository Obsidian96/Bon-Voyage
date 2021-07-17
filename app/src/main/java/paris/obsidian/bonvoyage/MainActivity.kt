package paris.obsidian.bonvoyage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import paris.obsidian.bonvoyage.trips.*

class MainActivity : AppCompatActivity() {

    private val tripsListViewModel by viewModels<TripsListViewModel> {
        TripsListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tripAdapter = TripAdapter {trip -> adapterOnClick(trip) }

        val recyclerView: RecyclerView = findViewById(R.id.countryRecycler)
        recyclerView.adapter = tripAdapter

        tripsListViewModel.tripsLiveData.observe(this, {
            it?.let {
                tripAdapter.submitList(it as MutableList<Trip>)
            }
        })

        //recyclerView.setLayoutManager(new LinearLayoutManager(this){
        //    @Override
        //    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        //        // force height of viewHolder here, this will override layout_height from xml
        //        lp.height = getHeight() / 3;
        //        return true;
        //    }
        //});
    }

    private fun adapterOnClick(trip: Trip) {

        if (trip.id == 0) {
            //Do something different
        }
        else {
            val intent = Intent(this, TripDetailActivity()::class.java)
            intent.putExtra("id", trip.id)
            startActivity(intent)
        }
    }
}