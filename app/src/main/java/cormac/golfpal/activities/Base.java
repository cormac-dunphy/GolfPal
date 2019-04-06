package cormac.golfpal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import cormac.golfpal.R;

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
}
