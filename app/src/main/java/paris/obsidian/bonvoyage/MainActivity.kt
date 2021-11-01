package paris.obsidian.bonvoyage

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.trips.*
import java.util.*

class MainActivity: AppCompatActivity() {

    @Database(entities = [Trip::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun tripDao(): TripDao
    }

    private val tripsListViewModel by viewModels<TripsListViewModel> {
        TripsListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tripAdapter = TripAdapter(onClick = {trip -> adapterOnClick(trip)},
            onRemoveClick = {trip -> adapterRemove(trip)})

        val recyclerView: RecyclerView = findViewById(R.id.countryRecycler)
        recyclerView.adapter = tripAdapter

        tripsListViewModel.tripsLiveData.observe(this, {
            it?.let {
                tripAdapter.submitList(it as MutableList<Trip>)
            }
        })

       /* val backgroundImage: ImageView = findViewById(R.id.imageViewBackground)
        Glide.with(this).load(R.drawable.airplane)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(1, 2)))
            .into(backgroundImage)*/

        recyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun adapterOnClick(trip: Trip) {


        if (trip.id == 0) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "bonVoyage"
                    ).build()
                    val tripDao : TripDao = db.tripDao()
                    trip.id = 1
                    trip.name = "Iceland"
                    trip.dateBegin = "1"// Date().toString()
                    trip.dateEnd = "2"//Date().toString()
                    trip.image = R.drawable.original
                    tripDao.insertOne(trip)
                }
            }

            tripsListViewModel.insertTrip(tripsListViewModel.getTripCount(), "Iceland", Date().toString())

            /*
            val intent = Intent(this, TripAddingActivity()::class.java)
            startActivity(intent)*/
        }
        else {
            val intent = Intent(this, TripDetailActivity()::class.java)
            intent.putExtra("id", trip.id)
            startActivity(intent)
        }
    }

    private fun adapterRemove(trip: Trip) {
        if (trip.id != 0) {

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "bonVoyage"
                    ).build()
                    val tripDao : TripDao = db.tripDao()
                    tripDao.removeOne(trip)
                }
            }
            tripsListViewModel.removeTrip(trip)
        }
    }
}