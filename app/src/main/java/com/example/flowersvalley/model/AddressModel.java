package com.example.flowersvalley.model;

public class AddressModel {
    String addressId,name,houseNo, address, mobileNumber, city, district, state, postalCode;

    public AddressModel(String name, String houseNo, String address, String mobileNumber, String city, String district, String state, String postalCode) {
        this.name = name;
        this.houseNo = houseNo;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.city = city;
        this.district = district;
        this.state = state;
        this.postalCode = postalCode;
    }

    public AddressModel() {
    }

    public String getName() {
        return name;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
