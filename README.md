# Student Management System using Java SOA Microservices & Docker

A production-ready Service-Oriented Architecture (SOA) and Microservices project built with **Java 17**, **Spring Boot**, **Maven**, and **Docker**.

---

## 1. Project Introduction

The **Student Management System** is composed of three decoupled, independently designed Spring Boot microservices:
1. **Student Service**: Manages student records (creation, lookup, listing, deletion).
2. **Course Service**: Manages academic courses and curriculum details.
3. **Result Service**: Manages academic marks, computes total/average marks, calculates GPA/grades, and orchestrates calls to both Student and Course services to construct consolidated student scorecards.

### Key Highlight: Single-Container Architecture
As per the core DevOps requirement, **all three microservices run simultaneously inside exactly ONE Docker container** monitored by a process supervisor script (`docker-start.sh`). This eliminates multi-container overhead while retaining clean SOA boundary separation between services.

---

## 2. SOA (Service-Oriented Architecture) & Communication

```
                         [ Client / Postman / curl ]
                                      │
              ┌───────────────────────┼───────────────────────┐
              │                       │                       │
      Port 8081 (REST)        Port 8082 (REST)        Port 8083 (REST)
              │                       │                       │
     ┌────────▼────────┐     ┌────────▼────────┐     ┌────────▼────────┐
     │ Student Service │     │  Course Service │     │  Result Service │
     │  (Port: 8081)   │     │   (Port: 8082)  │     │   (Port: 8083)  │
     └────────▲────────┘     └────────▲────────┘     └────────┬────────┘
              │                       │                       │
              └──────── RestTemplate ─┴───────────────────────┘
                     (Localhost Inter-Service Calls)
                     
                     ═════════════════════════════════════════
                     SINGLE DOCKER CONTAINER ("student-soa")
                     ═════════════════════════════════════════
```

### Inter-Service Communication Flow
- **Composite SOA Orchestration**: When a client requests a student's scorecard via `GET /api/results/student/{studentId}` on Result Service (port 8083):
  1. Result Service queries `http://localhost:8081/api/students/{studentId}` (Student Service) to retrieve student details (`name`, `email`, `department`).
  2. Result Service retrieves the student's marks from its local repository.
  3. For every enrolled subject, Result Service queries `http://localhost:8082/api/courses/{courseId}` (Course Service) to enrich each mark entry with the course name, code, and credits.
  4. Result Service dynamically computes total marks obtained, maximum total marks, average percentage, individual grades, overall grade, and pass/fail status.
- Because all three applications share the container's network namespace, inter-service HTTP requests resolve directly over `localhost` with minimal latency and no complex service discovery required.

---

## 3. Technology Stack

- **Language:** Java 17 (Eclipse Temurin OpenJDK)
- **Framework:** Spring Boot 3.2.5
- **Web Layer:** Spring Web MVC (REST APIs)
- **Validation:** Jakarta Bean Validation (Hibernate Validator)
- **Data Persistence:** Thread-safe In-Memory Storage (`ConcurrentHashMap`)
- **Build Tool:** Apache Maven
- **Containerization:** Docker (Multi-service single container with POSIX supervisor script)
- **API Testing:** cURL and Postman Collection

---

## 4. Project Folder Structure

```
student-management-soa/
│
├── student-service/                   # Student Management Microservice
│   ├── src/main/java/com/example/studentservice/
│   │   ├── StudentServiceApplication.java
│   │   ├── controller/StudentController.java
│   │   ├── model/Student.java
│   │   ├── service/StudentService.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── ResourceNotFoundException.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── course-service/                    # Course Catalog Microservice
│   ├── src/main/java/com/example/courseservice/
│   │   ├── CourseServiceApplication.java
│   │   ├── controller/CourseController.java
│   │   ├── model/Course.java
│   │   ├── service/CourseService.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── ResourceNotFoundException.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── result-service/                    # Result & Scorecard Microservice
│   ├── src/main/java/com/example/resultservice/
│   │   ├── ResultServiceApplication.java
│   │   ├── controller/ResultController.java
│   │   ├── model/
│   │   │   ├── Mark.java
│   │   │   ├── CourseDto.java
│   │   │   ├── StudentDto.java
│   │   │   ├── CourseMarkDetail.java
│   │   │   ├── StudentResultResponse.java
│   │   │   └── ResultSummaryResponse.java
│   │   ├── config/AppConfig.java
│   │   ├── service/ResultService.java
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── ResourceNotFoundException.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── pom.xml                            # Root aggregator POM
├── Dockerfile                         # Builds single Docker image housing all 3 JARs
├── docker-start.sh                    # Process supervisor & health monitoring script
├── docker-compose.yml                 # Optional Compose configuration
├── postman_collection.json            # Complete Postman test collection
├── test-all-apis.sh                   # Automated end-to-end test script
├── .dockerignore
└── README.md
```

