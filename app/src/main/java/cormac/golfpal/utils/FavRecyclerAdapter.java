package cormac.golfpal.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import cormac.golfpal.R;
import cormac.golfpal.activities.Favourite;
import cormac.golfpal.models.Course;

import static cormac.golfpal.activities.Base.dbFavouritesList;

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.ViewHolder> {

    private final ArrayList<Course> favouriteList;
    Favourite favourite = new Favourite();

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
        holder.favCoursePrice.setText("€" + String.valueOf(course.price) + "0");
        holder.favCourseRating.setRating((float) course.rating);

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
        Log.i("removefavourite", "deleteFavourite: course.name = " + course.name);
        dbFavouritesList.remove(position);
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
        public RatingBar favCourseRating;
        public Button removeFavButton;

        public ViewHolder(View itemView) {
            super(itemView);

            favCourseName = itemView.findViewById(R.id.favCourseName);
            favCourseLocation = itemView.findViewById(R.id.favCourseLocation);
            favCoursePrice = itemView.findViewById(R.id.favCoursePrice);
            favCourseRating = itemView.findViewById(R.id.clCourseRating);
            removeFavButton = itemView.findViewById(R.id.favRemove);
        }
    }
}
