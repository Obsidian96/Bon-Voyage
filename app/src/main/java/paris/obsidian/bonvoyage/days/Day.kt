package paris.obsidian.bonvoyage.days

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Day (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo (name = "type")  var type: String = "", //Values: Trip(transportation)/Day/...
    @ColumnInfo (name = "content")  var content: String = "", //What's in the day
    @ColumnInfo (name = "hotel")  var hotel: String = "", //Where to sleep (Lat/Lon)
    @ColumnInfo (name = "dayNumber")  var dayNumber: Int = 0, //Which day is it in the trip
    @ColumnInfo (name = "tripID")  var tripID: Int = 0 ///To which trip is it linked to
) {
}
