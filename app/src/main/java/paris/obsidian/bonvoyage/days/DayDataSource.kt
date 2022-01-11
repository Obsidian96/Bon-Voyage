package paris.obsidian.bonvoyage.days

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import paris.obsidian.bonvoyage.DatabaseClient

class DayDataSource (resources: Resources, ctx: Context) {
    private val initialDayList =  Days().getList(resources)
    private var daysLiveData = MutableLiveData(initialDayList)
    private val gctx = ctx

    @Database(entities = [Day::class], version = 2)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun dayDao(): DayDao
    }

    /* Adds Day to liveData and posts value. */
    fun addDay(day: Day) {
        val currentList = daysLiveData.value
        if (currentList == null) {
            daysLiveData.postValue(listOf(day))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.size
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
    fun getDayForId(id: Int): Day? {
        daysLiveData.value?.let { days ->
            return@let days.firstOrNull{ it.id == id}
        }
        return null
    }

     fun getDayCount() : Int {
         val currentList = daysLiveData.value
         return if (currentList != null) {
             val updatedList = currentList.toMutableList()
             updatedList.size
         } else
             0
     }

    fun getDayList(): LiveData<List<Day>> {

        if (daysLiveData.value!!.size <= 1) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {

                    val db = DatabaseClient.getInstance(gctx)

                    val dayDao : DayDao = db.dayDao()
                    val listOfDays = dayDao.getAll()
                    if (listOfDays.isNotEmpty()) {
                        val currentList = daysLiveData.value
                        val updatedList = currentList?.toMutableList()
                        updatedList?.size
                        updatedList?.addAll(listOfDays)
                        daysLiveData.postValue(updatedList)
                    }
                }
            }
        }

        return daysLiveData
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: DayDataSource? = null

        fun getDayDataSource(resources: Resources, ctx: Context): DayDataSource {
            return synchronized(DayDataSource::class) {
                val newInstance = INSTANCE ?: DayDataSource(resources, ctx)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}