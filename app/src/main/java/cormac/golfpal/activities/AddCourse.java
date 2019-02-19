package cormac.golfpal.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.DatabaseHelper;

public class AddCourse extends Base {
    //database
    public DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        myDb = new DatabaseHelper(this);
        onAddCourseButtonPressed();
    }

    public void onAddCourseButtonPressed() {
        Button addCourseButton = findViewById(R.id.addCourseBtn);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse();
            }
        });
    }

    public void addCourse() {
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText coursePriceET = findViewById(R.id.addPriceET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);

        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        Double coursePrice = Double.parseDouble(coursePriceET.getText().toString());
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;

        Course course = new Course(courseName, courseLocation, coursePrice, courseRating, courseFavourite);

        //courseList.add(course);

        //Toast.makeText(this, "courseListSize: " + courseList.size(), Toast.LENGTH_SHORT).show();

        //add to database
        myDb.insertCourseData(courseName, courseLocation, String.valueOf(coursePrice), String.valueOf(courseRating), String.valueOf(courseFavourite));

        //ArrayList<Course> res = myDb.getAllCourseData();
        //Log.i("database", "res: " + String.valueOf(res));
        Intent toHome = new Intent(this, Home.class);
        startActivity(toHome);
    }
}
