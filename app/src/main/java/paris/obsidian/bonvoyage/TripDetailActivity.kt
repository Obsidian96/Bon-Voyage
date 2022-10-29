package paris.obsidian.bonvoyage

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
import paris.obsidian.bonvoyage.days.Day
import paris.obsidian.bonvoyage.days.DayAdapter
import paris.obsidian.bonvoyage.days.DaysListViewModel
import paris.obsidian.bonvoyage.days.DaysListViewModelFactory
import paris.obsidian.bonvoyage.trips.Trip
import paris.obsidian.bonvoyage.trips.TripDao
import java.util.*

private const val TAG = "TripDetailActivity"

class TripDetailActivity : AppCompatActivity() {

    lateinit var trip : Trip
    lateinit var recyclerView: RecyclerView
    lateinit var dayAdapter: DayAdapter

    var initialized = 0;

    private val daysListViewModel by viewModels<DaysListViewModel> {
        DaysListViewModelFactory(this, intent.getIntExtra("id", 0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val beginDate : TextView = findViewById(R.id.beginDate)
        val endDate : TextView = findViewById(R.id.endDate)
        val tripName : TextView = findViewById(R.id.tripName)
        val closeButton: ImageButton = findViewById(R.id.closeButton)
        val imageViewBackground: ImageView = findViewById(R.id.imageViewBackground)

        recyclerView = findViewById(R.id.tripDaysContainer)

        recyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)

        //Empty for loading
        recyclerView.adapter = DayAdapter(Trip(), onTextEdited = {},  onClick = {}, onRemoveClick = {}, onAddClick = {})

        val owner = this

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = DatabaseClient.getInstance(applicationContext)

                val tripDao: TripDao = db.tripDao()
                trip = tripDao.findByID(intent.getIntExtra("id", 0))

                withContext(Dispatchers.Main) {
                    Glide.with(applicationContext).load(trip.image)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 4)))
                        .into(imageViewBackground)

                    tripName.text = getString(R.string.trip_to, trip.name)
                    beginDate.text = getString(R.string.trip_start, trip.dateBegin)
                    endDate.text = getString(R.string.trip_end, trip.dateEnd)
                }
            }
            dayAdapter = DayAdapter(
                trip,
                onTextEdited = { adapterOnEdit(it)},
                onClick = {day -> adapterOnClick(day)},
                onRemoveClick = {day -> adapterRemove(day)},
                onAddClick = {
                    it.tripID = trip.id
                    it.dayNumber = daysListViewModel.getDayNumberForNewDay()
                    adapterAdd(it)

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            refresh()
                        }
                    }, 500)
                })

            recyclerView.adapter = dayAdapter

            daysListViewModel.daysData.observe(owner) {
                it?.let {
                    dayAdapter.submitList(it as MutableList<Day>)
                    recyclerView.post {
                        if (daysListViewModel.getDayCount() == 0 && initialized >= 1 || initialized >= 2) //Stupid workaround First number is for add a day, second one is for the rest
                            recyclerView.scrollToPosition(daysListViewModel.getDayCount() - 1)
                        else
                            initialized += 1
                    }
                }
            }

            Log.v(TAG, "Initialization finished");
        }

        closeButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun refresh() {
        runOnUiThread {
            recyclerView.scrollToPosition(daysListViewModel.getDayCount() - 1)
        }
    }

    private fun adapterOnClick(day: Day) {
    }

    private fun adapterOnEdit(day: Day) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = DatabaseClient.getInstance(applicationContext)
                db.dayDao().updateOne(day)
            }
        }
    }

    private fun adapterAdd(day: Day) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = DatabaseClient.getInstance(applicationContext)
                db.dayDao().insertOne(day)
            }
        }
        daysListViewModel.insertDay(day)
        dayAdapter.notifyItemInserted(day.dayNumber)
        dayAdapter.notifyItemRangeChanged(day.dayNumber, dayAdapter.itemCount - day.dayNumber);
        Log.v(TAG, "Item added: " + day.type + " number" + day.dayNumber + " (id: " + day.id + ")");
    }

    private fun adapterRemove(day: Day) {
        if (day.id != 0L) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = DatabaseClient.getInstance(applicationContext)
                    db.dayDao().removeOne(day)
                }
            }
            daysListViewModel.deleteDay(day)
            dayAdapter.notifyItemRemoved(day.dayNumber)
            dayAdapter.notifyItemRangeChanged(day.dayNumber, dayAdapter.itemCount - day.dayNumber);
            Log.v(TAG, "Item removed: " + day.type + " number" + day.dayNumber + " (id: " + day.id + ")");
        }
    }
}