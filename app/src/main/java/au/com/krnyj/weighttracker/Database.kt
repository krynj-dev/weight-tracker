package au.com.krnyj.weighttracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import au.com.krnyj.weighttracker.db.WTDatabase

class Database private constructor() {
    companion object {
        @Volatile
        private var instance: WTDatabase? = null

        @Composable
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder<WTDatabase>(LocalContext.current, "database-name")
                .setDriver(AndroidSQLiteDriver()).build()
        }
    }
}