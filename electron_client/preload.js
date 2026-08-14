const { contextBridge, ipcRenderer } = require('electron');

// 暴露安全的 API 给渲染进程（Vue 前端）
contextBridge.exposeInMainWorld('electronAPI', {
  // 平台信息
  platform: process.platform,

  // 发送通知（调用系统通知）
  sendNotification: (title, body) => {
    ipcRenderer.send('show-notification', { title, body });
  },

  // 窗口控制
  minimize: () => ipcRenderer.send('window-minimize'),
  maximize: () => ipcRenderer.send('window-maximize'),
  close: () => ipcRenderer.send('window-close'),

  // 应用信息
  getAppVersion: () => ipcRenderer.invoke('get-app-version'),
});
