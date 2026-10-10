package au.com.krnyj.weighttracker.db.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import au.com.krnyj.weighttracker.db.entity.LogEntry

@Dao
interface LogEntryDao {
    @Query("SELECT * FROM log_entry")
    suspend fun getAll(): List<LogEntry>

    @Insert
    suspend fun insertAll(vararg logEntries: LogEntry)

    @Delete
    suspend fun delete(user: LogEntry)
}