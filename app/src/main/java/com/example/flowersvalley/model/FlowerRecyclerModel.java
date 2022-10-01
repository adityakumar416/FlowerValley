package com.example.flowersvalley.model;

public class FlowerRecyclerModel {

    private String flowerPrice;
    private String flowerImageUrl;
    private String flowerName;
    public FlowerRecyclerModel(){

    }



    public String getFlowerPrice() {
        return flowerPrice;
    }

    public String getFlowerImageUrl() {
        return flowerImageUrl;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public FlowerRecyclerModel(String flowerPrice, String flowerImageUrl, String flowerName) {
        this.flowerPrice = flowerPrice;
        this.flowerImageUrl = flowerImageUrl;
        this.flowerName = flowerName;
    }
}
