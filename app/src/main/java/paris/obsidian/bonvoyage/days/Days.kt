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
                dateBegin = "01/01/1970",
                dateEnd = "31/12/2012",
                type = "day"
            )
        )
    }
}