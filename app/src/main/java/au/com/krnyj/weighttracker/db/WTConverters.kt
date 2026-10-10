package au.com.krnyj.weighttracker.db

import androidx.room3.ColumnTypeConverter
import java.math.BigDecimal
import java.time.Instant

object WTConverters {

    @ColumnTypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @ColumnTypeConverter
    fun stringToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value)
    }

    @ColumnTypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @ColumnTypeConverter
    fun instantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
}
