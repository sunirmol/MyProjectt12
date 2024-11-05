package com.example.myprojectt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button sendResetEmailButton;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Reference UI elements
        emailInput = findViewById(R.id.emailInput);
        sendResetEmailButton = findViewById(R.id.sendResetEmailButton);

        // Set up the button click listener to send the password reset email
        sendResetEmailButton.setOnClickListener(v -> sendPasswordResetEmail());
    }

    // Method to send password reset email
    private void sendPasswordResetEmail() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send password reset email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent. Check your inbox.", Toast.LENGTH_LONG).show();
                        // Navigate to login screen after sending reset email
                        Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If the email is not registered or an error occurs
                        Toast.makeText(ForgotPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
