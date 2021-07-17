package paris.obsidian.bonvoyage.trips

import android.graphics.drawable.Drawable
import android.media.Image
import java.util.*

data class Trip(var id: Int = 0,
                var name: String=  "",
                var date: Date = Date(),
                var image: Int = 0
) {}
