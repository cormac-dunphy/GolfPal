package cormac.golfpal.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.AddCourseListViewAdapter;
import cormac.golfpal.utils.CourseListViewAdapter;

public class AddCourse extends Base implements LocationListener{
    Double lat;
    Double lon;
    DatabaseReference databaseCourses;
    DatabaseReference databaseUniversalCourses;
    CheckBox currentLocationCB;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;
    ListView addCourseListView;
    List<Course> addCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //get the current logged in firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //database reference to that users courses
        databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");
        //database reference for universal courses
        databaseUniversalCourses = FirebaseDatabase.getInstance().getReference("universal-courses");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addCourseList = new ArrayList<>();
        addCourseListView = findViewById(R.id.addCourseListView);

        databaseUniversalCourses.addValueEventListener(new ValueEventListener() {
            //goes through all courses for user and displays them on course list view
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addCourseList.clear();
                for(DataSnapshot courseSnapShot: dataSnapshot.getChildren())
                {
                    Course course = courseSnapShot.getValue(Course.class);
                    addCourseList.add(course);
                }
                AddCourseListViewAdapter addCourseListViewAdapter = new AddCourseListViewAdapter(AddCourse.this, addCourseList);
                addCourseListView.setAdapter(addCourseListViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddCourse.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        AddCourseListViewAdapter addCourseListViewAdapter = new AddCourseListViewAdapter(AddCourse.this, addCourseList);
        addCourseListView.setAdapter(addCourseListViewAdapter);
        addCourseListView.setClickable(true);

        addCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = (Course) addCourseListView.getItemAtPosition(i);

                String id = course.courseId;
                databaseCourses.child(id).setValue(course);
                Toast.makeText(getApplicationContext(), "Course Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddCourse.this, Home.class));
            }
        });

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
            String uniId = databaseUniversalCourses.push().getKey();

            Course course = new Course(id, courseName, courseLocation, coursePar, courseRating, courseFavourite, courseLat, courseLon);

            databaseCourses.child(id).setValue(course);
            databaseUniversalCourses.child(uniId).setValue(course);

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
