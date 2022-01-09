package paris.obsidian.bonvoyage.trips

import android.content.res.Resources
import paris.obsidian.bonvoyage.R

//Sample for hard coded data
class Trips {

    /* Returns initial list of trips. */
    fun getList(resources: Resources): List<Trip> {
        return listOf(
            Trip(
                id = 0,
                name = resources.getString(R.string.addTrip),
                dateBegin = "0",
                dateEnd = "0",
                image = R.drawable.ic_add
            )
        )
    }
}