package com.example.resultservice.controller;

import com.example.resultservice.model.Mark;
import com.example.resultservice.model.ResultSummaryResponse;
import com.example.resultservice.model.StudentResultResponse;
import com.example.resultservice.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/marks")
    public ResponseEntity<Mark> addMark(@Valid @RequestBody Mark mark) {
        Mark createdMark = resultService.addMark(mark);
        return new ResponseEntity<>(createdMark, HttpStatus.CREATED);
    }

    @GetMapping("/marks")
    public ResponseEntity<List<Mark>> getAllMarks() {
        return ResponseEntity.ok(resultService.getAllMarks());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentResultResponse> getStudentResult(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.getStudentResult(studentId));
    }

    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<ResultSummaryResponse> getStudentResultSummary(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.calculateSummary(studentId));
    }

    @GetMapping("/calculate/{studentId}")
    public ResponseEntity<ResultSummaryResponse> calculateTotalAndAverage(@PathVariable String studentId) {
        return ResponseEntity.ok(resultService.calculateSummary(studentId));
    }
}
