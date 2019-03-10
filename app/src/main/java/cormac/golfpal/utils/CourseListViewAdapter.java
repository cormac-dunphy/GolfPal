package cormac.golfpal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import static cormac.golfpal.activities.Base.dbCourseList;
import static cormac.golfpal.activities.Base.dbFavouritesList;

public class CourseListViewAdapter extends ArrayAdapter<Course> {
    //layout inflater
    private LayoutInflater theInflater = null;
    DatabaseHelper myDb = new DatabaseHelper(getContext());

    public CourseListViewAdapter(Context context, ArrayList<Course> courseList) {
        super(context, R.layout.course_list_layout, courseList);
        theInflater = LayoutInflater.from(getContext());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            //inflate the course_list_layout view
            convertView = theInflater.inflate(R.layout.course_list_layout, parent, false);
        }
        //bind the view with the data in bind view method
        convertView = BindView(position, convertView);
        //click listener for the delete button on the home page course list
        Button delButton = convertView.findViewById(R.id.clDeleteBtn);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                Log.i("favlist", "favlist: " + String.valueOf(dbFavouritesList));
                //checks if the course is a favourite and if it is removes it from favourites
                for (Course c : dbFavouritesList){
                    Log.i("favlist", "c.name: " + c.name);
                    if(c.name == course.name){
                        dbFavouritesList.remove(course);
                    }
                }
                //pop up a toast to the user which tells them which course was deleted
                Toast toast = Toast.makeText(getContext(), "Deleted " + course.name, Toast.LENGTH_LONG);
                toast.show();
                //passes courses into deleteCourseRow method in db helper which removes it from the db
                myDb.deleteCourseRow(course);
                //remove course from the dbCourseList
                dbCourseList.remove(course);
                //refreshes the list
                notifyDataSetChanged();

            }
        });
        //click listener for the favourite button on the course list on the home page
        Button favButton = convertView.findViewById(R.id.clFavouriteBtn);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                //changes the value of course.favourite to true
                course.favourite = true;
                Log.i("favCourse", "course = " + String.valueOf(course) + String.valueOf(course.favourite));
                //adds course with favourite value set to 1 to the dbFavouritesList
                dbFavouritesList.add(course);
                //pops up a toast telling the user which course was favourited
                Toast.makeText(getContext(), "Added " + course.name + " to favourites", Toast.LENGTH_LONG).show();
                //passes the course name to the db helper to change the value of favourite field to 1 in the db
                myDb.markAsFavourite(course.name);
                //refreshes the list
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    //inserting the data to the view
    private View BindView(int position, View courseListView){
        Course course = getItem(position);
        //getting the views on the page and setting them to the appropriate values
        TextView courseName = courseListView.findViewById(R.id.clCourseName);
        courseName.setText(course.name);
        TextView courseLocation = courseListView.findViewById(R.id.clCourseLocation);
        courseLocation.setText(course.location);
        TextView coursePar = courseListView.findViewById(R.id.clCoursePar);
        coursePar.setText("Par " + String.valueOf(course.par));
        RatingBar courseRating = courseListView.findViewById(R.id.clCourseRating);
        courseRating.setRating((float) course.rating);

        return courseListView;
    }
}
