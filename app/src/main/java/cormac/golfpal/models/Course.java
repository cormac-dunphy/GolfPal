package cormac.golfpal.models;

import java.io.Serializable;

/**
 * Created by Cormac on 22/01/2019.
 */
//Course object
public class Course implements Serializable {
    public String courseId;
    public String name;
    public String location;
    public double rating;
    public double par;
    public boolean favourite;
    public double lat;
    public double lon;

    public Course() {}

    public Course(String courseId, String name, String location, double par, double rating, boolean favourite, double lat, double lon)
    {
        this.courseId = courseId;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.par = par;
        this.lat = lat;
        this.lon = lon;
        this.favourite = favourite;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getPar() {
        return par;
    }

    public void setPar(double par) {
        this.par = par;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return courseId + ", " + name + ", " + location + ", " + rating
                + ", " + par + ", " + lat + ", " + lon;
    }
}
