package cormac.golfpal.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.CourseListViewAdapter;
//import cormac.golfpal.utils.DatabaseHelper;
import cormac.golfpal.utils.FavListViewAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class Home extends Base {
    TextView emptyList;
    ListView courseListView;
    DatabaseReference databaseCourses;
    List<Course> courseList;
    List<Course> favList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");
        courseList = new ArrayList<>();
        favList = new ArrayList<>();
        //sets background on courses button on create
        Button courseButton = (Button) findViewById(R.id.homeCoursesButton);
        courseButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        //method for handling the buttons
        buttonHandlers();
        //setting list view to emptyList text view if it is empty
        emptyList = findViewById(R.id.emptyList);
        courseListView = findViewById(R.id.recentlyAddedList);
        courseListView.setEmptyView(emptyList);
        courseListView.setLongClickable(true);

        //long click listener for courses which brings user to an update course screen
        courseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = (Course) courseListView.getItemAtPosition(i);
                Log.i("updateCourse", "onItemLongClick: Home courseId: " + course.courseId);
                Intent toUpdate = new Intent(Home.this, Update.class);
                toUpdate.putExtra("courseId", course.courseId);
                toUpdate.putExtra("name", course.name);
                toUpdate.putExtra("favourite", course.favourite);
                toUpdate.putExtra("lat", course.lat);
                toUpdate.putExtra("lon", course.lon);
                startActivity(toUpdate);

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for(DataSnapshot courseSnapShot: dataSnapshot.getChildren())
                {
                    Course course = courseSnapShot.getValue(Course.class);
                    courseList.add(course);
                }
                CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(Home.this, courseList);
                courseListView.setAdapter(courseListViewAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected  void getFavourites()
    {
        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favList.clear();
                for(DataSnapshot courseSnapShot: dataSnapshot.getChildren())
                {
                    Course course = courseSnapShot.getValue(Course.class);
                    if(course.favourite == true)
                    {
                        favList.add(course);
                    }else{
                        Log.i("favourites", "No favourites added");
                    }
                }
                FavListViewAdapter favListViewAdapter = new FavListViewAdapter(Home.this, favList);
                courseListView.setAdapter(favListViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //goes to add course page
    public void add(View v)
    {
        startActivity(new Intent(this,AddCourse.class));
    }

    public void buttonHandlers(){
        final Button coursesButton = (Button) findViewById(R.id.homeCoursesButton);
        final Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coursesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                //loadDataCourseList();
                onStart();
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                coursesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                getFavourites();
            }
        });

    }

}
