#!/bin/sh
set -e

echo "=========================================================="
echo " Starting Student Management SOA Services"
echo " All services running inside ONE Docker container"
echo "=========================================================="

mkdir -p /app/logs

# Start Student Service (port 8081)
echo "[1/3] Starting Student Service on port 8081..."
java -jar /app/student-service.jar > /app/logs/student-service.log 2>&1 &
STUDENT_PID=$!
echo "Student Service started (PID: $STUDENT_PID)"

# Start Course Service (port 8082)
echo "[2/3] Starting Course Service on port 8082..."
java -jar /app/course-service.jar > /app/logs/course-service.log 2>&1 &
COURSE_PID=$!
echo "Course Service started (PID: $COURSE_PID)"

# Start Result Service (port 8083)
echo "[3/3] Starting Result Service on port 8083..."
java -jar /app/result-service.jar > /app/logs/result-service.log 2>&1 &
RESULT_PID=$!
echo "Result Service started (PID: $RESULT_PID)"

# Graceful shutdown handler
shutdown_handler() {
    echo "Signal received. Shutting down all services..."
    kill -TERM "$STUDENT_PID" "$COURSE_PID" "$RESULT_PID" 2>/dev/null || true
    wait "$STUDENT_PID" "$COURSE_PID" "$RESULT_PID" 2>/dev/null || true
    echo "All services stopped."
    exit 0
}

trap shutdown_handler SIGTERM SIGINT

echo "=========================================================="
echo " All 3 microservices have been launched!"
echo " - Student Service : http://localhost:8081/api/students"
echo " - Course Service  : http://localhost:8082/api/courses"
echo " - Result Service  : http://localhost:8083/api/results"
echo "=========================================================="

# Stream logs in background so docker logs shows output
tail -q -F /app/logs/student-service.log /app/logs/course-service.log /app/logs/result-service.log 2>/dev/null &
TAIL_PID=$!

# Health monitor loop: check if any microservice exits unexpectedly
while true; do
    if ! kill -0 "$STUDENT_PID" 2>/dev/null; then
        echo "[ERROR] Student Service (PID $STUDENT_PID) terminated unexpectedly!"
        cat /app/logs/student-service.log
        kill -TERM "$COURSE_PID" "$RESULT_PID" "$TAIL_PID" 2>/dev/null || true
        exit 1
    fi
    if ! kill -0 "$COURSE_PID" 2>/dev/null; then
        echo "[ERROR] Course Service (PID $COURSE_PID) terminated unexpectedly!"
        cat /app/logs/course-service.log
        kill -TERM "$STUDENT_PID" "$RESULT_PID" "$TAIL_PID" 2>/dev/null || true
        exit 1
    fi
    if ! kill -0 "$RESULT_PID" 2>/dev/null; then
        echo "[ERROR] Result Service (PID $RESULT_PID) terminated unexpectedly!"
        cat /app/logs/result-service.log
        kill -TERM "$STUDENT_PID" "$COURSE_PID" "$TAIL_PID" 2>/dev/null || true
        exit 1
    fi
    sleep 2
done
