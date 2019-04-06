package cormac.golfpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class AddCourse extends Base {
    Double lat;
    Double lon;
    DatabaseReference databaseCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //get the current logged in firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //database reference to that users courses
        databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");
        //if user has come from 'check in at course' they will have a lat lon value for a marker on the map
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");
        }

        onAddCourseButtonPressed();

    }

    public void onAddCourseButtonPressed() {
        Button addCourseButton = findViewById(R.id.addCourseDoneButton);
        //click listener for add course button
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if course has lat lon value use the addLocationCourseFB method
                if(lat != null && lon != null)
                {
                    Log.i("addCourse", "onClick: lat = " + lat);
                    addLocationCourseFB();
                //if course doesn't have lat lon value use the addCourseFB method
                }else{
                    Log.i("addCourse", "onClick: lat = " + lat);
                    addCourseFB();
                }

            }
        });
    }

    private void addLocationCourseFB() {
        //get inputted course data from the user
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText courseParET = findViewById(R.id.addParET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);

        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        String courseParText = courseParET.getText().toString();
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;
        //check if all values are inputted first
        if(!TextUtils.isEmpty(courseName) || !TextUtils.isEmpty(courseLocation) || !TextUtils.isEmpty(String.valueOf(courseParText)))
        {
            //parse cours par to double
            Double coursePar = Double.parseDouble(courseParText);
            //get course id
            String id = databaseCourses.push().getKey();

            Course course = new Course(id, courseName, courseLocation, coursePar, courseRating, courseFavourite, lat, lon);
            //add new course to firebase database
            databaseCourses.child(id).setValue(course);

            Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
            Log.i("addCourse", "addCourseFB: course = " + course.toString());
            Intent toHome = new Intent(AddCourse.this, Home.class);
            startActivity(toHome);
        }else{
            //pop a toast if values aren't entered
            Toast.makeText(this, "Enter Course Details", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCourseFB()
    {
        //get inputted course data from the user
        EditText courseNameET = findViewById(R.id.addNameET);
        EditText courseLocationET = findViewById(R.id.addLocationET);
        EditText courseParET = findViewById(R.id.addParET);
        RatingBar courseRatingBar = findViewById(R.id.addRatingBar);

        String courseName = courseNameET.getText().toString();
        String courseLocation = courseLocationET.getText().toString();
        String courseParText = courseParET.getText().toString();
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;
        //set course lat/lon to 0 because they are null
        Integer courseLat = 0;
        Integer courseLon = 0;

        if(!TextUtils.isEmpty(courseName) || !TextUtils.isEmpty(courseLocation) || !TextUtils.isEmpty(String.valueOf(courseParText)))
        {
            Double coursePar = Double.parseDouble(courseParText);

            String id = databaseCourses.push().getKey();

            Course course = new Course(id, courseName, courseLocation, coursePar, courseRating, courseFavourite, courseLat, courseLon);

            databaseCourses.child(id).setValue(course);

            Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
            Log.i("addCourse", "addCourseFB: course = " + course.toString());
            Intent toHome = new Intent(AddCourse.this, Home.class);
            startActivity(toHome);
        }else{
            Toast.makeText(this, "Enter Course Details", Toast.LENGTH_SHORT).show();
        }
    }
}
