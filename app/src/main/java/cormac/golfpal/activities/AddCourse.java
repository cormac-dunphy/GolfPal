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
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");

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
                if(lat != null && lon != null)
                {
                    Log.i("addCourse", "onClick: lat = " + lat);
                    addLocationCourseFB();
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
        Double coursePar = Double.parseDouble(courseParText);
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;

        if(!TextUtils.isEmpty(courseName) || !TextUtils.isEmpty(courseLocation) || !TextUtils.isEmpty(String.valueOf(coursePar)))
        {
            String id = databaseCourses.push().getKey();

            Course course = new Course(id, courseName, courseLocation, coursePar, courseRating, courseFavourite, lat, lon);

            databaseCourses.child(id).setValue(course);

            Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
            Log.i("addCourse", "addCourseFB: course = " + course.toString());
            Intent toHome = new Intent(AddCourse.this, Home.class);
            startActivity(toHome);
        }else{
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
        Double coursePar = Double.parseDouble(courseParText);
        float courseRating = courseRatingBar.getRating();
        boolean courseFavourite = false;
        Integer courseLat = 0;
        Integer courseLon = 0;

        if(!TextUtils.isEmpty(courseName) || !TextUtils.isEmpty(courseLocation) || !TextUtils.isEmpty(String.valueOf(coursePar)))
        {
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
