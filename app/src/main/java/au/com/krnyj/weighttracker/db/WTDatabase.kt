package au.com.krnyj.weighttracker.db

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import au.com.krnyj.weighttracker.db.dao.LogEntryDao
import au.com.krnyj.weighttracker.db.entity.LogEntry

@Database(entities = [LogEntry::class], version = 1, exportSchema = true)
@ColumnTypeConverters(WTConverters::class)
abstract class WTDatabase : RoomDatabase() {
    abstract fun logEntryDao(): LogEntryDao
}