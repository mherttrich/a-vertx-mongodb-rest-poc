package de.micha.rest;

/**
 * Created by micha on 4.09.16.
 */
public class Glider {


    //for simplify just Strings
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

   /* public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }*/

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
