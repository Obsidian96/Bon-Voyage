package paris.obsidian.bonvoyage.trips

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TripDataSource (resources: Resources) {
    private val initialTripList =  Trips().getList(resources)
    private val tripsLiveData = MutableLiveData(initialTripList)

    /* Adds Trip to liveData and posts value. */
    fun addTrip(trip: Trip) {
        val currentList = tripsLiveData.value
        if (currentList == null) {
            tripsLiveData.postValue(listOf(trip))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, trip)
            tripsLiveData.postValue(updatedList)
        }
    }

    /* Removes Trip from liveData and posts value. */
    fun removeTrip(trip: Trip) {
        val currentList = tripsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(trip)
            tripsLiveData.postValue(updatedList)
        }
    }

    /* Returns Trip given an ID. */
    fun getTripForId(id: Int): Trip? {
        tripsLiveData.value?.let { trips ->
            return@let trips.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getTripList(): LiveData<List<Trip>> {
        return tripsLiveData
    }
    companion object {
        private var INSTANCE: TripDataSource? = null

        fun getTripDataSource(resources: Resources): TripDataSource {
            return synchronized(TripDataSource::class) {
                val newInstance = INSTANCE ?: TripDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}