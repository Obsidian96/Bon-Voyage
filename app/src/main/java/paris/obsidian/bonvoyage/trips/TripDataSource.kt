package paris.obsidian.bonvoyage.trips

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.DatabaseClient

private const val TAG = "TripDataSource"

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
            Log.v(TAG, "Trip added to Datasource");
        }
    }

    /* Removes Trip from liveData and posts value. */
    fun removeTrip(trip: Trip) {
        val currentList = tripsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(trip)
            tripsLiveData.postValue(updatedList)
            Log.v(TAG, "Trip removed from Datasource");
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

    fun getTripList(refresh : Boolean): LiveData<List<Trip>> {

        if (tripsLiveData.value!!.size <= 1 || refresh) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val db = DatabaseClient.getInstance(gctx)

                    val tripDao : TripDao = db.tripDao()
                    val listOfTrips = tripDao.getAll()
                    if (listOfTrips.isNotEmpty()) {
                        val list : MutableList<Trip> = mutableListOf()
                        tripsLiveData.postValue(list)
                        list.add(initialTripList[0])
                        list.addAll(listOfTrips)
                        tripsLiveData.postValue(list)
                        Log.v(TAG, "List of trips retrieved");
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