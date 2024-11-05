package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class PoliceDashboardActivity extends AppCompatActivity {

    private TextView totalReportedCrimes, totalSolvedCases;
    private ListView recentReportsList;
    private Button viewComplaintsButton, addNewReportButton, manageUsersButton, logoutButton, viewFeedbackButton;

    private FirebaseFirestore db;

    // Dropdown buttons for complaints
    private LinearLayout complaintDropdownContainer;
    private Button viewNormalComplaintsButton;
    private Button viewAnonymousComplaintsButton;
    private Button viewMissingReportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_police_dashboard2);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        viewComplaintsButton = findViewById(R.id.viewComplaintsButton);
        totalReportedCrimes = findViewById(R.id.totalReportedCrimes);
        totalSolvedCases = findViewById(R.id.totalSolvedCases);
        recentReportsList = findViewById(R.id.recentReportsList);
//        addNewReportButton = findViewById(R.id.addNewReportButton);
        manageUsersButton = findViewById(R.id.manageUsersButton);
        logoutButton = findViewById(R.id.logoutButton);
        viewFeedbackButton = findViewById(R.id.viewFeedbackButton);
        viewMissingReportsButton=findViewById(R.id.viewMissingReportsButton);
        // Dropdown views
        complaintDropdownContainer = findViewById(R.id.complaintDropdownContainer);
        viewNormalComplaintsButton = findViewById(R.id.viewNormalComplaintsButton);
        viewAnonymousComplaintsButton = findViewById(R.id.viewAnonymousComplaintsButton);

        // Load crime statistics
        loadCrimeStatistics();

        // View Complaints button action
        viewComplaintsButton.setOnClickListener(v -> toggleDropdownVisibility());

        // Normal Complaints button action
        viewNormalComplaintsButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this, NormalComplaintsActivity.class));
            complaintDropdownContainer.setVisibility(View.GONE);  // Hide dropdown after selection
        });

        // Anonymous Complaints button action
        viewAnonymousComplaintsButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this, AnonymousComplaintsActivity.class));
            complaintDropdownContainer.setVisibility(View.GONE);  // Hide dropdown after selection
        });
        // Anonymous Complaints button action
        viewMissingReportsButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this,ViewpoliceMissingPersonReportsActivity.class));
            complaintDropdownContainer.setVisibility(View.GONE);  // Hide dropdown after selection
        });
        // Manage users button action
        manageUsersButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this, ManageUsersActivity.class));
        });

        // View Feedback button action
        viewFeedbackButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this, FeedbackAdminActivity.class));
        });

        // Logout button action
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(PoliceDashboardActivity.this, MainActivity.class));
          //  FirebaseAuth.getInstance().signOut(); // Optional: sign out user from Firebase Auth
            finish(); // End the current activity
        });
    }

    private void toggleDropdownVisibility() {
        if (complaintDropdownContainer.getVisibility() == View.GONE) {
            complaintDropdownContainer.setVisibility(View.VISIBLE);
        } else {
            complaintDropdownContainer.setVisibility(View.GONE);
        }
    }

    private void loadCrimeStatistics() {
        db.collection("complaints").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int totalReports = 0;
                int solvedCases = 0;

                for (QueryDocumentSnapshot doc : task.getResult()) {
                    totalReports++;
                    if (doc.getBoolean("isSolved") != null && doc.getBoolean("isSolved")) {
                        solvedCases++;
                    }
                }

                totalReportedCrimes.setText(String.valueOf(totalReports));
                totalSolvedCases.setText(String.valueOf(solvedCases));
            } else {
                Toast.makeText(this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
