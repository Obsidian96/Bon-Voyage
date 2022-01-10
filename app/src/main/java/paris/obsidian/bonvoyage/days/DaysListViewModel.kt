package paris.obsidian.bonvoyage.days


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import paris.obsidian.bonvoyage.R
import paris.obsidian.bonvoyage.days.Day
import paris.obsidian.bonvoyage.days.DayDataSource
import java.util.*

class DaysListViewModel(val dayDataSource: DayDataSource) : ViewModel() {

    val daysLiveData = dayDataSource.getDayList()

    /* If the name and description are present, create new Day and add it to the datasource */
    fun insertDay(dayID: Int = getDayCount(), number: Int = 0, date: String = Date().toString()) {

        val newId : Int = dayID + 1

        val newDay = Day(newId,
            number,
            date,
            date,
            "day"
        )

        dayDataSource.addDay(newDay)
    }

    fun removeDay(day: Day? ) {
        if (day == null) {
            return
        }

        dayDataSource.removeDay(day)
    }

    fun getDayCount() : Int{
        return dayDataSource.getDayCount()
    }
}

class DaysListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DaysListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DaysListViewModel(
                dayDataSource = DayDataSource.getDayDataSource(context.resources, context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}