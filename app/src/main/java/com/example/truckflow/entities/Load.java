package com.example.truckflow.entities;

import java.io.Serializable;
import java.util.Date;

public class Load implements Serializable {

    private String id;
    private String additionalReq;
    private String contactInfo;
    private String currentLoc;
    private String dropLoc;
    private Date estArrivalTime;
    private String loadDesc;
    private String loadDims;
    private String loadName;
    private String loadWeight;
    private String pickupLoc;
    private float distance;
    private Date pickUpDateTime;
    private Date dropDateTime;
    private String truckerId; // assigned after load matching
    private String shipperId;

    // Default constructor (required for Firebase Realtime Database)
    public Load() {
        // Default constructor required for calls to DataSnapshot.getValue(Load.class)
    }

    // Parameterized constructor
    public Load(String id, String additionalReq, String contactInfo, String currentLoc, String dropLoc,
                String loadDesc, float distance, String loadDims, String loadName, String loadWeight, String pickupLoc, Date pickUpDateTime, Date dropDateTime, String truckerId, String shipperId) {
        this.additionalReq = additionalReq;
        this.contactInfo = contactInfo;
        this.currentLoc = currentLoc;
        this.dropLoc = dropLoc;
        this.loadDesc = loadDesc;
        this.loadDims = loadDims;
        this.loadName = loadName;
        this.loadWeight = loadWeight;
        this.pickupLoc = pickupLoc;
        this.truckerId = truckerId;
        this.shipperId = shipperId;
        this.distance = distance;
        this.pickUpDateTime = pickUpDateTime;
        this.dropDateTime = dropDateTime;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdditionalReq() {
        return additionalReq;
    }

    public void setAdditionalReq(String additionalReq) {
        this.additionalReq = additionalReq;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(String currentLoc) {
        this.currentLoc = currentLoc;
    }

    public String getDropLoc() {
        return dropLoc;
    }

    public void setDropLoc(String dropLoc) {
        this.dropLoc = dropLoc;
    }

    public Date getEstArrivalTime() {
        return estArrivalTime;
    }

    public void setEstArrivalTime(Date estArrivalTime) {
        this.estArrivalTime = estArrivalTime;
    }

    public String getLoadDesc() {
        return loadDesc;
    }

    public void setLoadDesc(String loadDesc) {
        this.loadDesc = loadDesc;
    }

    public String getLoadDims() {
        return loadDims;
    }

    public void setLoadDims(String loadDims) {
        this.loadDims = loadDims;
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public void setLoadWeight(String loadWeight) {
        this.loadWeight = loadWeight;
    }

    public String getPickupLoc() {
        return pickupLoc;
    }

    public void setPickupLoc(String pickupLoc) {
        this.pickupLoc = pickupLoc;
    }

    public String getTruckerId() {
        return truckerId;
    }

    public void setTruckerId(String truckerId) {
        this.truckerId = truckerId;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;

    }

    public Date getPickUpDateTime() {
        return pickUpDateTime;
    }

    public void setPickUpDateTime(Date pickUpDateTime) {
        this.pickUpDateTime = pickUpDateTime;
    }

    public Date getDropDateTime() {
        return dropDateTime;
    }

    public void setDropDateTime(Date dropDateTime) {
        this.dropDateTime = dropDateTime;
    }


    public float getDistance() {
        return distance;
    }

    public void setDistance(float Distance) {
        this.distance = distance;

    }
}