package cormac.golfpal.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.text.DecimalFormat;
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
        //inflate course list view
        View courseListItem = inflater.inflate(R.layout.course_list_layout, null, true);

        TextView courseName = courseListItem.findViewById(R.id.clCourseName);
        TextView coursePar = courseListItem.findViewById(R.id.clCoursePar);
        RatingBar courseRating = courseListItem.findViewById(R.id.clCourseRating);

        Course course = courseList.get(position);

        courseName.setText(course.getName());
        //remove the .0 after the par value
        DecimalFormat format = new DecimalFormat("0.#");
        String par = String.valueOf(format.format(course.getPar()));

        coursePar.setText("Par " + par);
        courseRating.setRating((float) course.getRating());

        Button deleteButton = courseListItem.findViewById(R.id.clDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get course that user clicked
                Course course = getItem(position);
                user = FirebaseAuth.getInstance().getCurrentUser();
                //gets database reference of the course the user clicked
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses").child(course.courseId);
                //show alert dialog before deleting a course
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                aBuilder.setMessage("Are you sure you want to delete " + course.getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //remove the course from the database
                                databaseReference.removeValue();
                                Toast.makeText(getContext(), "Course Deleted", LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = aBuilder.create();
                alertDialog.setTitle("Delete a Course");
                alertDialog.show();
            }
        });

        Button favButon = courseListItem.findViewById(R.id.clFavouriteBtn);
        favButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Course course = getItem(position);
                user = FirebaseAuth.getInstance().getCurrentUser();
                //chosen course with favourite value set to true
                Course markAsFavourite = new Course(course.courseId, course.getName(), course.getLocation(), course.getPar(), course.getRating(), true, course.getLat(), course.getLon());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("courses").child(course.courseId);
                //set database reference to the course with favourite value set to true
                databaseReference.setValue(markAsFavourite);
                Toast.makeText(getContext(), "Course Favourited", LENGTH_SHORT).show();
            }
        });
        return courseListItem;
    }
}

