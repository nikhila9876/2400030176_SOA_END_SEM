package com.example.resultservice.service;

import com.example.resultservice.exception.ResourceNotFoundException;
import com.example.resultservice.model.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private static final Logger log = LoggerFactory.getLogger(ResultService.class);

    private final Map<String, Mark> markRepository = new ConcurrentHashMap<>();
    private final AtomicLong markIdSequence = new AtomicLong(100);

    private final RestTemplate restTemplate;

    @Value("${service.student.url:http://localhost:8081/api/students}")
    private String studentServiceUrl;

    @Value("${service.course.url:http://localhost:8082/api/courses}")
    private String courseServiceUrl;

    public ResultService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initDefaultData() {
        // Seed initial marks for demonstration
        addMark(new Mark(null, "S101", "C101", 88.0, 100.0));
        addMark(new Mark(null, "S101", "C102", 92.0, 100.0));
        addMark(new Mark(null, "S101", "C103", 85.0, 100.0));
        addMark(new Mark(null, "S102", "C101", 74.0, 100.0));
        addMark(new Mark(null, "S102", "C104", 81.0, 100.0));
    }

    public Mark addMark(Mark mark) {
        if (mark.getId() == null || mark.getId().isBlank()) {
            mark.setId("M" + markIdSequence.incrementAndGet());
        }
        markRepository.put(mark.getId(), mark);
        return mark;
    }

    public List<Mark> getAllMarks() {
        return new ArrayList<>(markRepository.values());
    }

    public List<Mark> getMarksByStudentId(String studentId) {
        List<Mark> list = markRepository.values().stream()
                .filter(m -> m.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No marks found for student ID: " + studentId);
        }
        return list;
    }

    public StudentResultResponse getStudentResult(String studentId) {
        List<Mark> studentMarks = getMarksByStudentId(studentId);

        // Inter-service REST call: Fetch Student details from Student Service (SOA)
        StudentDto student = fetchStudentDetails(studentId);

        List<CourseMarkDetail> courseDetails = new ArrayList<>();
        double totalObtained = 0.0;
        double totalMax = 0.0;

        for (Mark mark : studentMarks) {
            // Inter-service REST call: Fetch Course details from Course Service (SOA)
            CourseDto course = fetchCourseDetails(mark.getCourseId());

            double percentage = (mark.getMarksObtained() / mark.getMaxMarks()) * 100.0;
            String grade = calculateGrade(percentage);

            CourseMarkDetail detail = new CourseMarkDetail(
                    mark.getCourseId(),
                    course != null ? course.getName() : "Course (" + mark.getCourseId() + ")",
                    course != null ? course.getCode() : "N/A",
                    course != null ? course.getCredits() : 0,
                    mark.getMarksObtained(),
                    mark.getMaxMarks(),
                    Math.round(percentage * 100.0) / 100.0,
                    grade
            );
            courseDetails.add(detail);

            totalObtained += mark.getMarksObtained();
            totalMax += mark.getMaxMarks();
        }

        double averagePercentage = totalMax > 0 ? (totalObtained / totalMax) * 100.0 : 0.0;
        averagePercentage = Math.round(averagePercentage * 100.0) / 100.0;
        String overallGrade = calculateGrade(averagePercentage);
        String status = averagePercentage >= 50.0 ? "PASS" : "FAIL";

        return new StudentResultResponse(
                studentId,
                student != null ? student.getName() : "Unknown Student",
                student != null ? student.getEmail() : "N/A",
                student != null ? student.getDepartment() : "N/A",
                courseDetails,
                totalObtained,
                totalMax,
                averagePercentage,
                overallGrade,
                status
        );
    }

    public ResultSummaryResponse calculateSummary(String studentId) {
        StudentResultResponse fullResult = getStudentResult(studentId);
        return new ResultSummaryResponse(
                fullResult.getStudentId(),
                fullResult.getStudentName(),
                fullResult.getCourses().size(),
                fullResult.getTotalMarksObtained(),
                fullResult.getTotalMaxMarks(),
                fullResult.getAveragePercentage(),
                fullResult.getOverallGrade(),
                fullResult.getStatus()
        );
    }

    private StudentDto fetchStudentDetails(String studentId) {
        try {
            String url = studentServiceUrl + "/" + studentId;
            return restTemplate.getForObject(url, StudentDto.class);
        } catch (Exception e) {
            log.warn("Could not retrieve student details from Student Service for ID {}: {}", studentId, e.getMessage());
            return new StudentDto(studentId, "Student " + studentId, "N/A", "N/A");
        }
    }

    private CourseDto fetchCourseDetails(String courseId) {
        try {
            String url = courseServiceUrl + "/" + courseId;
            return restTemplate.getForObject(url, CourseDto.class);
        } catch (Exception e) {
            log.warn("Could not retrieve course details from Course Service for ID {}: {}", courseId, e.getMessage());
            return new CourseDto(courseId, "Course " + courseId, "N/A", 0, "N/A");
        }
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90.0) return "A+";
        if (percentage >= 80.0) return "A";
        if (percentage >= 70.0) return "B";
        if (percentage >= 60.0) return "C";
        if (percentage >= 50.0) return "D";
        return "F";
    }
}
