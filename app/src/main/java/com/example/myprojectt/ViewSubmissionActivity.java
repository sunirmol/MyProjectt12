package com.example.myprojectt;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ViewSubmissionActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1001;

    private TextView tvFirNo, tvUserDetails, tvComplaintDetails;
    private Button btnDownload;
    private LinearLayout layoutToCapture;

    // Firestore and Firebase Authentication
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submission);

        // Initialize Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        tvFirNo = findViewById(R.id.tv_fir_no);
        tvUserDetails = findViewById(R.id.tv_user_details);
        tvComplaintDetails = findViewById(R.id.tv_complaint_details);
        btnDownload = findViewById(R.id.btn_download);
        layoutToCapture = findViewById(R.id.layout_to_capture);

        // Fetch and display complaint details
        fetchComplaintDetails();

        // Handle download button click
        btnDownload.setOnClickListener(v -> {
            btnDownload.setVisibility(View.INVISIBLE);
            Bitmap bitmap = createBitmapFromView(layoutToCapture);
            btnDownload.setVisibility(View.VISIBLE);

            // Pass both the bitmap and the unique document ID
            saveBitmap(bitmap, tvFirNo.getText().toString());
        });
    }

    private void fetchComplaintDetails() {
        String userId = auth.getCurrentUser().getUid(); // Get the logged-in user's ID

        // Query the Firestore database for the user's complaints
        db.collection("complaints")
                .whereEqualTo("UserId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Set FIR No.
                                tvFirNo.setText("FIR No: " + document.getId());

                                // Fetch and set user information
                                fetchUserInformation(document.getString("UserId"));

                                // Set complaint information
                                setComplaintInformation(document);
                            }
                        } else {
                            Toast.makeText(ViewSubmissionActivity.this, "No complaints found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewSubmissionActivity.this, "Error fetching complaint details: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserInformation(String userId) {
        // Fetch user information from the users collection using userId (document ID)
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String userDetails = "1. Name: " + task.getResult().getString("name") + "\n" +
                        "2. Mobile: " + task.getResult().getString("phone") + "\n" +
                        "3. NID: " + task.getResult().getString("nid") + "\n" +
                        "4. Address: " + task.getResult().getString("address");
                // Set user details in the TextView
                tvUserDetails.setText(userDetails);
            } else {
                tvUserDetails.setText("Error fetching user information.");
            }
        }).addOnFailureListener(e -> {
            tvUserDetails.setText("Error fetching user information: " + e.getMessage());
        });
    }

    private void setComplaintInformation(QueryDocumentSnapshot document) {
        String complaintDetails = "1. Complaint Type: " + document.getString("complaintType") + "\n" +
                "2. Incident Date and Time: " + document.getString("incidentDate") + "\n" +
                "3. Division: " + document.getString("division") + "\n" +
                "4. District: " + document.getString("district") + "\n" +
                "5. Thana: " + document.getString("thana") + "\n" +
                "6. Reporting Date: " + document.getString("reportingDate") + "\n" +
                "7. Description: " + document.getString("description");
        // Set complaint details in the TextView
        tvComplaintDetails.setText(complaintDetails);
    }

    // Create a bitmap from the complaint details layout
    private Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // Handle saving the bitmap based on Android version
    private void saveBitmap(Bitmap bitmap, String documentId) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            } else {
                saveBitmapToFile(bitmap, documentId);
            }
        } else {
            saveBitmapToFile(bitmap, documentId);
        }
    }

    // Save the generated bitmap as an image file
    private void saveBitmapToFile(Bitmap bitmap, String documentId) {
        // Use the documentId as part of the filename to ensure uniqueness
        String fileName = "Complaint_Copy_" + documentId + ".png";

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (uri != null) {
                OutputStream fos = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                Toast.makeText(this, "Complaint copy saved as image", Toast.LENGTH_SHORT).show();
                openImageInGallery(uri);
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open the saved image in the gallery or file viewer
    private void openImageInGallery(Uri imageUri) {
        if (imageUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(imageUri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No app found to view this image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Image Uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted! You can now save the image.", Toast.LENGTH_SHORT).show();
                saveBitmapToFile(createBitmapFromView(layoutToCapture), tvFirNo.getText().toString());
            } else {
                Toast.makeText(this, "Permission denied! Unable to save the image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
