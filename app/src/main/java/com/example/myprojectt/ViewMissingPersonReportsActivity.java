package com.example.myprojectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.Map;

public class ViewMissingPersonReportsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout missingpersonsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_missing_person_reports);

        db = FirebaseFirestore.getInstance();
        missingpersonsa = findViewById(R.id.missingpersonsa);

        loadMissingPersonReports();
    }

    private void loadMissingPersonReports() {
        db.collection("missingpersons")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> reports = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot report : reports) {
                            Map<String, Object> reportData = report.getData();
                            if (reportData != null) {
                                displayReport(reportData);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewMissingPersonReportsActivity.this, "Failed to load reports.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayReport(Map<String, Object> reportData) {
        View reportView = LayoutInflater.from(this).inflate(R.layout.missing_person_report_item, null, false);

        TextView nameTextView = reportView.findViewById(R.id.nameTextView);
        TextView ageTextView = reportView.findViewById(R.id.ageTextView);
        TextView genderTextView = reportView.findViewById(R.id.genderTextView);
        TextView physicalDescTextView = reportView.findViewById(R.id.physicalDescriptionTextView);
        TextView clothingDescTextView = reportView.findViewById(R.id.clothingDescriptionTextView);
        TextView lastSeenTextView = reportView.findViewById(R.id.lastSeenTextView);
        TextView statusTextView = reportView.findViewById(R.id.statusTextView);
        TextView reportedByTextView = reportView.findViewById(R.id.reportedByTextView);
        TextView mobileNumberTextView = reportView.findViewById(R.id.mobileNumberTextView);
        ImageView missingPersonImageView = reportView.findViewById(R.id.missingPersonImageView);

        // Set the missing person report details
        nameTextView.setText("Name: " + reportData.get("name"));
        ageTextView.setText("Age: " + reportData.get("age"));
        genderTextView.setText("Gender: " + reportData.get("gender"));
        physicalDescTextView.setText("Physical Description: " + reportData.get("physicalDescription"));
        clothingDescTextView.setText("Clothing Description: " + reportData.get("clothingDescription"));
        lastSeenTextView.setText("Last Seen Location: " + reportData.get("lastSeenLocation"));
        statusTextView.setText("Status: " + reportData.get("status"));

        // Fetch and display the image from Firebase Storage using the imageUrl
        String imageUrl = (String) reportData.get("imageUrl");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Use Glide to load the image from the URL into the ImageView
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)  // Optional placeholder image
                    .error(R.drawable.error_image)              // Optional error image
                    .into(missingPersonImageView);
        } else {
            // Hide ImageView if no image is available
            missingPersonImageView.setVisibility(View.GONE);
        }

        String userId = (String) reportData.get("userId");

        // Fetch user details and display them
        fetchUserDetails(userId, reportedByTextView, mobileNumberTextView);

        // Add the reportView to the reportListLayout
        missingpersonsa.addView(reportView);
    }

    private void fetchUserDetails(String userId, TextView reportedByTextView, TextView mobileNumberTextView) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot userSnapshot) {
                        if (userSnapshot.exists()) {
                            String userName = userSnapshot.getString("name");  // Correct field for name
                            String mobileNumber = userSnapshot.getString("phone");  // Correct field for phone
                            reportedByTextView.setText("Submitted by: " + userName);
                            mobileNumberTextView.setText("Mobile Number: " + mobileNumber);
                        } else {
                            reportedByTextView.setText("Submitted by: Unknown");
                            mobileNumberTextView.setText("Mobile Number: N/A");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reportedByTextView.setText("Submitted by: Error");
                        mobileNumberTextView.setText("Mobile Number: Error");
                    }
                });
    }
}
