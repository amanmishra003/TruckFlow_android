package com.example.truckflow.entities;

import java.io.Serializable;
import java.util.Date;

public class Load  {

   public String loadName;
   public String loadDescription;

   public String loadWeight;

   public String loadDimensions;

   public String pickDate;

   public String deliverDate;

   public String contactInformation;

   public String requirement;

//    public Load(String loadDescription, String loadWeight,
//                String loadDimensions, String pickDate, String deliverDate, String contactInformation, String requirement) {
//        this.loadDescription = loadDescription;
//        this.loadWeight = loadWeight;
//        this.loadDimensions = loadDimensions;
//        this.pickDate = pickDate;
//        this.deliverDate = deliverDate;
//        this.contactInformation = contactInformation;
//        this.requirement = requirement;
//    }


    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public Load() {
    }

    public Load(String loadName, String loadDescription, String loadWeight, String loadDimensions, String pickDate, String deliverDate, String contactInformation, String requirement) {
        this.loadName = loadName;
        this.loadDescription = loadDescription;
        this.loadWeight = loadWeight;
        this.loadDimensions = loadDimensions;
        this.pickDate = pickDate;
        this.deliverDate = deliverDate;
        this.contactInformation = contactInformation;
        this.requirement = requirement;
    }

    public String getLoadDescription() {
        return loadDescription;
    }

    public void setLoadDescription(String loadDescription) {
        this.loadDescription = loadDescription;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public void setLoadWeight(String loadWeight) {
        this.loadWeight = loadWeight;
    }

    public String getLoadDimensions() {
        return loadDimensions;
    }


    public void setLoadDimensions(String loadDimensions) {
        this.loadDimensions = loadDimensions;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
}

