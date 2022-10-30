package com.example.flowersvalley.model;

public class CartModel {
    private String flowerId;
    private String flowerName;
    private int price;
    private String imageUrl;
    private int flowerQuantity;


    public CartModel() {
        //Required constructor
    }

    public CartModel(String flowerId, String flowerName, int price, String imageUrl, int flowerQuantity) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.flowerQuantity = flowerQuantity;
    }

    public String getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(String flowerId) {
        this.flowerId = flowerId;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getFlowerQuantity() {
        return flowerQuantity;
    }

    public void setFlowerQuantity(int flowerQuantity) {
        this.flowerQuantity = flowerQuantity;
    }
}
