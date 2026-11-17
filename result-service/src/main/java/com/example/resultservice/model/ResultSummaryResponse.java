package com.example.resultservice.model;

public class ResultSummaryResponse {
    private String studentId;
    private String studentName;
    private int totalCourses;
    private Double totalMarksObtained;
    private Double totalMaxMarks;
    private Double averagePercentage;
    private String overallGrade;
    private String status;

    public ResultSummaryResponse() {
    }

    public ResultSummaryResponse(String studentId, String studentName, int totalCourses,
                                 Double totalMarksObtained, Double totalMaxMarks,
                                 Double averagePercentage, String overallGrade, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalCourses = totalCourses;
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

    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
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
