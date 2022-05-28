package paris.obsidian.bonvoyage.days

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
import paris.obsidian.bonvoyage.trips.Trip
import paris.obsidian.bonvoyage.trips.Trips

class DayDataSource (resources: Resources, ctx: Context, id: Int) {

    private var daysLiveData : MutableLiveData<List<Day>> = MutableLiveData()
    private val gctx = ctx
    private val currentTrip = id
    private val initialDaysList = Days().getList(resources)

    /* Adds Day to liveData and posts value. */
    fun addDay(day: Day) {
        val currentList = daysLiveData.value
        if (currentList == null) {
            daysLiveData.postValue(listOf(day))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(day)
            daysLiveData.postValue(updatedList)
        }
    }

    /* Removes Day from liveData and posts value. */
    fun removeDay(day: Day) {
        val currentList = daysLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(day)
            daysLiveData.postValue(updatedList)
        }
    }

    /* Returns Day given an ID. */
    fun getDayForId(id: Long): Day? {
        daysLiveData.value?.let { days ->
            return@let days.firstOrNull{ it.id == id}
        }
        return null
    }

     fun getDayCount() : Int {
         val currentList = daysLiveData.value
         return if (currentList != null) {
             return currentList.size
         } else
             0
     }

    fun getDayList(): LiveData<List<Day>> {

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val db = DatabaseClient.getInstance(gctx)
                val dayDao : DayDao = db.dayDao()
                val listOfDays = dayDao.getAll(currentTrip)

                if (listOfDays.isNotEmpty()) {
                    val list : MutableList<Day> = mutableListOf()
                    daysLiveData.postValue(list)
                    list.add(initialDaysList[0])
                    list.addAll(listOfDays)
                    daysLiveData.postValue(list)
                }
            }
        }

        return daysLiveData
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: DayDataSource? = null

        fun getDayDataSource(resources: Resources, ctx: Context, id: Int): DayDataSource {
            return synchronized(DayDataSource::class) {
                val newInstance = INSTANCE ?: DayDataSource(resources, ctx, id)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}