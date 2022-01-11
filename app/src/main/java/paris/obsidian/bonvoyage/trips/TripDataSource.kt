package paris.obsidian.bonvoyage.trips

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.DatabaseClient

class TripDataSource (resources: Resources, ctx: Context) {
    private val initialTripList =  Trips().getList(resources)
    private var tripsLiveData = MutableLiveData(initialTripList)
    private val gctx = ctx

    /* Adds Trip to liveData and posts value. */
    fun addTrip(trip: Trip) {
        val currentList = tripsLiveData.value
        if (currentList == null) {
            tripsLiveData.postValue(listOf(trip))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.size
            updatedList.add(trip)
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

     fun getTripCount() : Int {
         val currentList = tripsLiveData.value
         return if (currentList != null) {
             val updatedList = currentList.toMutableList()
             updatedList.size
         } else
             0
     }

    fun getTripList(): LiveData<List<Trip>> {

        if (tripsLiveData.value!!.size <= 1) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = DatabaseClient.getInstance(gctx)

                    val tripDao : TripDao = db.tripDao()
                    val listOfTrips = tripDao.getAll()
                    if (listOfTrips.isNotEmpty()) {
                        val currentList = tripsLiveData.value
                        val updatedList = currentList?.toMutableList()
                        updatedList?.size
                        updatedList?.addAll(listOfTrips)
                        tripsLiveData.postValue(updatedList)
                    }
                }
            }
        }

        return tripsLiveData
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: TripDataSource? = null

        fun getTripDataSource(resources: Resources, ctx: Context): TripDataSource {
            return synchronized(TripDataSource::class) {
                val newInstance = INSTANCE ?: TripDataSource(resources, ctx)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}