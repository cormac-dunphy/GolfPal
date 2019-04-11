package cormac.golfpal.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class AddCourse extends Base implements LocationListener{
    Double lat;
    Double lon;
    DatabaseReference databaseCourses;
    CheckBox currentLocationCB;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //get the current logged in firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //database reference to that users courses
        databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //if user has come from 'check in at course' they will have a lat lon value for a marker on the map
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");
        }

        currentLocationCB = findViewById(R.id.addLocationCB);
        onAddCourseButtonPressed();

    }



    public void onAddCourseButtonPressed() {
        Button addCourseButton = findViewById(R.id.addCourseDoneButton);
        //click listener for add course button
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLocationCB.isChecked()) {
                    lat = currentLocation.getLatitude();
                    lon = currentLocation.getLongitude();
                }
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

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.i("currentLocation", "currentLocation: " + currentLocation.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
