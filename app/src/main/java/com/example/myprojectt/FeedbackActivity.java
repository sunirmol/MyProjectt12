package com.example.myprojectt;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private Spinner spinnerCategory;
    private RatingBar ratingBar;
    private EditText etFeedback;
    private CheckBox cbAnonymous;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        spinnerCategory = findViewById(R.id.spinner_category);
        ratingBar = findViewById(R.id.rating_bar);
        etFeedback = findViewById(R.id.et_feedback);
        cbAnonymous = findViewById(R.id.cb_anonymous);
        btnSubmit = findViewById(R.id.btn_submit_feedback);

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        String category = spinnerCategory.getSelectedItem().toString();
        float rating = ratingBar.getRating();
        String feedbackText = etFeedback.getText().toString();
        boolean isAnonymous = cbAnonymous.isChecked();
        String userId = isAnonymous ? "anonymous" : auth.getCurrentUser().getUid();

        // Create feedback data with timestamp
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("userId", userId);
        feedbackData.put("rating", rating);
        feedbackData.put("category", category);
        feedbackData.put("feedbackText", feedbackText);
        feedbackData.put("isAnonymous", isAnonymous);
        feedbackData.put("timestamp", Timestamp.now()); // Add current timestamp

        // Save to Firestore
        db.collection("feedbacks")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
