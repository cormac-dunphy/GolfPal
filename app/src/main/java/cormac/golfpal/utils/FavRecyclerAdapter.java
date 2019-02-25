package cormac.golfpal.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import cormac.golfpal.R;
import cormac.golfpal.activities.Base;
import cormac.golfpal.models.Course;

import static cormac.golfpal.activities.Base.dbFavouritesList;

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.ViewHolder> {

    private final ArrayList<Course> favouriteList;
    //Favourite favourite = new Favourite();
    //Base base = new Base();

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

        Locale locale = Locale.FRANCE;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        String priceString = formatter.format(course.price);
        Log.i("courselist", "onBindViewHolder: priceString = " + priceString);
        holder.favCoursePrice.setText(priceString);
        holder.favCourseRating.setRating((float) course.rating);

        holder.removeFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFavourite(position);
            }
        });
    }

    private void deleteFavourite(int position) {
        Course course = getCourse(position);
        course.favourite = false;
        Log.i("removefavourite", "favouriteList = " + Base.favouriteList.size());
        for(Course c : Base.favouriteList){
            //Log.i("removeFavourite", "c.favourite = " + c.favourite);
            if(c.getName() != null && c.getName().contains(course.name)){
                int index = Base.favouriteList.indexOf(c);
                Base.favouriteList.set(index, course);
            }
        }
//        favouriteList.remove(course);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, favouriteList.size());
//        notifyDataSetChanged();
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
            favCourseRating = itemView.findViewById(R.id.favCourseRating);
            removeFavButton = itemView.findViewById(R.id.favRemove);
        }
    }
}
