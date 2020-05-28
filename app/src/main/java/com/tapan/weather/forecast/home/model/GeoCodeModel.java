package com.tapan.weather.forecast.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GeoCodeModel implements Parcelable {

  public static final Parcelable.Creator<GeoCodeModel> CREATOR =
      new Parcelable.Creator<GeoCodeModel>() {
        @Override
        public GeoCodeModel createFromParcel(Parcel source) {
          return new GeoCodeModel(source);
        }

        @Override
        public GeoCodeModel[] newArray(int size) {
          return new GeoCodeModel[size];
        }
      };
  private String featureName;
  private String locality;
  private String state;
  private String country;
  private String pinCode;
  private double latitude;
  private double longitude;

  public GeoCodeModel() {
  }

  protected GeoCodeModel(Parcel in) {
    this.featureName = in.readString();
    this.locality = in.readString();
    this.state = in.readString();
    this.country = in.readString();
    this.pinCode = in.readString();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
  }

  public static Creator<GeoCodeModel> getCREATOR() {
    return CREATOR;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.featureName);
    dest.writeString(this.locality);
    dest.writeString(this.state);
    dest.writeString(this.country);
    dest.writeString(this.pinCode);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
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
}
