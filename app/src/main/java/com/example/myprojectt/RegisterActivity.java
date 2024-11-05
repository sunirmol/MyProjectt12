package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, nid, age, phone, address, email, password, confirmPassword;
    private Button registerButton;
    private TextView loginLink;
    private RadioGroup genderRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Initialize Views
        name = findViewById(R.id.publicName);
        nid = findViewById(R.id.publicNID);
        age = findViewById(R.id.publicAge);
        phone = findViewById(R.id.publicPhone);
        address = findViewById(R.id.publicAddress);
        email = findViewById(R.id.publicEmail);
        password = findViewById(R.id.publicPassword);
        confirmPassword = findViewById(R.id.publicConfirmPassword);
        registerButton = findViewById(R.id.publicRegisterButton);
        loginLink = findViewById(R.id.loginLink);  // Add login link
        genderRadioGroup = findViewById(R.id.genderRadioGroup);  // Initialize gender RadioGroup

        // Register Button Click Listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registerUser();
                }
            }
        });

        // Login link click listener to navigate to login page
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login activity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to validate form inputs
    private boolean validateInputs() {
        if (name.getText().toString().isEmpty() || nid.getText().toString().isEmpty() ||
                age.getText().toString().isEmpty() || phone.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to register the user with Firebase Authentication
    private void registerUser() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        // Register user using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, store additional user info
                            String userId = mAuth.getCurrentUser().getUid();
                            storeUserInfo(userId);
                        } else {
                            // If registration fails, show error message
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to store additional user info in Firestore
    private void storeUserInfo(String userId) {
        String userName = name.getText().toString();
        String userNID = nid.getText().toString();
        String userAge = age.getText().toString();
        String userPhone = phone.getText().toString();
        String userAddress = address.getText().toString();
        String userSex = getSelectedGender();  // Get the selected gender

        // Create a map to store user info
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", userName);
        userMap.put("nid", userNID);
        userMap.put("age", userAge);
        userMap.put("sex", userSex);
        userMap.put("phone", userPhone);
        userMap.put("address", userAddress);

        // Store the user info in Firestore
        db.collection("users").document(userId)
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Successfully stored user info
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to login activity
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();  // Close the current activity
                        } else {
                            // Failed to store user info
                            Toast.makeText(RegisterActivity.this, "Failed to store user info: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to get the selected gender from the RadioGroup
    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedId);
        return selectedGender != null ? selectedGender.getText().toString() : null;
    }
}
