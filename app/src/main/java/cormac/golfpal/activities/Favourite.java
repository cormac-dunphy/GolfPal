package cormac.golfpal.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import cormac.golfpal.R;
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
        //getting the list views and if is empty setting it to text view
        favEmptyList = findViewById(R.id.favEmptyList);
        favListView = findViewById(R.id.favListView);
        favListView.setEmptyView(favEmptyList);
        //getting favourite courses from database
        loadFavouritesData();
    }

    private void loadFavouritesData() {
        //putting all courses which are favourites into ArrayList 'dbFavouritesList'
        dbFavouritesList = myDb.getFavouriteCourseData();
        //set the favourite list adapter to use the list of favourite courses
        FavListViewAdapter adapter = new FavListViewAdapter(this, dbFavouritesList);
        favListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
