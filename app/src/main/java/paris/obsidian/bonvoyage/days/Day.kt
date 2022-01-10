package paris.obsidian.bonvoyage.days

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Day(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo (name = "number") var number: Int = 0,
    @ColumnInfo (name = "dateBegin") var dateBegin: String = Date().toString(),
    @ColumnInfo (name = "dateEnd") var dateEnd: String = Date().toString(),
    @ColumnInfo (name = "Type")  var type: String = ""
) {}
