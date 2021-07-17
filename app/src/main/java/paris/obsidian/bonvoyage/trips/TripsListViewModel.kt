package paris.obsidian.bonvoyage.trips


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import paris.obsidian.bonvoyage.R
import java.util.*
import kotlin.random.Random

class TripsListViewModel(val tripDataSource: TripDataSource) : ViewModel() {

    val tripsLiveData = tripDataSource.getTripList()

    /* If the name and description are present, create new Trip and add it to the datasource */
    fun insertTrip(tripName: String?, tripDescription: String?) {
        if (tripName == null || tripDescription == null) {
            return
        }
        
        val newTrip = Trip(2,
            tripName,
            Date(),
            R.drawable.ic_launcher_background
        )

        tripDataSource.addTrip(newTrip)
    }
}

class TripsListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripsListViewModel(
                tripDataSource = TripDataSource.getTripDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}