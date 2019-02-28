package cormac.golfpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.DatabaseHelper;
import cormac.golfpal.utils.FavListViewAdapter;

public class Favourite extends Base {
    DatabaseHelper myDb;
    ListView favListView;
    TextView favEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        dbFavouritesList = new ArrayList<>();
        myDb = new DatabaseHelper(this);

        favEmptyList = findViewById(R.id.favEmptyList);
        favListView = findViewById(R.id.favListView);
        favListView.setEmptyView(favEmptyList);

        loadFavouritesData();
    }

    private void loadFavouritesData() {
        dbFavouritesList = myDb.getFavouriteCourseData();

        FavListViewAdapter adapter = new FavListViewAdapter(this, dbFavouritesList);
        favListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
