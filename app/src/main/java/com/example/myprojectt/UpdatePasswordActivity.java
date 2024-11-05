package com.example.myprojectt;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText newPasswordField, confirmNewPasswordField;
    private Button updatePasswordButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password); // Ensure this matches your XML layout file name

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Initialize UI elements
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmNewPasswordField = findViewById(R.id.confirmNewPasswordField);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        // Set up the button click listener
        updatePasswordButton.setOnClickListener(view -> updatePassword());
    }

    private void updatePassword() {
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmNewPasswordField.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
            newPasswordField.setError("Password must be at least 6 characters");
            newPasswordField.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword) || !newPassword.equals(confirmPassword)) {
            confirmNewPasswordField.setError("Passwords do not match");
            confirmNewPasswordField.requestFocus();
            return;
        }

        // Update password in Firebase Authentication
        user.updatePassword(newPassword)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(UpdatePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(UpdatePasswordActivity.this, "Failed to update password: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
