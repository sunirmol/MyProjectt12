package com.example.myprojectt;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment; // Add this import
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LawArticleActivity extends AppCompatActivity {

    private ImageView homeIcon;
    private ImageView logoutIcon;
    private Button downloadPdf1;
    private Button downloadPdf2;

    // URLs for the PDFs (adjusted for direct access)
    private String pdfUrl1 = "https://drive.google.com/file/d/1R7CP2XxtyKICutTHgZdRIoKMCUIhkgE3/view?usp=sharing";
    private String pdfUrl2 = "https://drive.google.com/file/d/1QR1cuSg-U4tbqY8cUOo1UTJZ0WSxbzvd/view?usp=sharing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_article);

        // Initialize views
        homeIcon = findViewById(R.id.homeIcon);
        logoutIcon = findViewById(R.id.logoutIcon);
        downloadPdf1 = findViewById(R.id.downloadPdf1);
        downloadPdf2 = findViewById(R.id.downloadPdf2);

        setUpClickListeners();
    }

    private void setUpClickListeners() {
        // Set onClickListener for home icon
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(LawArticleActivity.this,UserDashboardActivity.class);
            startActivity(intent);
            finish(); // Close this activity
        });

        // Set onClickListener for logout icon
        logoutIcon.setOnClickListener(view -> {
            // Handle logout logic (clear shared preferences or any user session management)
            Intent intent = new Intent(LawArticleActivity.this, MainActivity.class); // Change to your actual login activity
            startActivity(intent);
            finish(); // Close this activity
        });

        // Set onClickListener for viewing PDF 1
        downloadPdf1.setOnClickListener(view -> viewPdf(pdfUrl1));

        // Set onClickListener for viewing PDF 2
        downloadPdf2.setOnClickListener(view -> viewPdf(pdfUrl2));
    }

    // Method to open the PDF URL
    private void viewPdf(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url)); // Use Uri.parse to convert the string URL into a Uri
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Optional: to not keep this activity in history
        startActivity(intent);
    }
}
