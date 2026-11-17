package com.example.studentservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Student {

    @NotBlank(message = "Student ID is required")
    private String id;

    @NotBlank(message = "Student name is required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Student email is required")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    public Student() {
    }

    public Student(String id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
