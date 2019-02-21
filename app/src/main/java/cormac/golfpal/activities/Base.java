package cormac.golfpal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

/**
 * Created by Cormac on 22/01/2019.
 */

public class Base extends AppCompatActivity {

    public static ArrayList<Course> courseList = new ArrayList<>();
    public static ArrayList<Course> favouriteList = new ArrayList<>();
//    public static String beforeUpdateName;
//    public int beforeUpdatePosition;
    public static ArrayList<Course> dbCourseList = new ArrayList<>();
    public static ArrayList<Course> dbFavouritesList = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void menuHome(MenuItem m) {
        startActivity(new Intent(this, Home.class));
    }

    public void menuInfo(MenuItem m)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.appAbout))
                .setMessage(getString(R.string.appDesc)
                        + "\n\n"
                        + getString(R.string.appMoreInfo))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we could put some code here too
                    }
                })
                .show();
    }

    public void menuHelp(MenuItem m)
    {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.appHelp))
                .setMessage(getString(R.string.appHelp1)
                        + "\n\n"
                        + getString(R.string.appHelp2)
                        +"\n\n"
                        + getString(R.string.appHelp3)
                        +"\n\n"
                        + getString(R.string.appHelp4)
                        +"\n\n"
                        + getString(R.string.apphelp5))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we could put some code here too
                    }
                })
                .show();
    }
}
