package cormac.golfpal.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cormac.golfpal.R;
import cormac.golfpal.utils.FavRecyclerAdapter;

public class Favourite extends Base {
    RecyclerView favRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favRecyclerView = findViewById(R.id.favRecyclerView);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FavRecyclerAdapter adapter = new FavRecyclerAdapter(favouriteList);
        favRecyclerView.setAdapter(adapter);


    }

}
