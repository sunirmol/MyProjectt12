package com.example.myprojectt;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyComplaintsActivity extends AppCompatActivity {

    private ListView lvComplaints;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayList<String> complaintsList;
    private ComplaintsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);

        // Initialize Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        lvComplaints = findViewById(R.id.lv_complaints);
        complaintsList = new ArrayList<>();

        // Fetch complaints from both collections
        fetchComplaints();
    }

    private void fetchComplaints() {
        String userId = auth.getCurrentUser().getUid(); // Get the logged-in user's ID

        // Fetch complaints from the "complaints" collection
        db.collection("complaints")
                .whereEqualTo("UserId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String userInfo = document.getString("userInfo");
                                String filedByText = userInfo != null ? "Filed by: " + userInfo + "\n" : ""; // Only include if userInfo is not null

                                String complaintDetails = "Category: General\n" +
                                        "FIR No: " + document.getId() + "\n" +
                                        "Complaint Type: " + document.getString("complaintType") + "\n" +
                                        "Incident Date: " + document.getString("incidentDate") + "\n" +
                                        "Location: " + document.getString("location") + "\n" +
                                        "Division: " + document.getString("division") + "\n" +
                                        "District: " + document.getString("district") + "\n" +
                                        "Thana: " + document.getString("thana") + "\n" +
                                        "Description: " + document.getString("description") + "\n" +
                                        filedByText; // Add filed by text if available

                                String status = document.getString("status");
                                complaintsList.add(complaintDetails + "\nStatus: " + status);
                            }
                        }
                        // Fetch anonymous complaints
                        fetchAnonymousComplaints(userId);
                    } else {
                        Toast.makeText(MyComplaintsActivity.this, "Error fetching complaints: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchAnonymousComplaints(String userId) {
        // Fetch complaints from the "anonymouscomplaints" collection
        db.collection("anonymouscomplaints")
                .whereEqualTo("UserId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String complaintDetails = "Category: Anonymous\n" +
                                        "FIR No: " + document.getId() + "\n" +
                                        "Complaint Type: " + document.getString("complaintType") + "\n" +
                                        "Incident Date: " + document.getString("incidentDate") + "\n" +
                                        "Location: " + document.getString("location") + "\n" +
                                        "Division: " + document.getString("division") + "\n" +
                                        "District: " + document.getString("district") + "\n" +
                                        "Thana: " + document.getString("thana") + "\n" +
                                        "Description: " + document.getString("description") + "\n" +
                                        "Filed by: Anonymous\n"; // Always show as anonymous

                                String status = document.getString("status");
                                complaintsList.add(complaintDetails + "\nStatus: " + status);
                            }
                        }
                        displayComplaints();
                    } else {
                        Toast.makeText(MyComplaintsActivity.this, "Error fetching anonymous complaints: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayComplaints() {
        // Use custom adapter to format status in red
        adapter = new ComplaintsAdapter(this, complaintsList);
        lvComplaints.setAdapter(adapter);
    }

    // Custom ArrayAdapter to handle text formatting
    private class ComplaintsAdapter extends ArrayAdapter<String> {

        public ComplaintsAdapter(@NonNull MyComplaintsActivity context, ArrayList<String> complaintsList) {
            super(context, 0, complaintsList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            String complaintInfo = getItem(position);
            TextView tvComplaintDetails = convertView.findViewById(android.R.id.text1);
            TextView tvStatus = convertView.findViewById(android.R.id.text2);

            if (complaintInfo != null) {
                // Separate status from the rest of the complaint info
                String[] parts = complaintInfo.split("\nStatus: ");
                tvComplaintDetails.setText(parts[0]);

                // Set status text in red
                String status = parts.length > 1 ? parts[1] : "";
                SpannableString spannableStatus = new SpannableString("Status: " + status);
                spannableStatus.setSpan(new ForegroundColorSpan(Color.RED), 8, spannableStatus.length(), 0);
                tvStatus.setText(spannableStatus);
            }

            return convertView;
        }
    }
}
