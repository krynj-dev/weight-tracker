package au.com.krnyj.weighttracker.db.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime

@Entity(tableName = "log_entry")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name="date_time") val dateTime: Instant,
    @ColumnInfo(name="measurement") val measurement: BigDecimal,
)
