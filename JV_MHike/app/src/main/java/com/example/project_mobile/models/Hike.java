package com.example.project_mobile.models;

public class Hike {
    private long hikeId;
    private String hikeName;
    private String locationHike;
    private String dateHike;
    private String parkingAvailable;
    private String hikeLength;
    private String hikeLevel;
    private String hikeDescription;

    public Hike() {
    }

    public Hike(long hikeId, String hikeName, String locationHike, String dateHike, String parkingAvailable, String hikeLength, String hikeLevel, String hikeDescription) {
        this.hikeId = hikeId;
        this.hikeName = hikeName;
        this.locationHike = locationHike;
        this.dateHike = dateHike;
        this.parkingAvailable = parkingAvailable;
        this.hikeLength = hikeLength;
        this.hikeLevel = hikeLevel;
        this.hikeDescription = hikeDescription;
    }

    public long getHikeId() {
        return hikeId;
    }

    public void setHikeId(long hikeId) {
        this.hikeId = hikeId;
    }

    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public String getLocationHike() {
        return locationHike;
    }

    public void setHikeLocation(String locationHike) {
        this.locationHike = locationHike;
    }

    public String getDateHike() {
        return dateHike;
    }

    public void setDateHike(String dateHike) {
        this.dateHike = dateHike;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getHikeLength() {
        return hikeLength;
    }

    public void setHikeLength(String hikeLength) {
        this.hikeLength = hikeLength;
    }

    public String getHikeLevel() {
        return hikeLevel;
    }

    public void setHikeLevel(String hikeLevel) {
        this.hikeLevel = hikeLevel;
    }

    public String getHikeDescription() {
        return hikeDescription;
    }

    public void setHikeDescription(String hikeDescription) {
        this.hikeDescription = hikeDescription;
    }
}