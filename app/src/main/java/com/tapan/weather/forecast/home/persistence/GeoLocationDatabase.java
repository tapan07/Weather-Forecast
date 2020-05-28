package com.tapan.weather.forecast.home.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.tapan.weather.forecast.core.util.CoreConstants;

@Database(entities = { GeoLocation.class }, version = 1)
public abstract class GeoLocationDatabase extends RoomDatabase {

  private static GeoLocationDatabase geoLocationDatabase;

  public static GeoLocationDatabase getInstance(Context context) {
    if (geoLocationDatabase == null) {
      geoLocationDatabase = buildDatabaseInstance(context);
    }
    return geoLocationDatabase;
  }

  private static GeoLocationDatabase buildDatabaseInstance(Context context) {
    return Room.databaseBuilder(context,
        GeoLocationDatabase.class,
        CoreConstants.DB_NAME)
        .allowMainThreadQueries().build();
  }

  public abstract GeoLocationDao getGeoLocationDao();

  public void cleanUp() {
    geoLocationDatabase = null;
  }
}
