package com.example.studentservice.service;

import com.example.studentservice.exception.ResourceNotFoundException;
import com.example.studentservice.model.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {

    private final Map<String, Student> studentRepository = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDefaultData() {
        // Seed initial data for testing and demonstration
        studentRepository.put("S101", new Student("S101", "Alice Johnson", "alice@example.com", "Computer Science"));
        studentRepository.put("S102", new Student("S102", "Bob Smith", "bob@example.com", "Information Technology"));
        studentRepository.put("S103", new Student("S103", "Charlie Brown", "charlie@example.com", "Data Science"));
    }

    public Student addStudent(Student student) {
        studentRepository.put(student.getId(), student);
        return student;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(studentRepository.values());
    }

    public Student getStudentById(String id) {
        Student student = studentRepository.get(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with ID: " + id);
        }
        return student;
    }

    public boolean deleteStudent(String id) {
        if (!studentRepository.containsKey(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with ID: " + id);
        }
        studentRepository.remove(id);
        return true;
    }
}
