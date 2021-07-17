package paris.obsidian.bonvoyage.trips

import android.content.res.Resources
import paris.obsidian.bonvoyage.R

//Sample for hard coded data
class Trips {

    /* Returns initial list of flowers. */
    fun getList(resources: Resources): List<Trip> {
        return listOf(
            Trip(
                id = 1,
                name = resources.getString(R.string.addTrip),
                image = R.drawable.ic_launcher_foreground
            ),
            Trip(
                id = 2,
                name = resources.getString(R.string.iceland),
                image = R.drawable.ic_launcher_foreground
            )
        )
    }
}