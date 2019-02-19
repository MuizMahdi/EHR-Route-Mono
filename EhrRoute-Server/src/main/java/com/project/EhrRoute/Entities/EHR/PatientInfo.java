package com.project.EhrRoute.Entities.EHR;


public class PatientInfo
{
    private String name; // Normal input
    private String gender; // Radio buttons
    private String address; // Use API
    private String phone; // Phone input
    private String birthDate; // Date pick
    private String email; // Get it from user


    public PatientInfo() { }
    public PatientInfo(String name, String gender, String address, String phone, String birthDate, String email) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
    }


    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getGender() {
        return gender;
    }
    public String getAddress() {
        return address;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
