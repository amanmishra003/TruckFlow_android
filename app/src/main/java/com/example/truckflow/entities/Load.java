package com.example.truckflow.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Load {

    public String loadName;
    public String loadDescription;
    public String loadWeight;
    public String loadLength;
    public String pickUpDate;
    public String deliveryDate;
    public String totalDistance;
    public String pickupAddress;
    public String deliveryAddress;
    public String expectedPrice;


    public Load(String loadName, String loadDescription, String loadWeight, String loadLength, String pickUpDate, String deliveryDate, String totalDistance, String pickupAddress, String deliveryAddress, String expectedPrice, String contactInformation, String requirement) {
        this.loadName = loadName;
        this.loadDescription = loadDescription;
        this.loadWeight = loadWeight;
        this.loadLength = loadLength;
        this.pickUpDate = pickUpDate;
        this.deliveryDate = deliveryDate;
        this.totalDistance = totalDistance;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.expectedPrice = expectedPrice;
        this.contactInformation = contactInformation;
        this.requirement = requirement;
    }

    public String contactInformation;

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String requirement;

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
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

    public String getLoadLength() {
        return loadLength;
    }

    public void setLoadLength(String loadLength) {
        this.loadLength = loadLength;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(String expectedPrice) {
        this.expectedPrice = expectedPrice;
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


    public Load() {

    }

    public static String[] getAddressParts(String addressString) {
        // Split the addressString using commas as separators
        String[] parts = addressString.split(",");

        // Trim any leading/trailing whitespaces from the parts
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        // Initialize the address parts array with empty strings
        String[] addressParts = new String[5];
        for (int i = 0; i < addressParts.length; i++) {
            addressParts[i] = "";
        }

        // Define the mapping of Canadian province full names to abbreviations
        Map<String, String> provinceAbbreviations = new HashMap<>();
        provinceAbbreviations.put("Alberta", "AB");
        provinceAbbreviations.put("British Columbia", "BC");
        provinceAbbreviations.put("Manitoba", "MB");
        provinceAbbreviations.put("New Brunswick", "NB");
        provinceAbbreviations.put("Newfoundland and Labrador", "NL");
        provinceAbbreviations.put("Nova Scotia", "NS");
        provinceAbbreviations.put("Ontario", "ON");
        provinceAbbreviations.put("Prince Edward Island", "PE");
        provinceAbbreviations.put("Quebec", "QC");
        provinceAbbreviations.put("Saskatchewan", "SK");
        provinceAbbreviations.put("Northwest Territories", "NT");
        provinceAbbreviations.put("Nunavut", "NU");
        provinceAbbreviations.put("Yukon", "YT");

        // Determine the length of the parts array (can be less than 5 if the address is not fully specified)
        int length = Math.min(parts.length, 5);

        // Assign the parts to the corresponding address components
        for (int i = 0; i < length; i++) {
            if (i == 2 && provinceAbbreviations.containsKey(parts[i])) {
                addressParts[i] = provinceAbbreviations.get(parts[i]); // Use abbreviation for province
            } else {
                addressParts[i] = parts[i]; // Use full name for other address components
            }
        }

        return addressParts;
    }
}

