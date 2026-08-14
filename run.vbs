' 自习室预约系统 - 静默启动器
' 双击 → 无黑窗口，后端 + 桌面窗口直接弹出
' 关闭桌面窗口后系统自动退出

Dim fso, scriptDir, shell
Set fso = CreateObject("Scripting.FileSystemObject")
scriptDir = fso.GetParentFolderName(WScript.ScriptFullName)
Set shell = CreateObject("WScript.Shell")

shell.Run """" & scriptDir & "\StudyRoom Launcher.exe""", 0, False
' 参数说明: 0=隐藏窗口, False=不等待返回