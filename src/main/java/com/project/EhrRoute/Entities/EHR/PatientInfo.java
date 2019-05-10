package com.project.EhrRoute.Entities.EHR;


public class PatientInfo
{
    private String name;
    private String gender; // Radio buttons
    private String country;
    private String city;
    private String address; // Use API
    private String phone; // Phone input
    private long birthDate;
    private String email; // Get it from user
    private long userID;


    public PatientInfo() { }
    public PatientInfo(String name, String gender, String country, String city, String address, String phone, long birthDate, String email, long userID) {
        this.name = name;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.birthDate = birthDate;
        this.email = email;
        this.userID = userID;
    }


    public String getName() {
        return name;
    }
    public String getCity() {
        return city;
    }
    public long getUserID() {
        return userID;
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
    public String getCountry() {
        return country;
    }
    public long getBirthDate() {
        return birthDate;
    }
    public void setCity(String city) {
        this.city = city;
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
    public void setUserID(long userID) {
        this.userID = userID;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }


    @Override
    public String toString() {
        return "PatientInfo {" +
            "name='" + name + '\'' +
            ", gender='" + gender + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", address='" + address + '\'' +
            ", phone='" + phone + '\'' +
            ", birthDate=" + birthDate +
            ", email='" + email + '\'' +
            ", userID=" + userID +
        '}';
    }
}
