package paris.obsidian.bonvoyage.trips

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo (name = "name") var name: String = "",
    @ColumnInfo (name = "dateBegin") var dateBegin: String = Date().toString(),
    @ColumnInfo (name = "dateEnd") var dateEnd: String = Date().toString(),
    @ColumnInfo (name = "image")  var image: Int = 0
) {}
