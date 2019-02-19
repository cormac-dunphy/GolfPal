package cormac.golfpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import cormac.golfpal.utils.DatabaseHelper;
import cormac.golfpal.utils.FavRecyclerAdapter;

public class Favourite extends Base {
    RecyclerView favRecyclerView;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        dbFavouritesList = new ArrayList<>();
        myDb = new DatabaseHelper(this);

        favRecyclerView = findViewById(R.id.favRecyclerView);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFavouritesData();
    }

    private void loadFavouritesData() {
        dbFavouritesList = myDb.getFavouriteCourseData();
        FavRecyclerAdapter adapter = new FavRecyclerAdapter(dbFavouritesList);
        favRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
