#!/bin/bash
set -e

BASE_URL_STUDENT="http://localhost:8081"
BASE_URL_COURSE="http://localhost:8082"
BASE_URL_RESULT="http://localhost:8083"

echo "=========================================================="
echo "    STUDENT MANAGEMENT SYSTEM - FULL API TEST SUITE"
echo "=========================================================="
echo ""

echo "----------------------------------------------------------"
echo "1. STUDENT SERVICE (Port 8081)"
echo "----------------------------------------------------------"

echo ">> [1.1] GET /api/students (Get all initial students)"
curl -s -X GET "${BASE_URL_STUDENT}/api/students" | python3 -m json.tool
echo ""

echo ">> [1.2] POST /api/students (Add new student: S104)"
curl -s -X POST "${BASE_URL_STUDENT}/api/students" \
  -H "Content-Type: application/json" \
  -d '{"id":"S104","name":"David Miller","email":"david.miller@example.com","department":"Cybersecurity"}' | python3 -m json.tool
echo ""

echo ">> [1.3] GET /api/students/S104 (Get student by ID)"
curl -s -X GET "${BASE_URL_STUDENT}/api/students/S104" | python3 -m json.tool
echo ""

echo ">> [1.4] DELETE /api/students/S104 (Delete student)"
curl -s -X DELETE "${BASE_URL_STUDENT}/api/students/S104" | python3 -m json.tool
echo ""

echo ">> [1.5] GET /api/students/S104 (Verify 404 after delete)"
curl -s -X GET "${BASE_URL_STUDENT}/api/students/S104" | python3 -m json.tool
echo ""

echo "----------------------------------------------------------"
echo "2. COURSE SERVICE (Port 8082)"
echo "----------------------------------------------------------"

echo ">> [2.1] GET /api/courses (Get all initial courses)"
curl -s -X GET "${BASE_URL_COURSE}/api/courses" | python3 -m json.tool
echo ""

echo ">> [2.2] POST /api/courses (Add new course: C105)"
curl -s -X POST "${BASE_URL_COURSE}/api/courses" \
  -H "Content-Type: application/json" \
  -d '{"id":"C105","name":"Cloud Computing & DevOps","code":"CS501","credits":3,"instructor":"Dr. Werner Vogels"}' | python3 -m json.tool
echo ""

echo ">> [2.3] GET /api/courses/C105 (Get course by ID)"
curl -s -X GET "${BASE_URL_COURSE}/api/courses/C105" | python3 -m json.tool
echo ""

echo "----------------------------------------------------------"
echo "3. RESULT SERVICE (Port 8083 - SOA Composite)"
echo "----------------------------------------------------------"

echo ">> [3.1] POST /api/results/marks (Add mark for S102 in C102)"
curl -s -X POST "${BASE_URL_RESULT}/api/results/marks" \
  -H "Content-Type: application/json" \
  -d '{"studentId":"S102","courseId":"C102","marksObtained":89.0,"maxMarks":100.0}' | python3 -m json.tool
echo ""

echo ">> [3.2] GET /api/results/marks (Get all recorded marks)"
curl -s -X GET "${BASE_URL_RESULT}/api/results/marks" | python3 -m json.tool
echo ""

echo ">> [3.3] GET /api/results/student/S101 (Consolidated student result - calls Student & Course services)"
curl -s -X GET "${BASE_URL_RESULT}/api/results/student/S101" | python3 -m json.tool
echo ""

echo ">> [3.4] GET /api/results/student/S102 (Consolidated student result for S102)"
curl -s -X GET "${BASE_URL_RESULT}/api/results/student/S102" | python3 -m json.tool
echo ""

echo ">> [3.5] GET /api/results/student/S101/summary (Total and average marks calculation)"
curl -s -X GET "${BASE_URL_RESULT}/api/results/student/S101/summary" | python3 -m json.tool
echo ""

echo ">> [3.6] GET /api/results/calculate/S101 (Calculation endpoint)"
curl -s -X GET "${BASE_URL_RESULT}/api/results/calculate/S101" | python3 -m json.tool
echo ""

echo "=========================================================="
echo "    ALL API TESTS COMPLETED SUCCESSFULLY!"
echo "=========================================================="
