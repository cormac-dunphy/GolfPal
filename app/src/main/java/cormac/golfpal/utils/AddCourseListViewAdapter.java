package cormac.golfpal.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cormac.golfpal.R;
import cormac.golfpal.models.Course;

public class AddCourseListViewAdapter extends ArrayAdapter<Course> {
    private List<Course> courseList;
    private Activity context;

    public AddCourseListViewAdapter(Activity context, List<Course> courseList) {
        super(context, R.layout.course_list_layout, courseList);
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View courseListItem = inflater.inflate(R.layout.add_course_list_layout, null, true);

        Course course = courseList.get(position);

        TextView courseName = courseListItem.findViewById(R.id.addCourseNameTV);
        TextView coursePar = courseListItem.findViewById(R.id.addCourseParTV);

        //remove the .0 after the par value
        DecimalFormat format = new DecimalFormat("0.#");
        String par = String.valueOf(format.format(course.getPar()));

        courseName.setText(course.getName());
        coursePar.setText("Par " + par);

        return courseListItem;
    }
}
