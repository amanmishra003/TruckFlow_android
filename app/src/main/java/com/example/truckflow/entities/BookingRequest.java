package com.example.truckflow.entities;

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
    private String deliveryAddress;
    private String expectedPrice;
    private String contactInformation;
    private String durationInHours;


    //Trucker
    public String company_name;
    public String truckerEmail;
    public String company_phone;
    public String truck_name;
    public String truck_type;
    public String max_length;
    public String max_weight;


}
