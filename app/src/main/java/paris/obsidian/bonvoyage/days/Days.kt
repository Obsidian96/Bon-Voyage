package paris.obsidian.bonvoyage.days

import android.content.res.Resources

//Sample for hard coded data
class Days {

    /* Returns initial list of trips. */
    fun getList(resources: Resources): List<Day> {
        return listOf(
            Day(
                id = 0,
                type = "day",
                content = "",
                hotel = "",
                dayNumber = 0,
                tripID = 0
            )
        )
    }
}