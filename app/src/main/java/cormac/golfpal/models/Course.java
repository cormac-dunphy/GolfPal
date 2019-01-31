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

    @Override
    public String toString() {
        return name + ", " + location + ", " + rating
                + ", " + price;
    }
}
