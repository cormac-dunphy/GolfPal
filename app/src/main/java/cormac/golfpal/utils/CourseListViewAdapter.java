package cormac.golfpal.utils;

import android.app.Activity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import cormac.golfpal.R;
import cormac.golfpal.activities.Home;
import cormac.golfpal.models.Course;

import static android.widget.Toast.LENGTH_SHORT;
import static cormac.golfpal.activities.Base.dbCourseList;
import static cormac.golfpal.activities.Base.dbFavouritesList;

public class CourseListViewAdapter extends ArrayAdapter<Course> {
    //layout inflater
    private List<Course> courseList;
    private Activity context;
    //DatabaseHelper myDb = new DatabaseHelper(getContext());

    public CourseListViewAdapter(Activity context, List<Course> courseList) {
        super(context, R.layout.course_list_layout, courseList);
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View courseListItem = inflater.inflate(R.layout.course_list_layout, null, true);

        TextView courseName = courseListItem.findViewById(R.id.clCourseName);
        TextView courseLocation = courseListItem.findViewById(R.id.clCourseLocation);
        TextView coursePar = courseListItem.findViewById(R.id.clCoursePar);
        RatingBar courseRating = courseListItem.findViewById(R.id.clCourseRating);

        Course course = courseList.get(position);

        courseName.setText(course.getName());
        courseLocation.setText(course.getLocation());
        coursePar.setText(String.valueOf(course.getPar()));
        courseRating.setRating((float) course.getRating());

        Button deleteButton = courseListItem.findViewById(R.id.clDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses").child(course.courseId);
                databaseReference.removeValue();
                Toast.makeText(getContext(), "Course Deleted", LENGTH_SHORT).show();
            }
        });

        Button favButon = courseListItem.findViewById(R.id.clFavouriteBtn);
        favButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses").child(course.courseId);

                Course markAsFavourite = new Course(course.courseId, course.getName(), course.getLocation(), course.getPar(), course.getRating(), true, course.getLat(), course.getLon());

                databaseReference.setValue(markAsFavourite);

                Toast.makeText(getContext(), "Updated Course", Toast.LENGTH_SHORT).show();
            }
        });

        return courseListItem;
//        if(convertView == null) {
//            //inflate the course_list_layout view
//            convertView = theInflater.inflate(R.layout.course_list_layout, parent, false);
//        }
//        //bind the view with the data in bind view method
//        convertView = BindView(position, convertView);
//        //click listener for the delete button on the home page course list
//        Button delButton = convertView.findViewById(R.id.clDeleteBtn);
//        delButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Course course = getItem(position);
//                Log.i("favlist", "favlist: " + String.valueOf(dbFavouritesList));
//                //checks if the course is a favourite and if it is removes it from favourites
//                for (Course c : dbFavouritesList){
//                    Log.i("favlist", "c.name: " + c.name);
//                    if(c.name == course.name){
//                        dbFavouritesList.remove(course);
//                    }
//                }
//                //pop up a toast to the user which tells them which course was deleted
//                Toast toast = Toast.makeText(getContext(), "Deleted " + course.name, Toast.LENGTH_LONG);
//                toast.show();
//                //passes courses into deleteCourseRow method in db helper which removes it from the db
//                //myDb.deleteCourseRow(course);
//                //remove course from the dbCourseList
//                dbCourseList.remove(course);
//                //refreshes the list
//                notifyDataSetChanged();
//
//            }
//        });
//        //click listener for the favourite button on the course list on the home page
//        Button favButton = convertView.findViewById(R.id.clFavouriteBtn);
//        favButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Course course = getItem(position);
//                //changes the value of course.favourite to true
//                course.favourite = true;
//                Log.i("favCourse", "course = " + String.valueOf(course) + String.valueOf(course.favourite));
//                //adds course with favourite value set to 1 to the dbFavouritesList
//                dbFavouritesList.add(course);
//                //pops up a toast telling the user which course was favourited
//                Toast.makeText(getContext(), "Added " + course.name + " to favourites", Toast.LENGTH_LONG).show();
//                //passes the course name to the db helper to change the value of favourite field to 1 in the db
//                //myDb.markAsFavourite(course.name);
//                //refreshes the list
//                notifyDataSetChanged();
//            }
//        });
//
    }
//    //inserting the data to the view
//    private View BindView(int position, View courseListView){
//        Course course = getItem(position);
//        //getting the views on the page and setting them to the appropriate values
//        TextView courseName = courseListView.findViewById(R.id.clCourseName);
//        courseName.setText(course.name);
//        TextView courseLocation = courseListView.findViewById(R.id.clCourseLocation);
//        courseLocation.setText(course.location);
//        TextView coursePar = courseListView.findViewById(R.id.clCoursePar);
//        coursePar.setText("Par " + String.valueOf(course.par));
//        RatingBar courseRating = courseListView.findViewById(R.id.clCourseRating);
//        courseRating.setRating((float) course.rating);
//
//        return courseListView;
//    }
}
