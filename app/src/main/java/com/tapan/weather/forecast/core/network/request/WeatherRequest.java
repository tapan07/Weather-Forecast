package com.tapan.weather.forecast.core.network.request;

public class WeatherRequest {

  private double latitude;
  private double longitude;

  private WeatherRequest(Builder builder) {
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public static class Builder {

    private double latitude;
    private double longitude;

    public Builder() {
    }

    public Builder setLatitude(double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder setLongitude(double longitude) {
      this.longitude = longitude;
      return this;
    }

    public WeatherRequest build() {
      return new WeatherRequest(this);
    }
  }
}
