package paris.obsidian.bonvoyage.days

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DayDao {
    @Query("SELECT * FROM day")
    fun getAll(): List<Day>

    @Query("SELECT * FROM day WHERE id LIKE :id LIMIT 1")
    fun findByID(id: Int): Day

    @Insert
    fun insertAll(vararg days: Day)

    @Insert
    fun insertOne(day: Day)

    @Delete
    fun removeOne(user: Day)
}