---

## 5. Microservices Overview & REST API Documentation

### 5.1 Student Service (Port: 8081)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/students` | Get all students |
| `GET` | `/api/students/{id}` | Get student by ID |
| `POST` | `/api/students` | Register a new student |
| `DELETE` | `/api/students/{id}` | Delete student by ID |

#### Sample Request (`POST /api/students`):
```json
{
  "id": "S104",
  "name": "David Miller",
  "email": "david.miller@example.com",
  "department": "Cybersecurity"
}
```

#### Sample Response (`201 Created`):
```json
{
  "id": "S104",
  "name": "David Miller",
  "email": "david.miller@example.com",
  "department": "Cybersecurity"
}
```

---

### 5.2 Course Service (Port: 8082)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/courses` | Get all courses |
| `GET` | `/api/courses/{id}` | Get course by ID |
| `POST` | `/api/courses` | Register a new course |

#### Sample Request (`POST /api/courses`):
```json
{
  "id": "C105",
  "name": "Cloud Computing & DevOps",
  "code": "CS501",
  "credits": 3,
  "instructor": "Dr. Werner Vogels"
}
```

#### Sample Response (`201 Created`):
```json
{
  "id": "C105",
  "name": "Cloud Computing & DevOps",
  "code": "CS501",
  "credits": 3,
  "instructor": "Dr. Werner Vogels"
}
```

---

### 5.3 Result Service (Port: 8083 - Composite SOA Service)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/results/marks` | Add marks for a student in a course |
| `GET` | `/api/results/marks` | Retrieve all recorded marks |
| `GET` | `/api/results/student/{studentId}` | Get consolidated scorecard (calls Student & Course services) |
| `GET` | `/api/results/student/{studentId}/summary` | Get total and average marks summary |
| `GET` | `/api/results/calculate/{studentId}` | Alias endpoint for calculating total and average |

#### Sample Request (`POST /api/results/marks`):
```json
{
  "studentId": "S102",
  "courseId": "C102",
  "marksObtained": 89.0,
  "maxMarks": 100.0
}
```

#### Sample Response (`GET /api/results/student/S101` - Consolidated Result):
```json
{
  "studentId": "S101",
  "studentName": "Alice Johnson",
  "studentEmail": "alice@example.com",
  "department": "Computer Science",
  "courses": [
    {
      "courseId": "C101",
      "courseName": "Introduction to Computer Science",
      "courseCode": "CS101",
      "credits": 4,
      "marksObtained": 88.0,
      "maxMarks": 100.0,
      "percentage": 88.0,
      "grade": "A"
    },
    {
      "courseId": "C102",
      "courseName": "Data Structures and Algorithms",
      "courseCode": "CS201",
      "credits": 4,
      "marksObtained": 92.0,
      "maxMarks": 100.0,
      "percentage": 92.0,
      "grade": "A+"
    },
    {
      "courseId": "C103",
      "courseName": "Database Management Systems",
      "courseCode": "CS301",
      "credits": 3,
      "marksObtained": 85.0,
      "maxMarks": 100.0,
      "percentage": 85.0,
      "grade": "A"
    }
  ],
  "totalMarksObtained": 265.0,
  "totalMaxMarks": 300.0,
  "averagePercentage": 88.33,
  "overallGrade": "A",
  "status": "PASS"
}
```

