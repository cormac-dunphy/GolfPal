package cormac.golfpal.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import cormac.golfpal.R;
import cormac.golfpal.utils.DatabaseHelper;

public class AddCourse extends Base {
    //database
    public DatabaseHelper myDb;
    Double lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");
        }

        //initialize database
        myDb = new DatabaseHelper(this);
        onAddCourseButtonPressed();
    }

    public void onAddCourseButtonPressed() {
        Button addCourseButton = findViewById(R.id.addCourseDoneButton);
        //click listener for add course button
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lat != null && lon != null)
                {
                    Log.i("addingdiffcourses", "onClick: lat = " + lat);
                    addLocationCourse();
                }else{
                    Log.i("addingdiffcourses", "onClick: lat = " + lat);
                    addCourse();
                }
            }
        });
    }

    public void addCourse() {
        //get inputted course data from the user
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText courseParET = findViewById(R.id.addParET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);
        //convert values to appropriate data type
        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        String courseParText = courseParET.getText().toString();
        Double coursePar = Double.parseDouble(courseParText);
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;
        Integer courseLat = 0;
        Integer courseLon = 0;

        //if any edit texts are left empty pop up a toast to enter a value
        if(courseName.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
        }else if(courseLocation.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Location", Toast.LENGTH_SHORT).show();
        }else if(courseParText.trim().length()<=0) {
            Toast.makeText(this, "Please Enter Course Price", Toast.LENGTH_SHORT).show();
        }else{
            //if all values are entered then add the course to the database
            myDb.insertCourseData(courseName, courseLocation, String.valueOf(coursePar), String.valueOf(courseRating), String.valueOf(courseFavourite), String.valueOf(courseLat), String.valueOf(courseLon));
            //go to home page
            Intent toHome = new Intent(this, Home.class);
            startActivity(toHome);

        }
    }

    public void addLocationCourse() {
        //get inputted course data from the user
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText courseParET = findViewById(R.id.addParET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);
        //convert values to appropriate data type
        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        String courseParText = courseParET.getText().toString();
        Double coursePar = Double.parseDouble(courseParText);
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;

        //if any edit texts are left empty pop up a toast to enter a value
        if(courseName.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
        }else if(courseLocation.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Location", Toast.LENGTH_SHORT).show();
        }else if(courseParText.trim().length()<=0) {
            Toast.makeText(this, "Please Enter Course Price", Toast.LENGTH_SHORT).show();
        }else{
            //if all values are entered then add the course to the database
            myDb.insertCourseData(courseName, courseLocation, String.valueOf(coursePar), String.valueOf(courseRating), String.valueOf(courseFavourite), String.valueOf(lat), String.valueOf(lon));
            //go to home page
            Intent toMap = new Intent(AddCourse.this, Map.class);
            toMap.putExtra("courseName", courseName);
            toMap.putExtra("lat", lat);
            toMap.putExtra("lon", lon);
            startActivity(toMap);
        }
    }
}
