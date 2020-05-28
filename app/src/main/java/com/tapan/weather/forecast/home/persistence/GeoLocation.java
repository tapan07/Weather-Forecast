package com.tapan.weather.forecast.home.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "GeoLocation")
public class GeoLocation implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "latitude")
  private double latitude;

  @ColumnInfo(name = "longitude")
  private double longitude;

  @ColumnInfo(name = "featurename")
  private String featureName;

  @ColumnInfo(name = "locality")
  private String locality;

  @ColumnInfo(name = "state")
  private String state;

  @ColumnInfo(name = "country")
  private String country;

  @ColumnInfo(name = "pincode")
  private String pinCode;

  @ColumnInfo(name = "temperature")
  private String temperature;

  @ColumnInfo(name = "overalltemperature")
  private String overAllTemperature;

  @ColumnInfo(name = "description")
  private String description;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getOverAllTemperature() {
    return overAllTemperature;
  }

  public void setOverAllTemperature(String overAllTemperature) {
    this.overAllTemperature = overAllTemperature;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
