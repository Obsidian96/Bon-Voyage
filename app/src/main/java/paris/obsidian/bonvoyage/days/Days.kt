package paris.obsidian.bonvoyage.days

import android.content.res.Resources

//Sample for hard coded data
class Days {

    /* Returns initial list of trips. */
    fun getList(resources: Resources): List<Day> {
        return listOf(
            Day(
                id = 0,
                number = 0,
                dateBegin = "0",
                dateEnd = "0",
                type = "day"
            )
        )
    }
}