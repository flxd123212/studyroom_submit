#!/bin/bash
echo "====================================================="
echo "  Study Room Reservation System - Starting..."
echo "====================================================="
echo ""
echo "  Open in browser: http://localhost:8082"
echo ""
echo "  Admin account: admin / admin123"
echo "  Student account: 2024001 / 123456"
echo ""
echo "  Press Ctrl+C to stop the server"
echo "====================================================="
echo ""
java -jar studyroom.jar --spring.profiles.active=demo --server.port=8082
