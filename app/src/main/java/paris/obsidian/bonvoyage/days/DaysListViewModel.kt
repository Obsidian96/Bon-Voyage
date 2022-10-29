package paris.obsidian.bonvoyage.days



import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import paris.obsidian.bonvoyage.TripDetailActivity
import java.util.*

class DaysListViewModel(val dayDataSource: DayDataSource) : ViewModel() {

    val daysData = dayDataSource.getDayList()

    /* If the name and description are present, create new Day and add it to the datasource */
    fun insertDay(day: Day?) {

        val newId : Long = getDayCount() + 1L
        val newDay : Day

        if (day != null) {
            newDay = Day(newId,
                day.type,
                day.content,
                day.hotel,
                day.dayNumber,
                day.tripID
            )
        }
        else {
            newDay = Day(newId,
                "day",
                "",
                "",
                day?.dayNumber!!,
                0
            )
        }

        dayDataSource.addDay(newDay)
    }

    fun deleteDay(day: Day? ) {
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

        var normalDayNumber = 1
        daysData.value?.forEach {
            if (it.type == "day" && it.id != 0L)
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