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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import cormac.golfpal.R;
import cormac.golfpal.models.Course;
import static android.widget.Toast.LENGTH_SHORT;

public class CourseListViewAdapter extends ArrayAdapter<Course> {
    //layout inflater
    private List<Course> courseList;
    private Activity context;
    FirebaseUser user;

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
                user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses").child(course.courseId);
                databaseReference.removeValue();
                Toast.makeText(getContext(), "Course Deleted", LENGTH_SHORT).show();
            }
        });

        Button favButon = courseListItem.findViewById(R.id.clFavouriteBtn);
        favButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                user = FirebaseAuth.getInstance().getCurrentUser();
                Course markAsFavourite = new Course(course.courseId, course.getName(), course.getLocation(), course.getPar(), course.getRating(), true, course.getLat(), course.getLon());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses").child(course.courseId);
                databaseReference.setValue(markAsFavourite);
                Toast.makeText(getContext(), "Course Favourited", LENGTH_SHORT).show();
            }
        });
        return courseListItem;
    }
}

