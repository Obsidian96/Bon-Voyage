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
    fun insertTrip(tripID: Int = getTripCount(), tripName: String?, date: String = Date().toString()) {
        if (tripName == null) {
            return
        }

        val newId : Int = tripID + 1

        val newTrip = Trip(newId,
            tripName,
            date,
            date,
            R.drawable.original
        )

        tripDataSource.addTrip(newTrip)
    }

    fun removeTrip(trip: Trip? ) {
        if (trip == null) {
            return
        }

        tripDataSource.removeTrip(trip)
    }

    fun getTripCount() : Int{
        return tripDataSource.getTripCount()
    }
}

class TripsListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripsListViewModel(
                tripDataSource = TripDataSource.getTripDataSource(context.resources, context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}