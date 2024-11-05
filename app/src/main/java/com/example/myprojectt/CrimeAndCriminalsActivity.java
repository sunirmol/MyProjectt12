package com.example.myprojectt;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeAndCriminalsActivity extends AppCompatActivity {

    private EditText locationSearchField;
    private Button searchButton;
    private RecyclerView crimeListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_and_criminals);

        locationSearchField = findViewById(R.id.locationSearchField);
        searchButton = findViewById(R.id.searchButton);
        crimeListRecyclerView = findViewById(R.id.crimeListRecyclerView);

        searchButton.setOnClickListener(view -> searchCrimes());
    }

    private void searchCrimes() {
        String location = locationSearchField.getText().toString().trim();

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call a method to fetch crimes and criminals based on the location
        //fetchCrimesAndCriminals(location);
    }

//    private void fetchCrimesAndCriminals(String location) {
//        // Implement the logic to fetch crimes and criminals from your database
//        List<Crime> crimeList = getCrimesFromDatabase(location); // Replace with actual data fetching logic
//
//        // Update RecyclerView
//        if (crimeList.isEmpty()) {
//            Toast.makeText(this, "No results found for: " + location, Toast.LENGTH_SHORT).show();
//            crimeListRecyclerView.setVisibility(View.GONE);
//        } else {
//            updateRecyclerView(crimeList);
//            crimeListRecyclerView.setVisibility(View.VISIBLE);
//        }
//    }

//    private void updateRecyclerView(List<Crime> crimeList) {
//        CrimeAdapter adapter = new CrimeAdapter(crimeList);
//        crimeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        crimeListRecyclerView.setAdapter(adapter);
//    }

    // Implement getCrimesFromDatabase to fetch data from your source
}
