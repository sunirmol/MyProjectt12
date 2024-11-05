package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private RadioGroup roleGroup;
    private Button loginButton;
    private TextView forgotPassword, registerNow;
    private ImageView togglePasswordVisibility;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;

    // Replace this with your actual admin email
    private static final String ADMIN_EMAIL = "sunirmol.stu2019@juniv.edu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        username = findViewById(R.id.loginUsername); // Email input
        password = findViewById(R.id.loginPassword);
        roleGroup = findViewById(R.id.roleGroup);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        registerNow = findViewById(R.id.registerNow);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        // Handle password visibility toggle
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye); // Set eye icon
                isPasswordVisible = false;
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.eye_closed); // Set close-eye icon
                isPasswordVisible = true;
            }
            password.setSelection(password.getText().length());
        });

        // Handle login button click
        loginButton.setOnClickListener(v -> attemptLogin());

        // Handle forgot password click
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Handle register now click
        registerNow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String usernameInput = username.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        // Validate fields
        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check selected role
        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(MainActivity.this, "Please select a role (Public or Police)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine if Police (admin) role is selected
        boolean isPoliceRoleSelected = (selectedRoleId == R.id.radioPolice);

        // If admin email is used but the Police role isn't selected, show an error
        if (usernameInput.equals(ADMIN_EMAIL) && !isPoliceRoleSelected) {
            Toast.makeText(MainActivity.this, "Invalid role for admin credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        // If the user selects Police role but doesn't use the admin email, show an error
        if (isPoliceRoleSelected && !usernameInput.equals(ADMIN_EMAIL)) {
            Toast.makeText(MainActivity.this, "Only admin email can access Police role", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with Firebase Authentication
        mAuth.signInWithEmailAndPassword(usernameInput, passwordInput)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Redirect based on the selected role
                        if (selectedRoleId == R.id.radioPublic) {
                            // Launch User Dashboard for public users
                            startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                        } else if (selectedRoleId == R.id.radioPolice) {
                            // Launch Police Dashboard for police admin
                            startActivity(new Intent(MainActivity.this, PoliceDashboardActivity.class));
                        }

                        finish(); // Close MainActivity
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Helper method to check if the logged-in user is the admin
    private boolean isPoliceAdmin(FirebaseUser user) {
        return user != null && user.getEmail().equals(ADMIN_EMAIL);
    }
}
