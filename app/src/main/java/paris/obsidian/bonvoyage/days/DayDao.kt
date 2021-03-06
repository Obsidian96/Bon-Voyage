package paris.obsidian.bonvoyage.days

import androidx.room.*

@Dao
interface DayDao {
    @Query("SELECT * FROM day WHERE tripID LIKE:tripID")
    fun getAll(tripID: Int): List<Day>

    @Query("SELECT * FROM day WHERE id LIKE :id LIMIT 1")
    fun findByID(id: Int): Day

    @Insert
    fun insertAll(vararg days: Day)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(day: Day): Long

    @Update
    fun updateOne(day: Day)

    @Delete
    fun removeOne(day: Day)
}