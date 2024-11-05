package com.example.myprojectt;

public class Complaint {
    private String userid;
    private String userName;
    private String phone;
    private String complaintType;
    private String description;
    private String reportingDate;
    private String status;

    public Complaint(String id, String userName, String phone, String complaintType, String description, String reportingDate, String status) {
        this.userid = id;
        this.userName = userName;
        this.phone = phone;
        this.complaintType = complaintType;
        this.description = description;
        this.reportingDate = reportingDate;
        this.status = status;
    }

    public String getId() {
        return userid;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public String getDescription() {
        return description;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
