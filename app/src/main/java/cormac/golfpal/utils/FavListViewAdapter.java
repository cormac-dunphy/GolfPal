package cormac.golfpal.utils;

import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class FavListViewAdapter extends ArrayAdapter<Course> {
    private List<Course> favList;
    private Activity context;

    public FavListViewAdapter(@NonNull Activity context, List<Course> favList) {
        super(context, R.layout.fav_list_layout, favList);
        this.favList = favList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View favListItem = inflater.inflate(R.layout.fav_list_layout, null, true);

        TextView courseName = favListItem.findViewById(R.id.favCourseName);
        TextView courseLocation = favListItem.findViewById(R.id.favCourseLocation);
        TextView coursePar = favListItem.findViewById(R.id.favCoursePar);
        RatingBar courseRating = favListItem.findViewById(R.id.favCourseRating);

        Course course = favList.get(position);

        courseName.setText(course.getName());
        courseLocation.setText(course.getLocation());
        coursePar.setText(String.valueOf(course.getPar()));
        courseRating.setRating((float) course.getRating());

        Button favRemove = favListItem.findViewById(R.id.favRemove);
        favRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses").child(course.courseId);

                Course markAsFavourite = new Course(course.courseId, course.getName(), course.getLocation(), course.getPar(), course.getRating(), false, course.getLat(), course.getLon());

                databaseReference.setValue(markAsFavourite);

                Toast.makeText(getContext(), "Removed Course from Favourites", Toast.LENGTH_SHORT).show();
            }
        });
        return favListItem;
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
