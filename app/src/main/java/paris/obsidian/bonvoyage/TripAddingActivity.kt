package paris.obsidian.bonvoyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.DatePickerDialog
import android.content.res.Resources
import android.view.View
import android.widget.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.trips.Trip
import paris.obsidian.bonvoyage.trips.TripDao
import java.text.SimpleDateFormat
import java.util.*


class TripAddingActivity : AppCompatActivity() {

    private lateinit  var editBeginDate: TextView
    private lateinit var editEndDate: TextView
    private lateinit var spinner: Spinner
    private lateinit var imagePreview: ImageView
    var lastKnownPosition = 0

    //In case of double tap - ignore multiple creation of trips
    var softLock = false

    @Database(entities = [Trip::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun tripDao(): TripDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        editBeginDate = findViewById(R.id.editBeginDate)
        editEndDate = findViewById(R.id.editEndDate)
        spinner = findViewById(R.id.countrySpinner)
        imagePreview = findViewById(R.id.imagePreview)

        val closeButton: ImageButton = findViewById(R.id.closeButton)
        val validateButton: Button = findViewById(R.id.validateButton)
        val cal = Calendar.getInstance()

        closeButton.setOnClickListener {
            super.onBackPressed()
        }

        validateButton.setOnClickListener {
            validateNewTrip(lastKnownPosition)
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.country_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lastKnownPosition = position
                selectImageBasedOnCountry(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val dateFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.FRANCE)
        editBeginDate.text = sdf.format(System.currentTimeMillis()).toString()
        cal.add(Calendar.DATE, 1)
        editEndDate.text = sdf.format(cal.time)

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editBeginDate.text = sdf.format(cal.time)
        }
        val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editEndDate.text = sdf.format(cal.time)
        }

        editBeginDate.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        editEndDate.setOnClickListener {
            DatePickerDialog(this, dateSetListener2,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun validateNewTrip(position: Int = 0) {
        val country = resources.getStringArray(R.array.country_array)[position]
        val dateBegin = editBeginDate.text.toString()
        val dateEnd = editEndDate.text.toString()

        if (softLock)
            return

        softLock = true
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "bonVoyage.db"
                ).build()
                val tripDao : TripDao = db.tripDao()
                val trip = Trip()
                trip.name = country
                trip.dateBegin = dateBegin
                trip.dateEnd = dateEnd

                var resID = resources.getIdentifier(country, "drawable", packageName)
                if (resID == 0)
                    resID = R.drawable.iceland

                trip.image = resID
                tripDao.insertOne(trip)
                super.onBackPressed()
            }
        }

    }

    fun selectImageBasedOnCountry(position: Int = 0) {

        val country = resources.getStringArray(R.array.country_array)[position].lowercase()
        var resID = resources.getIdentifier(country, "drawable", packageName)

        if (resID == 0)
            resID = R.drawable.iceland

        Glide.with(this).load(resID)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 2)))
            .into(imagePreview)
    }
}