package cormac.golfpal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import static cormac.golfpal.activities.Base.dbFavouritesList;

public class FavListViewAdapter extends ArrayAdapter<Course> {
    private LayoutInflater theInflater = null;
    DatabaseHelper myDb = new DatabaseHelper(getContext());

    public FavListViewAdapter(@NonNull Context context, ArrayList<Course> favouritelist) {
        super(context, R.layout.fav_list_layout, favouritelist);
        theInflater = LayoutInflater.from(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = theInflater.inflate(R.layout.fav_list_layout, parent, false);
        }
        convertView = BindView(position, convertView);
        //click listener for remove favourite button on favourite page
        Button removeFavourite = convertView.findViewById(R.id.favRemove);
        removeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                //pops up a toast to tell the user shich course was removed from favourites
                Toast toast = Toast.makeText(getContext(), "Removed " + course.name + " from favourites", Toast.LENGTH_LONG);
                toast.show();
                //passes course name to db helper to remove favourite
                myDb.unmarkAsFavourite(course.name);
                //removes course from dbCourselist
                dbFavouritesList.remove(course);
                //refreshes the list
                notifyDataSetChanged();

            }
        });
        return convertView;
    }
    //binds data to the favourite view
    private View BindView(int position, View favListView){
        Course course = getItem(position);

        TextView favCourseNameTV = favListView.findViewById(R.id.favCourseName);
        favCourseNameTV.setText(course.name);
        TextView favCourseLocationTV = favListView.findViewById(R.id.favCourseLocation);
        favCourseLocationTV.setText(course.location);
        TextView favCourseParTV = favListView.findViewById(R.id.favCoursePar);
        favCourseParTV.setText("Par " + String.valueOf(course.par));
        RatingBar favRatingBar = favListView.findViewById(R.id.favCourseRating);
        favRatingBar.setRating((float) course.rating);

        return favListView;
    }
}
