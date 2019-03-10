package cormac.golfpal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import cormac.golfpal.models.Course;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "golfpal.db";
    //course table
    public static final String COURSES = "courses";
    public static final String COURSE_NAME = "coursename";
    public static final String COURSE_LOCATION = "courselocation";
    public static final String COURSE_PAR = "coursepar";
    public static final String COURSE_RATING = "courserating";
    public static final String COURSE_FAVOURITE = "coursefavourite";
    public static final String COURSE_LAT = "courselat";
    public static final String COURSE_LON = "courselon";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("drop table courses");
        db.execSQL("create table if not exists " + COURSES + "(id INTEGER PRIMARY KEY AUTOINCREMENT, coursename TEXT, courselocation TEXT, coursepar TEXT, courserating TEXT, coursefavourite INT, courselat INT, courselon INT)");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //insert new course to the database
    public boolean insertCourseData(String courseName, String courseLocation, String coursePar, String courseRating, String courseFavourite, String courseLat, String courseLon) {
        SQLiteDatabase db = this.getWritableDatabase();
        //put all values into the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_NAME, courseName);
        contentValues.put(COURSE_LOCATION, courseLocation);
        contentValues.put(COURSE_PAR, coursePar);
        contentValues.put(COURSE_RATING, courseRating);
        contentValues.put(COURSE_FAVOURITE, courseFavourite);
        contentValues.put(COURSE_LAT, courseLat);
        contentValues.put(COURSE_LON, courseLon);
        long result = db.insert(COURSES, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    //gets courses in the database
    public ArrayList<Course> getAllCourseData(){
        ArrayList<Course> courseArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLite query to retrieve the data
        Cursor cursor = db.rawQuery("SELECT * FROM COURSES", null);

        while (cursor.moveToNext()) {
            String courseName = cursor.getString(1);
            String courseLocation = cursor.getString(2);
            String coursePar = cursor.getString(3);
            String courseRating = cursor.getString(4);
            String courseFavourite = cursor.getString(5);
            String courseLat = cursor.getString(6);
            String courseLon = cursor.getString(7);
            Log.i("database", "getAllCourseData: courseName - " + courseName);
            Course course = new Course(courseName, courseLocation, Double.parseDouble(coursePar), Double.parseDouble(courseRating), Boolean.parseBoolean(courseFavourite), Double.parseDouble(courseLat), Double.parseDouble(courseLon));
            //adds the course to arrayList
            courseArrayList.add(course);
        }
        return courseArrayList;
    }
    //gets all courses that are favourited
    public ArrayList<Course> getFavouriteCourseData(){
        ArrayList<Course> favouritesArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //gets all courses
        Cursor cursor = db.rawQuery("SELECT * FROM COURSES", null);

        while (cursor.moveToNext()) {
            String courseName = cursor.getString(1);
            String courseLocation = cursor.getString(2);
            String coursePar = cursor.getString(3);
            String courseRating = cursor.getString(4);
            String courseFavourite = cursor.getString(5);
            String courseLat = cursor.getString(6);
            String courseLon = cursor.getString(7);
            Log.i("database", "getAllCourseData: courseName - " + courseName);
            Course course = new Course(courseName, courseLocation, Double.parseDouble(coursePar), Double.parseDouble(courseRating), Boolean.parseBoolean(courseFavourite), Double.parseDouble(courseLat), Double.parseDouble(courseLon));
            //checks if favourite value is 1 which means it is a favourited course
            if (courseFavourite.equals("1")){
                //adds course to favourite courses ArrayList
                favouritesArrayList.add(course);
            }
        }
        return favouritesArrayList;
    }
    //deletes a course from the db
    public void deleteCourseRow(Course course) {
        Log.i("database", "deleteCourseRow: in delete course row");
        SQLiteDatabase db = this.getWritableDatabase();
        String cleanCourseName = null;
        //handles if the course has a ' in it's name
        if (course.name.contains("'")) {
            //replaces the ' with '' which SQLite can deal with
            cleanCourseName = course.name.replaceAll("'", "''");
            //deletes course from db
            db.execSQL("DELETE FROM " + COURSES + " WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            //deletes course from db
            db.execSQL("DELETE FROM " + COURSES + " WHERE " + COURSE_NAME + " = '" + course.name + "'");
            db.close();
        }
    }
    //favourite a course
    public void markAsFavourite(String courseName) {
        Log.i("favourite", "markAsFavourite: in mark as favourite");
        SQLiteDatabase db = this.getWritableDatabase();
        //again checking the name for ' and handling it
        String cleanCourseName = null;
        if (courseName.contains("'")) {
            cleanCourseName = courseName.replaceAll("'", "''");
            //changing boolean value to 1 to favourite the course
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 1 WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            //changing boolean value to 1 to favourite the course
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 1 WHERE " + COURSE_NAME + " = '" + courseName + "'");
            db.close();
        }
    }
    //remove course from favourites
    public void unmarkAsFavourite(String courseName) {
        Log.i("favourite", "unmarkAsFavourite: courseName = " + courseName);
        SQLiteDatabase db = this.getWritableDatabase();
        //again checking the name for ' and handling it
        String cleanCourseName = null;
        if (courseName.contains("'")) {
            cleanCourseName = courseName.replaceAll("'", "''");
            //setting the boolean value to 0 to remove from favourites
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 0 WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            //setting the boolean value to 0 to remove from favourites
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 0 WHERE " + COURSE_NAME + " = '" + courseName + "'");
            db.close();
        }
    }
    //updating a course
    public void updateCourse(String courseName, Course course){
        Log.i("updateCourse", "updateCourse: " + courseName);
        SQLiteDatabase db = this.getWritableDatabase();
        //check if new name or location have a ' and handle it
        String cleanCourseName = null;
        String cleanCourseDotName = null;
        String cleanCourseLocation = null;
        //update db with new values for course
        if (courseName.contains("'") || course.name.contains("'") || course.location.contains("'")) {
            cleanCourseName = courseName.replaceAll("'", "''");
            cleanCourseDotName = course.name.replaceAll("'", "''");
            cleanCourseLocation = course.location.replaceAll("'", "''");
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_NAME + " = '" + cleanCourseDotName + "', " + COURSE_LOCATION + " = '" + cleanCourseLocation + "' , " + COURSE_PAR + " = '" + course.par + "', " + COURSE_RATING + " = '" + course.rating + "', " + COURSE_FAVOURITE + " = '" + course.favourite + "' WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        }else {
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_NAME + " = '" + course.name + "', " + COURSE_LOCATION + " = '" + course.location + "' , " + COURSE_PAR + " = '" + course.par + "', " + COURSE_RATING + " = '" + course.rating + "', " + COURSE_FAVOURITE + " = '" + course.favourite + "' WHERE " + COURSE_NAME + " = '" + courseName + "'");
            db.close();
        }
    }
}
