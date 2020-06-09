package com.tapan.weather.forecast.home.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tapan.weather.forecast.core.util.CoreConstants

@Database(entities = [GeoLocation::class], version = 1)
abstract class GeoLocationDatabase : RoomDatabase() {
  abstract val geoLocationDao: GeoLocationDao?

  fun cleanUp() {
    geoLocationDatabase = null
  }

  companion object {
    private var geoLocationDatabase: GeoLocationDatabase? = null

    fun getInstance(context: Context): GeoLocationDatabase? {
      if (geoLocationDatabase == null) {
        synchronized(GeoLocationDatabase::class) {
          geoLocationDatabase = buildDatabaseInstance(context)
        }
      }
      return geoLocationDatabase
    }

    private fun buildDatabaseInstance(context: Context): GeoLocationDatabase {
      return Room.databaseBuilder(
          context,
          GeoLocationDatabase::class.java,
          CoreConstants.DB_NAME
      )
          .allowMainThreadQueries()
          .build()
    }
  }
}