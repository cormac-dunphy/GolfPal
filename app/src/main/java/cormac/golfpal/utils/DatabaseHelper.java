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

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "golfpal.db";
    //course table
    public static final String COURSES = "courses";
    public static final String COURSE_NAME = "coursename";
    public static final String COURSE_LOCATION = "courselocation";
    public static final String COURSE_PRICE = "courseprice";
    public static final String COURSE_RATING = "courserating";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("drop table courses");
        db.execSQL("create table if not exists " + COURSES + "(id INTEGER PRIMARY KEY AUTOINCREMENT, coursename TEXT, courselocation TEXT, courseprice TEXT, courserating TEXT)");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertCourseData(String courseName, String courseLocation, String coursePrice, String courseRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COURSE_NAME, courseName);
        contentValues.put(COURSE_LOCATION, courseLocation);
        contentValues.put(COURSE_PRICE, coursePrice);
        contentValues.put(COURSE_RATING, courseRating);
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
            Log.i("database", "getAllCourseData: courseName - " + courseName);
            Course course = new Course(courseName, courseLocation, Double.parseDouble(coursePrice), Double.parseDouble(courseRating));
            courseArrayList.add(course);
        }

        return courseArrayList;

    }
}
