package com.example.myprojectt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogComplaintActivity extends AppCompatActivity {

    private EditText etIncidentDate, etLocation, etDescription, etReportingDate;
    private Spinner spinnerComplaintType, spinnerDivisions, spinnerDistricts, spinnerThanas;
    private CheckBox checkboxAnonymous;
    private Button btnSubmitComplaint;

    private FirebaseFirestore db;
    private CollectionReference complaintsRef;
    private CollectionReference anonymousComplaintsRef;
    private FirebaseAuth auth;

    private Map<String, List<String>> districtMap = new HashMap<>();
    private Map<String, List<String>> thanaMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_complaint);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        complaintsRef = db.collection("complaints");
        anonymousComplaintsRef = db.collection("anonymouscomplaints");

        etIncidentDate = findViewById(R.id.et_incident_date);
        etReportingDate = findViewById(R.id.et_reporting_date);
        etLocation = findViewById(R.id.et_location);
        etDescription = findViewById(R.id.et_description);
        spinnerComplaintType = findViewById(R.id.spinner_complaint_type);
        spinnerDivisions = findViewById(R.id.spinner_divisions);
        spinnerDistricts = findViewById(R.id.spinner_districts);
        spinnerThanas = findViewById(R.id.spinner_thanas);
        checkboxAnonymous = findViewById(R.id.checkbox_anonymous);
        btnSubmitComplaint = findViewById(R.id.btn_submit_complaint);

        etIncidentDate.setOnClickListener(v -> showDatePickerDialog(etIncidentDate));
        etReportingDate.setOnClickListener(v -> showDatePickerDialog(etReportingDate));
        btnSubmitComplaint.setOnClickListener(v -> submitComplaint());

        loadDivisions();
    }

    private void loadDivisions() {
        List<String> divisions = new ArrayList<>();
        divisions.add("Select Division");
        divisions.add("Dhaka");
        divisions.add("Chattagram");
        divisions.add("Sylhet");
        divisions.add("Barisal");
        divisions.add("Khulna");
        divisions.add("Rajshahi");
        divisions.add("Rangpur");
        divisions.add("Mymensingh");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDivisions.setAdapter(adapter);

        setupDistrictMap();
        setupThanaMap();

        spinnerDivisions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDivision = (String) parent.getItemAtPosition(position);
                loadDistricts(selectedDivision);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupDistrictMap() {
        districtMap.put("Dhaka", List.of("Dhaka", "Gazipur", "Narayanganj", "Faridpur", "Gopalganj", "Jamalpur", "Kishoreganj", "Madaripur", "Manikganj", "Munshiganj", "Narsingdi", "Netrokona", "Rajbari", "Shariatpur", "Tangail"));
        districtMap.put("Chattagram", List.of("Chattogram", "Bandarban", "Brahmanbaria", "Chandpur", "Comilla", "Cox's Bazar", "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"));
        districtMap.put("Sylhet", List.of("Sylhet", "Habiganj", "Moulvibazar", "Sunamganj"));
        districtMap.put("Barisal", List.of("Barishal", "Barguna", "Bhola", "Jhalokathi", "Patuakhali", "Pirojpur"));
        districtMap.put("Khulna", List.of("Khulna", "Bagerhat", "Chuadanga", "Jashore", "Jhenaidah", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira"));
        districtMap.put("Rajshahi", List.of("Rajshahi", "Bogura", "Joypurhat", "Naogaon", "Natore", "Chapainawabganj", "Pabna", "Sirajganj"));
        districtMap.put("Rangpur", List.of("Rangpur", "Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Thakurgaon"));
        districtMap.put("Mymensingh", List.of("Mymensingh", "Jamalpur", "Netrokona", "Sherpur"));
    }


    private void setupThanaMap() {
        // Dhaka Division
        thanaMap.put("Dhaka", List.of("Dhanmondi", "Mirpur", "Gulshan", "Uttara", "Tejgaon", "Mohammadpur", "Motijheel", "Ramna", "Lalbagh", "Pallabi"));
        thanaMap.put("Gazipur", List.of("Tongi", "Gazipur Sadar", "Kaliganj", "Sreepur", "Kapasia"));
        thanaMap.put("Narayanganj", List.of("Narayanganj Sadar", "Bandar", "Rupganj", "Sonargaon", "Araihazar"));
        thanaMap.put("Faridpur", List.of("Faridpur Sadar", "Bhanga", "Boalmari", "Sadarpur", "Nagarkanda"));
        thanaMap.put("Gopalganj", List.of("Gopalganj Sadar", "Kashiani", "Kotalipara", "Tungipara", "Muksudpur"));
        thanaMap.put("Jamalpur", List.of("Jamalpur Sadar", "Melandaha", "Islampur", "Dewanganj", "Bakshiganj", "Madarganj"));
        thanaMap.put("Kishoreganj", List.of("Kishoreganj Sadar", "Bhairab", "Bajitpur", "Hossainpur", "Itna", "Karimganj", "Katiadi", "Kuliarchar", "Mithamoin", "Nikli"));
        thanaMap.put("Madaripur", List.of("Madaripur Sadar", "Rajoir", "Kalkini", "Shibchar"));
        thanaMap.put("Manikganj", List.of("Manikganj Sadar", "Saturia", "Singair", "Shibalaya", "Harirampur", "Ghior", "Daulatpur"));
        thanaMap.put("Munshiganj", List.of("Munshiganj Sadar", "Sreenagar", "Sirajdikhan", "Lohajang", "Tongibari", "Gazaria"));
        thanaMap.put("Narsingdi", List.of("Narsingdi Sadar", "Raipura", "Belabo", "Monohardi", "Shibpur", "Palash"));
        thanaMap.put("Netrokona", List.of("Netrokona Sadar", "Atpara", "Barhatta", "Durgapur", "Kalmakanda", "Kendua", "Madan", "Mohanganj", "Purbadhala"));
        thanaMap.put("Rajbari", List.of("Rajbari Sadar", "Baliakandi", "Pangsha", "Kalukhali", "Goalanda"));
        thanaMap.put("Shariatpur", List.of("Shariatpur Sadar", "Zanjira", "Bhedarganj", "Naria", "Damudya", "Gosairhat"));
        thanaMap.put("Tangail", List.of("Tangail Sadar", "Madhupur", "Kalihati", "Ghatail", "Gopalpur", "Delduar", "Sakhipur", "Mirzapur", "Nagarpur", "Dhanbari"));

        // Chattogram Division
        thanaMap.put("Chattogram", List.of("Kotwali", "Panchlaish", "Double Mooring", "Pahartali", "Bakalia", "Chandgaon", "Halishahar", "Chittagong EPZ"));
        thanaMap.put("Bandarban", List.of("Bandarban Sadar", "Thanchi", "Lama", "Ruma", "Rowangchhari", "Ali Kadam", "Naikhongchhari"));
        thanaMap.put("Brahmanbaria", List.of("Brahmanbaria Sadar", "Ashuganj", "Bijoynagar", "Nasirnagar", "Nabinagar", "Sarail", "Bancharampur", "Kasba", "Akhaura"));
        thanaMap.put("Chandpur", List.of("Chandpur Sadar", "Haimchar", "Kachua", "Matlab Dakshin", "Matlab Uttar", "Shahrasti", "Faridganj"));
        thanaMap.put("Comilla", List.of("Comilla Sadar", "Daudkandi", "Muradnagar", "Debidwar", "Meghna", "Brahmanpara", "Monohorgonj", "Titas"));
        thanaMap.put("Cox's Bazar", List.of("Cox's Bazar Sadar", "Ramu", "Ukhia", "Teknaf", "Chakaria", "Pekua", "Kutubdia"));
        thanaMap.put("Feni", List.of("Feni Sadar", "Chhagalnaiya", "Daganbhuiyan", "Parshuram", "Sonagazi"));
        thanaMap.put("Khagrachhari", List.of("Khagrachhari Sadar", "Dighinala", "Lakshmichhari", "Mahalchhari", "Manikchhari", "Matiranga", "Panchhari", "Ramgarh"));
        thanaMap.put("Lakshmipur", List.of("Lakshmipur Sadar", "Raipur","Chandraganj", "Ramgati", "Ramganj", "Komol Nagar"));
        thanaMap.put("Noakhali", List.of("Noakhali Sadar", "Begumganj", "Chatkhil", "Companiganj", "Hatiya", "Senbagh", "Subarnachar"));
        thanaMap.put("Rangamati", List.of("Rangamati Sadar", "Baghaichhari", "Barkal", "Belaichhari", "Kaptai", "Juraichhari", "Langadu", "Naniarchar", "Rajasthali"));

        // Sylhet Division
        thanaMap.put("Sylhet", List.of("Sylhet Sadar", "Beanibazar", "Bishwanath", "Companiganj", "Dakshin Surma", "Fenchuganj", "Golapganj", "Jaintiapur"));
        thanaMap.put("Habiganj", List.of("Habiganj Sadar", "Ajmiriganj", "Bahubal", "Baniachong", "Chunarughat", "Lakhai", "Madhabpur"));
        thanaMap.put("Moulvibazar", List.of("Moulvibazar Sadar", "Barlekha", "Juri", "Kamalganj", "Kulaura", "Rajnagar", "Sreemangal"));
        thanaMap.put("Sunamganj", List.of("Sunamganj Sadar", "Bishwambarpur", "Chhatak", "Derai", "Dharampasha", "Dowarabazar", "Jagannathpur", "Jamalganj", "Sullah", "Tahirpur"));

        // Barisal Division
        thanaMap.put("Barishal", List.of("Barishal Sadar", "Babuganj", "Bakerganj", "Banaripara", "Gouranadi", "Hizla", "Mehendiganj", "Muladi"));
        thanaMap.put("Barguna", List.of("Barguna Sadar", "Amtali", "Betagi", "Patharghata", "Bamna", "Taltali"));
        thanaMap.put("Bhola", List.of("Bhola Sadar", "Borhanuddin", "Char Fasson", "Daulatkhan", "Lalmohan", "Manpura", "Tazumuddin"));
        thanaMap.put("Jhalokathi", List.of("Jhalokathi Sadar", "Kathalia", "Nalchity", "Rajapur"));
        thanaMap.put("Patuakhali", List.of("Patuakhali Sadar", "Bauphal", "Dashmina", "Dumki", "Kalapara", "Mirzaganj", "Galachipa"));
        thanaMap.put("Pirojpur", List.of("Pirojpur Sadar", "Bhandaria", "Kawkhali", "Mathbaria", "Nazirpur", "Nesarabad (Swarupkathi)"));

        // Khulna Division
        thanaMap.put("Khulna", List.of("Khulna Sadar", "Koyra", "Dumuria", "Paikgachha", "Rupsa", "Terokhada", "Batiaghata", "Dakop"));
        thanaMap.put("Bagerhat", List.of("Bagerhat Sadar", "Chitalmari", "Fakirhat", "Kachua", "Mollahat", "Mongla", "Morrelganj", "Rampal", "Sarankhola"));
        thanaMap.put("Chuadanga", List.of("Chuadanga Sadar", "Alamdanga", "Damurhuda", "Jibannagar"));
        thanaMap.put("Jashore", List.of("Jashore Sadar", "Bagherpara", "Chaugachha", "Keshabpur", "Manirampur", "Sharsha", "Jhikargacha"));
        thanaMap.put("Jhenaidah", List.of("Jhenaidah Sadar", "Harinakunda", "Kaliganj", "Kotchandpur", "Maheshpur", "Shailkupa"));
        thanaMap.put("Kushtia", List.of("Kushtia Sadar", "Bheramara", "Daulatpur", "Khoksa", "Kumarkhali", "Mirpur"));
        thanaMap.put("Magura", List.of("Magura Sadar", "Mohammadpur", "Shalikha", "Sreepur"));
        thanaMap.put("Meherpur", List.of("Meherpur Sadar", "Gangni", "Mujibnagar"));
        thanaMap.put("Narail", List.of("Narail Sadar", "Kalia", "Lohagara"));
        thanaMap.put("Satkhira", List.of("Satkhira Sadar", "Assasuni", "Debhata", "Kaliganj", "Kolaroa", "Shyamnagar", "Tala"));

        // Continue for Rajshahi, Rangpur, and Mymensingh divisions
        // Add other divisions and districts similarly...
        // Rajshahi Division
        thanaMap.put("Rajshahi", List.of("Rajshahi Sadar", "Boalia", "Motihar", "Shah Makhdum", "Paba", "Bagha", "Charghat", "Durgapur", "Godagari", "Mohonpur", "Puthia", "Tanore"));
        thanaMap.put("Bogura", List.of("Bogura Sadar", "Gabtali", "Shibganj", "Dhunut", "Sherpur", "Sonatala", "Sariakandi", "Adamdighi", "Nandigram", "Kahaloo"));
        thanaMap.put("Chapainawabganj", List.of("Nawabganj Sadar", "Shibganj", "Gomastapur", "Nachole", "Bholahat"));
        thanaMap.put("Naogaon", List.of("Naogaon Sadar", "Badalgachhi", "Manda", "Dhamoirhat", "Mohadevpur", "Patnitala", "Porsha", "Raninagar", "Sapahar"));
        thanaMap.put("Natore", List.of("Natore Sadar", "Bagatipara", "Baraigram", "Gurudaspur", "Lalpur", "Singra"));
        thanaMap.put("Pabna", List.of("Pabna Sadar", "Bera", "Atgharia", "Chatmohar", "Faridpur", "Ishwardi", "Santhia", "Sujanagar"));
        thanaMap.put("Sirajganj", List.of("Sirajganj Sadar", "Belkuchi", "Chauhali", "Kamarkhanda", "Kazipur", "Raiganj", "Shahjadpur", "Tarash", "Ullapara"));
        thanaMap.put("Joypurhat", List.of("Joypurhat Sadar", "Akkelpur", "Kalai", "Khetlal", "Panchbibi"));

        // Rangpur Division
        thanaMap.put("Rangpur", List.of("Rangpur Sadar", "Badarganj", "Gangachara", "Kaunia", "Mithapukur", "Pirgachha", "Pirganj", "Taraganj"));
        thanaMap.put("Dinajpur", List.of("Dinajpur Sadar", "Birganj", "Birampur", "Biral", "Bochaganj", "Chirirbandar", "Ghoraghat", "Hakimpur", "Kaharole", "Khansama", "Nawabganj", "Parbatipur"));
        thanaMap.put("Thakurgaon", List.of("Thakurgaon Sadar", "Baliadangi", "Haripur", "Pirganj", "Ranishankoil"));
        thanaMap.put("Panchagarh", List.of("Panchagarh Sadar", "Atwari", "Boda", "Debiganj", "Tetulia"));
        thanaMap.put("Kurigram", List.of("Kurigram Sadar", "Bhurungamari", "Char Rajibpur", "Chilmari", "Phulbari", "Nageshwari", "Rajarhat", "Raomari", "Ulipur"));
        thanaMap.put("Lalmonirhat", List.of("Lalmonirhat Sadar", "Aditmari", "Hatibandha", "Kaliganj", "Patgram"));
        thanaMap.put("Gaibandha", List.of("Gaibandha Sadar", "Phulchhari", "Gobindaganj", "Palashbari", "Sadullapur", "Saghata", "Sundarganj"));
        thanaMap.put("Nilphamari", List.of("Nilphamari Sadar", "Dimla", "Domar", "Jaldhaka", "Kishoreganj", "Saidpur"));

        // Mymensingh Division
        thanaMap.put("Mymensingh", List.of("Mymensingh Sadar", "Bhaluka", "Dhobaura", "Fulbaria", "Gaffargaon", "Gauripur", "Haluaghat", "Ishwarganj", "Muktagachha", "Nandail", "Phulpur", "Trishal"));
        thanaMap.put("Jamalpur", List.of("Jamalpur Sadar", "Baksiganj", "Dewanganj", "Islampur", "Madarganj", "Melandaha", "Sarishabari"));
        thanaMap.put("Netrokona", List.of("Netrokona Sadar", "Atpara", "Barhatta", "Durgapur", "Kalmakanda", "Kendua", "Khaliajuri", "Madan", "Mohanganj", "Purbadhala"));
        thanaMap.put("Sherpur", List.of("Sherpur Sadar", "Jhenaigati", "Nakla", "Nalitabari", "Sreebardi"));
    }


    private void loadDistricts(String division) {
        List<String> districts = districtMap.getOrDefault(division, List.of("Select District"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistricts.setAdapter(adapter);

        spinnerDistricts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = (String) parent.getItemAtPosition(position);
                loadThanas(selectedDistrict);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadThanas(String district) {
        List<String> thanas = thanaMap.getOrDefault(district, List.of("Select Thana"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thanas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThanas.setAdapter(adapter);
    }

    private void showDatePickerDialog(final EditText dateField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateField.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void submitComplaint() {
        String complaintType = spinnerComplaintType.getSelectedItem().toString();
        String incidentDate = etIncidentDate.getText().toString().trim();
        String reportingDate = etReportingDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String division = spinnerDivisions.getSelectedItem().toString();
        String district = spinnerDistricts.getSelectedItem().toString();
        String thana = spinnerThanas.getSelectedItem().toString();
        boolean isAnonymous = checkboxAnonymous.isChecked();

        if (TextUtils.isEmpty(complaintType) || TextUtils.isEmpty(incidentDate) || TextUtils.isEmpty(reportingDate)
                || TextUtils.isEmpty(location) || TextUtils.isEmpty(description) || "Select Division".equals(division)
                || "Select District".equals(district) || "Select Thana".equals(thana)) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> complaintData = new HashMap<>();
        complaintData.put("complaintType", complaintType);
        complaintData.put("incidentDate", incidentDate);
        complaintData.put("reportingDate", reportingDate);
        complaintData.put("location", location);
        complaintData.put("description", description);
        complaintData.put("division", division);
        complaintData.put("district", district);
        complaintData.put("thana", thana);
        complaintData.put("status", "Submitted");
        complaintData.put("UserId", auth.getCurrentUser().getUid());

        CollectionReference ref = isAnonymous ? anonymousComplaintsRef : complaintsRef;
        ref.add(complaintData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(LogComplaintActivity.this, "Complaint logged successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogComplaintActivity.this, ComplaintSuccessActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(LogComplaintActivity.this, "Failed to log complaint: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
