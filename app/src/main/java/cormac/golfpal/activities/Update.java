package cormac.golfpal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.DatabaseHelper;

public class Update extends AppCompatActivity {
    DatabaseHelper myDb;
    String courseName;
    Boolean courseFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        myDb = new DatabaseHelper(this);
        //get courseName and courseFavourite sent in intent and set to variables
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            courseName = extras.getString("name");
            courseFavourite = extras.getBoolean("favourite");
        }

        onUpdateCourseButtonPressed();
    }

    private void onUpdateCourseButtonPressed() {
        //click listener for update button
        Button updateCourseButton = findViewById(R.id.updateButton);
        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCourse();
            }
        });
    }

    private void updateCourse() {
        //get course data inputted by user
        EditText updateCourseNameET = findViewById(R.id.updateName);
        EditText updateCourseLocationET = findViewById(R.id.updateLocation);
        EditText updateCoursePriceET = findViewById(R.id.updatePrice);
        RatingBar updateCourseRatingBar = findViewById(R.id.updateRatingBar);
        //assign these values to variables
        String updateCourseName = updateCourseNameET.getText().toString();
        String updateCourseLocation = updateCourseLocationET.getText().toString();
        //convert coursePrice to double
        String updateCoursePriceText = updateCoursePriceET.getText().toString();
        Double updateCoursePrice = null;

        if(updateCoursePriceText.trim().length()!=0) {
            updateCoursePrice = Double.parseDouble(updateCoursePriceText);
        }

        float updateCourseRating = updateCourseRatingBar.getRating();
        //if edit texts are left blank prompt the user to input value
        if(updateCourseName.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
        }else if(updateCourseLocation.trim().length()<=0){
            Toast.makeText(this, "Please Enter Course Location", Toast.LENGTH_SHORT).show();
        }else if(updateCoursePriceText.trim().length()<=0) {
            Toast.makeText(this, "Please Enter Course Price", Toast.LENGTH_SHORT).show();
        }else {
            //create the new updated course object
            //favourite value stays the same
            Course course = new Course(updateCourseName, updateCourseLocation, updateCoursePrice, updateCourseRating, courseFavourite);
            //pass the original course name and new course object to the update course method in the db helper
            myDb.updateCourse(courseName, course);
            //go to home page
            Intent toHome = new Intent(Update.this, Home.class);
            startActivity(toHome);
        }
    }
}
