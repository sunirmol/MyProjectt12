package com.example.myprojectt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Ensure you have CardView imported for styling

public class PoliceStationNearMeActivity extends AppCompatActivity {

    private CardView policeStationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard); // Ensure this layout file exists

        // Initialize the CardView for police station
        policeStationCard = findViewById(R.id.policeStationCard);

        // Set click listener for the police station card
        policeStationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNearbyPoliceStations();
            }
        });
    }

    private void showNearbyPoliceStations() {
        // Create a URI for the Google Maps search
        String uri = "geo:0,0?q=police+station"; // Change "police+station" to any other query if needed
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps"); // Optional: Open only in Google Maps
        startActivity(intent);
    }
}
