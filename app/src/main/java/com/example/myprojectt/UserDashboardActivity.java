package com.example.myprojectt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.cardview.widget.CardView;

public class UserDashboardActivity extends AppCompatActivity {

    private ImageView profileIcon;
    private ImageView logoutIcon;
    private CardView logComplaintCard;
    private CardView myComplaintsCard;
    private CardView viewCaseProgressCard;
    private CardView policeStationCard;
    private CardView viewLawArticleButton;
    private CardView missingReportCard;
    private CardView giveFeedbackCard;
    private CardView crimeAndCriminalCard;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Log.d("UserDashboardActivity", "Activity created successfully");

        // Initialize views
        profileIcon = findViewById(R.id.profileIcon);
        logoutIcon = findViewById(R.id.logoutIcon);
        logComplaintCard = findViewById(R.id.logComplaintCard);
        myComplaintsCard = findViewById(R.id.myComplaintsCard);
        viewCaseProgressCard = findViewById(R.id.viewCaseProgressCard);
        policeStationCard = findViewById(R.id.policeStationCard);
        viewLawArticleButton = findViewById(R.id.viewLawArticleButton);
        missingReportCard = findViewById(R.id.missingReportCard);
        crimeAndCriminalCard=findViewById(R.id.crimeAndCriminalCard);
        giveFeedbackCard = findViewById(R.id.giveFeedbackCard);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set click listeners for cards
        logComplaintCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, LogComplaintActivity.class);
            startActivity(intent);
        });
        crimeAndCriminalCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this,CrimeAndCriminalsActivity.class);
            startActivity(intent);
        });
        myComplaintsCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, MyComplaintsActivity.class);
            startActivity(intent);
        });

        viewCaseProgressCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, ReportMissingPersonActivity.class);
            startActivity(intent);
        });

        giveFeedbackCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });


        policeStationCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, PoliceStationNearMeActivity.class);
            startActivity(intent);
        });

        viewLawArticleButton.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, LawArticleActivity.class);
            startActivity(intent);
        });

        missingReportCard.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, ViewMissingPersonReportsActivity.class);
            startActivity(intent);
        });

        // Set onClickListeners for profileIcon and logoutIcon
        profileIcon.setOnClickListener(view -> drawerLayout.openDrawer(navigationView));
        logoutIcon.setOnClickListener(view -> {
            // Handle logout logic here
        });

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(UserDashboardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);

            } else if (itemId == R.id.nav_logout) {
                Intent profileIntent = new Intent(UserDashboardActivity.this, MainActivity.class);
                startActivity(profileIntent);

            } else if (itemId == R.id.nav_reviews) {
               // Navigate to ReviewsActivity
               Intent reviewsIntent = new Intent(UserDashboardActivity.this, ReviewsActivity.class);
                startActivity(reviewsIntent);

            } else if (itemId == R.id.nav_announcements) {
                // Navigate to AnnouncementsActivity
               // Intent announcementsIntent = new Intent(UserDashboardActivity.this, AnnouncementsActivity.class);
               // startActivity(announcementsIntent);

            } else if (itemId == R.id.nav_privacy) {///;
                // Navigate to PrivacyActivity
               // Intent privacyIntent = new Intent(UserDashboardActivity.this, PrivacyActivity.class);
               // startActivity(privacyIntent);

            }// else if (itemId == R.id.nav_contact) {
                // Navigate to ContactUsActivity
               // Intent contactIntent = new Intent(UserDashboardActivity.this, ContactUsActivity.class);
               // startActivity(contactIntent);


            drawerLayout.closeDrawers(); // Close the drawer after selection
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
