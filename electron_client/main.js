const { app, BrowserWindow, Tray, Menu, nativeImage, Notification, dialog } = require('electron');
const path = require('path');
const http = require('http');

const BACKEND_URL = 'http://localhost:8082';
const APP_NAME = '自习室预约系统';

let mainWindow = null;
let tray = null;
let isQuitting = false;

// ========== 工具函数 ==========

/** 通知后端关闭 */
function shutdownBackend() {
  return new Promise((resolve) => {
    const req = http.request(`${BACKEND_URL}/api/shutdown`, {
      method: 'POST',
      timeout: 3000,
    }, () => resolve(true));
    req.on('error', () => resolve(false));
    req.on('timeout', () => { req.destroy(); resolve(false); });
    req.end();
  });
}

/** 检查后端是否已启动 */
function checkBackend() {
  return new Promise((resolve) => {
    const req = http.get(`${BACKEND_URL}/`, (res) => {
      resolve(res.statusCode >= 200 && res.statusCode < 400);
    });
    req.on('error', () => resolve(false));
    req.setTimeout(3000, () => { req.destroy(); resolve(false); });
  });
}

/** 等待后端启动 */
async function waitForBackend(maxRetries = 30, interval = 2000) {
  for (let i = 0; i < maxRetries; i++) {
    const ok = await checkBackend();
    if (ok) return true;
    await new Promise(r => setTimeout(r, interval));
  }
  return false;
}

/** 创建系统托盘 */
function createTray() {
  // 用原生图标（16x16 简单图标）
  const icon = nativeImage.createFromBuffer(
    Buffer.alloc(16 * 16 * 4, 0),
    { width: 16, height: 16 }
  );
  // 画一个简单的蓝色方块作为托盘图标
  const canvas = Buffer.alloc(16 * 16 * 4);
  for (let y = 0; y < 16; y++) {
    for (let x = 0; x < 16; x++) {
      const i = (y * 16 + x) * 4;
      canvas[i] = 64;     // B
      canvas[i+1] = 158;  // G
      canvas[i+2] = 255;  // R
      canvas[i+3] = 255;  // A
    }
  }
  const trayIcon = nativeImage.createFromBuffer(canvas, { width: 16, height: 16 });

  tray = new Tray(trayIcon);
  tray.setToolTip(APP_NAME);

  const contextMenu = Menu.buildFromTemplate([
    {
      label: '显示窗口',
      click: () => {
        if (mainWindow) {
          mainWindow.show();
          mainWindow.focus();
        }
      }
    },
    { type: 'separator' },
    {
      label: '退出',
      click: () => {
        isQuitting = true;
        shutdownBackend();
        app.quit();
      }
    }
  ]);

  tray.setContextMenu(contextMenu);
  tray.on('double-click', () => {
    if (mainWindow) {
      mainWindow.show();
      mainWindow.focus();
    }
  });
}

// ========== 窗口管理 ==========

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 800,
    minWidth: 960,
    minHeight: 600,
    title: APP_NAME,
    icon: path.join(__dirname, 'icon.png'),
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
    show: false,
  });

  // 加载后端服务的 Web 页面
  mainWindow.loadURL(BACKEND_URL);

  // 页面加载完成后再显示，避免白屏闪烁
  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  // 点击关闭按钮时：通知后端关闭，然后退出应用
  mainWindow.on('close', (event) => {
    if (!isQuitting) {
      event.preventDefault();
      shutdownBackend();
      isQuitting = true;
      app.quit();
    }
  });

  mainWindow.on('closed', () => {
    mainWindow = null;
  });

  // 设置页面标题
  mainWindow.webContents.on('page-title-updated', (event) => {
    event.preventDefault();
  });
}

// ========== IPC 处理 ==========

function setupIPC() {
  const { ipcMain, Notification } = require('electron');

  ipcMain.on('show-notification', (event, { title, body }) => {
    if (Notification.isSupported()) {
      new Notification({ title: title || APP_NAME, body: body || '' }).show();
    }
  });

  ipcMain.on('window-minimize', () => {
    if (mainWindow) mainWindow.minimize();
  });

  ipcMain.on('window-maximize', () => {
    if (mainWindow) {
      mainWindow.isMaximized() ? mainWindow.unmaximize() : mainWindow.maximize();
    }
  });

  ipcMain.on('window-close', () => {
    if (mainWindow) mainWindow.close();
  });

  ipcMain.handle('get-app-version', () => {
    return require('./package.json').version;
  });
}

// ========== 应用生命周期 ==========

app.whenReady().then(async () => {
  // 创建托盘图标
  createTray();

  // 检查后端是否运行
  const backendRunning = await checkBackend();

  if (!backendRunning) {
    // 后端未启动，弹窗提示
    const result = dialog.showMessageBoxSync({
      type: 'warning',
      title: APP_NAME,
      message: '后端服务未启动',
      detail: '检测到自习室后端服务尚未运行。\n\n请先运行 start.bat 启动后端，然后再启动本客户端。\n\n点击"重试"再次检测，点击"退出"关闭。',
      buttons: ['重试', '退出'],
      defaultId: 0,
      cancelId: 1,
    });

    if (result === 0) {
      // 重试：等待后端最多 60 秒
      const started = await waitForBackend(30, 2000);
      if (!started) {
        dialog.showErrorBox('连接失败', '无法连接到后端服务 http://localhost:8082\n\n请确保已运行 start.bat 并等待启动完成。');
        app.quit();
        return;
      }
    } else {
      app.quit();
      return;
    }
  }

  // 注册 IPC 处理
  setupIPC();

  // 后端已就绪，创建窗口
  createWindow();

  // macOS: 点击dock图标重新显示窗口
  app.on('activate', () => {
    if (mainWindow === null) {
      createWindow();
    } else {
      mainWindow.show();
    }
  });
});

app.on('window-all-closed', () => {
  // 在非 macOS 下，如果没有托盘则不退出（实际上我们用了托盘，所以不退出）
  if (process.platform !== 'darwin') {
    // 不退出，保持托盘运行
  }
});

app.on('before-quit', () => {
  isQuitting = true;
});
