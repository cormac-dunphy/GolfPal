package cormac.golfpal.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.activities.Favourite;
import cormac.golfpal.models.Course;

import static android.content.ContentValues.TAG;
import static cormac.golfpal.activities.Base.dbFavouritesList;
import static cormac.golfpal.activities.Base.favouriteList;

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.ViewHolder> {

    private final ArrayList<Course> favouriteList;

    public FavRecyclerAdapter(ArrayList<Course> favouriteList) {
        this.favouriteList = favouriteList;
    }

    @Override
    public FavRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavRecyclerAdapter.ViewHolder holder, final int position) {
        Course course = favouriteList.get(position);

        holder.favCourseName.setText(course.name);
        holder.favCourseLocation.setText(course.location);
        holder.favCoursePrice.setText(String.valueOf(course.price));

        holder.removeFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFavourite(position);
            }
        });
    }

    private void deleteFavourite(int position) {
        Log.i("deletefavourite", "course at position: " + String.valueOf(getCourse(position)));

        Course course = getCourse(position);



        favouriteList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favouriteList.size());
        notifyDataSetChanged();
    }

    private Course getCourse(int position){
        return dbFavouritesList.get(position);
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView favCourseName;
        public TextView favCourseLocation;
        public TextView favCoursePrice;
        public Button removeFavButton;

        public ViewHolder(View itemView) {
            super(itemView);

            favCourseName = itemView.findViewById(R.id.favCourseName);
            favCourseLocation = itemView.findViewById(R.id.favCourseLocation);
            favCoursePrice = itemView.findViewById(R.id.favCoursePrice);
            removeFavButton = itemView.findViewById(R.id.favRemove);
        }
    }
}
