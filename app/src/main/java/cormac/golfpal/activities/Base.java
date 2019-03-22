package cormac.golfpal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

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

/**
 * Created by Cormac on 22/01/2019.
 */

public class Base extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    //when home button pressed go to home page
    public void menuMap(MenuItem m) {
        startActivity(new Intent(this, Map.class));
    }

    public void menuSearch(MenuItem item) {
        Log.i("searchCourses", "menuSearch: in menu search");
    }

//    public void menuLogOut(MenuItem item) {
//////        startActivity(new Intent(getApplicationContext(), Account.class));
//////    }
}
