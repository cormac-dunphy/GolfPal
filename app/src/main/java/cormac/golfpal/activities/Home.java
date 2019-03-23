package cormac.golfpal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.CourseListViewAdapter;
//import cormac.golfpal.utils.DatabaseHelper;
import cormac.golfpal.utils.FavListViewAdapter;

import static android.widget.Toast.LENGTH_SHORT;
import static cormac.golfpal.R.id.navImage;
import static cormac.golfpal.R.id.navNumberOfCourses;
import static cormac.golfpal.R.id.progressBar;
import static cormac.golfpal.R.id.visible;

public class Home extends Base {
    TextView emptyList;
    ListView courseListView;
    DatabaseReference databaseCourses;
    List<Course> courseList;
    List<Course> favList;
    List<Course> countList;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout = findViewById(R.id.drawer_layout);
        FirebaseApp.initializeApp(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(user == null){
            Intent toSignIn = new Intent(Home.this, SignIn.class);
            startActivity(toSignIn);
        }else {
            Log.i("nulluser", "onCreate: user not null");
            Log.i("nulluser", "onCreate: " + user);
            databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");

            Log.i("user", "onCreate: " + user);
            final Uri photoUrl = user.getPhotoUrl();
            final String userName = user.getDisplayName();

            NavigationView nav_view = findViewById(R.id.nav_view);
            nav_view.setItemIconTintList(null);
            Log.i("photourl", "onCreate: " + String.valueOf(photoUrl));
            Log.i("googleSignIn", "onCreate: user = " + user);

            checkUser();

            courseList = new ArrayList<>();
            favList = new ArrayList<>();
            countList = new ArrayList<>();
            //sets background on courses button on create
            Button courseButton = (Button) findViewById(R.id.homeCoursesButton);
            courseButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            //method for handling the buttons
            buttonHandlers();
            //setting list view to emptyList text view if it is empty
            emptyList = findViewById(R.id.emptyList);
            courseListView = findViewById(R.id.recentlyAddedList);
            courseListView.setEmptyView(progressBar);
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

            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
//
                }

                @Override
                public void onDrawerOpened(View drawerView) {
//
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    //use picasso library to cast users google picture to the nav drawer
                    Picasso.with(getApplicationContext()).load(photoUrl).into((ImageView) findViewById(navImage));
                    //set Text View to user name
                    TextView name = findViewById(R.id.navUsername);
                    name.setText(userName);
                    TextView numberOfCourses = findViewById(R.id.navNumberOfCourses);
                    numberOfCourses.setText(String.valueOf(courseList.size()) + " courses");
                }
            });
        }
    }

    private boolean checkUser() {
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.i("googleSignIn", "checkUser: user is null");
            return false;
        }else{
            Log.i("googleSignIn", "checkUser: user = " + FirebaseAuth.getInstance().getCurrentUser());
            return true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent toSignIn = new Intent(Home.this, SignIn.class);
            startActivity(toSignIn);
        }else {
            Log.i("nulluser", "onStart: user is not null" + user);
            databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");
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

                }
            });
        }
    }

    protected  void getFavourites() {
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

        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //goes to add course page
    public void add(View v)
    {
        //startActivity(new Intent(this, SignIn.class));
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

    public void menuLogOut(View view) {

        mAuth.signOut();
        startActivity(new Intent(Home.this, SignIn.class));
    }

}
