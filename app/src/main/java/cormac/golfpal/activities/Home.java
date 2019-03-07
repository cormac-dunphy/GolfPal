package cormac.golfpal.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.CourseListViewAdapter;
import cormac.golfpal.utils.DatabaseHelper;
import cormac.golfpal.utils.FavListViewAdapter;

public class Home extends Base {
    TextView emptyList;
    ListView courseListView;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //sets underline on courses button on create
        Button courseButton = (Button) findViewById(R.id.homeCoursesButton);
        courseButton.setPaintFlags(courseButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        //ArrayList for the courses
        dbCourseList = new ArrayList<>();
        myDb = new DatabaseHelper(this);
        buttonHandlers();
        //setting list view to emptyList text view if it is empty
        emptyList = findViewById(R.id.emptyList);
        courseListView = findViewById(R.id.recentlyAddedList);
        courseListView.setEmptyView(emptyList);
        courseListView.setLongClickable(true);

        loadDataCourseList();
        //long click listener for courses which brings user to an update course screen
        courseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = (Course) courseListView.getItemAtPosition(i);

                Intent toUpdate = new Intent(Home.this, Update.class);
                toUpdate.putExtra("name", course.name);
                toUpdate.putExtra("favourite", course.favourite);
                startActivity(toUpdate);

                return true;
            }
        });
    }


    private void loadDataCourseList() {
        //goes to db helper and returns list of courses in database
        dbCourseList = myDb.getAllCourseData();
        Log.i("database", "loadDataCourseList: dbCourseList - " + String.valueOf(dbCourseList));
        //sets courseListView to use the list of courses retrieved
        CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(this, dbCourseList);
        courseListView.setAdapter(courseListViewAdapter);
        courseListViewAdapter.notifyDataSetChanged();
    }

    private void loadDataFavouriteCourseList(){
        dbFavouritesList = myDb.getFavouriteCourseData();
        FavListViewAdapter favListViewAdapter = new FavListViewAdapter(this, dbFavouritesList);
        courseListView.setAdapter(favListViewAdapter);
        favListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onResume() {
        //refreshes the courseListView on resume
        CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(this, dbCourseList);
        courseListViewAdapter.notifyDataSetChanged();
        Log.i("courses", "courses: " + String.valueOf(dbCourseList));

        Button courseButton = (Button) findViewById(R.id.homeCoursesButton);
        courseButton.setPaintFlags(courseButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

        Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);
        favouritesButton.setPaintFlags(favouritesButton.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

        loadDataCourseList();
        super.onResume();
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
    //goes to favourites page
    public void toFavourites(View v)
    {
        startActivity(new Intent(this, Favourite.class));
    }

    public void buttonHandlers(){
        final Button coursesButton = (Button) findViewById(R.id.homeCoursesButton);
        final Button favouritesButton = (Button) findViewById(R.id.homeFavouriteCoursesButton);

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coursesButton.setPaintFlags(coursesButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                favouritesButton.setPaintFlags(favouritesButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

                loadDataCourseList();

            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coursesButton.setPaintFlags(coursesButton.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
                favouritesButton.setPaintFlags(favouritesButton.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                loadDataFavouriteCourseList();
            }
        });

    }

}
