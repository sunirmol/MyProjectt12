package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView profileImage;
    private EditText nameValue, phoneValue, addressValue;
    private TextView nidValue;
    private Button editProfileButton, updatePasswordButton, saveChangesButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileImage = findViewById(R.id.profileImage);
        nameValue = findViewById(R.id.nameValue);
        phoneValue = findViewById(R.id.phoneValue);
        addressValue = findViewById(R.id.addressValue);
        nidValue = findViewById(R.id.nidValue);
        editProfileButton = findViewById(R.id.editProfileButton);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        saveChangesButton = findViewById(R.id.saveChangesButton);
        saveChangesButton.setVisibility(View.GONE);

        loadUserProfile();

        editProfileButton.setOnClickListener(view -> {
            enableEditing();
            editProfileButton.setVisibility(View.GONE);
            saveChangesButton.setVisibility(View.VISIBLE);
        });

        saveChangesButton.setOnClickListener(view -> saveChanges());

        // Set click listener for Update Password button
        updatePasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String address = documentSnapshot.getString("address");
                        String nid = documentSnapshot.getString("nid");

                        nameValue.setText(name != null ? name : "N/A");
                        phoneValue.setText(phone != null ? phone : "N/A");
                        addressValue.setText(address != null ? address : "N/A");
                        nidValue.setText((nid != null ? nid : "N/A") + " (Verified)");
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void enableEditing() {
        nameValue.setEnabled(true);
        phoneValue.setEnabled(true);
        addressValue.setEnabled(true);
    }

    private void saveChanges() {
        String userId = mAuth.getCurrentUser().getUid();

        String updatedName = nameValue.getText().toString();
        String updatedPhone = phoneValue.getText().toString();
        String updatedAddress = addressValue.getText().toString();

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", updatedName);
        updates.put("phone", updatedPhone);
        updates.put("address", updatedAddress);

        db.collection("users").document(userId)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();

                    nameValue.setEnabled(false);
                    phoneValue.setEnabled(false);
                    addressValue.setEnabled(false);

                    saveChangesButton.setVisibility(View.GONE);
                    editProfileButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating profile", e);
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}
