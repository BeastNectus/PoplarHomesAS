package com.szy.loginscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder> {

    private List<Building> buildings;

    public BuildingAdapter() {
        this.buildings = new ArrayList<>();
    }

    public void setBuildings(List<GetBuildingsQuery.Building> buildings) {
        List<Building> updatedBuildings = new ArrayList<>();
        for (GetBuildingsQuery.Building building : buildings) {
            int rent = (int) Math.round(building.rent());
            String imageUrl = building.imageUrl(); // Add this line to retrieve the imageURL
            Building updatedBuilding = new Building(building.address(), building.baths(), building.beds(), rent, imageUrl);
            updatedBuildings.add(updatedBuilding);
        }
        this.buildings = updatedBuildings;
        notifyDataSetChanged();
    }

    public void addBuilding(GetBuildingsQuery.Building building) {
        int rent = (int) Math.round(building.rent());
        String imageUrl = building.imageUrl();
        Building newBuilding = new Building(building.address(), building.baths(), building.beds(), rent, imageUrl);
        buildings.add(newBuilding);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building building = buildings.get(position);

        holder.addressTextView.setText(building.getAddress());
        holder.bathsTextView.setText(String.valueOf(building.getBaths()));
        holder.bedsTextView.setText(String.valueOf(building.getBeds()));
        holder.rentTextView.setText(String.valueOf(building.getRent()));

        Picasso.get().load(building.getImageURL()).into(holder.buildingImageView);
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class BuildingViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        TextView bathsTextView;
        TextView bedsTextView;
        TextView rentTextView;

        ImageView buildingImageView;

        public BuildingViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            bathsTextView = itemView.findViewById(R.id.bathsTextView);
            bedsTextView = itemView.findViewById(R.id.bedsTextView);
            rentTextView = itemView.findViewById(R.id.rentTextView);
            buildingImageView = itemView.findViewById(R.id.buildingImageView);

        }
    }

    public static class Building {
        private String address;
        private int baths;
        private int beds;
        private int rent;

        private String imageUrl;

        public Building(String address, int baths, int beds, int rent, String imageUrl) {
            this.address = address;
            this.baths = baths;
            this.beds = beds;
            this.rent = rent;
            this.imageUrl = imageUrl;
        }

        public String getAddress() {
            return address;
        }

        public int getBaths() {
            return baths;
        }

        public int getBeds() {
            return beds;
        }

        public int getRent() {
            return rent;
        }

        public String getImageURL() {
            return imageUrl;
        }
    }
}



