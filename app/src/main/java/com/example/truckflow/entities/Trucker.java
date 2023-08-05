package com.example.truckflow.entities;

public class Trucker {

    public String company_name;

    public String truckerEmail;

    public String company_phone;

    public String dot;

    public String mc;

    public String truck_name;

    public String truck_type;

    public String max_length;

    public String max_weight;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
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

    public Trucker(String company_name, String company_phone, String dot, String mc) {
        this.company_name = company_name;
        this.company_phone = company_phone;
        this.dot = dot;
        this.mc = mc;

    }
    public Trucker() {

    }
    public Trucker(String company_name, String company_phone, String dot, String mc, String truck_name, String truck_type, String max_length, String max_weight, String email) {
        this.company_name = company_name;
        this.company_phone = company_phone;
        this.dot = dot;
        this.mc = mc;
        this.truck_name = truck_name;
        this.truck_type = truck_type;
        this.max_length = max_length;
        this.max_weight = max_weight;
        this.truckerEmail = email;
    }

    private boolean availability;

    public String getTruckerEmail() {
        return truckerEmail;
    }

    public void setTruckerEmail(String truckerEmail) {
        this.truckerEmail = truckerEmail;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }



}
