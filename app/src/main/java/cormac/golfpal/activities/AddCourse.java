package cormac.golfpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import cormac.golfpal.R;
import cormac.golfpal.utils.DatabaseHelper;

public class AddCourse extends Base {
    //database
    public DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //initialize database
        myDb = new DatabaseHelper(this);
        onAddCourseButtonPressed();
    }

    public void onAddCourseButtonPressed() {
        Button addCourseButton = findViewById(R.id.addCourseBtn);
        //click listener for add course button
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCourse();
            }
        });
    }

    public void addCourse() {
        //get inputted course data from the user
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText coursePriceET = findViewById(R.id.addPriceET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);
        //convert values to appropriate data type
        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        String coursePriceText = coursePriceET.getText().toString();
        Double coursePrice = null;

        if(coursePriceText.trim().length()!=0) {
            coursePrice = Double.parseDouble(coursePriceText);
        }

        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;

        //if any edit texts are left empty pop up a toast to enter a value
        if(courseName.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
        }else if(courseLocation.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Location", Toast.LENGTH_SHORT).show();
        }else if(coursePriceText.trim().length()<=0) {
            Toast.makeText(this, "Please Enter Course Price", Toast.LENGTH_SHORT).show();
        }else{
            //if all values are entered then add the course to the database
            myDb.insertCourseData(courseName, courseLocation, String.valueOf(coursePrice), String.valueOf(courseRating), String.valueOf(courseFavourite));
            //go to home page
            Intent toHome = new Intent(this, Home.class);
            startActivity(toHome);

        }
    }
}
