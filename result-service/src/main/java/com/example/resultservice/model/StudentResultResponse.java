package com.example.resultservice.model;

import java.util.List;

public class StudentResultResponse {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String department;
    private List<CourseMarkDetail> courses;
    private Double totalMarksObtained;
    private Double totalMaxMarks;
    private Double averagePercentage;
    private String overallGrade;
    private String status;

    public StudentResultResponse() {
    }

    public StudentResultResponse(String studentId, String studentName, String studentEmail, String department,
                                 List<CourseMarkDetail> courses, Double totalMarksObtained, Double totalMaxMarks,
                                 Double averagePercentage, String overallGrade, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.department = department;
        this.courses = courses;
        this.totalMarksObtained = totalMarksObtained;
        this.totalMaxMarks = totalMaxMarks;
        this.averagePercentage = averagePercentage;
        this.overallGrade = overallGrade;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<CourseMarkDetail> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseMarkDetail> courses) {
        this.courses = courses;
    }

    public Double getTotalMarksObtained() {
        return totalMarksObtained;
    }

    public void setTotalMarksObtained(Double totalMarksObtained) {
        this.totalMarksObtained = totalMarksObtained;
    }

    public Double getTotalMaxMarks() {
        return totalMaxMarks;
    }

    public void setTotalMaxMarks(Double totalMaxMarks) {
        this.totalMaxMarks = totalMaxMarks;
    }

    public Double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(Double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
