package com.example.courseservice.service;

import com.example.courseservice.exception.ResourceNotFoundException;
import com.example.courseservice.model.Course;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CourseService {

    private final Map<String, Course> courseRepository = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDefaultData() {
        // Seed initial courses for testing and demonstration
        courseRepository.put("C101", new Course("C101", "Introduction to Computer Science", "CS101", 4, "Dr. Alan Turing"));
        courseRepository.put("C102", new Course("C102", "Data Structures and Algorithms", "CS201", 4, "Dr. Donald Knuth"));
        courseRepository.put("C103", new Course("C103", "Database Management Systems", "CS301", 3, "Dr. Edgar Codd"));
        courseRepository.put("C104", new Course("C104", "Computer Networks", "CS401", 3, "Dr. Andrew Tanenbaum"));
    }

    public Course addCourse(Course course) {
        courseRepository.put(course.getId(), course);
        return course;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courseRepository.values());
    }

    public Course getCourseById(String id) {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        return course;
    }
}
