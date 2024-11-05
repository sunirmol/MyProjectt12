package com.example.myprojectt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ReportMissingPersonActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText missingPersonName, missingPersonAge, lastSeenLocation, lastSeenDateTime, physicalDescription, clothingDescription;
    private ImageView selectedImageView;
    private Button selectImageButton, submitButton;
    private RadioGroup genderRadioGroup;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_missing_person);

        missingPersonName = findViewById(R.id.missingPersonName);
        missingPersonAge = findViewById(R.id.missingPersonAge);
        lastSeenLocation = findViewById(R.id.lastSeenLocation);
        lastSeenDateTime = findViewById(R.id.lastSeenDateTime);
        physicalDescription = findViewById(R.id.physicalDescription);
        clothingDescription = findViewById(R.id.clothingDescription);
        selectedImageView = findViewById(R.id.selectedImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        submitButton = findViewById(R.id.submitButton);
        genderRadioGroup = findViewById(R.id.genderRadioGroup); // Assuming you have this in your layout

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("images");

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
            selectedImageView.setVisibility(View.VISIBLE); // Make the ImageView visible once the image is selected
        }
    }

    private void submitReport() {
        if (imageUri != null) {
            uploadImageToFirebase();
        } else {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        String imageId = System.currentTimeMillis() + ""; // or use any unique ID

        StorageReference fileReference = storageRef.child(userId).child(imageId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveReportToFirestore(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportMissingPersonActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveReportToFirestore(String imageUrl) {
        Map<String, Object> report = new HashMap<>();
        report.put("name", missingPersonName.getText().toString());
        report.put("age", missingPersonAge.getText().toString());
        report.put("lastSeenLocation", lastSeenLocation.getText().toString());
        report.put("lastSeenDateTime", lastSeenDateTime.getText().toString());
        report.put("physicalDescription", physicalDescription.getText().toString());
        report.put("clothingDescription", clothingDescription.getText().toString());
        report.put("imageUrl", imageUrl);
        report.put("status", "Submitted"); // Set the status field
        report.put("userId", mAuth.getCurrentUser().getUid());

        // Get selected gender
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        if (selectedGenderButton != null) {
            report.put("gender", selectedGenderButton.getText().toString()); // Store selected gender
        }

        db.collection("missingpersons")
                .add(report)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ReportMissingPersonActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportMissingPersonActivity.this, "Failed to submit report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
