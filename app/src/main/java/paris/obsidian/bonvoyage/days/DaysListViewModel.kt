package paris.obsidian.bonvoyage.days


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import paris.obsidian.bonvoyage.R
import paris.obsidian.bonvoyage.days.Day
import paris.obsidian.bonvoyage.days.DayDataSource
import paris.obsidian.bonvoyage.trips.Trip
import java.util.*

class DaysListViewModel(val dayDataSource: DayDataSource) : ViewModel() {

    val daysLiveData = dayDataSource.getDayList()

    /* If the name and description are present, create new Day and add it to the datasource */
    fun insertDay(dayID: Int = getDayCount(), number: Int = 0, date: String = Date().toString()) {

        val newId : Int = dayID + 1

        val newDay = Day(newId,
            "day",
            "",
            "",
            0,
            0
        )

        dayDataSource.addDay(newDay)
    }

    fun removeDay(day: Day? ) {
        if (day == null) {
            return
        }

        dayDataSource.removeDay(day)
    }

    fun updateDay(day: Day? ) {
        if (day == null) {
            return
        }
    }

    fun getDayCount() : Int{
        return dayDataSource.getDayCount()
    }

    fun getDayNumberForNewDay() : Int {
        //If only the placeHolder is present, then it's the first day (day 1 in human language)
        if (getDayCount() <= 1)
            return 1

        var normalDayNumber = 0
        daysLiveData.value?.forEach {
            if (normalDayNumber != it.dayNumber)
                return normalDayNumber + 1
            //if (it.type == "day")
                ++normalDayNumber
        }
        return normalDayNumber
    }
}

class DaysListViewModelFactory(private val context: Context, private val id: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DaysListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DaysListViewModel(
                dayDataSource = DayDataSource.getDayDataSource(context.resources, context, id)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}