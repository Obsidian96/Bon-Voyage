package paris.obsidian.bonvoyage.trips

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAll(): List<Trip>

    @Query("SELECT * FROM trip WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Trip

    @Query("SELECT * FROM trip WHERE id LIKE :id LIMIT 1")
    fun findByID(id: Int): Trip

    @Insert
    fun insertAll(vararg trips: Trip)

    @Insert
    fun insertOne(trip: Trip) : Long

    @Delete
    fun removeOne(trip: Trip)
}