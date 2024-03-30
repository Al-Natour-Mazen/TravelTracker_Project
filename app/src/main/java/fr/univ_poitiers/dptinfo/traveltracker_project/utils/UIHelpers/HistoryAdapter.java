package fr.univ_poitiers.dptinfo.traveltracker_project.utils.UIHelpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.univ_poitiers.dptinfo.traveltracker_project.DataBase.Entities.Trip;
import fr.univ_poitiers.dptinfo.traveltracker_project.DetailsTripActivity;
import fr.univ_poitiers.dptinfo.traveltracker_project.R;
import fr.univ_poitiers.dptinfo.traveltracker_project.utils.LogHelper;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final List<Trip> historyList;
    private final Context context;

    public HistoryAdapter(List<Trip> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = historyList.get(position);
        holder.tripNameTextView.setText(trip.getName());
        String Location = trip.getCity() + ", " + trip.getCountry();
        holder.tripLocationTextView.setText(Location);
        holder.tripDateTextView.setText(trip.getDepartureDate());

        // Set click listener to launch DetailActivity with trip data
        holder.seeDetailsButton.setOnClickListener(v -> {
            LogHelper.logError("", trip.toString());
            Intent intent = new Intent(context, DetailsTripActivity.class);
            intent.putExtra("TripToSee", trip);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tripNameTextView;
        TextView tripLocationTextView;
        TextView tripDateTextView;
        ImageButton seeDetailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTextView = itemView.findViewById(R.id.text_trip_name);
            tripLocationTextView = itemView.findViewById(R.id.text_trip_location);
            tripDateTextView = itemView.findViewById(R.id.text_trip_date);
            seeDetailsButton = itemView.findViewById(R.id.button_see_trip_details);
        }
    }
}