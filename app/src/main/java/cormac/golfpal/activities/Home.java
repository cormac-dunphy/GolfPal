package cormac.golfpal.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.CourseListViewAdapter;
import cormac.golfpal.utils.DatabaseHelper;

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

        dbCourseList = new ArrayList<>();
        myDb = new DatabaseHelper(this);

        emptyList = findViewById(R.id.emptyList);
        courseListView = findViewById(R.id.recentlyAddedList);
        courseListView.setEmptyView(emptyList);

        //CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(this, courseList);
        //courseListView.setAdapter(courseListViewAdapter);
        loadDataCourseList();

        courseListView.setLongClickable(true);

        courseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("longclick", "item clicked");
                Log.i("longclick", "int i: " + i);
                Log.i("longclick", "course at i: " + courseList.get(i));
                beforeUpdateName = courseList.get(i).name;
                beforeUpdatePosition = i;
                Intent toUpdate = new Intent(Home.this, Update.class);
                startActivity(toUpdate);

                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Information", Snackbar.LENGTH_LONG)
                        .setAction("More Info", null).show();
            }
        });
    }

    private void loadDataCourseList() {
        dbCourseList = myDb.getAllCourseData();
        Log.i("database", "loadDataCourseList: dbCourseList - " + String.valueOf(dbCourseList));
        CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(this, dbCourseList);
        courseListView.setAdapter(courseListViewAdapter);
        courseListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onResume() {
        CourseListViewAdapter courseListViewAdapter = new CourseListViewAdapter(this, dbCourseList);
        courseListViewAdapter.notifyDataSetChanged();
        Log.i("courses", "courses: " + String.valueOf(dbCourseList));

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void add(View v)
    {
        startActivity(new Intent(this,AddCourse.class));
    }

    public void toFavourites(View v)
    {
        startActivity(new Intent(this, Favourite.class));
    }

}
