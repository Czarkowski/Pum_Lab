package com.example.pumlab7

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: Long,
    val date: Date,
    val difficulty: Int
)

@Dao
interface ScoreDao {
    @Insert
    fun insert(score: Score)
    @Query("SELECT * FROM Score ORDER BY difficulty Desc, time ASC LIMIT 10")
    fun getBestScores(): LiveData<List<Score>>
}

@Database(entities = [Score::class], version = 2)
@TypeConverters(Converters::class)
abstract class ScoreDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        private var INSTANCE: ScoreDatabase? = null

        fun getDatabase(context: Context): ScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "score_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
        fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }
    @TypeConverter
        fun dateToTimestamp(date: Date?): Long? = date?.time
}
