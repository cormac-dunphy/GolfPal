package cormac.golfpal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import cormac.golfpal.models.Course;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "golfpal.db";
    //course table
    public static final String COURSES = "courses";
    public static final String COURSE_NAME = "coursename";
    public static final String COURSE_LOCATION = "courselocation";
    public static final String COURSE_PRICE = "courseprice";
    public static final String COURSE_RATING = "courserating";
    public static final String COURSE_FAVOURITE = "coursefavourite";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("drop table courses");
        db.execSQL("create table if not exists " + COURSES + "(id INTEGER PRIMARY KEY AUTOINCREMENT, coursename TEXT, courselocation TEXT, courseprice TEXT, courserating TEXT, coursefavourite INT)");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertCourseData(String courseName, String courseLocation, String coursePrice, String courseRating, String courseFavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_NAME, courseName);
        contentValues.put(COURSE_LOCATION, courseLocation);
        contentValues.put(COURSE_PRICE, coursePrice);
        contentValues.put(COURSE_RATING, courseRating);
        contentValues.put(COURSE_FAVOURITE, courseFavourite);
        long result = db.insert(COURSES, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<Course> getAllCourseData(){
        ArrayList<Course> courseArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSES", null);

        while (cursor.moveToNext()) {
            String courseName = cursor.getString(1);
            String courseLocation = cursor.getString(2);
            String coursePrice = cursor.getString(3);
            String courseRating = cursor.getString(4);
            String courseFavourite = cursor.getString(5);
            Log.i("database", "getAllCourseData: courseName - " + courseName);
            Course course = new Course(courseName, courseLocation, Double.parseDouble(coursePrice), Double.parseDouble(courseRating), Boolean.parseBoolean(courseFavourite));
            courseArrayList.add(course);
        }

        return courseArrayList;

    }

    public ArrayList<Course> getFavouriteCourseData(){
        ArrayList<Course> favouritesArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSES", null);

        while (cursor.moveToNext()) {
            String courseName = cursor.getString(1);
            String courseLocation = cursor.getString(2);
            String coursePrice = cursor.getString(3);
            String courseRating = cursor.getString(4);
            String courseFavourite = cursor.getString(5);
            Log.i("database", "getAllCourseData: courseName - " + courseName);
            Course course = new Course(courseName, courseLocation, Double.parseDouble(coursePrice), Double.parseDouble(courseRating), Boolean.parseBoolean(courseFavourite));
            if (courseFavourite.equals("1")){
                favouritesArrayList.add(course);
            }
        }
        return favouritesArrayList;
    }

    public void deleteCourseRow(Course course) {
        Log.i("database", "deleteCourseRow: in delete course row");
        SQLiteDatabase db = this.getWritableDatabase();
        String cleanCourseName = null;
        if (course.name.contains("'")) {
            cleanCourseName = course.name.replaceAll("'", "''");
            db.execSQL("DELETE FROM " + COURSES + " WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            db.execSQL("DELETE FROM " + COURSES + " WHERE " + COURSE_NAME + " = '" + course.name + "'");
            db.close();
        }
    }

    public void markAsFavourite(String courseName) {
        Log.i("favourite", "markAsFavourite: in mark as favourite");
        SQLiteDatabase db = this.getWritableDatabase();
        String cleanCourseName = null;
        if (courseName.contains("'")) {
            cleanCourseName = courseName.replaceAll("'", "''");
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 1 WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 1 WHERE " + COURSE_NAME + " = '" + courseName + "'");
            db.close();
        }
    }

    public void unmarkAsFavourite(String courseName) {
        Log.i("favourite", "unmarkAsFavourite: courseName = " + courseName);
        SQLiteDatabase db = this.getWritableDatabase();
        String cleanCourseName = null;
        if (courseName.contains("'")) {
            cleanCourseName = courseName.replaceAll("'", "''");
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 0 WHERE " + COURSE_NAME + " LIKE '%" + cleanCourseName + "%'");
            db.close();
        } else {
            db.execSQL("UPDATE " + COURSES + " SET " + COURSE_FAVOURITE + " = 0 WHERE " + COURSE_NAME + " = '" + courseName + "'");
            db.close();
        }
    }

    public void updateCourse(String courseName, Course course){
        Log.i("updateCourse", "updateCourse: " + courseName);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + COURSES + " SET " + COURSE_NAME + " = '" + course.name + "', " + COURSE_LOCATION + " = '" + course.location + "' , " + COURSE_PRICE + " = '" + course.price + "', " + COURSE_RATING + " = '" + course.rating + "', " + COURSE_FAVOURITE + " = '" + course.favourite + "' WHERE " + COURSE_NAME + " = '" + courseName + "'");
        Log.i("updateCourse", "updateCourse: " + course.name);
        db.close();
    }
}
