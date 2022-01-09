package paris.obsidian.bonvoyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.room.Database
import androidx.room.RoomDatabase
import paris.obsidian.bonvoyage.R
import paris.obsidian.bonvoyage.trips.Trip
import paris.obsidian.bonvoyage.trips.TripDao

class TripDetailActivity : AppCompatActivity() {

    @Database(entities = [Trip::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun tripDao(): TripDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val beginDate : TextView = findViewById(R.id.beginDate)
        val endDate : TextView = findViewById(R.id.endDate)
        val tripName : TextView = findViewById(R.id.tripName)
        val closeButton: ImageButton = findViewById(R.id.closeButton)

        closeButton.setOnClickListener {
            super.onBackPressed()
        }

    }
}