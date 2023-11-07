package com.example.project_mobile.models;

public class Observation {
    private long obsId;
    private long hikeId;
    private String obsName;
    private String obsTime;
    private String obsComment;

    public Observation() {
    }

    public Observation(long obsId, long hikeId, String obsName, String obsTime, String obsComment) {
        this.obsId = obsId;
        this.hikeId = hikeId;
        this.obsName = obsName;
        this.obsTime = obsTime;
        this.obsComment = obsComment;
    }

    public long getObsId() {
        return obsId;
    }

    public void setObsId(long obsId) {
        this.obsId = obsId;
    }

    public long getHikeId() {
        return hikeId;
    }

    public void setHikeId(long hikeId) {
        this.hikeId = hikeId;
    }

    public String getObsName() {
        return obsName;
    }

    public void setObsName(String obsName) {
        this.obsName = obsName;
    }

    public String getObsTime() {
        return obsTime;
    }

    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }

    public String getObsComment() {
        return obsComment;
    }

    public void setObsComment(String obsComment) {
        this.obsComment = obsComment;
    }
}