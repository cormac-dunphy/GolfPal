package cormac.golfpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class Update extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        onUpdateButtonPressed();
    }

    public void onUpdateButtonPressed(){
        Button updateButton = (Button) findViewById(R.id.updateCourseButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText updatedCourseNameET = (EditText) findViewById(R.id.updateCourseNameET);
                EditText updatedCourseLocationET = (EditText) findViewById(R.id.updateCourseLocationET);
                EditText updatedCoursePriceET = (EditText) findViewById(R.id.updateCoursePriceET);
                RatingBar updatedCourseRatingBar = (RatingBar) findViewById(R.id.updateCourseRatingBar);



                String updatedCourseName = updatedCourseNameET.getText().toString();
                String updatedCourseLocation = updatedCourseLocationET.getText().toString();
                double updatedCoursePrice = Double.parseDouble(updatedCoursePriceET.getText().toString());
                float updatedCourseRating = updatedCourseRatingBar.getRating();

                Course updatedCourse = new Course(updatedCourseName, updatedCourseLocation, updatedCoursePrice, updatedCourseRating);

                courseList.set(beforeUpdatePosition, updatedCourse);

                Intent backToHome = new Intent(Update.this, Home.class);
                startActivity(backToHome);

                Log.i("updatepressed", "update pressed");
                Log.i("updatepressed", "beforeUpdatePosition: " + String.valueOf(beforeUpdatePosition));
            }
        });
    }

}
