package cormac.golfpal.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.CourseListViewAdapter;
import cormac.golfpal.utils.FavListViewAdapter;
import static cormac.golfpal.R.id.navImage;

public class Home extends Base {
    TextView emptyList;
    ListView courseListView;
    DatabaseReference databaseCourses;
    List<Course> courseList;
    List<Course> favList;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //display toolbar with home button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        //nav drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        //allows app to use firebase
        FirebaseApp.initializeApp(this);
        //current logged in user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //gets the firebase db
        mAuth = FirebaseAuth.getInstance();
        //spinner appears while course list is loading
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        //if no user is signed in send them to sign in page
        if(user == null){
            Intent toSignIn = new Intent(Home.this, SignIn.class);
            startActivity(toSignIn);
        }else {
            //reference for current users courses
            databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");
            //gets current users display picture and user name
            final Uri photoUrl = user.getPhotoUrl();
            final String userName = user.getDisplayName();
            //nav drawer view
            final NavigationView nav_view = findViewById(R.id.nav_view);
            nav_view.setItemIconTintList(null);

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
            courseListView.setEmptyView(progressBar);
            courseListView.setLongClickable(true);

            //long click listener for courses which brings user to an update course screen
            //sends all course info to update in an intent
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

            nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //handles all the options on the nav drawer
                    if(item.getTitle().toString().equals("Help")){

                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(Home.this);
                        aBuilder.setMessage("-To add a course press the button at the bottom of the home screen\n" +
                                            "-To delete a course click the trash can\n" +
                                            "-To update a course long click on a course\n" +
                                            "-To navigate open the nav drawer by clicking the hamburger menu on the top left of the home screen\n" +
                                            "-To add course with location marker go to check in at course on the Map page or check the checkbox available on the add course activity.")
                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = aBuilder.create();
                        alertDialog.setTitle("Help");
                        alertDialog.show();
                    }

                    if(item.getTitle().toString().equals("Info")){
                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(Home.this);
                        aBuilder.setMessage("-This app is to help users keep track of courses they've played and their favourite courses\n" +
                                            "-It also allows users to mark courses they've played on a Map\n" +
                                            "-Created by Cormac Dunphy 2019")
                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = aBuilder.create();
                        alertDialog.setTitle("Information");
                        alertDialog.show();
                    }

                    if(item.getTitle().toString().equals("Map")){
                        startActivity(new Intent(Home.this, Map.class));
                    }

                    if(item.getTitle().toString().equals("Favourites")){
                        drawerLayout.closeDrawers();

                        getFavourites();
                    }

                    if(item.getTitle().toString().equals("Courses")){
                        drawerLayout.closeDrawers();

                        final Button coursesButton = (Button) findViewById(R.id.homeCoursesButton);
                        final Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);

                        coursesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        onStart();
                    }

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
                //display users display picture and username when drawer state changes
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

    @Override
    protected void onStart() {
        super.onStart();
        //get current firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if no user is signed in send them to sign in page
        if(user == null){
            Intent toSignIn = new Intent(Home.this, SignIn.class);
            startActivity(toSignIn);
        }else {
            //reference to current users courses
            databaseCourses = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses");
            databaseCourses.addValueEventListener(new ValueEventListener() {
                //goes through all courses for user and displays them on course list view
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
    }
    //get all courses that are favourited
    protected  void getFavourites() {
        final Button coursesButton = (Button) findViewById(R.id.homeCoursesButton);
        final Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);
        //changes background of both buttons to indicate that you are looking at the favourites list
        favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        coursesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        databaseCourses.addValueEventListener(new ValueEventListener() {
            //goes through all courses for that user and adds any which are favourited to favlist
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
        // Inflate the home menu
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
        //if nav drawer button is pressed open the nav drawer
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
    //handlers for courses and favourite buttons
    public void buttonHandlers(){
        final Button coursesButton = (Button) findViewById(R.id.homeCoursesButton);
        final Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when courses button is clicked change background of both buttons to indicate user is looking at all courses
                coursesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //onStart puts all courses on the list view
                onStart();
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when favourites button is clicked change background of both buttons to indicate user is looking at favourites
                favouritesButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                coursesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                //get favourites method displays favourites
                getFavourites();
            }
        });

    }
    //logs user out when they click logout button
    public void menuLogOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(Home.this, SignIn.class));
    }

}
