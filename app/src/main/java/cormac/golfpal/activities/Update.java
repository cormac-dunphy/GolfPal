package cormac.golfpal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
//import cormac.golfpal.utils.DatabaseHelper;

public class Update extends AppCompatActivity {
    //DatabaseHelper myDb;
    String courseName;
    Boolean favourite;
    Double lat;
    Double lon;
    String courseId;
    EditText newNameET;
    EditText newLocationET;
    EditText newParET;
    RatingBar newRatingRB;
    Button updateCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            courseId = extras.getString("courseId");
            courseName = extras.getString("name");
            favourite = extras.getBoolean("favourite");
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");
        }
        Log.i("updateCourse", "onCreate: courseId = " + courseId);
        newNameET = findViewById(R.id.updateNameET);
        newLocationET = findViewById(R.id.updateLocationET);
        newParET = findViewById(R.id.updateParET);
        newRatingRB = findViewById(R.id.updateRatingBar);
        updateCourseButton = findViewById(R.id.updateCourseDoneButton);

        onUpdateCourseButtonPressed();
    }

    private void onUpdateCourseButtonPressed() {
        //click listener for update button
        updateCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newNameET.getText().toString().trim().length() <= 0) {
                    Toast.makeText(Update.this, "Please Enter Course Name", Toast.LENGTH_SHORT).show();
                } else if (newLocationET.getText().toString().trim().length() <= 0) {
                    Toast.makeText(Update.this, "Please Enter Course Location", Toast.LENGTH_SHORT).show();
                } else if (newParET.getText().toString().trim().length() <= 0) {
                    Toast.makeText(Update.this, "Please Enter Course Price", Toast.LENGTH_SHORT).show();
                } else {
                    update(courseId);

                    Intent toHome = new Intent(Update.this, Home.class);
                    startActivity(toHome);
                }
            }

        });
    }

        private void update(String id)
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses").child(id);

            //get course data inputted by user
            EditText updateCourseNameET = findViewById(R.id.updateNameET);
            EditText updateCourseLocationET = findViewById(R.id.updateLocationET);
            EditText updateCourseParET = findViewById(R.id.updateParET);
            RatingBar updateCourseRatingBar = findViewById(R.id.updateRatingBar);
            String name = updateCourseNameET.getText().toString();
            String location = updateCourseLocationET.getText().toString();
            String parString = updateCourseParET.getText().toString();
            Double par = Double.parseDouble(parString);
            float rating = updateCourseRatingBar.getRating();

            Course course = new Course(id, name, location, par, rating, favourite, lat, lon);

            databaseReference.setValue(course);

            Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();

        }
    }
