# 签名脚本 - 放在项目目录下运行
$signtool = "C:\Program Files (x86)\Windows Kits\10\bin\10.0.22621.0\x64\signtool.exe"
$exeName = "StudyRoom Launcher.exe"

# 获取脚本所在目录
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$exePath = Join-Path $scriptDir $exeName

Write-Host "Exe path: $exePath"
Write-Host "Exists: $(Test-Path $exePath)"

if (Test-Path $exePath) {
    & $signtool sign /fd SHA256 /a $exePath
    Write-Host "Signing done, exit code: $LASTEXITCODE"
} else {
    Write-Host "ERROR: File not found" -ForegroundColor Red
}

Read-Host "Press Enter to exit"
