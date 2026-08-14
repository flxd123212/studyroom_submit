@echo off
cd /d "%~dp0"
C:\PROGRA~2\WI3CF2~1\10\100226~1.0\x64\signtool.exe sign /fd SHA256 /a "StudyRoom Launcher.exe" 2>&1
echo Exit code: %ERRORLEVEL%
pause
