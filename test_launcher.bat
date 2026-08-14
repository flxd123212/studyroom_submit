@echo off
cd /d "%~dp0"
echo Running from: %CD%
echo Starting launcher...
start /b "" "StudyRoom Launcher.exe"
echo Waiting 10 seconds...
ping -n 10 127.0.0.1 >nul
echo Checking log...
if exist launcher.log (
  echo === LAUNCHER LOG ===
  type launcher.log
) else (
  echo NO LOG FOUND
)
echo Killing launcher...
taskkill /f /im "StudyRoom Launcher.exe" 2>nul
echo Done.
pause
