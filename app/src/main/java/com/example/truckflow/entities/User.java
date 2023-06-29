package com.example.truckflow.entities;

public class User {


    public String name;
    public String email;
    public String phone;
    public String password;

    public String role;

    public User(String name, String email, String phone, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }


<<<<<<< HEAD





=======
    public User() {
    }
>>>>>>> origin/rk-integrationapi
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
<<<<<<< HEAD
=======

    public void setRole(String role) {
        this.role = role;
    }
>>>>>>> origin/rk-integrationapi

    public void setRole(String Role) {
        this.role = Role;
    }
}
