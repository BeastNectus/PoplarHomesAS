package com.szy.loginscreen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;



public class HomeScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BuildingAdapter adapter;
    private ApolloClient apolloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("OnCreate()");
        setContentView(R.layout.home_screen);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BuildingAdapter();
        recyclerView.setAdapter(adapter);

        apolloClient = ApolloClientProvider.getApolloClient();

        Button addPropertyBtn = findViewById(R.id.btnAddProperty);
        addPropertyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPropertyDialog();
            }
        });
        fetchBuildings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Perform any necessary cleanup tasks here
        System.out.println("OnDestroy()");
    }




    private void fetchBuildings() {
        apolloClient.query(GetBuildingsQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetBuildingsQuery.Data>() {
                    @Override
                    public void onResponse(@NonNull Response<GetBuildingsQuery.Data> response) {
                        final List<GetBuildingsQuery.Building> buildings = response.getData().buildings();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setBuildings(buildings);

                                for (GetBuildingsQuery.Building building : buildings) {
                                    System.out.println (building.address());
                                    System.out.println (building.baths());
                                    System.out.println (building.beds());
                                    System.out.println (building.rent());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull ApolloException e) {
                        e.printStackTrace();
                    }
                });
    }


    private void addBuilding(String address, int baths, int beds, int rent) {
        AddBuildingMutation mutation = new AddBuildingMutation(address, baths, beds, rent);

        apolloClient.mutate(mutation)
                .enqueue(new ApolloCall.Callback<AddBuildingMutation.Data>() {
                    @Override
                    public void onResponse(@NonNull Response<AddBuildingMutation.Data> response) {
                        if (response.getData() != null && response.getData().addBuilding() != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fetchBuildings();
                                    Log.d("HomeScreen", "Building added successfully");
                                }
                            });
                        } else {
                            Log.e("HomeScreen", "Failed to add building");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull ApolloException e) {
                        e.printStackTrace();
                        Log.e("HomeScreen", "Failed to add building");
                    }
                });
    }



    private void showAddPropertyDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_property, null);

        final EditText addressEditText = dialogView.findViewById(R.id.addressEditText);
        final EditText bathsEditText = dialogView.findViewById(R.id.bathsEditText);
        final EditText bedsEditText = dialogView.findViewById(R.id.bedsEditText);
        final EditText rentEditText = dialogView.findViewById(R.id.rentEditText);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Add Property")
                .setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address = addressEditText.getText().toString();
                        int baths = Integer.parseInt(bathsEditText.getText().toString());
                        int beds = Integer.parseInt(bedsEditText.getText().toString());
                        int rent = Integer.parseInt(rentEditText.getText().toString());

                        addBuilding(address, baths, beds, rent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}




