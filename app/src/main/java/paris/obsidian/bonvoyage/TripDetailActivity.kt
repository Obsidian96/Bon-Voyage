package paris.obsidian.bonvoyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.days.*
import paris.obsidian.bonvoyage.trips.*

class TripDetailActivity : AppCompatActivity() {

    lateinit var trip : Trip

    private val daysListViewModel by viewModels<DaysListViewModel> {
        DaysListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val beginDate : TextView = findViewById(R.id.beginDate)
        val endDate : TextView = findViewById(R.id.endDate)
        val tripName : TextView = findViewById(R.id.tripName)
        val closeButton: ImageButton = findViewById(R.id.closeButton)
        val imageViewBackground: ImageView = findViewById(R.id.imageViewBackground)

        val recyclerView: RecyclerView = findViewById(R.id.tripDaysContainer)

        val dayAdapter = DayAdapter(onClick = {day -> adapterOnClick(day)},
            onRemoveClick = {day -> adapterRemove(day)})
        recyclerView.adapter = dayAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)

        daysListViewModel.daysLiveData.observe(this, {
            it?.let {
                dayAdapter.submitList(it as MutableList<Day>)
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = DatabaseClient.getInstance(applicationContext)

                val tripDao : TripDao = db.tripDao()
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
        }

        closeButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun adapterOnClick(day: Day) {
        //
    }

    private fun adapterRemove(day: Day) {
        if (day.id != 0) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = DatabaseClient.getInstance(applicationContext)
                    db.dayDao().removeOne(day)
                }
            }
            daysListViewModel.removeDay(day)
        }
    }
}