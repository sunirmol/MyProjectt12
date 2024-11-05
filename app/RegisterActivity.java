import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myprojectt.R;

public class RegisterActivity {
    package com.example.myprojectt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

    public class RegisterActivity extends AppCompatActivity {

        private EditText name, nid, age, sex, phone, address, city, email, password, confirmPassword;
        private Button registerButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            // Initialize Views
            name = findViewById(R.id.publicName);
            nid = findViewById(R.id.publicNID);
            age = findViewById(R.id.publicAge);
            sex = findViewById(R.id.publicSex);
            phone = findViewById(R.id.publicPhone);
            address = findViewById(R.id.publicAddress);
            city = findViewById(R.id.publicCity);
            email = findViewById(R.id.publicEmail);
            password = findViewById(R.id.publicPassword);
            confirmPassword = findViewById(R.id.publicConfirmPassword);
            registerButton = findViewById(R.id.publicRegisterButton);

            // Register Button Click Listener
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInputs()) {
                        // Perform registration process
                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        // You can add code here to send the data to the backend
                    }
                }
            });
        }

        // Function to validate form inputs
        private boolean validateInputs() {
            if (name.getText().toString().isEmpty() || nid.getText().toString().isEmpty() ||
                    age.getText().toString().isEmpty() || sex.getText().toString().isEmpty() ||
                    phone.getText().toString().isEmpty() || address.getText().toString().isEmpty() ||
                    city.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                    password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {

                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
    }

}
