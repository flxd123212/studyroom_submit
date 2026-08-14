@echo off
chcp 65001 >nul
title StudyRoom - Desktop Client

:: Check Node.js
where node >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Node.js not found!
    echo Please install Node.js from: https://nodejs.org/
    pause
    exit /b 1
)

:: Go to electron client directory
pushd "%~dp0electron_client"

:: Install dependencies if needed
if not exist "node_modules" (
    echo [INFO] First run, installing dependencies...
    call npm install --registry=https://registry.npmmirror.com
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] npm install failed!
        pause
        exit /b 1
    )
)

:: Verify electron is installed
if not exist "node_modules\.bin\electron.cmd" (
    echo [ERROR] electron binary not found in node_modules
    pause
    exit /b 1
)

echo [INFO] Starting desktop client...
echo [INFO] Make sure backend is running at http://localhost:8082
echo.
call npx electron .
pause