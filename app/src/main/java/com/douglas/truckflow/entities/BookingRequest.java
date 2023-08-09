package com.douglas.truckflow.entities;

public class BookingRequest {

    private  String loadId;  //auto generated Mongo-ID
    private String shipperId;

    private String loadName;
    private String loadDescription;
    private String loadWeight;
    private String loadLength;
    private String pickUpDate;
    private String deliveryDate;
    private String totalDistance;
    private String pickupAddress;

    public BookingRequest(String loadId, String shipperId, String loadName, String loadDescription, String loadWeight, String loadLength, String pickUpDate, String deliveryDate, String totalDistance, String pickupAddress, String deliveryAddress, String expectedPrice, String contactInformation, String durationInHours, String company_name, String truckerEmail, String company_phone, String truck_name, String truck_type, String max_length, String max_weight) {
        this.loadId = loadId;
        this.shipperId = shipperId;
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
        this.durationInHours = durationInHours;
        this.company_name = company_name;
        this.truckerEmail = truckerEmail;
        this.company_phone = company_phone;
        this.truck_name = truck_name;
        this.truck_type = truck_type;
        this.max_length = max_length;
        this.max_weight = max_weight;
    }

    private String deliveryAddress;
    private String expectedPrice;
    private String contactInformation;
    private String durationInHours;


    //Trucker
    private String company_name;
    private String truckerEmail;
    private String company_phone;
    private String truck_name;
    private String truck_type;
    private String max_length;

    public BookingRequest(){

    }


    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

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

    public String getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(String durationInHours) {
        this.durationInHours = durationInHours;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTruckerEmail() {
        return truckerEmail;
    }

    public void setTruckerEmail(String truckerEmail) {
        this.truckerEmail = truckerEmail;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getTruck_name() {
        return truck_name;
    }

    public void setTruck_name(String truck_name) {
        this.truck_name = truck_name;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public String getMax_length() {
        return max_length;
    }

    public void setMax_length(String max_length) {
        this.max_length = max_length;
    }

    public String getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(String max_weight) {
        this.max_weight = max_weight;
    }

    private String max_weight;


}
