# 自习室预约系统

> 基于 Spring Boot + H2 的桌面/Web 双模自习室预约管理系统。

---

## 功能

- 自习室/座位管理
- 预约/签到/签退
- 用户管理
- 数据统计
- 桌面客户端（Electron）+ 浏览器双模式

## 快速启动

### 方式一：浏览器直接访问（推荐，无需安装）

```bash
cd backend
java -jar studyroom.jar
```

浏览器打开 `http://localhost:8082`

### 方式二：旧版桌面客户端

```bash
# 先启动后端
cd backend && java -jar studyroom.jar

# 再启动 Electron 客户端（可选）
cd electron_client && npx electron .
```

## 项目结构

```
studyroom_submit/
├── backend/               # 编译好的后端（3 个 jar）
├── backend_src/           # Spring Boot 源码
├── electron_client/       # Electron 桌面壳（已弃用，推荐直接用浏览器）
├── frontend_src/          # Vue 前端源码
├── Launcher.cs            # C# 启动器源码
├── StudyRoom Launcher.exe # 编译后的启动器
├── run.vbs                # 静默启动（无黑窗口）
├── start-electron.bat     # Electron 启动脚本
└── start.bat              # 后端启动脚本
```

## 技术栈

| 组件 | 技术 |
|---|---|
| 后端框架 | Spring Boot 3.2 |
| 数据库 | H2（文件模式） |
| 前端 | Vue 2 + Element-UI |
| 桌面壳 | Electron（可选） |
| 构建工具 | Maven |

## 远程访问设置

见 [docs/remote-access.md](docs/remote-access.md)