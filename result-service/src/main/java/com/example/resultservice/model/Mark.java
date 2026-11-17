package com.example.resultservice.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Mark {

    private String id;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotNull(message = "Marks obtained is required")
    @DecimalMin(value = "0.0", message = "Marks obtained cannot be negative")
    private Double marksObtained;

    @NotNull(message = "Max marks is required")
    @DecimalMin(value = "1.0", message = "Max marks must be greater than 0")
    private Double maxMarks;

    public Mark() {
    }

    public Mark(String id, String studentId, String courseId, Double marksObtained, Double maxMarks) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Double maxMarks) {
        this.maxMarks = maxMarks;
    }
}
