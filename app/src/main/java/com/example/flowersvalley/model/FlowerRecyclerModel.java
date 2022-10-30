package com.example.flowersvalley.model;

public class FlowerRecyclerModel {

    private String flowerId;
    private String flowerName;
    private int flowerPrice;
    private String flowerQuantity;
    private String flowerDescription;
    private String flowerImageUrl;


    public FlowerRecyclerModel(){

    }

    public FlowerRecyclerModel(String flowerId, String flowerName, int flowerPrice, String flowerQuantity, String flowerDescription, String flowerImageUrl) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.flowerPrice = flowerPrice;
        this.flowerQuantity = flowerQuantity;
        this.flowerDescription = flowerDescription;
        this.flowerImageUrl = flowerImageUrl;
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

    public int getFlowerPrice() {
        return flowerPrice;
    }

    public void setFlowerPrice(int flowerPrice) {
        this.flowerPrice = flowerPrice;
    }

    public String getFlowerQuantity() {
        return flowerQuantity;
    }

    public void setFlowerQuantity(String flowerQuantity) {
        this.flowerQuantity = flowerQuantity;
    }

    public String getFlowerDescription() {
        return flowerDescription;
    }

    public void setFlowerDescription(String flowerDescription) {
        this.flowerDescription = flowerDescription;
    }

    public String getFlowerImageUrl() {
        return flowerImageUrl;
    }

    public void setFlowerImageUrl(String flowerImageUrl) {
        this.flowerImageUrl = flowerImageUrl;
    }


    @Override
    public String toString() {
        return flowerName;
    }

}
