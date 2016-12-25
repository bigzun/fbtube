package com.bigzun.video.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String TAG = LocationModel.class.getSimpleName();

    @SerializedName("ip")
    private String ip;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("country_name")
    private String countryName;

    @SerializedName("region_code")
    private String regionCode;

    @SerializedName("region_name")
    private String regionName;

    @SerializedName("city")
    private String city;

    @SerializedName("zip_code")
    private String zipCode;

    @SerializedName("time_zone")
    private String timeZone;

    @SerializedName("latitude")
    private float latitude;
    @SerializedName("longitude")
    private float longitude;

    @SerializedName("metro_code")
    private int metroCode;

    /**
     * @return The ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip The ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return The countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode The country_code
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return The countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName The country_name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return The regionCode
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * @param regionCode The region_code
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * @return The regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName The region_name
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode The zip_code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone The time_zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return The latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The metroCode
     */
    public Integer getMetroCode() {
        return metroCode;
    }

    /**
     * @param metroCode The metro_code
     */
    public void setMetroCode(Integer metroCode) {
        this.metroCode = metroCode;
    }

    @Override
    public String toString() {
        return TAG + "{ "
                + "ip: " + ip
                + ", countryCode: " + countryCode
                + ", countryName: " + countryName
                + ", regionCode: " + regionCode
                + ", regionName: " + regionName
                + ", city: " + city
                + ", zipCode: " + zipCode
                + ", timeZone: " + timeZone
                + ", latitude: " + latitude
                + ", longitude: " + longitude
                + ", metroCode: " + metroCode
                + " }";
    }
}