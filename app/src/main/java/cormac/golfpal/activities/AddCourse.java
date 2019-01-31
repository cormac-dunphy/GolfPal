package cormac.golfpal.activities;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class AddCourse extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
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

        Course course = new Course(courseName, courseLocation, coursePrice, courseRating);

        courseList.add(course);

        Toast.makeText(this, "courseListSize: " + courseList.size(), Toast.LENGTH_SHORT).show();

        Intent toHome = new Intent(this, Home.class);
        startActivity(toHome);
    }
}
