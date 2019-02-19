package cormac.golfpal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

import static android.content.ContentValues.TAG;
import static cormac.golfpal.activities.Base.courseList;
import static cormac.golfpal.activities.Base.dbCourseList;
import static cormac.golfpal.activities.Base.favouriteList;

public class CourseListViewAdapter extends ArrayAdapter<Course> {
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
            convertView = theInflater.inflate(R.layout.course_list_layout, parent, false);
        }
        convertView = BindView(position, convertView);

        Button delButton = convertView.findViewById(R.id.clDeleteBtn);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                Log.i("favlist", "favlist: " + String.valueOf(favouriteList));
                for (Course c : favouriteList){
                    Log.i("favlist", "c.name: " + c.name);
                    if(c.name == course.name){
                        favouriteList.remove(course);
                    }
                }
                //courseList.remove(course);
                myDb.deleteCourseRow(course);
                dbCourseList.remove(course);
                notifyDataSetChanged();
                Toast toast = Toast.makeText(getContext(), "Deleted " + course.name, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        Button favButton = convertView.findViewById(R.id.clFavouriteBtn);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                favouriteList.add(course);

                myDb.markAsFavourite(course.name);

                notifyDataSetChanged();
                Toast toast = Toast.makeText(getContext(), "Added " + course.name + " to favourites", Toast.LENGTH_LONG);
                toast.show();
                Log.i("favlist", "favlist length: " + favouriteList.size());
            }
        });

        return convertView;
    }

    private View BindView(int position, View courseListView){
        Course course = getItem(position);

        TextView courseName = courseListView.findViewById(R.id.clCourseName);
        courseName.setText(course.name);
        TextView courseLocation = courseListView.findViewById(R.id.clCourseLocation);
        courseLocation.setText(course.location);
        TextView coursePrice = courseListView.findViewById(R.id.clCoursePrice);
        coursePrice.setText(String.valueOf(course.price));

        return courseListView;
    }
}
