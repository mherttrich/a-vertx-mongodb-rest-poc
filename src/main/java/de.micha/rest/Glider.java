package de.micha.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by micha on 4.09.16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Glider {

/*
change
change2

change3
 */
    private String name;
    private Float price;
    private String itemId;

    public Glider() {
        super();
    }

    public Glider(String name, Float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getItemId() {
        return itemId;
    }


    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
