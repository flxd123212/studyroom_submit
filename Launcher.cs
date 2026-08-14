using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Threading;

class StudyRoomLauncher
{
    static string BaseDir;
    static Process backendProcess;

    static int Main(string[] args)
    {
        BaseDir = Path.GetDirectoryName(typeof(StudyRoomLauncher).Assembly.Location);
        Console.Title = "自习室预约系统";
        Console.WriteLine("========================================");
        Console.WriteLine("  自习室预约系统 - 一键启动");
        Console.WriteLine("========================================");
        Console.WriteLine();

        if (!CheckJava()) return 1;

        string jarPath = Path.Combine(BaseDir, "backend", "studyroom.jar");
        if (!File.Exists(jarPath))
        {
            Console.WriteLine("[ERROR] 找不到后端文件: backend\\studyroom.jar");
            Pause();
            return 1;
        }

        string electronDir = Path.Combine(BaseDir, "electron_client");
        if (!Directory.Exists(electronDir) || !File.Exists(Path.Combine(electronDir, "package.json")))
        {
            Console.WriteLine("[ERROR] 找不到 Electron 客户端: electron_client\\package.json");
            Pause();
            return 1;
        }

        // Step 1: Start backend
        Console.WriteLine("[1/3] 启动后端服务...");
        if (!StartBackend(jarPath))
        {
            Console.WriteLine("[ERROR] 后端启动失败!");
            Pause();
            return 1;
        }

        // Step 2: Wait for backend ready
        Console.WriteLine("[2/3] 等待后端服务就绪...");
        if (!WaitForBackendReady())
        {
            Console.WriteLine("[ERROR] 后端服务启动超时!");
            StopBackend();
            Pause();
            return 1;
        }
        Console.WriteLine("  -> 后端服务已就绪 (http://localhost:8082)");

        // Step 3: Start Electron client
        Console.WriteLine("[3/3] 启动桌面客户端...");
        StartElectron(electronDir);

        // Cleanup
        Console.WriteLine();
        Console.WriteLine("[INFO] 客户端已关闭, 正在停止后端服务...");
        StopBackend();
        Console.WriteLine("[INFO] 系统已安全关闭。");
        Pause();
        return 0;
    }

    static bool CheckJava()
    {
        try
        {
            var psi = new ProcessStartInfo("java", "-version")
            {
                RedirectStandardError = true,
                UseShellExecute = false
            };
            var p = Process.Start(psi);
            p.WaitForExit(5000);
            if (p.ExitCode == 0)
            {
                string ver = p.StandardError.ReadToEnd();
                Console.WriteLine("  [检测到] Java: " + ver.Trim().Split('\n')[0]);
                return true;
            }
        }
        catch { }

        Console.WriteLine("[ERROR] 未找到 Java! 请安装 Java 11+");
        Console.WriteLine("下载: https://adoptium.net/");
        return false;
    }

    static bool StartBackend(string jarPath)
    {
        try
        {
            backendProcess = new Process();
            backendProcess.StartInfo.FileName = "java";
            backendProcess.StartInfo.Arguments = "-jar \"" + jarPath + "\"";
            backendProcess.StartInfo.WorkingDirectory = Path.GetDirectoryName(jarPath);
            backendProcess.StartInfo.UseShellExecute = true;

            backendProcess.Start();
            Console.WriteLine("  -> 后端进程已启动 (PID: " + backendProcess.Id + ")");
            return true;
        }
        catch (Exception ex)
        {
            Console.WriteLine("  [ERROR] " + ex.Message);
            return false;
        }
    }

    static bool WaitForBackendReady()
    {
        int maxAttempts = 90;
        int attempt = 0;

        while (attempt < maxAttempts)
        {
            if (backendProcess != null && backendProcess.HasExited)
            {
                Console.WriteLine("  [ERROR] 后端进程已意外退出!");
                return false;
            }

            try
            {
                var req = WebRequest.CreateHttp("http://127.0.0.1:8082");
                req.Timeout = 1500;
                using (var resp = req.GetResponse())
                {
                    return true;
                }
            }
            catch
            {
                // port not open yet
            }

            attempt++;
            if (attempt % 10 == 0)
                Console.WriteLine("  -> 等待中... (" + attempt + "秒)");
            Thread.Sleep(1000);
        }

        return false;
    }

    static void StartElectron(string electronDir)
    {
        try
        {
            var psi = new ProcessStartInfo("cmd.exe", "/c npx electron .")
            {
                WorkingDirectory = electronDir,
                UseShellExecute = true
            };
            var electronProcess = Process.Start(psi);
            Console.WriteLine("  -> 客户端已启动 (PID: " + electronProcess.Id + ")");
            Console.WriteLine();
            Console.WriteLine("========================================");
            Console.WriteLine("  自习室预约系统 - 运行中");
            Console.WriteLine("  关闭客户端窗口将自动停止后端服务");
            Console.WriteLine("========================================");
            Console.WriteLine();

            electronProcess.WaitForExit();
        }
        catch (Exception ex)
        {
            Console.WriteLine("  [ERROR] 启动客户端失败: " + ex.Message);
        }
    }

    static void StopBackend()
    {
        if (backendProcess != null && !backendProcess.HasExited)
        {
            try
            {
                backendProcess.Kill();
                backendProcess.WaitForExit(5000);
                Console.WriteLine("  -> 后端服务已停止");
            }
            catch (Exception ex)
            {
                Console.WriteLine("  [WARN] 停止后端时出现异常: " + ex.Message);
            }
        }
    }

    static void Pause()
    {
        Console.WriteLine();
        Console.Write("按任意键退出...");
        Console.ReadKey(true);
    }
}
