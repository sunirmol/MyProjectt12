package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ComplaintSuccessActivity extends AppCompatActivity {

    private Button btnBackToDashboard;
    private Button btnViewSubmission;  // Button to view submission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_success2); // Linking to the XML file

        // Initialize buttons
        btnBackToDashboard = findViewById(R.id.btn_back_to_dashboard);
        btnViewSubmission = findViewById(R.id.btn_view_submission); // Initialize View Submission button

        // Set click listener for "Back to Dashboard" button
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity (dashboard)
                Intent intent = new Intent(ComplaintSuccessActivity.this, UserDashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear all previous activities
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });

        // Set click listener for "View Submission" button
        btnViewSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the ViewSubmissionActivity to display the submitted document
                Intent intent = new Intent(ComplaintSuccessActivity.this, ViewSubmissionActivity.class);
                startActivity(intent); // Start the ViewSubmissionActivity
            }
        });
    }
}
