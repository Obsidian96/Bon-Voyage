package paris.obsidian.bonvoyage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import paris.obsidian.bonvoyage.days.Day
import paris.obsidian.bonvoyage.days.DayDao
import paris.obsidian.bonvoyage.trips.Trip
import paris.obsidian.bonvoyage.trips.TripDao

@Database(entities = [Trip::class, Day::class], version = 2)
abstract class DatabaseClient : RoomDatabase() {

        abstract fun tripDao(): TripDao
        abstract fun dayDao(): DayDao

        companion object {
                private var instance: DatabaseClient? = null

                @Synchronized
                fun getInstance(ctx: Context): DatabaseClient {
                        if(instance == null)
                                instance = Room.databaseBuilder(ctx.applicationContext,
                                        DatabaseClient::class.java,
                                        "bonVoyage.db")
                                        .allowMainThreadQueries()
                                        .build()

                        return instance!!
                }
        }
}