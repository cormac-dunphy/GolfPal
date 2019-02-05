package cormac.golfpal.models;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Cormac on 22/01/2019.
 */

public class Course implements Serializable {
    public String name;
    public String location;
    public double rating;
    public double price;


    public Course() {}

    public Course(String name, String location, double price, double rating)
    {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ", " + location + ", " + rating
                + ", " + price;
    }
}