#### Sample Response (`GET /api/results/student/S101/summary`):
```json
{
  "studentId": "S101",
  "studentName": "Alice Johnson",
  "totalCourses": 3,
  "totalMarksObtained": 265.0,
  "totalMaxMarks": 300.0,
  "averagePercentage": 88.33,
  "overallGrade": "A",
  "status": "PASS"
}
```

---

## 6. Build & Execution Guide

### Step 1: Build Executable JARs using Maven
From the project root directory, run the Maven aggregator command:
```bash
mvn clean package -DskipTests
```
This produces three executable Spring Boot JAR files:
- `student-service/target/student-service.jar`
- `course-service/target/course-service.jar`
- `result-service/target/result-service.jar`

### Step 2: Build the Single Docker Image
Build the unified Docker image containing all 3 microservices:
```bash
docker build -t student-soa .
```

### Step 3: Run the Single Docker Container
Run one container mapping all three service ports:
```bash
docker run -d \
  -p 8081:8081 \
  -p 8082:8082 \
  -p 8083:8083 \
  --name student-soa-app \
  student-soa
```

Or using Docker Compose:
```bash
docker compose up -d
```

### Step 4: Verify Container & Services are Running
Check container status:
```bash
docker ps
```
Output:
```
CONTAINER ID   IMAGE         COMMAND                  STATUS         PORTS                                             NAMES
9ce1fbc2d472   student-soa   "/app/docker-start.sh"   Up (healthy)   0.0.0.0:8081-8083->8081-8083/tcp                 student-soa-app
```

Check consolidated container logs:
```bash
docker logs -f student-soa-app
```

---

## 7. Testing Instructions

### Option A: Automated Test Script (cURL)
Run the automated test suite provided with the project:
```bash
chmod +x test-all-apis.sh
./test-all-apis.sh
```

### Option B: Individual cURL Commands

#### 1. Student Service
```bash
# Get all students
curl -X GET http://localhost:8081/api/students

# Get student S101
curl -X GET http://localhost:8081/api/students/S101

# Add student S104
curl -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{"id":"S104","name":"David Miller","email":"david.miller@example.com","department":"Cybersecurity"}'

# Delete student S104
curl -X DELETE http://localhost:8081/api/students/S104
```

#### 2. Course Service
```bash
# Get all courses
curl -X GET http://localhost:8082/api/courses

# Get course C101
curl -X GET http://localhost:8082/api/courses/C101

# Add course C105
curl -X POST http://localhost:8082/api/courses \
  -H "Content-Type: application/json" \
  -d '{"id":"C105","name":"Cloud Computing & DevOps","code":"CS501","credits":3,"instructor":"Dr. Werner Vogels"}'
```

#### 3. Result Service
```bash
# Get consolidated scorecard for Alice Johnson (S101)
curl -X GET http://localhost:8083/api/results/student/S101

# Get calculation summary
curl -X GET http://localhost:8083/api/results/student/S101/summary

# Record marks for student S102 in course C102
curl -X POST http://localhost:8083/api/results/marks \
  -H "Content-Type: application/json" \
  -d '{"studentId":"S102","courseId":"C102","marksObtained":89.0,"maxMarks":100.0}'
```

### Option C: Postman
1. Open **Postman**.
2. Click **Import** and select `postman_collection.json`.
3. Execute the pre-configured requests grouped by microservice.

---

## 8. Process Supervisor & Fault Tolerance

The container utilizes `docker-start.sh` as the main `ENTRYPOINT`:
- Launches `student-service.jar` (Port 8081), `course-service.jar` (Port 8082), and `result-service.jar` (Port 8083) concurrently as background processes.
- Tails log outputs to `stdout` so standard `docker logs` displays real-time server activity.
- Continuously polls PID health. If any individual microservice crashes or encounters a fatal error, the supervisor intercepts the exit, flushes the service's log file, terminates sibling processes, and exits with code `1`, accurately communicating container failure to Docker and orchestrators.
- Gracefully handles `SIGTERM` and `SIGINT` signals for zero-downtime stops (`docker stop`).
"# 2400030176_SOA_END_SEM" 
