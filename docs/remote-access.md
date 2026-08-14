# 远程访问配置

> 让其他电脑通过浏览器访问自习室预约系统。

---

## 前提

后端已启动（`java -jar backend/studyroom.jar`），运行在 `http://localhost:8082`。

---

## 方案一：同一局域网（最简单）

### 1. 查看服务器 IP

在运行后端的电脑上打开命令提示符：

```bash
ipconfig
```

找到 IPv4 地址，例如 `192.168.1.100`。

### 2. 其他电脑访问

同一 WiFi/交换机下的其他设备，浏览器打开：

```
http://192.168.1.100:8082
```

---

## 方案二：跨网络（端口转发）

### 路由器端口映射

```
路由器设置 → 端口转发 / 虚拟服务器 → 添加规则:

外部端口: 8082
内部 IP:   192.168.1.100（运行后端的电脑）
内部端口: 8082
协议:     TCP
```

### 获取公网 IP

```bash
# 在运行后端的电脑上打开
curl ip.sb
# 或浏览器访问 https://ip.sb
```

### 远程访问

```
http://公网IP:8082
```

---

## 方案三：Tailscale 虚拟局域网（推荐，无需公网IP）

### 1. 两台电脑都安装 Tailscale

```bash
# 下载: https://tailscale.com/download
# 登录同一账号
```

### 2. 获取 Tailscale IP

```bash
# 在后端电脑上运行
ipconfig
# 看到 100.x.x.x 开头的 IP
```

### 3. 远程访问

```
http://100.x.x.x:8082
```

---

## 后端配置修改（可选）

Spring Boot 默认只绑定 `localhost`，需要改成 `0.0.0.0` 才能被其他电脑访问。

如果你用 `start.bat` 启动，后端已经绑定了 `0.0.0.0:8082`，可以直接访问。

如果启动脚本里没有指定，修改：

```bash
java -jar backend/studyroom.jar --server.address=0.0.0.0 --server.port=8082
```

---

## 防火墙设置

Windows 防火墙可能阻止外部访问，需放行端口：

```powershell
# 管理员 PowerShell
New-NetFirewallRule -DisplayName "StudyRoom 8082" -Direction Inbound -Protocol TCP -LocalPort 8082 -Action Allow
```

---

## 安全提醒

| 场景 | 建议 |
|---|---|
| 仅局域网使用 | 无需额外安全措施 |
| 暴露到公网 | 建议加反向代理（nginx）+ HTTPS + 登录认证 |
| 生产部署 | 改用 MySQL，替换 H2 数据库 |