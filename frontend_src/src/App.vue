<template>
  <!-- ============================================================
   根容器：根据登录状态显示「登录页」或「主界面」
   ============================================================ -->
  <div class="app-container">
    <!-- ========== 登录页（未登录时显示） ========== -->
    <div v-if="token === null" class="auth-page">
      <div class="auth-bg">
        <!-- ===== 星空背景（大量小点） ===== -->
        <div class="starfield"></div>
        <!-- ===== 3D 漂浮粒子 ===== -->
        <div class="auth-particles">
          <div class="particle p1"></div>
          <div class="particle p2"></div>
          <div class="particle p3"></div>
          <div class="particle p4"></div>
          <div class="particle p5"></div>
          <div class="particle p6"></div>
          <div class="particle p7"></div>
          <div class="particle p8"></div>
        </div>
        <!-- ===== 3D 几何体装饰 ===== -->
        <div class="geo-shapes">
          <div class="geo-cube">
            <div class="cube-face front"></div>
            <div class="cube-face back"></div>
            <div class="cube-face right"></div>
            <div class="cube-face left"></div>
            <div class="cube-face top"></div>
            <div class="cube-face bottom"></div>
          </div>
          <div class="geo-ring">
            <div class="ring-segment" v-for="i in 12" :key="i" :style="{transform: 'rotateY(' + (i * 30) + 'deg) translateZ(40px)'}"></div>
          </div>
          <div class="geo-triangle">
            <div class="tri-face"></div>
            <div class="tri-face"></div>
            <div class="tri-face"></div>
          </div>
          <div class="geo-orb"></div>
        </div>
      </div>
      <div class="auth-card">
        <div class="auth-header">
          <div class="auth-logo">
            <svg viewBox="0 0 48 48" fill="none" width="56" height="56">
              <rect x="6" y="20" width="36" height="24" rx="3" fill="currentColor" opacity="0.2"/>
              <rect x="10" y="24" width="28" height="16" rx="2" fill="currentColor" opacity="0.4"/>
              <path d="M24 4L24 20M16 10L24 4L32 10" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="24" cy="20" r="3" fill="currentColor"/>
              <rect x="22" y="22" width="4" height="8" rx="1" fill="currentColor"/>
            </svg>
          </div>
          <h1 class="auth-title">自习室预约系统</h1>
          <p class="auth-subtitle" data-typewriter="Study Room Reservation System"></p>
        </div>

        <!-- 端选择器：切换"学生端" / "管理员端"登录 -->
        <div class="role-tabs">
          <span class="role-tab" :class="{ active: loginRole === 'student' }" @click="loginRole = 'student'">🎓 学生端</span>
          <span class="role-tab" :class="{ active: loginRole === 'admin' }" @click="loginRole = 'admin'">⚙️ 管理员端</span>
        </div>

        <!-- 登录/注册表单（带切换动画） -->
        <Transition name="form-fade" mode="out-in">
          <div v-if="!showRegister" key="login" class="auth-form">
            <div class="form-item stagger" style="--i:1">
              <el-input
                v-model="loginForm.studentId"
                :placeholder="loginRole === 'admin' ? '请输入管理员账号' : '请输入学号'"
                size="large"
                clearable
                @keyup.enter="login"
              >
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </template>
              </el-input>
            </div>
            <div class="form-item stagger" style="--i:2">
              <el-input
                v-model="loginForm.password"
                placeholder="请输入密码"
                type="password"
                size="large"
                show-password
                @keyup.enter="login"
              >
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                </template>
              </el-input>
            </div>
            <el-button type="primary" size="large" class="auth-btn stagger" style="--i:3" @click="login" :loading="loginLoading">
              {{ loginRole === 'admin' ? '管理员登录' : '登 录' }}
            </el-button>
            <div v-if="loginRole === 'student'" class="auth-footer stagger" style="--i:4">
              <span class="auth-tip">还没有账号？</span>
              <el-link type="primary" :underline="false" @click="showRegister = true; Object.assign(regForm, { studentId: '', username: '', password: '' })">立即注册</el-link>
              <div style="margin-top: 10px;">
                <el-link type="info" :underline="false" @click="showResetPwd = true" style="font-size: 13px;">忘记密码？</el-link>
              </div>
            </div>
          </div>
          <div v-else key="register" class="auth-form">
            <div class="form-item">
              <label class="form-label">学号</label>
              <el-input v-model="regForm.studentId" placeholder="请输入学号" size="large" clearable>
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </template>
              </el-input>
            </div>
            <div class="form-item">
              <label class="form-label">姓名</label>
              <el-input v-model="regForm.username" placeholder="请输入姓名" size="large" clearable>
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                </template>
              </el-input>
            </div>
            <div class="form-item">
              <label class="form-label">邮箱</label>
              <el-input v-model="regForm.email" placeholder="请输入邮箱（用于找回密码）" size="large" clearable>
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 7L2 7"/></svg>
                </template>
              </el-input>
            </div>
            <div class="form-item">
              <label class="form-label">邮箱验证码</label>
              <div style="display:flex;gap:10px;">
                <el-input v-model="regForm.code" placeholder="输入邮箱验证码" size="large" style="flex:1;">
                  <template #prefix>
                    <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                  </template>
                </el-input>
                <el-button type="primary" :disabled="regCooldown > 0 || regSending" @click="sendRegisterCode" :loading="regSending" style="flex-shrink:0;border-radius:12px;">
                  {{ regCooldown > 0 ? regCooldown + 's' : '获取验证码' }}
                </el-button>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label">密码</label>
              <el-input v-model="regForm.password" placeholder="请设置密码" type="password" size="large" show-password>
                <template #prefix>
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                </template>
              </el-input>
            </div>
            <el-button type="primary" size="large" class="auth-btn" @click="register" :loading="regLoading">注 册</el-button>
            <div class="auth-footer">
              <span class="auth-tip">已有账号？</span>
              <el-link type="primary" :underline="false" @click="showRegister = false; Object.assign(regForm, { studentId: '', username: '', password: '', email: '', code: '' })">返回登录</el-link>
            </div>
          </div>
        </Transition>
      </div>
    </div>

    <!-- ========== 重置密码弹窗 ==========
     学生忘记密码时，通过邮箱验证码重置密码
     ===================================== -->
    <el-dialog v-model="showResetPwd" title="重置密码" width="420px" top="5vh" class="auth-dialog" destroy-on-close append-to-body>
      <div class="reset-form">
        <div class="form-item">
          <label class="form-label">学号</label>
          <el-input v-model="resetForm.studentId" placeholder="请输入学号" size="large" clearable @keyup.enter="sendResetCode">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </template>
          </el-input>
        </div>
        <div class="form-item">
          <label class="form-label">绑定的邮箱</label>
          <el-input v-model="resetForm.email" placeholder="请输入绑定的邮箱" size="large" clearable @keyup.enter="sendResetCode">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 7L2 7"/></svg>
            </template>
          </el-input>
        </div>
        <div class="form-item" style="display:flex;gap:10px;flex-direction:column;">
          <label class="form-label">验证码</label>
          <div style="display:flex;gap:10px;">
            <el-input v-model="resetForm.code" placeholder="输入验证码" size="large" style="flex:1;" @keyup.enter="doResetPwd">
              <template #prefix>
                <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
              </template>
            </el-input>
            <el-button type="primary" :disabled="resetCooldown > 0 || resetSending" @click="sendResetCode" :loading="resetSending" style="flex-shrink:0;border-radius:12px;">
              {{ resetCooldown > 0 ? resetCooldown + 's' : '获取验证码' }}
            </el-button>
          </div>
        </div>
        <div class="form-item">
          <label class="form-label">新密码</label>
          <el-input v-model="resetForm.newPassword" placeholder="输入新密码" type="password" size="large" show-password @keyup.enter="doResetPwd">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
            </template>
          </el-input>
        </div>
        <div class="form-item">
          <label class="form-label">确认新密码</label>
          <el-input v-model="resetForm.confirmPassword" placeholder="再次输入新密码" type="password" size="large" show-password @keyup.enter="doResetPwd">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
            </template>
          </el-input>
        </div>
        <el-button type="primary" size="large" class="auth-btn" @click="doResetPwd" :loading="resetLoading">重置密码</el-button>
      </div>
    </el-dialog>

    <!-- ========== 学生端主界面 ==========
     已登录且 userRole === 0（学生角色）时显示
     包含：自习室列表 → 座位弹窗 → 预约 → 我的预约
     ===================================== -->
    <div v-if="token !== null && userRole === 0" class="main-page">
      <!-- 主背景浮动装饰 -->
      <div class="main-bg">
        <div class="bg-blob blob1"></div>
        <div class="bg-blob blob2"></div>
        <div class="bg-blob blob3"></div>
      </div>
      <header class="topbar">
        <div class="topbar-inner">
          <div class="topbar-left">
            <div class="topbar-logo">
              <svg viewBox="0 0 48 48" fill="none" width="30" height="30">
                <rect x="6" y="20" width="36" height="24" rx="3" fill="currentColor" opacity="0.2"/>
                <rect x="10" y="24" width="28" height="16" rx="2" fill="currentColor" opacity="0.4"/>
                <path d="M24 4L24 20M16 10L24 4L32 10" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="24" cy="20" r="3" fill="currentColor"/>
                <rect x="22" y="22" width="4" height="8" rx="1" fill="currentColor"/>
              </svg>
              <span class="topbar-title">自习室预约系统</span>
            </div>
          </div>
          <div class="topbar-center">
            <!-- 学习计时器 -->
            <div class="study-timer" v-if="timerActive" @click="showTimerDetail = !showTimerDetail">
              <div class="timer-icon">⏱</div>
              <div class="timer-body">
                <div class="timer-label">学习时长</div>
                <div class="timer-display">{{ formattedTime }}</div>
              </div>
            </div>
            <div class="tab-list">
              <span class="tab-item" :class="{ active: page === 'room' }" @click="getRoom">📚 自习室列表</span>
              <span class="tab-item" :class="{ active: page === 'my' }" @click="getMy">📋 我的预约</span>
              <span class="tab-item" :class="{ active: page === 'history' }" @click="loadHistory">📜 历史记录</span>
              <span class="tab-item" :class="{ active: page === 'message' }" @click="loadMessages">💬 留言板</span>
            </div>
          </div>
          <div class="topbar-right">
            <el-button class="dark-toggle" :title="darkMode ? '切换亮色' : '切换暗色'" @click="toggleDark" circle>
              <template v-if="darkMode">☀️</template>
              <template v-else>🌙</template>
            </el-button>
            <div class="user-info">
              <span class="user-avatar">{{ (username || '').slice(0, 2) || 'U' }}</span>
              <span class="user-name">{{ username || '用户' }}</span>
            </div>
            <el-button class="logout-btn" @click="logout">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
              退出
            </el-button>
          </div>
        </div>
      </header>

      <!-- 顶部公告横幅（所有学生页面可见） -->
      <div v-if="announcements.length > 0 && !dismissBanner" class="announcement-banner">
        <div class="announcement-banner-inner">
          <span class="ann-banner-icon">📢</span>
          <div class="ann-banner-scroll">
            <div class="ann-banner-track" :style="{ transform: `translateY(-${currentAnnIndex * 100}%)` }">
              <div v-for="a in announcements" :key="a.id" class="ann-banner-item">
                <span class="ann-banner-title">{{ a.title }}</span>
                <span class="ann-banner-sep">·</span>
                <span class="ann-banner-text" v-html="a.content"></span>
                <span v-if="isNewAnn(a.createTime)" class="ann-banner-new">NEW</span>
              </div>
            </div>
          </div>
          <span class="ann-banner-all" @click="showAnnouncementList = true">📋 全部</span>
          <span class="ann-banner-close" @click="dismissBanner = true">✕</span>
        </div>
      </div>

      <main class="main-content">
        <!-- 两栏布局容器 -->
        <div class="content-layout">
          <!-- 左侧面板：用户信息 + 快捷统计 + 公告 -->
          <aside class="side-panel" v-show="page === 'room'">
            <!-- 用户信息卡片 -->
            <div class="profile-card">
              <div class="profile-avatar">{{ (username || '').slice(0, 2) || 'U' }}</div>
              <div class="profile-info">
                <div class="profile-name">{{ username || '用户' }}</div>
                <div class="profile-id">{{ loginForm.studentId || '' }}</div>
              </div>
              <el-button class="profile-logout" size="small" circle @click="logout" title="退出登录">🚪</el-button>
            </div>

            <!-- 快捷统计 -->
            <div class="quick-stats">
              <div class="qs-item">
                <span class="qs-num">{{ roomList.length }}</span>
                <span class="qs-label">自习室</span>
              </div>
              <div class="qs-item">
                <span class="qs-num">{{ roomList.reduce((s,r) => s + availableCount(r.roomId), 0) }}</span>
                <span class="qs-label">空闲座位</span>
              </div>
              <div class="qs-item">
                <span class="qs-num">{{ myList.filter(r => r.status === 1).length }}</span>
                <span class="qs-label">进行中</span>
              </div>
            </div>

            <!-- 公告栏（侧边版） -->
            <div v-if="announcements.length > 0" class="side-announcements">
              <div class="side-ann-header" @click="showAllAnnouncements = !showAllAnnouncements">
                <span>📢 公告</span>
                <span class="side-ann-toggle">{{ showAllAnnouncements ? '▲' : '▼' }}</span>
              </div>
              <div class="side-ann-list" :class="{ expanded: showAllAnnouncements }">
                <div v-for="a in (showAllAnnouncements ? announcements : announcements.slice(0,2))" :key="a.id" class="side-ann-item">
                  <div class="side-ann-title">{{ a.title }}</div>
                  <div class="side-ann-content" v-if="showAllAnnouncements" v-html="a.content"></div>
                  <div class="side-ann-time" v-if="showAllAnnouncements">{{ a.createTime }}</div>
                </div>
                <div v-if="announcements.length > 2 && !showAllAnnouncements" class="side-ann-more" @click="showAllAnnouncements = true">
                  还有 {{ announcements.length - 2 }} 条公告 →
                </div>
              </div>
            </div>
          </aside>

          <!-- 右侧主内容区 -->
          <div class="content-main">
            <!-- 自习室列表 -->
            <div v-show="page === 'room' && !seatDialog" class="room-page">
              <div class="room-grid">
                <div v-for="(room, idx) in roomList" :key="room.roomId" class="room-card" :style="{animationDelay: idx * 0.08 + 's'}" @click="openSeat(room.roomId)">
              <div class="room-card-bg" :style="{ background: roomGradient(room.roomId) }"></div>
              <div class="room-card-icon">{{ roomIcon(room.roomId) }}</div>
              <div class="room-card-body">
                <h3 class="room-name">{{ room.roomName }}</h3>
                <div class="room-stats">
                  <div class="stat">
                    <span class="stat-num">{{ room.totalSeat }}</span>
                    <span class="stat-label">总座位</span>
                  </div>
                  <div class="stat-divider"></div>
                  <div class="stat">
                    <span class="stat-num">{{ availableCount(room.roomId) }}</span>
                    <span class="stat-label">可用</span>
                  </div>
                </div>
                <div class="room-time">
                  <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                  {{ room.openTime || '08:00' }} - {{ room.closeTime || '22:00' }}
                </div>
              </div>
              <div class="room-card-arrow">
                <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12h14M12 5l7 7-7 7"/></svg>
              </div>
            </div>
          </div>
        </div>
          </div>
        </div>

        <!-- 座位弹窗
         点击自习室后弹出，显示该自习室的座位网格
         seat.status: 0=空闲, 1=已预约
         seat.reserveUser: 预约该座位的用户ID（与学生端当前token比较，区分"我的座位"和"别人占的"）
        -->
        <el-dialog v-model="seatDialog" :title="'选择座位 — ' + (roomList.find(r => r.roomId === currentRoomId)?.roomName || '')" width="640px" top="5vh" class="seat-dialog" destroy-on-close append-to-body>
          <!-- 座位特征筛选 -->
          <div v-if="seatFeatureOptions.length > 0" class="seat-feature-filter">
            <span class="filter-label">特征：</span>
            <el-check-tag v-for="opt in seatFeatureOptions" :key="opt" :checked="selectedFeatures.includes(opt)" @change="toggleFeatureFilter(opt)" style="margin-right:6px;margin-bottom:6px;">
              {{ featureLabel(opt) }}
            </el-check-tag>
            <el-button v-if="selectedFeatures.length > 0" size="small" text @click="selectedFeatures=[]">清除</el-button>
          </div>
          <div class="seat-feature-filter" style="margin-top:-4px;">
            <span class="filter-label">状态：</span>
            <el-check-tag :checked="selectedStatuses.length===0" @change="selectedStatuses=[]" style="margin-right:6px;margin-bottom:6px;">全部</el-check-tag>
            <el-check-tag v-for="opt in statusFilterOptions" :key="opt.value" :checked="selectedStatuses.includes(opt.value)" @change="toggleStatusFilter(opt.value)" style="margin-right:6px;margin-bottom:6px;">
              {{ opt.label }}
            </el-check-tag>
          </div>
          <div class="seat-grid">
            <div v-for="(seat, idx) in filteredSeatList" :key="seat.seatId" class="seat-item"
              :style="{animationDelay: idx * 0.03 + 's'}"
              :class="{
                available: seat.status === 0,
                reserved: seat.status === 1 && seat.reserveUser != token,
                mine: seat.reserveUser == token,
                damaged: seat.status === 2,
                maintain: seat.status === 3,
                loading: reservingSeat === seat.seatId
              }"
              @click="seat.status === 0 && reserve(seat.seatId)">
              <div class="seat-inner">
                <span class="seat-no">{{ seat.seatNo }}</span>
                <span class="seat-status">
                  <template v-if="seat.status === 0">空</template>
                  <template v-else-if="seat.status === 2">🔧</template>
                  <template v-else-if="seat.status === 3">🛠</template>
                  <template v-else-if="seat.reserveUser == token">我</template>
                  <template v-else>占</template>
                </span>
                <div class="seat-features" v-if="seat.features">
                  <span v-for="f in (seat.features||'').split(',').map(x=>x.trim()).filter(Boolean)" :key="f" class="seat-feature-tag" :title="featureLabel(f)">{{ {window:'🪟',power:'🔌',quiet:'🔇'}[f] || '' }}</span>
                </div>
              </div>
            </div>
          </div>
          <template #footer>
            <div style="display:flex;align-items:center;justify-content:space-between;width:100%;">
              <div class="seat-legend">
                <span><span class="dot dot-avail"></span> 空闲</span>
                <span><span class="dot dot-mine"></span> 我的座位</span>
                <span><span class="dot dot-occ"></span> 已被预约</span>
                <span><span class="dot dot-damaged"></span> 损坏</span>
                <span><span class="dot dot-maintain"></span> 维护中</span>
              </div>
              <el-button size="small" circle @click="openSeat(currentRoomId)" title="刷新座位状态">🔄</el-button>
            </div>
          </template>
        </el-dialog>

        <!-- 我的预约（含签到/签退/续约） -->
        <div v-show="page === 'my'" class="my-reserves">
          <div v-if="myList.length === 0" class="empty-state">
            <div class="empty-icon">📋</div>
            <p>暂无预约记录</p>
            <el-button type="primary" @click="getRoom">去预约座位</el-button>
          </div>
          <div v-else class="reserve-cards">
            <div v-for="(item, idx) in myList" :key="item.resId" class="reserve-card" :style="{animationDelay: idx * 0.08 + 's'}">
              <div class="reserve-card-badge" :class="statusBadgeClass(item.status)">{{ statusText(item.status) }}</div>
              <div class="reserve-card-body">
                <div class="reserve-room">{{ item.roomName }}</div>
                <div class="reserve-details">
                  <span class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                    座位 {{ item.seatNo }}
                  </span>
                  <span class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                    {{ item.resTime }}
                  </span>
                  <span v-if="item.checkinTime" class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                    签到 {{ item.checkinTime }}
                  </span>
                </div>
              </div>
              <div class="reserve-card-action" style="display:flex;gap:6px;flex-wrap:wrap;">
                <el-button v-if="item.status === 0" type="success" size="small" round @click="doCheckin(item.resId)">✅ 签到</el-button>
                <el-button v-if="item.status === 1" type="warning" size="small" round @click="doCheckout(item.resId, item.seatId)">🚪 签退</el-button>
                <el-button v-if="item.status === 1" type="primary" size="small" round @click="doRenew(item.resId)">⏱ 续约</el-button>
                <el-button v-if="item.status === 0" type="danger" size="small" round @click="cancelRes(item.resId, item.seatId)">取消预约</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 历史记录（已完成/已取消的预约） -->
        <div v-show="page === 'history'" class="my-reserves">
          <div v-if="historyList.length === 0" class="empty-state">
            <div class="empty-icon">📜</div>
            <p>暂无历史记录</p>
          </div>
          <div v-else class="reserve-cards">
            <div v-for="(item, idx) in paginatedHistory" :key="item.resId" class="reserve-card reserve-card-history" :style="{animationDelay: idx * 0.08 + 's'}">
              <div class="reserve-card-badge history-badge">{{ statusText(item.status) }}</div>
              <div class="reserve-card-body">
                <div class="reserve-room">{{ item.roomName }}</div>
                <div class="reserve-details">
                  <span class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                    座位 {{ item.seatNo }}
                  </span>
                  <span class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                    {{ item.resTime }}
                  </span>
                  <span v-if="item.checkinTime" class="reserve-detail">✅ {{ item.checkinTime }}</span>
                  <span v-if="item.checkoutTime" class="reserve-detail">⏰ {{ item.checkoutTime }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="pagination-wrap" v-if="historyList.length > pageSize">
            <el-pagination small layout="prev, pager, next" :total="historyList.length" :page-size="pageSize" v-model:current-page="historyPage"/>
          </div>
        </div>

        <!-- 留言板 -->
        <div v-show="page === 'message'" class="my-reserves">
          <div class="section-header" style="padding:0 0 16px;">
            <h2 style="font-size:18px;">💬 留言板</h2>
            <el-button type="primary" size="small" @click="showMsgDialog = true; msgForm.content = ''">✏️ 写留言</el-button>
          </div>
          <div v-if="messageList.length === 0" class="empty-state">
            <div class="empty-icon">💬</div>
            <p>暂无留言，快来写一条吧</p>
          </div>
          <div v-else class="reserve-cards">
            <div v-for="(item, idx) in paginatedMessages" :key="item.id" class="reserve-card reserve-card-history" :style="{animationDelay: idx * 0.05 + 's'}">
              <div class="reserve-card-body">
                <div class="reserve-room" style="font-size:14px;">
                  <span class="user-avatar mini-avatar">{{ (item.username || '?').slice(0,2) }}</span>
                  <strong>{{ item.username || '匿名' }}</strong>
                  <span style="color:#999;font-size:12px;margin-left:8px;">{{ item.studentId }}</span>
                </div>
                <div class="reserve-details">
                  <p style="margin:6px 0 8px;color:#333;font-size:14px;line-height:1.6;white-space:pre-wrap;">{{ item.content }}</p>
                  <span class="reserve-detail">
                    <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                    {{ item.createTime }}
                  </span>
                  <!-- 显示回复 -->
                  <div v-if="item.replies && item.replies.length" class="reply-list" style="margin-top:8px;">
                    <div class="reply-item" v-for="rep in item.replies" :key="rep.id">
                      <span class="reply-role">{{ rep.username || '管理员' }}</span>
                      <span class="reply-text">{{ rep.content }}</span>
                      <span class="reply-time">{{ rep.createTime }}</span>
                    </div>
                  </div>
                </div>
                <el-button v-if="Number(token) === item.userId" size="small" type="danger" link @click="deleteMessage(item.id)" style="position:absolute;top:8px;right:8px;font-size:12px;">删除</el-button>
              </div>
            </div>
          </div>
          <div class="pagination-wrap" v-if="messageList.length > pageSize">
            <el-pagination small layout="prev, pager, next" :total="messageList.length" :page-size="pageSize" v-model:current-page="msgPage"/>
          </div>
        </div>

        <!-- 写留言弹窗 -->
        <el-dialog v-model="showMsgDialog" title="✏️ 写留言" width="480px" top="20vh" destroy-on-close append-to-body>
          <div style="padding:4px 0;">
            <el-input type="textarea" v-model="msgForm.content" :rows="4" placeholder="你想说什么…" maxlength="500" show-word-limit/>
            <el-button type="primary" style="width:100%;margin-top:12px;" @click="submitMessage" :loading="msgSending">发布留言</el-button>
          </div>
        </el-dialog>
      </main>
    </div>

    <!-- ========== 邮箱绑定弹窗 ========== -->
    <el-dialog v-model="showBindDialog" title="绑定邮箱" width="420px" top="30vh" class="auth-dialog" destroy-on-close append-to-body>
      <div class="reset-form" style="text-align:center;">
        <div style="font-size:40px;margin-bottom:12px;">📧</div>
        <p style="color:#666;margin-bottom:20px;font-size:14px;">请先绑定邮箱，然后才能预约座位</p>
        <div class="form-item">
          <el-input v-model="bindForm.email" placeholder="请输入邮箱" size="large" clearable @keyup.enter="sendBindCode">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 7L2 7"/></svg>
            </template>
          </el-input>
        </div>
        <div class="form-item" style="display:flex;gap:10px;">
          <el-input v-model="bindForm.code" placeholder="输入验证码" size="large" style="flex:1;" @keyup.enter="doBindEmail">
            <template #prefix>
              <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
            </template>
          </el-input>
          <el-button type="primary" :disabled="bindCooldown > 0 || bindSending" @click="sendBindCode" :loading="bindSending" style="flex-shrink:0;border-radius:12px;">
            {{ bindCooldown > 0 ? bindCooldown + 's' : '获取验证码' }}
          </el-button>
        </div>
        <el-button type="primary" size="large" class="auth-btn" @click="doBindEmail" :loading="bindLoading" style="margin-top:4px;">确认绑定</el-button>
        <el-button size="large" style="width:100%;margin-top:10px;border-radius:12px;" @click="showBindDialog = false">稍后再说</el-button>
      </div>
    </el-dialog>

    <!-- ========== 管理员端主界面（已登录+admin角色） ========== -->
    <div v-if="token !== null && userRole === 1" class="main-page admin-page">
      <header class="topbar admin-topbar">
        <div class="topbar-inner">
          <div class="topbar-left">
            <div class="topbar-logo" @click="adminPage='dashboard';loadDashboard()" style="cursor:pointer;">
              <svg viewBox="0 0 48 48" fill="none" width="30" height="30">
                <rect x="6" y="20" width="36" height="24" rx="3" fill="currentColor" opacity="0.2"/>
                <rect x="10" y="24" width="28" height="16" rx="2" fill="currentColor" opacity="0.4"/>
                <path d="M24 4L24 20M16 10L24 4L32 10" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="24" cy="20" r="3" fill="currentColor"/>
                <rect x="22" y="22" width="4" height="8" rx="1" fill="currentColor"/>
              </svg>
              <span class="admin-topbar-title">管理后台</span>
            </div>
          </div>
          <div class="topbar-right">
            <el-button class="dark-toggle" :title="darkMode ? '切换亮色' : '切换暗色'" @click="toggleDark" circle>
              <template v-if="darkMode">☀️</template>
              <template v-else>🌙</template>
            </el-button>
            <div class="user-info">
              <span class="user-avatar admin-avatar">{{ (username || '').slice(0, 2) || 'A' }}</span>
              <span class="user-name">{{ username || '管理员' }}</span>
            </div>
            <el-button class="logout-btn" @click="logout">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
              退出
            </el-button>
          </div>
        </div>
      </header>

      <main class="main-content admin-layout">
        <!-- 左侧导航 -->
        <aside class="admin-nav">
          <div class="admin-nav-item" :class="{active: adminPage === 'dashboard'}" @click="adminPage='dashboard';loadDashboard()">
            <span class="admin-nav-icon">📊</span>
            <span class="admin-nav-label">仪表盘</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'rooms'}" @click="adminPage='rooms';loadAdminRooms()">
            <span class="admin-nav-icon">🏢</span>
            <span class="admin-nav-label">自习室管理</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'seats'}" @click="adminPage='seats';seatRoomFilter=null;loadAdminSeats()">
            <span class="admin-nav-icon">💺</span>
            <span class="admin-nav-label">座位管理</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'users'}" @click="adminPage='users';loadUsers()">
            <span class="admin-nav-icon">👥</span>
            <span class="admin-nav-label">用户管理</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'reserves'}" @click="adminPage='reserves';loadAllReserves()">
            <span class="admin-nav-icon">📋</span>
            <span class="admin-nav-label">预约记录</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'announcements'}" @click="adminPage='announcements';loadAnnouncements()">
            <span class="admin-nav-icon">📢</span>
            <span class="admin-nav-label">公告管理</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'messages'}" @click="adminPage='messages';loadAdminMessages()">
            <span class="admin-nav-icon">💬</span>
            <span class="admin-nav-label">留言管理</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'hours'}" @click="adminPage='hours'">
            <span class="admin-nav-icon">🕐</span>
            <span class="admin-nav-label">开放时间</span>
          </div>
          <div class="admin-nav-item" :class="{active: adminPage === 'rules'}" @click="adminPage='rules';loadRule()">
            <span class="admin-nav-icon">⚙️</span>
            <span class="admin-nav-label">预约规则</span>
          </div>
        </aside>

        <!-- 右侧内容 -->
        <div class="admin-content">
        <!-- === 仪表盘 === -->
        <div v-show="adminPage === 'dashboard'" class="admin-section">
          <div class="section-header"><h2>📊 管理仪表盘</h2></div>
          <!-- 6 个统计卡片，带计数动画 + 3D 入场错位 -->
          <div class="dashboard-grid">
            <div class="dash-card dash-card-blue" style="animation-delay:0.05s">
              <div class="dash-icon">🏢</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.totalRooms }}</div>
                <div class="dash-label">自习室总数</div>
              </div>
            </div>
            <div class="dash-card dash-card-green" style="animation-delay:0.12s">
              <div class="dash-icon">💺</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.totalSeats }}</div>
                <div class="dash-label">总座位数</div>
              </div>
            </div>
            <div class="dash-card dash-card-orange" style="animation-delay:0.19s">
              <div class="dash-icon">✅</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.availableSeats }}</div>
                <div class="dash-label">可用座位</div>
              </div>
            </div>
            <div class="dash-card dash-card-purple" style="animation-delay:0.26s">
              <div class="dash-icon">👤</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.checkedIn }}</div>
                <div class="dash-label">当前签到</div>
              </div>
            </div>
            <div class="dash-card dash-card-pink">
              <div class="dash-icon">📋</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.todayReserves }}</div>
                <div class="dash-label">今日预约</div>
              </div>
            </div>
            <div class="dash-card dash-card-teal">
              <div class="dash-icon">👥</div>
              <div class="dash-info">
                <div class="dash-num">{{ dashboard.totalUsers }}</div>
                <div class="dash-label">注册用户</div>
              </div>
            </div>
          </div>
          <!-- 快捷操作面板 -->
          <div class="quick-actions">
            <div class="qa-title">⚡ 快捷操作</div>
            <div class="qa-grid">
              <div class="qa-item" @click="adminPage='rooms';loadAdminRooms()">
                <span class="qa-icon">🏢</span>
                <span class="qa-label">自习室管理</span>
              </div>
              <div class="qa-item" @click="adminPage='seats';seatRoomFilter=null;loadAdminSeats()">
                <span class="qa-icon">💺</span>
                <span class="qa-label">座位管理</span>
              </div>
              <div class="qa-item" @click="adminPage='users';loadUsers()">
                <span class="qa-icon">👥</span>
                <span class="qa-label">用户管理</span>
              </div>
              <div class="qa-item" @click="showAnnouncementForm = true; editingAnnouncement=null; announcementForm.title=''; announcementForm.content=''; announcementForm.priority=0; announcementForm.status=1">
                <span class="qa-icon">📢</span>
                <span class="qa-label">发布公告</span>
              </div>
            </div>
          </div>
          <!-- ECharts 图表区域 -->
          <div class="chart-section">
            <div class="chart-box">
              <div class="chart-title">📈 座位使用分布</div>
              <div ref="pieChartRef" class="chart-container" style="height:300px;"></div>
            </div>
            <div class="chart-box">
              <div class="chart-title">📊 平台数据总览</div>
              <div ref="barChartRef" class="chart-container" style="height:300px;"></div>
            </div>
          </div>
          <!-- 峰值时段热力图 -->
          <div class="heatmap-section">
            <div class="heatmap-header">
              <div class="chart-title">🔥 峰值时段热力图</div>
              <div class="heatmap-summary" id="heatmapSummary">📊 加载数据中...</div>
            </div>
            <div class="heatmap-legend">
              <span class="hml-item"><span class="hml-dot" style="background:#2c3e9e"></span>空闲</span>
              <span class="hml-item"><span class="hml-dot" style="background:#3d6bc7"></span>较低</span>
              <span class="hml-item"><span class="hml-dot" style="background:#6da5e8"></span>适中</span>
              <span class="hml-item"><span class="hml-dot" style="background:#f8e445"></span>较忙</span>
              <span class="hml-item"><span class="hml-dot" style="background:#f5a623"></span>忙</span>
              <span class="hml-item"><span class="hml-dot" style="background:#e6503a"></span>高峰</span>
              <span class="hml-item"><span class="hml-dot" style="background:#a82020"></span>爆满</span>
            </div>
            <div ref="heatmapChartRef" class="chart-container" style="height:340px;"></div>
          </div>
        </div>

        <!-- === 自习室管理 === -->
        <div v-show="adminPage === 'rooms'" class="admin-section">
          <div class="section-header">
            <h2>🏢 自习室管理</h2>
<el-button type="primary" @click="showRoomForm = true; Object.assign(roomForm, {roomName:'', totalSeat:30, openTime:'08:00', closeTime:'22:00'})">+ 新增自习室</el-button>
          </div>
          <el-table :data="adminRoomList" stripe style="width:100%" v-loading="roomLoading">
            <el-table-column type="index" label="#" width="70"/>
            <el-table-column prop="roomName" label="名称"/>
            <el-table-column prop="totalSeat" label="总座位" width="100"/>
            <el-table-column label="开放时间" width="200">
              <template #default="{row}">
                {{ row.openTime || '08:00' }} - {{ row.closeTime || '22:00' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="{row}">
                <el-button size="small" @click="editRoom(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteRoom(row.roomId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-dialog v-model="showRoomForm" :title="editingRoom ? '编辑自习室' : '新增自习室'" width="460px" top="20vh" destroy-on-close append-to-body>
            <div class="admin-form">
              <div class="form-item">
                <label class="form-label">名称</label>
                <el-input v-model="roomForm.roomName" placeholder="自习室名称"/>
              </div>
              <div class="form-item">
                <label class="form-label">总座位数</label>
                <el-input-number v-model="roomForm.totalSeat" :min="1" :max="500"/>
              </div>
              <div class="form-item">
                <label class="form-label">开放时间</label>
                <el-time-picker v-model="roomForm.openTime" format="HH:mm" value-format="HH:mm" placeholder="开放时间" style="width:100%"/>
              </div>
              <div class="form-item">
                <label class="form-label">关闭时间</label>
                <el-time-picker v-model="roomForm.closeTime" format="HH:mm" value-format="HH:mm" placeholder="关闭时间" style="width:100%"/>
              </div>
              <el-button type="primary" style="width:100%;margin-top:8px;" @click="saveRoom">保存</el-button>
            </div>
          </el-dialog>
        </div>

        <!-- === 座位管理 === -->
        <div v-show="adminPage === 'seats'" class="admin-section">
          <div class="section-header">
            <h2>💺 座位管理</h2>
            <div style="display:flex;gap:10px;align-items:center;">
              <el-input v-model="seatSearch" placeholder="🔍 搜索座位号…" clearable style="width:160px;" @input="filterSeats"/>
              <el-select v-model="seatRoomFilter" placeholder="选择自习室" style="width:180px;" clearable @change="filterSeats">
                <el-option v-for="r in adminRoomList" :key="r.roomId" :label="r.roomName" :value="r.roomId"/>
              </el-select>
              <el-button type="primary" :disabled="!seatRoomFilter" @click="showSeatForm = true; Object.assign(seatForm, {roomId: seatRoomFilter, seatNo:''}); seatBatchInput = ''">+ 添加座位</el-button>
            </div>
          </div>
          <el-table :data="filteredSeats" stripe style="width:100%" v-loading="seatLoading">
            <el-table-column type="index" label="#" width="60"/>
            <el-table-column prop="roomName" label="自习室"/>
            <el-table-column prop="seatNo" label="座位号" width="100"/>
            <el-table-column label="状态" width="120">
              <template #default="{row}">
                <el-tag v-if="row.status === 0" type="success" size="small">空闲</el-tag>
                <el-tag v-else-if="row.status === 1" type="warning" size="small">已预约</el-tag>
                <el-tag v-else-if="row.status === 2" type="danger" size="small">损坏</el-tag>
                <el-tag v-else-if="row.status === 3" type="info" size="small">维护中</el-tag>
                <el-tag v-else type="" size="small">未知</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="380">
              <template #default="{row}">
                <!-- 状态切换下拉 -->
                <el-select v-model="row.status" size="small" style="width:110px;margin-right:6px;" @change="changeSeatStatus(row)">
                  <el-option :value="0" label="空闲"/>
                  <el-option :value="1" label="已预约"/>
                  <el-option :value="2" label="损坏"/>
                  <el-option :value="3" label="维护中"/>
                </el-select>
                <el-button size="small" @click="openSeatFeatureEdit(row)">🏷 特征</el-button>
                <el-button size="small" type="success" @click="openAdminReserve(row)">📌 代预约</el-button>
                <el-button size="small" type="danger" @click="deleteSeat(row.seatId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-dialog v-model="showSeatForm" title="批量添加座位" width="440px" top="25vh" destroy-on-close append-to-body>
            <div class="admin-form">
              <div class="form-item">
                <label class="form-label">自习室</label>
                <el-input :model-value="(adminRoomList.find(r=>r.roomId===seatForm.roomId)||{}).roomName" disabled/>
              </div>
              <div class="form-item">
                <label class="form-label">座位号</label>
                <el-input type="textarea" v-model="seatBatchInput" placeholder="支持多种输入格式：&#10;A01, A02, A03  用逗号分隔&#10;A01-A20  用短横线表示范围&#10;每行一个，或混合使用" :rows="4" style="font-size:13px;"/>
              </div>
              <div class="form-item" v-if="seatPreview.length > 0" style="font-size:13px;color:#909399;">
                即将添加 <strong>{{ seatPreview.length }}</strong> 个座位：{{ seatPreview.slice(0,10).join(', ') }}<span v-if="seatPreview.length > 10">…等{{ seatPreview.length }}个</span>
              </div>
              <el-button type="primary" style="width:100%;margin-top:8px;" @click="batchAddSeats" :disabled="seatPreview.length === 0">确认添加 ({{ seatPreview.length }})</el-button>
            </div>
          </el-dialog>

          <!-- 座位特征编辑弹窗 -->
          <el-dialog v-model="showSeatFeatureDialog" title="🏷 设置座位特征" width="420px" top="25vh" destroy-on-close append-to-body>
            <div class="admin-form">
              <div class="form-item">
                <label class="form-label">座位</label>
                <el-input :model-value="editingFeatureSeat?.roomName + ' - ' + editingFeatureSeat?.seatNo" disabled/>
              </div>
              <div class="form-item" style="margin-top:16px;">
                <label class="form-label">特征（可多选）</label>
                <div style="display:flex;gap:10px;margin-top:8px;flex-wrap:wrap;">
                  <el-check-tag v-for="opt in seatFeatureOptions" :key="opt"
                    :checked="selectedEditFeatures.includes(opt)"
                    @change="toggleEditFeature(opt)"
                    style="padding:8px 16px;border-radius:20px;font-size:14px;">
                    {{ featureLabel(opt) }}
                  </el-check-tag>
                </div>
              </div>
              <el-button type="primary" style="width:100%;margin-top:16px;" @click="saveSeatFeatures" :loading="savingFeatures">保存特征</el-button>
            </div>
          </el-dialog>

          <!-- 管理员代预约弹窗：按学号为特定学生预约座位 -->
          <el-dialog v-model="showAdminReserveDialog" title="📌 管理员代预约" width="420px" top="25vh" destroy-on-close append-to-body>
            <div class="admin-form">
              <div class="form-item">
                <label class="form-label">座位</label>
                <el-input :model-value="adminReserveSeat?.roomName + ' - ' + adminReserveSeat?.seatNo" disabled/>
              </div>
              <div class="form-item">
                <label class="form-label">学生学号</label>
                <el-input v-model="adminReserveForm.studentId" placeholder="输入学生学号" @keyup.enter="doAdminReserve"/>
              </div>
              <el-button type="primary" style="width:100%;margin-top:8px;" @click="doAdminReserve" :loading="adminReserveLoading">确认代预约</el-button>
            </div>
          </el-dialog>
        </div>

        <!-- === 用户管理 === -->
        <div v-show="adminPage === 'users'" class="admin-section">
          <div class="section-header">
            <h2>👥 用户管理</h2>
          </div>
          <el-table :data="userList" stripe style="width:100%" v-loading="userLoading">
            <el-table-column type="index" label="#" width="70"/>
            <el-table-column prop="studentId" label="学号" width="140"/>
            <el-table-column prop="username" label="姓名"/>
            <el-table-column prop="email" label="邮箱" min-width="180"/>
            <el-table-column label="角色" width="80">
              <template #default="{row}">
                <el-tag :type="row.role === 1 ? 'danger' : 'primary'" size="small">{{ row.role === 1 ? '管理员' : '学生' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="{row}">
                <el-tag :type="row.status === 1 ? 'danger' : 'success'" size="small">{{ row.status === 1 ? '已拉黑' : '正常' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{row}">
                <el-button v-if="row.status === 1 && row.role !== 1" size="small" type="success" @click="unbanUser(row.id)">解封</el-button>
                <el-button v-else-if="row.role !== 1" size="small" type="warning" @click="banUser(row.id)">拉黑</el-button>
                <span v-else style="color:#999;font-size:12px;">-</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- === 预约记录管理 === -->
        <div v-show="adminPage === 'reserves'" class="admin-section">
          <div class="section-header">
            <h2>📋 预约记录管理</h2>
            <div style="display:flex;align-items:center;">
              <el-input v-model="reserveSearch" placeholder="🔍 搜索学号、姓名、自习室…" clearable style="width:260px;" @input="filterReserves"/>
              <el-button size="small" @click="loadAllReserves" style="margin-left:8px;">🔄 刷新</el-button>
              <el-button size="small" type="success" @click="exportReservesCSV" style="margin-left:6px;">📥 导出 CSV</el-button>
            </div>
          </div>
          <el-table :data="paginatedAdminReserves" stripe style="width:100%" v-loading="reserveLoading">
            <el-table-column type="index" label="#" width="60"/>
            <el-table-column prop="studentId" label="学号" width="120"/>
            <el-table-column prop="username" label="姓名" width="100"/>
            <el-table-column prop="roomName" label="自习室" width="140"/>
            <el-table-column prop="seatNo" label="座位号" width="80"/>
            <el-table-column prop="resTime" label="预约时间" min-width="160"/>
            <el-table-column label="状态" width="90">
              <template #default="{row}">
                <el-tag :type="row.status===1?'warning':row.status===2?'success':'info'" size="small">{{ statusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="签到时间" min-width="160">
              <template #default="{row}">{{ row.checkinTime || '-' }}</template>
            </el-table-column>
            <el-table-column label="签退时间" min-width="160">
              <template #default="{row}">{{ row.checkoutTime || '-' }}</template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrap" v-if="allReserves.length > pageSize" style="margin-top:16px;">
            <el-pagination small background layout="total, prev, pager, next" :total="allReserves.length" :page-size="pageSize" v-model:current-page="reservePage"/>
          </div>
        </div>

        <!-- === 开放时间管理 === -->
        <div v-show="adminPage === 'hours'" class="admin-section">
          <div class="section-header">
            <h2>🕐 自习室开放时间设置</h2>
          </div>
          <div class="hour-cards">
            <div v-for="room in adminRoomList" :key="room.roomId" class="hour-card">
              <div class="hour-card-header">{{ room.roomName }}</div>
              <div class="hour-card-body">
                <div class="form-item">
                  <label class="form-label">开放时间</label>
                  <el-time-picker v-model="room.openTime" format="HH:mm" value-format="HH:mm" placeholder="开放时间" style="width:100%"/>
                </div>
                <div class="form-item">
                  <label class="form-label">关闭时间</label>
                  <el-time-picker v-model="room.closeTime" format="HH:mm" value-format="HH:mm" placeholder="关闭时间" style="width:100%"/>
                </div>
                <el-button type="primary" size="small" style="width:100%;margin-top:4px;" @click="updateRoomHours(room)">保存</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- === 公告管理 === -->
        <div v-show="adminPage === 'announcements'" class="admin-section">
          <div class="section-header">
            <h2>📢 公告管理</h2>
            <el-button type="primary" @click="showAnnouncementForm = true; Object.assign(announcementForm, {title:'', content:'', priority:0, status:1})">+ 新增公告</el-button>
          </div>
          <el-table :data="paginatedAnnouncements" stripe style="width:100%" v-loading="announcementLoading">
            <el-table-column type="index" label="#" width="60"/>
            <el-table-column prop="title" label="标题" min-width="200"/>
            <el-table-column label="状态" width="80">
              <template #default="{row}">
                <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '已发布' : '草稿' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"/>
            <el-table-column label="操作" width="160">
              <template #default="{row}">
                <el-button size="small" @click="editAnnouncement(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteAnnouncement(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrap" v-if="announcementList.length > pageSize" style="margin-top:16px;">
            <el-pagination small background layout="total, prev, pager, next" :total="announcementList.length" :page-size="pageSize" v-model:current-page="announcePage"/>
          </div>

          <el-dialog v-model="showAnnouncementForm" :title="editingAnnouncement ? '编辑公告' : '新增公告'" width="560px" top="5vh" destroy-on-close append-to-body>
            <div class="admin-form">
              <div class="form-item">
                <label class="form-label">标题</label>
                <el-input v-model="announcementForm.title" placeholder="公告标题"/>
              </div>
              <div class="form-item">
                <label class="form-label">内容</label>
                <el-input type="textarea" v-model="announcementForm.content" :rows="4" placeholder="公告内容（支持HTML）"/>
              </div>
              <div class="form-item">
                <label class="form-label">优先级（数字越大越靠前）</label>
                <el-input-number v-model="announcementForm.priority" :min="0" :max="999"/>
              </div>
              <div class="form-item">
                <label class="form-label">状态</label>
                <el-radio-group v-model="announcementForm.status">
                  <el-radio :value="1">发布</el-radio>
                  <el-radio :value="0">草稿</el-radio>
                </el-radio-group>
              </div>
              <el-button type="primary" style="width:100%;margin-top:8px;" @click="saveAnnouncement">保存</el-button>
            </div>
          </el-dialog>
        </div>

        <!-- === 留言管理（管理员） === -->
        <div v-show="adminPage === 'messages'" class="admin-section">
          <div class="section-header">
            <h2>💬 留言管理</h2>
          </div>
          <div v-if="adminMessageList.length === 0" class="empty-state">
            <div class="empty-icon">💬</div>
            <p>暂无留言</p>
          </div>
          <div v-else class="reserve-cards">
            <div v-for="(item, idx) in adminMessageList" :key="'am'+item.id" class="reserve-card reserve-card-history" :style="{animationDelay: idx * 0.05 + 's'}">
              <div class="reserve-card-body">
                <div class="reserve-room" style="font-size:14px;">
                  <span class="user-avatar mini-avatar">{{ (item.username || '?').slice(0,2) }}</span>
                  <strong>{{ item.username || '匿名' }}</strong>
                  <span style="color:#999;font-size:12px;margin-left:8px;">{{ item.studentId }}</span>
                  <span style="color:#bbb;font-size:11px;margin-left:10px;">{{ item.createTime }}</span>
                </div>
                <div class="reserve-details">
                  <p style="margin:6px 0 8px;color:#333;font-size:14px;line-height:1.6;white-space:pre-wrap;">{{ item.content }}</p>
                  <!-- 回复列表 -->
                  <div v-if="item.replies && item.replies.length" class="reply-list">
                    <div v-for="rep in item.replies" :key="rep.id" class="reply-item">
                      <span class="reply-role">{{ rep.username || '管理员' }}</span>
                      <span class="reply-text">{{ rep.content }}</span>
                      <span class="reply-time">{{ rep.createTime }}</span>
                    </div>
                  </div>
                  <div class="admin-msg-actions">
                    <el-button size="small" @click="openAdminReply(item)" style="font-size:12px;">💬 回复</el-button>
                    <el-button size="small" type="danger" link @click="adminDeleteMessage(item.id)" style="font-size:12px;">删除</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- === 管理员回复弹窗 === -->
        <el-dialog v-model="showAdminReplyDialog" title="💬 回复留言" width="480px" top="25vh" destroy-on-close append-to-body>
          <div style="padding:4px 0;">
            <div style="background:#f5f7fa;border-radius:8px;padding:10px;margin-bottom:12px;font-size:13px;color:#666;">
              <div><strong>{{ adminReplyTarget?.username || '匿名' }}</strong>：</div>
              <div style="margin-top:4px;white-space:pre-wrap;">{{ adminReplyTarget?.content }}</div>
            </div>
            <el-input type="textarea" v-model="adminReplyForm.content" :rows="3" placeholder="输入回复内容…" maxlength="500" show-word-limit/>
            <el-button type="primary" style="width:100%;margin-top:12px;" @click="submitAdminReply" :loading="adminReplySending">发送回复</el-button>
          </div>
        </el-dialog>

        <!-- === 预约规则配置 === -->
        <div v-show="adminPage === 'rules'" class="admin-section">
          <div class="section-header">
            <h2>⚙️ 预约规则配置</h2>
            <el-button type="primary" @click="saveRule" :loading="ruleSaving">保存规则</el-button>
          </div>
          <div class="rule-card">
            <div class="rule-item">
              <label class="rule-label">每人最多同时预约座位数</label>
              <el-input-number v-model="ruleForm.maxPerUser" :min="1" :max="10"/>
            </div>
            <div class="rule-item">
              <label class="rule-label">每次最长使用时间（小时）</label>
              <el-input-number v-model="ruleForm.maxHours" :min="0.5" :max="24" :step="0.5"/>
            </div>
            <div class="rule-item">
              <label class="rule-label">预约后多少分钟内可取消（0=随时可取消）</label>
              <el-input-number v-model="ruleForm.allowCancelMinutes" :min="0" :max="1440"/>
            </div>
            <div class="rule-item">
              <label class="rule-label">签到后自动签退时间（分钟，0=不自动）</label>
              <el-input-number v-model="ruleForm.autoCheckoutMinutes" :min="0" :max="1440"/>
              <div style="font-size:12px;color:#999;margin-top:4px;">签到后超过此时间未签退，系统自动释放座位</div>
            </div>
          </div>
        </div>
        </div>
      </main>
    </div>
  </div>

  <!-- === 公告列表弹窗（学生端查看全部公告，放在最外层确保所有角色可见） === -->
  <el-dialog v-model="showAnnouncementList" title="📢 全部公告" width="600px" top="5vh" destroy-on-close append-to-body>
    <div class="ann-list-modal">
      <div v-if="announcements.length === 0" class="empty-state" style="padding:40px 0;">
        <div class="empty-icon">📢</div>
        <p>暂无公告</p>
      </div>
      <div v-else v-for="(a, idx) in announcements" :key="a.id" class="ann-list-item" :style="{animationDelay: idx * 0.05 + 's'}">
        <div class="ann-list-item-header">
          <span class="ann-list-item-title">{{ a.title }}</span>
          <span v-if="isNewAnn(a.createTime)" class="ann-banner-new">NEW</span>
          <span class="ann-list-item-time">{{ a.createTime }}</span>
        </div>
        <div class="ann-list-item-content" v-html="a.content"></div>
      </div>
    </div>
  </el-dialog>

  <!-- ========== 返回顶部按钮 ========== -->
  <Transition name="scroll-fade">
    <button v-if="scrollBtnVisible" class="scroll-top-btn" @click="scrollToTop" title="返回顶部">
      <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M18 15l-6-6-6 6"/>
      </svg>
    </button>
  </Transition>

  <!-- ========== 庆祝粒子效果（签到/签退时出现） ========== -->
  <Transition name="celebration-fade">
    <div v-if="celebrationVisible" class="celebration-overlay">
      <div class="celebration-particles">
        <div v-for="i in 30" :key="i" class="celeb-particle" :style="{
          '--x': Math.random() * 100 + '%',
          '--y': (Math.random() * 40 + 30) + '%',
          '--d': Math.random() * 1 + 0.5 + 's',
          '--r': (Math.random() * 360) + 'deg',
          '--c': ['#667eea','#764ba2','#f093fb','#f5576c','#4facfe','#43e97b','#fa709a'][Math.floor(Math.random() * 7)]
        }"></div>
      </div>
      <div class="celebration-text">{{ celebrationText }}</div>
    </div>
  </Transition>

</template>

<script setup>
import { ref, onMounted, onUnmounted, reactive, computed, nextTick, watch } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'

// ===== 后端 API 基地址 =====
// 如果后端端口改了，只需改这里
const API_BASE = 'http://localhost:8082'

// ========== 登录状态 ==========
const token = ref(sessionStorage.getItem('userId') || null)       // 当前登录用户的ID（null=未登录）
const userRole = ref(Number(sessionStorage.getItem('userRole') || 0)) // 0=学生, 1=管理员
const loginRole = ref('student') // 'student' | 'admin'
const showRegister = ref(false)
const showResetPwd = ref(false)
const showBindDialog = ref(false)
const loginLoading = ref(false)
const username = ref(sessionStorage.getItem('username') || '用户')

// ========== 暗黑模式 ==========
const darkMode = ref(localStorage.getItem('darkMode') === 'true')
const toggleDark = () => {
  darkMode.value = !darkMode.value
  localStorage.setItem('darkMode', darkMode.value)
  document.documentElement.classList.toggle('dark', darkMode.value)
}
// 初始化暗黑模式
if (darkMode.value) document.documentElement.classList.add('dark')

// ========== 签到庆祝粒子 ==========
const celebrationVisible = ref(false)
const celebrationText = ref('')
const showCelebration = (text) => {
  celebrationText.value = text
  celebrationVisible.value = true
  setTimeout(() => { celebrationVisible.value = false }, 2000)
}

// ========== 登录/注册/重置/绑定表单数据 ==========
// reactive 对象里的字段对应表单的 v-model
const loginForm = reactive({ studentId: sessionStorage.getItem('studentId') || '', password: '' })
const regForm = reactive({ studentId: '', username: '', password: '', email: '', code: '' })
const resetForm = reactive({ studentId: '', email: '', code: '', newPassword: '', confirmPassword: '' })
const bindForm = reactive({ email: '', code: '' })
const resetCooldown = ref(0)   // 倒计时秒数，0=可发送
const resetSending = ref(false)
const regCooldown = ref(0)
const regSending = ref(false)
const resetLoading = ref(false)
const bindCooldown = ref(0)
const bindSending = ref(false)
const bindLoading = ref(false)
const regLoading = ref(false)

// 60秒倒计时工具：调用后按钮禁用，倒计时结束自动恢复
const startCooldown = (cd) => {
  cd.value = 60
  const t = setInterval(() => {
    cd.value--
    if (cd.value <= 0) { cd.value = 0; clearInterval(t) }
  }, 1000)
}

// ========== 学生端状态 ==========
const page = ref('room')              // 当前页面：'room'=自习室列表, 'my'=我的预约
const seatDialog = ref(false)         // 座位选择弹窗开关
const currentRoomId = ref(null)       // 当前选中的自习室ID
const reservingSeat = ref(null)       // 正在预约中的座位ID（用于加载动画）
const roomList = ref([])              // 自习室列表数据
const seatList = ref([])              // 当前自习室的座位列表数据
const myList = ref([])                // 当前学生的预约列表
// 各房间可用座位数缓存 { roomId: count }
const availableCounts = ref({})

// ========== 管理员端状态 ==========
const adminPage = ref('rooms')        // 管理员当前标签页
const adminRoomList = ref([])         // 管理员端自习室列表
const userList = ref([])              // 用户管理列表
const allReserves = ref([])           // 全部预约记录
const reserveSearch = ref('')         // 预约记录搜索关键词
const filteredReserves = computed(() => {
  const kw = reserveSearch.value.trim().toLowerCase()
  if (!kw) return allReserves.value
  return allReserves.value.filter(r =>
    (r.studentId && r.studentId.toLowerCase().includes(kw)) ||
    (r.username && r.username.toLowerCase().includes(kw)) ||
    (r.roomName && r.roomName.toLowerCase().includes(kw))
  )
})
const filterReserves = () => { /* computed handles filtering */ }
const filteredSeats = ref([])         // 按自习室筛选后的座位列表（显示用）

// ========== 学习计时器 ==========
const timerActive = ref(false)
const showTimerDetail = ref(false)
const timerSeconds = ref(0)
let timerInterval = null
const formattedTime = computed(() => {
  const h = Math.floor(timerSeconds.value / 3600)
  const m = Math.floor((timerSeconds.value % 3600) / 60)
  const s = timerSeconds.value % 60
  return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
})
const startTimer = () => {
  timerActive.value = true
  timerSeconds.value = 0
  if (timerInterval) clearInterval(timerInterval)
  timerInterval = setInterval(() => { timerSeconds.value++ }, 1000)
}
const stopTimer = () => {
  timerActive.value = false
  timerSeconds.value = 0
  if (timerInterval) { clearInterval(timerInterval); timerInterval = null }
}

// ========== 自动刷新（学生端轮询） ==========
let autoRefreshTimer = null
const startAutoRefresh = () => {
  if (autoRefreshTimer) clearInterval(autoRefreshTimer)
  autoRefreshTimer = setInterval(() => {
    if (token.value !== null && userRole.value === 0) {
      // 刷新空闲座位数
      roomList.value.forEach(room => {
        axios.get(`${API_BASE}/seat/availableCount/${room.roomId}`).then(r => {
          if (r.data.code === 1) availableCounts.value[room.roomId] = r.data.data
        }).catch(() => {})
      })
      // 刷新当前预约状态
      if (page.value === 'my' || page.value === 'room') {
        axios.get(`${API_BASE}/reserve/myReserves?userId=${token.value}&t=${Date.now()}`).then(r => {
          myList.value = r.data.data || []
        }).catch(() => {})
        // 检测是否有正在进行的预约（恢复计时器）
        const active = (myList.value || []).find(m => m.status === 1)
        if (active && !timerActive.value) startTimer()
        else if (!active && timerActive.value) stopTimer()
      }
    }
  }, 15000)
}

// ========== 返回顶部 ==========
const scrollBtnVisible = ref(false)
let scrollListener = null
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
const onScroll = () => {
  scrollBtnVisible.value = window.scrollY > 300
}

// ========== 峰值时段数据（热力图用） ==========
const peakHourData = ref([])
const loadPeakHourData = async () => {
  try {
    let r = await axios.get(`${API_BASE}/stats/peak-hours`)
    if (r.data.code === 1) peakHourData.value = r.data.data || []
  } catch (e) {
    // 兜底：生成模拟数据
    peakHourData.value = []
    for (let d = 0; d < 7; d++) {
      for (let h = 8; h < 22; h++) {
        const val = Math.floor(Math.random() * 30 + 5)
        peakHourData.value.push([d, h - 8, val])
      }
    }
  }
}
const adminSeatsAll = ref([])         // 所有座位原始数据（缓存，用于筛选）
const seatRoomFilter = ref(null)      // 座位管理中选择的自习室ID
const seatSearch = ref('')            // 座位关键词搜索
const seatBatchInput = ref('')        // 批量添加座位输入
const seatPreview = computed(() => parseSeatInput(seatBatchInput.value))
const roomLoading = ref(false)
const seatLoading = ref(false)
const userLoading = ref(false)
const reserveLoading = ref(false)

const showRoomForm = ref(false)       // 自习室表单弹窗开关
const showSeatForm = ref(false)       // 添加座位弹窗开关
const editingRoom = ref(null)         // 正在编辑的自习室（null=新增模式）
const roomForm = reactive({ roomName: '', totalSeat: 30, openTime: '08:00', closeTime: '22:00' })
const seatForm = reactive({ roomId: null, seatNo: '' })

// ========== 管理员端：座位特征编辑 ==========
const showSeatFeatureDialog = ref(false)
const editingFeatureSeat = ref(null)
const selectedEditFeatures = ref([])
const savingFeatures = ref(false)

const openSeatFeatureEdit = (seat) => {
  editingFeatureSeat.value = seat
  selectedEditFeatures.value = (seat.features || '').split(',').map(x => x.trim()).filter(Boolean)
  showSeatFeatureDialog.value = true
}

const toggleEditFeature = (opt) => {
  const idx = selectedEditFeatures.value.indexOf(opt)
  if (idx >= 0) selectedEditFeatures.value.splice(idx, 1)
  else selectedEditFeatures.value.push(opt)
}

const saveSeatFeatures = async () => {
  savingFeatures.value = true
  try {
    const seat = editingFeatureSeat.value
    const r = await axios.post(`${API_BASE}/seat/updateFeatures`, {
      seatId: seat.seatId,
      features: selectedEditFeatures.value.join(',')
    })
    if (r.data.code === 1) {
      ElMessage.success('特征已更新')
      showSeatFeatureDialog.value = false
      loadAdminSeats()
    } else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('保存失败') }
  finally { savingFeatures.value = false }
}

// ========== 管理员端：代预约状态 ==========
const showAdminReserveDialog = ref(false)
const adminReserveSeat = ref(null)
const adminReserveForm = reactive({ studentId: '' })
const adminReserveLoading = ref(false)

// ========== 公告栏（学生端显示） ==========
const announcements = ref([])
const showAllAnnouncements = ref(false)
const dismissBanner = ref(false)
const currentAnnIndex = ref(0)
const showAnnouncementList = ref(false)

const isNewAnn = (t) => {
  if (!t) return false
  const now = Date.now()
  const time = new Date(t).getTime()
  return (now - time) < 7 * 24 * 60 * 60 * 1000
}

// ========== 历史记录 ==========
const historyList = ref([])

// ========== 分页 ==========
const pageSize = ref(10)
const historyPage = ref(1)
const reservePage = ref(1)
const announcePage = ref(1)
const msgPage = ref(1)

const paginatedHistory = computed(() => {
  const start = (historyPage.value - 1) * pageSize.value
  return historyList.value.slice(start, start + pageSize.value)
})
const paginatedMessages = computed(() => {
  const start = (msgPage.value - 1) * pageSize.value
  return messageList.value.slice(start, start + pageSize.value)
})
const paginatedAdminReserves = computed(() => {
  const p = reservePage.value - 1
  const kw = reserveSearch.value.trim().toLowerCase()
  const filtered = !kw ? allReserves.value
    : allReserves.value.filter(r =>
        (r.studentId && r.studentId.toLowerCase().includes(kw)) ||
        (r.username && r.username.toLowerCase().includes(kw)) ||
        (r.roomName && r.roomName.toLowerCase().includes(kw))
      )
  return filtered.slice(p * pageSize.value, p * pageSize.value + pageSize.value)
})
const paginatedAnnouncements = computed(() => {
  const start = (announcePage.value - 1) * pageSize.value
  return announcementList.value.slice(start, start + pageSize.value)
})

// ========== 留言板 ==========
const messageList = ref([])
const showMsgDialog = ref(false)
const msgSending = ref(false)
const msgForm = reactive({ content: '' })

const loadMessages = async () => {
  page.value = 'message'
  if (token.value === null) return
  try {
    let r = await axios.get(`${API_BASE}/message/list`)
    const msgs = r.data.data || []
    // 为每条留言加载回复
    for (const m of msgs) {
      try {
        let rep = await axios.get(`${API_BASE}/message/replies/${m.id}`)
        m.replies = rep.data.data || []
      } catch { m.replies = [] }
    }
    messageList.value = msgs
    msgPage.value = 1
  } catch (e) { ElMessage.error('获取留言失败') }
}

const submitMessage = async () => {
  if (!msgForm.content.trim()) { ElMessage.warning('请输入留言内容'); return }
  msgSending.value = true
  try {
    let r = await axios.post(`${API_BASE}/message/add`, { userId: Number(token.value), content: msgForm.content.trim() })
    if (r.data.code === 1) { ElMessage.success('💬 留言成功'); showMsgDialog.value = false; loadMessages() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('留言失败') }
  finally { msgSending.value = false }
}

const deleteMessage = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除这条留言？', '确认')
    let r = await axios.post(`${API_BASE}/message/delete/${id}?userId=${token.value}`)
    if (r.data.code === 1) { ElMessage.success('已删除'); loadMessages() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

// ========== 管理员端留言管理 ==========
const adminMessageList = ref([])
const showAdminReplyDialog = ref(false)
const adminReplyTarget = ref(null)
const adminReplyForm = reactive({ content: '' })
const adminReplySending = ref(false)

const loadAdminMessages = async () => {
  adminMessageList.value = []
  try {
    let r = await axios.get(`${API_BASE}/message/list`)
    const msgs = r.data.data || []
    // 为每条留言加载回复
    for (const m of msgs) {
      try {
        let rep = await axios.get(`${API_BASE}/message/replies/${m.id}`)
        m.replies = rep.data.data || []
      } catch { m.replies = [] }
    }
    adminMessageList.value = msgs
  } catch (e) { ElMessage.error('获取留言失败') }
}

const openAdminReply = (msg) => {
  adminReplyTarget.value = msg
  adminReplyForm.content = ''
  showAdminReplyDialog.value = true
}

const submitAdminReply = async () => {
  if (!adminReplyForm.content.trim()) { ElMessage.warning('请输入回复内容'); return }
  adminReplySending.value = true
  try {
    let r = await axios.post(`${API_BASE}/message/add`, {
      userId: Number(token.value),
      content: adminReplyForm.content.trim(),
      replyTo: adminReplyTarget.value.id
    })
    if (r.data.code === 1) {
      ElMessage.success('💬 回复成功')
      showAdminReplyDialog.value = false
      loadAdminMessages() // 重新加载
    } else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('回复失败') }
  finally { adminReplySending.value = false }
}

const adminDeleteMessage = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除此留言及所有回复？', '确认')
    let r = await axios.post(`${API_BASE}/message/admin-delete/${id}`)
    if (r.data.code === 1) { ElMessage.success('已删除'); loadAdminMessages() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

// ========== 座位特征 & 状态筛选 ==========
const seatFeatureOptions = ['window', 'power', 'quiet']
const selectedFeatures = ref([])
const statusFilterOptions = [
  { value: 0, label: '🟢 空闲' },
  { value: 1, label: '🔵 已预约' },
  { value: 2, label: '🔧 损坏' },
  { value: 3, label: '🛠 维护中' },
]
const selectedStatuses = ref([])
const featureLabel = (f) => ({ window:'🪟 靠窗', power:'🔌 有电源', quiet:'🔇 安静区' }[f] || f)
const filteredSeatList = computed(() => {
  let list = seatList.value
  if (selectedFeatures.value.length > 0) {
    list = list.filter(s => {
      const feats = (s.features || '').split(',').map(f => f.trim())
      return selectedFeatures.value.every(f => feats.includes(f))
    })
  }
  if (selectedStatuses.value.length > 0) {
    list = list.filter(s => selectedStatuses.value.includes(s.status))
  }
  return list
})
const toggleFeatureFilter = (opt) => {
  const idx = selectedFeatures.value.indexOf(opt)
  if (idx >= 0) selectedFeatures.value.splice(idx, 1)
  else selectedFeatures.value.push(opt)
}
const toggleStatusFilter = (val) => {
  const idx = selectedStatuses.value.indexOf(val)
  if (idx >= 0) selectedStatuses.value.splice(idx, 1)
  else selectedStatuses.value.push(val)
}

// ========== 管理员端：公告管理状态 ==========
const announcementList = ref([])
const announcementLoading = ref(false)
const showAnnouncementForm = ref(false)
const editingAnnouncement = ref(null)
const announcementForm = reactive({ title: '', content: '', priority: 0, status: 1 })

// ========== 管理员端：仪表盘状态 ==========
const dashboard = reactive({ totalRooms: 0, totalSeats: 0, availableSeats: 0, checkedIn: 0, todayReserves: 0, totalUsers: 0 })
const pieChartRef = ref(null)
const barChartRef = ref(null)
const heatmapChartRef = ref(null)
let pieChartInstance = null
let barChartInstance = null
let heatmapChartInstance = null

// ========== 管理员端：规则配置状态 ==========
const ruleForm = reactive({ ruleId: null, maxPerUser: 1, maxHours: 4, allowCancelMinutes: 30, autoCheckoutMinutes: 0 })
const ruleSaving = ref(false)

// ========== 工具函数 ==========
// 自习室卡片图标（按 roomId 映射）
const roomIcon = (id) => {
  const icons = { 1: '🏠', 2: '🏢', 3: '🏫' }
  return icons[id] || '📚'
}

const roomGradient = (id) => {
  const gradients = {
    1: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    2: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    3: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  }
  return gradients[id] || 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
}

// 获取某房间可用座位数（优先取缓存，没有则用 totalSeat 兜底）
const availableCount = (roomId) => {
  return availableCounts.value[roomId] ?? (roomList.value.find(r => r.roomId === roomId)?.totalSeat ?? 0)
}

// ========== 发送重置密码验证码 ==========
// 学生忘记密码第一步：发送验证码到绑定邮箱
const sendResetCode = async () => {
  if (!resetForm.studentId.trim()) { ElMessage.warning('请输入学号'); return }
  if (!resetForm.email.trim()) { ElMessage.warning('请输入绑定的邮箱'); return }
  resetSending.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/send-code`, { studentId: resetForm.studentId, email: resetForm.email })
    if (res.data.code === 1) { startCooldown(resetCooldown); ElMessage.success('验证码已发送到您的邮箱') }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端') }
  finally { resetSending.value = false }
}

// ========== 执行重置密码 ==========
// 验证验证码后设置新密码
const doResetPwd = async () => {
  if (!resetForm.studentId.trim() || !resetForm.email.trim() || !resetForm.code.trim() || !resetForm.newPassword.trim()) { ElMessage.warning('请填写完整信息'); return }
  if (resetForm.newPassword !== resetForm.confirmPassword) { ElMessage.warning('两次密码输入不一致'); return }
  resetLoading.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/reset-password`, { studentId: resetForm.studentId, email: resetForm.email, code: resetForm.code, newPassword: resetForm.newPassword })
    if (res.data.code === 1) {
      ElMessage.success('密码重置成功，请重新登录')
      showResetPwd.value = false
      Object.assign(resetForm, { studentId: '', email: '', code: '', newPassword: '', confirmPassword: '' })
      resetCooldown.value = 0
    } else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端') }
  finally { resetLoading.value = false }
}

// ========== 发送绑定邮箱验证码 ==========
const sendBindCode = async () => {
  if (!bindForm.email.trim()) { ElMessage.warning('请输入邮箱'); return }
  bindSending.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/send-bind-code`, { email: bindForm.email, userId: token.value })
    if (res.data.code === 1) { startCooldown(bindCooldown); ElMessage.success('验证码已发送') }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端') }
  finally { bindSending.value = false }
}

// ========== 执行邮箱绑定 ==========
const doBindEmail = async () => {
  if (!bindForm.email.trim() || !bindForm.code.trim()) { ElMessage.warning('请填写完整信息'); return }
  bindLoading.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/bind-email`, { email: bindForm.email, code: bindForm.code, userId: token.value })
    if (res.data.code === 1) { ElMessage.success('邮箱绑定成功！'); showBindDialog.value = false; bindForm.email = ''; bindForm.code = ''; bindCooldown.value = 0 }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端') }
  finally { bindLoading.value = false }
}

// ========== 登录 ==========
// 支持学生端 / 管理员端两种登录，登录后根据角色加载首页数据
const login = async () => {
  if (!loginForm.studentId.trim() || !loginForm.password.trim()) { ElMessage.warning('请输入账号和密码'); return }
  loginLoading.value = true
  try {
    const endpoint = loginRole.value === 'admin' ? '/user/admin-login' : '/user/login'
    let res = await axios.post(`${API_BASE}${endpoint}`, loginForm)
    if (res.data.code === 1) {
      let userId = res.data.data.id
      let role = res.data.data.role || 0
      if (userId == null) { ElMessage.error('登录失败：服务器返回数据异常'); return }
      let name = res.data.data.username || loginForm.studentId
      sessionStorage.setItem('userId', userId)
      sessionStorage.setItem('userRole', role)
      sessionStorage.setItem('studentId', loginForm.studentId)
      sessionStorage.setItem('username', name)
      token.value = userId
      userRole.value = role
      username.value = name
      ElMessage.success('登录成功')
      // 根据角色加载对应的首页数据，避免整页刷新
      if (role === 0) {
        getRoom(); getMy()
      } else {
        loadAdminRooms(); adminPage.value = 'rooms'
      }
    } else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端服务') }
  finally { loginLoading.value = false }
}

// ========== 注册相关 ==========
const sendRegisterCode = async () => {
  if (!regForm.email.trim()) { ElMessage.warning('请输入邮箱'); return }
  regSending.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/register-send-code`, { email: regForm.email })
    if (res.data.code === 1) { startCooldown(regCooldown); ElMessage.success('验证码已发送') }
    else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端') }
  finally { regSending.value = false }
}

const register = async () => {
  // 学生注册：学号+姓名+邮箱+验证码+密码 → 调用后端注册接口
  if (!regForm.studentId.trim() || !regForm.username.trim() || !regForm.email.trim() || !regForm.code.trim() || !regForm.password.trim()) { ElMessage.warning('请填写完整信息'); return }
  regLoading.value = true
  try {
    let res = await axios.post(`${API_BASE}/user/register-with-email`, { ...regForm })
    if (res.data.code === 1) {
      ElMessage.success('注册成功，请登录')
      showRegister.value = false
      Object.assign(regForm, { studentId: '', username: '', password: '', email: '', code: '' })
      regCooldown.value = 0
    } else ElMessage.error(res.data.msg)
  } catch (e) { ElMessage.error('网络错误，请检查后端服务') }
  finally { regLoading.value = false }
}

// ========== 退出登录 ==========
// 清除 sessionStorage 中的登录信息，回到登录页
const logout = () => {
  sessionStorage.removeItem('userId')
  sessionStorage.removeItem('userRole')
  sessionStorage.removeItem('studentId')
  token.value = null
  userRole.value = 0
  username.value = ''
  // 切换到登录页，不需要整页刷新
  page.value = 'room'
  ElMessage.success('已退出登录')
}

// ========== 学生端：自习室列表 ==========
const getRoom = async () => {
  page.value = 'room'
  try {
    let r = await axios.get(`${API_BASE}/room/list`)
    roomList.value = r.data.data
  } catch (e) { ElMessage.error('获取自习室列表失败'); return }
  // 单独获取可用座位数，失败静默降级
  try {
    let c = await axios.get(`${API_BASE}/seat/available-counts`)
    let counts = {}
    if (c.data.data) c.data.data.forEach(item => { counts[item.roomId] = item.availableCount })
    availableCounts.value = counts
  } catch (e) { /* 可用座位数获取失败，使用 totalSeat 兜底 */ }
}

// ========== 学生端：打开座位弹窗 ==========
// 点击自习室卡片后触发：检查登录、拉黑、绑定邮箱状态，再获取座位列表
const openSeat = async (roomId) => {
  if (token.value === null) { ElMessage.warning('请登录'); return }
  try {
    let r = await axios.get(`${API_BASE}/user/info/${token.value}`)
    if (r.data.code === 1) {
      // 检查是否被拉黑
      if (r.data.data && r.data.data.status === 1) {
        ElMessage.error('您的账号已被拉黑，无法预约座位')
        setTimeout(() => logout(), 2000)
        return
      }
      if (!r.data.data.email) { showBindDialog.value = true; return }
    }
  } catch (e) {}
  currentRoomId.value = roomId
  try {
    let r = await axios.get(`${API_BASE}/seat/list/${roomId}`)
    seatList.value = (r.data.data || []).sort((a, b) => (a.seatNo || '').localeCompare(b.seatNo || '', undefined, { numeric: true }))
    seatDialog.value = true
  } catch (e) { ElMessage.error('获取座位信息失败') }
}

// ========== 学生端：预约座位 ==========
// 点击座位后：前端先检查座位是否已被占用（本地 seatList），再调后端接口
// 后端返回 code=1 表示成功，否则提示错误信息；若提示"拉黑"则自动退出
const reserve = async (seatId) => {
  if (token.value === null) { ElMessage.warning('请登录'); return }
  if (currentRoomId.value == null || seatId == null) { ElMessage.warning('数据异常，请重试'); return }
  let userId = Number(token.value)
  if (isNaN(userId) || userId <= 0) { ElMessage.warning('登录状态异常，请重新登录'); logout(); return }
  // 检查座位是否已被占用
  let seat = seatList.value.find(s => s.seatId === seatId)
  if (seat && seat.status !== 0) {
    ElMessage.warning('该座位已被占用，请选择其他座位')
    return
  }
  reservingSeat.value = seatId
  try {
    let r = await axios.post(`${API_BASE}/reserve/add`, { userId, roomId: currentRoomId.value, seatId })
    if (r.data.code === 1) { ElMessage.success('🎉 预约成功！'); seatDialog.value = false; getMy() }
    else {
      ElMessage.warning(r.data.msg)
      // 被拉黑：自动退出登录
      if (r.data.msg && r.data.msg.includes('拉黑')) {
        setTimeout(() => logout(), 1500)
      }
    }
  } catch (e) { ElMessage.error('预约失败，请重试') }
  finally { reservingSeat.value = null }
}

// ========== 学生端：获取我的预约列表 ==========
const getMy = async () => {
  if (token.value === null) return
  page.value = 'my'
  try {
    let r = await axios.get(`${API_BASE}/reserve/myList?userId=${token.value}&t=${Date.now()}`)
    myList.value = r.data.data || []
  } catch (e) { ElMessage.error('获取预约列表失败') }
}

// ========== 学生端：取消预约 ==========
const cancelRes = async (resId, seatId) => {
  if (token.value === null) return
  try {
    await axios.post(`${API_BASE}/reserve/cancel?resId=${resId}&userId=${token.value}&seatId=${seatId}`)
    ElMessage.success('已取消预约')
    getMy()
  } catch (e) { ElMessage.error('取消失败') }
}

// ========== 管理员端：自习室管理 ==========
const loadAdminRooms = async () => {
  roomLoading.value = true
  try {
    let r = await axios.get(`${API_BASE}/room/list`)
    adminRoomList.value = (r.data.data || []).map(room => ({
      ...room,
      openTime: room.openTime ? room.openTime.substring(0, 5) : '08:00',
      closeTime: room.closeTime ? room.closeTime.substring(0, 5) : '22:00'
    }))
  } catch (e) { ElMessage.error('获取自习室列表失败') }
  finally { roomLoading.value = false }
}

// 编辑自习室：将数据填入表单弹窗
const editRoom = (room) => {
  editingRoom.value = room
  roomForm.roomName = room.roomName
  roomForm.totalSeat = room.totalSeat
  roomForm.openTime = room.openTime || '08:00'
  roomForm.closeTime = room.closeTime || '22:00'
  showRoomForm.value = true
}

// ========== 保存/新增自习室 ==========
// editingRoom 有值则更新，否则新增
const saveRoom = async () => {
  if (!roomForm.roomName.trim()) { ElMessage.warning('请输入自习室名称'); return }
  try {
    if (editingRoom.value) {
      await axios.post(`${API_BASE}/room/update`, {
        roomId: editingRoom.value.roomId,
        roomName: roomForm.roomName,
        totalSeat: roomForm.totalSeat,
        openTime: roomForm.openTime,
        closeTime: roomForm.closeTime
      })
      ElMessage.success('更新成功')
    } else {
      await axios.post(`${API_BASE}/room/add`, {
        roomName: roomForm.roomName,
        totalSeat: roomForm.totalSeat,
        openTime: roomForm.openTime,
        closeTime: roomForm.closeTime
      })
      ElMessage.success('添加成功')
    }
    showRoomForm.value = false
    editingRoom.value = null
    loadAdminRooms()
  } catch (e) { ElMessage.error('操作失败') }
}

// ========== 删除自习室 ==========
const deleteRoom = async (roomId) => {
  try {
    await ElMessageBox.confirm('确定要删除该自习室吗？', '确认删除')
    await axios.post(`${API_BASE}/room/delete/${roomId}`)
    ElMessage.success('删除成功')
    if (seatRoomFilter.value === roomId) seatRoomFilter.value = null
    loadAdminRooms()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

// ========== 管理员端：座位管理 ==========
const loadAdminSeats = async () => {
  if (adminRoomList.value.length === 0) await loadAdminRooms()
  seatLoading.value = true
  try {
    let r = await axios.get(`${API_BASE}/seat/listAll`)
    adminSeatsAll.value = r.data.data || []
    filterSeats()
  } catch (e) { ElMessage.error('获取座位列表失败') }
  finally { seatLoading.value = false }
}

// 按自习室 + 关键词筛选座位列表（用于座位管理表格展示）
const filterSeats = () => {
  let list = adminSeatsAll.value
  // 按自习室筛选
  if (seatRoomFilter.value) {
    list = list.filter(s => s.roomId === seatRoomFilter.value)
  }
  // 按关键词搜索（座位号 / 自习室名称 / 状态文字）
  const kw = seatSearch.value.trim().toLowerCase()
  if (kw) {
    const statusMap = { '空闲':0, '已预约':1, '损坏':2, '维护中':3 }
    list = list.filter(s => {
      if (s.seatNo.toLowerCase().includes(kw)) return true
      if (s.roomName.toLowerCase().includes(kw)) return true
      // 支持中文状态搜索
      const statusText = Object.entries(statusMap).find(([,v]) => v === s.status)?.[0] || ''
      return statusText.includes(kw)
    })
  }
  filteredSeats.value = list
}

// ========== 批量座位解析 ==========
const parseSeatInput = (input) => {
  const result = []
  if (!input) return result
  // 按换行、逗号分隔
  const tokens = input.split(/[\n,]+/).map(s => s.trim()).filter(Boolean)
  for (const token of tokens) {
    // 尝试解析范围 A01-A20 或 A1-A20
    const rangeMatch = token.match(/^([a-zA-Z]+)(\d+)-([a-zA-Z]*)(\d+)$/)
    if (rangeMatch) {
      const prefix = rangeMatch[1]
      const start = parseInt(rangeMatch[2])
      const end = parseInt(rangeMatch[4])
      const pad = rangeMatch[2].length  // 保持原数字位数
      for (let i = start; i <= end; i++) {
        result.push(prefix + String(i).padStart(pad, '0'))
      }
    } else {
      result.push(token)
    }
  }
  return [...new Set(result)]  // 去重
}

// ========== 管理员端：批量添加座位 ==========
const batchAddSeats = async () => {
  const list = seatPreview.value
  if (list.length === 0) { ElMessage.warning('请输入座位号'); return }
  try {
    await axios.post(`${API_BASE}/seat/batchAdd`, {
      roomId: seatForm.roomId,
      seatNos: list
    })
    ElMessage.success(`批量添加成功，共 ${list.length} 个座位`)
    showSeatForm.value = false
    seatBatchInput.value = ''
    loadAdminSeats()
  } catch (e) { ElMessage.error('批量添加失败，请检查座位号是否重复') }
}

// ========== 管理员端：删除座位 ==========
const deleteSeat = async (seatId) => {
  try {
    await ElMessageBox.confirm('确定要删除该座位吗？', '确认删除')
    await axios.post(`${API_BASE}/seat/delete/${seatId}`)
    ElMessage.success('删除成功')
    loadAdminSeats()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

// ========== 管理员端：切换座位状态（支持多状态：0空闲 1已预约 2损坏 3维护中） ==========
const changeSeatStatus = async (seat) => {
  const statusMap = { 0: '空闲', 1: '已预约', 2: '损坏', 3: '维护中' }
  const text = statusMap[seat.status] || '未知'
  try {
    await ElMessageBox.confirm(`确定将此座位设为「${text}」吗？`, '确认操作')
    const params = new URLSearchParams()
    params.append('seatId', seat.seatId)
    params.append('status', seat.status)
    await axios.post(`${API_BASE}/seat/updateStatus`, params)
    ElMessage.success(`状态已更新为「${text}」`)
    loadAdminSeats()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('状态更新失败')
    } else {
      // 用户取消，刷新让下拉框回退
      loadAdminSeats()
    }
  }
}

// ========== 管理员端：代预约弹窗 ==========
const openAdminReserve = (seat) => {
  adminReserveSeat.value = seat
  adminReserveForm.studentId = ''
  showAdminReserveDialog.value = true
}

// ========== 管理员端：执行代预约 ==========
const doAdminReserve = async () => {
  if (!adminReserveForm.studentId.trim()) { ElMessage.warning('请输入学生学号'); return }
  const seat = adminReserveSeat.value
  if (!seat) { ElMessage.warning('请选择座位'); return }
  adminReserveLoading.value = true
  try {
    const params = new URLSearchParams()
    params.append('seatId', seat.seatId)
    params.append('studentId', adminReserveForm.studentId.trim())
    const r = await axios.post(`${API_BASE}/admin/reserve`, params)
    if (r.data.code === 1) {
      ElMessage.success(r.data.msg)
      showAdminReserveDialog.value = false
      loadAdminSeats()
    } else {
      ElMessage.warning(r.data.msg)
    }
  } catch (e) {
    ElMessage.error('代预约失败：' + (e.response?.data?.msg || e.message))
  } finally {
    adminReserveLoading.value = false
  }
}

// ========== 管理员端：用户管理 ==========
const loadUsers = async () => {
  userLoading.value = true
  try {
    let r = await axios.get(`${API_BASE}/admin/users`)
    userList.value = r.data.data || []
  } catch (e) { ElMessage.error('获取用户列表失败') }
  finally { userLoading.value = false }
}

// ========== 拉黑用户 ==========
const banUser = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要拉黑该用户吗？', '确认操作')
    await axios.post(`${API_BASE}/admin/user/ban/${userId}`)
    ElMessage.success('已拉黑')
    loadUsers()
  } catch (e) { if (e !== 'cancel') ElMessage.error('操作失败') }
}

// ========== 解封用户 ==========
const unbanUser = async (userId) => {
  try {
    await axios.post(`${API_BASE}/admin/user/unban/${userId}`)
    ElMessage.success('已解封')
    loadUsers()
  } catch (e) { ElMessage.error('操作失败') }
}

// ========== 管理员端：预约记录 ==========
const loadAllReserves = async () => {
  reserveLoading.value = true
  try {
    let r = await axios.get(`${API_BASE}/reserve/listAllWithHistory`)
    allReserves.value = r.data.data || []
  } catch (e) { ElMessage.error('获取预约记录失败') }
  finally { reserveLoading.value = false }
}

// ========== 管理员端：更新自习室开放时间 ==========
const updateRoomHours = async (room) => {
  try {
    await axios.post(`${API_BASE}/room/update`, {
      roomId: room.roomId,
      roomName: room.roomName,
      totalSeat: room.totalSeat,
      openTime: room.openTime,
      closeTime: room.closeTime
    })
    ElMessage.success('开放时间已更新')
    loadAdminRooms()
  } catch (e) { ElMessage.error('更新失败') }
}

// ========== 预约状态工具函数 ==========
// 根据 item.status 返回对应的徽章 CSS 类名 (0=已预约, 1=已签到, 2=已完成, 3=已取消)
const statusBadgeClass = (s) => {
  const map = { 0:'status-reserved', 1:'status-checked', 2:'status-done', 3:'status-cancelled' }
  return map[s] || ''
}
const statusText = (s) => {
  const map = { 0:'已预约', 1:'已签到', 2:'已完成', 3:'已取消' }
  return map[s] || '未知'
}

// ========== 签到（带庆祝粒子） ==========
const doCheckin = async (resId) => {
  try {
    let r = await axios.post(`${API_BASE}/reserve/checkin?resId=${resId}&userId=${token.value}`)
    if (r.data.code === 1) { ElMessage.success('✅ 签到成功！'); showCelebration('🎉 签到成功！'); getMy() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('签到失败') }
}

// ========== 签退（带庆祝粒子） ==========
const doCheckout = async (resId, seatId) => {
  try {
    let r = await axios.post(`${API_BASE}/reserve/checkout?resId=${resId}&userId=${token.value}&seatId=${seatId}`)
    if (r.data.code === 1) { ElMessage.success('🚪 签退成功'); showCelebration('📚 辛苦了！下次见'); getMy() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('签退失败') }
}

// ========== 续约 ==========
const doRenew = async (resId) => {
  try {
    let r = await axios.post(`${API_BASE}/reserve/renew?resId=${resId}&userId=${token.value}&hours=1`)
    if (r.data.code === 1) { ElMessage.success('⏱ 续约成功，延长1小时'); getMy() }
    else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('续约失败') }
}

// ========== 历史记录 ==========
const loadHistory = async () => {
  if (token.value === null) return
  page.value = 'history'
  try {
    let r = await axios.get(`${API_BASE}/reserve/myHistory?userId=${token.value}&t=${Date.now()}`)
    // 过滤掉当前有效的预约（status 0和1），只显示已完成/已取消的
    historyList.value = (r.data.data || []).filter(item => item.status === 2 || item.status === 3)
  } catch (e) { ElMessage.error('获取历史记录失败') }
}

// ========== 管理员端：仪表盘 ==========
const loadDashboard = async () => {
  try {
    let r = await axios.get(`${API_BASE}/stats/dashboard`)
    if (r.data.code === 1) {
      Object.assign(dashboard, r.data)
    }
  } catch (e) { /* 静默降级 */ }
  // 不主动渲染图表——watch(adminPage) + nextTick 自动处理，避免重复渲染闪烁
}

// ========== ECharts 仪表盘图表 ==========
const renderCharts = () => {
  // 饼图：座位状态分布
  if (pieChartRef.value) {
    if (pieChartInstance) pieChartInstance.dispose()
    pieChartInstance = echarts.init(pieChartRef.value)
    const used = dashboard.checkedIn || 0
    const free = dashboard.availableSeats || 0
    const other = Math.max(0, (dashboard.totalSeats || 0) - used - free)
    pieChartInstance.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0, textStyle: { fontSize: 12, color: '#999' } },
      series: [{
        type: 'pie', radius: ['42%', '68%'], center: ['50%', '45%'],
        avoidLabelOverlap: false,
        label: { show: true, formatter: '{b}\n{d}%', fontSize: 11, color: '#999' },
        emphasis: { scale: true, label: { show: true } },
        data: [
          { value: free, name: '空闲', itemStyle: { color: '#67c23a' } },
          { value: used, name: '使用中', itemStyle: { color: '#409eff' } },
          { value: other, name: '其他', itemStyle: { color: '#e6a23c' } }
        ]
      }]
    })
    pieChartInstance.resize()
  }
  // 柱状图：各维度对比
  if (barChartRef.value) {
    if (barChartInstance) barChartInstance.dispose()
    barChartInstance = echarts.init(barChartRef.value)
    barChartInstance.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: 40, right: 20, top: 20, bottom: 30 },
      xAxis: {
        type: 'category',
        data: ['自习室', '座位', '今日预约', '当前签到', '用户'],
        axisLabel: { fontSize: 11, color: '#999' }
      },
      yAxis: { type: 'value', minInterval: 1, axisLabel: { color: '#999' } },
      series: [{
        type: 'bar', barWidth: '55%',
        itemStyle: {
          borderRadius: [6,6,0,0],
          color: new echarts.graphic.LinearGradient(0,0,0,1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        },
        data: [
          dashboard.totalRooms || 0,
          dashboard.totalSeats || 0,
          dashboard.todayReserves || 0,
          dashboard.checkedIn || 0,
          dashboard.totalUsers || 0
        ]
      }]
    })
    barChartInstance.resize()
  }
  // 热力图：峰值时段 — 更直观的设计
  if (heatmapChartRef.value) {
    if (heatmapChartInstance) heatmapChartInstance.dispose()
    heatmapChartInstance = echarts.init(heatmapChartRef.value)
    const days = ['周一','周二','周三','周四','周五','周六','周日']
    const hours = []
    for (let h = 8; h < 22; h++) hours.push(h + ':00')
    const data = peakHourData.value.length > 0 ? peakHourData.value
      : (() => { const d=[]; for(let day=0;day<7;day++) for(let h=8;h<22;h++) d.push([day,h-8,Math.floor(Math.random()*3+2)]); return d })()

    // 计算实际数据范围（动态适配）
    const vals = data.map(d => d[2])
    const dataMax = Math.max(...vals, 1)
    const dataMin = Math.min(...vals, 0)
    const range = dataMax - dataMin || 1

    // 计算峰值摘要
    let peakDay = 0, peakHour = 0, peakVal = 0
    for (const d of data) {
      if (d[2] > peakVal) { peakVal = d[2]; peakDay = d[0]; peakHour = d[1] + 8 }
    }
    // 汇总：各时段繁忙概况
    const summaryEl = document.getElementById('heatmapSummary')
    if (summaryEl) {
      const dayNames = ['周一','周二','周三','周四','周五','周六','周日']
      let level = '适中'
      const pct = peakVal / dataMax
      if (pct > 0.85) level = '💥 爆满'
      else if (pct > 0.65) level = '🔴 高峰'
      else if (pct > 0.45) level = '🟠 较忙'
      else if (pct > 0.25) level = '🟡 一般'
      summaryEl.innerHTML = `🏆 最忙时段：<span class="peak-label">${dayNames[peakDay]} ${peakHour}:00</span>（预约 <strong>${peakVal}</strong> 次<span class="peak-level" style="margin-left:6px;font-weight:600;">${level}</span>）`
    }

    // 自定义标签：根据数值显示不同颜色的文字和级别
    const labelFormatter = p => {
      const val = p.data[2]
      const pct = val / dataMax
      if (pct > 0.85) return '💥'
      if (pct > 0.65) return '🔴'
      if (pct > 0.45) return '🟠'
      if (pct > 0.25) return '🟡'
      if (pct > 0.10) return '🟢'
      return '🔵'
    }

    heatmapChartInstance.setOption({
      tooltip: {
        position: 'top',
        formatter: p => {
          const val = p.data[2]
          const pct = val / dataMax
          let lv = '空闲', lvClr = '#2c3e9e'
          if (pct > 0.85) { lv='💥爆满'; lvClr='#a82020' }
          else if (pct > 0.65) { lv='🔴高峰'; lvClr='#e6503a' }
          else if (pct > 0.45) { lv='🟠较忙'; lvClr='#f5a623' }
          else if (pct > 0.25) { lv='🟡适中'; lvClr='#f8e445' }
          else if (pct > 0.10) { lv='🟢较低'; lvClr='#6da5e8' }
          else { lv='🔵空闲'; lvClr='#2c3e9e' }
          return `<b>${days[p.data[0]]} ${p.data[1]+8}:00</b><br/>
                  预约次数：<b>${val}</b><br/>
                  繁忙程度：<span style="color:${lvClr};font-weight:700">${lv}</span>`
        }
      },
      grid: { left: 55, right: 30, top: 10, bottom: 40 },
      xAxis: {
        type: 'category', data: hours,
        axisLabel: { fontSize: 11, color: '#888', fontWeight:500 },
        splitArea: { show: true, areaStyle: { color: ['rgba(0,0,0,0.02)','rgba(0,0,0,0.04)'] } }
      },
      yAxis: {
        type: 'category', data: days,
        axisLabel: { fontSize: 12, color: '#666', fontWeight:500 },
        splitArea: { show: true, areaStyle: { color: ['rgba(0,0,0,0.02)','rgba(0,0,0,0.04)'] } }
      },
      visualMap: {
        min: dataMin, max: dataMax,
        calculable: true,
        orient: 'horizontal',
        left: 'center', bottom: 2,
        text: ['高','低'],
        textStyle: { color: '#999', fontSize: 11 },
        inRange: {
          color: ['#2c3e9e','#3d6bc7','#6da5e8','#a8d8ea','#f8e445','#f5a623','#e6503a','#a82020']
        }
      },
      series: [{
        type: 'heatmap', data: data,
        label: {
          show: true,
          formatter: labelFormatter,
          fontSize: 16
        },
        emphasis: {
          itemStyle: { shadowBlur: 12, shadowColor: 'rgba(0,0,0,0.4)' }
        }
      }]
    })
    heatmapChartInstance.resize()
  }
}

// ========== CSV 导出 ==========
const exportReservesCSV = () => {
  const rows = allReserves.value
  if (!rows.length) { ElMessage.warning('没有数据可导出'); return }
  const header = '学号,姓名,自习室,座位号,预约时间,状态'
  const statusMap = { 0:'已取消',1:'已预约',2:'已完成',3:'已签到' }
  const csvRows = [header]
  rows.forEach(r => {
    const line = [
      r.studentId || '',
      r.username || '',
      r.roomName || '',
      r.seatNo || '',
      r.createTime || '',
      statusMap[r.status] || '未知'
    ].map(v => `"${v}"`).join(',')
    csvRows.push(line)
  })
  const csvStr = '\uFEFF' + csvRows.join('\n')
  const blob = new Blob([csvStr], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `预约记录_${new Date().toLocaleDateString()}.csv`
  link.click()
  URL.revokeObjectURL(link.href)
  ElMessage.success('✅ 导出成功')
}

// 监听 adminPage：切到仪表盘时重绘
watch(adminPage, (val) => {
  if (val === 'dashboard') {
    nextTick(() => renderCharts())
  }
})

// 监听 darkMode：切换时重绘图表配色
watch(darkMode, () => {
  if (adminPage.value === 'dashboard') {
    nextTick(() => renderCharts())
  }
})

// 窗口缩放时重绘
window.addEventListener('resize', () => {
  pieChartInstance?.resize()
  barChartInstance?.resize()
})

// ========== 管理员端：公告管理 ==========
const loadAnnouncements = async () => {
  announcementLoading.value = true
  try {
    let r = await axios.get(`${API_BASE}/announcement/listAll`)
    announcementList.value = r.data.data || []
  } catch (e) { ElMessage.error('获取公告列表失败') }
  finally { announcementLoading.value = false }
}

const editAnnouncement = (item) => {
  editingAnnouncement.value = item
  announcementForm.title = item.title
  announcementForm.content = item.content
  announcementForm.priority = item.priority ?? 0
  announcementForm.status = item.status ?? 1
  showAnnouncementForm.value = true
}

const saveAnnouncement = async () => {
  if (!announcementForm.title.trim()) { ElMessage.warning('请输入公告标题'); return }
  try {
    if (editingAnnouncement.value) {
      await axios.post(`${API_BASE}/announcement/update`, { ...announcementForm, id: editingAnnouncement.value.id })
      ElMessage.success('公告已更新')
    } else {
      await axios.post(`${API_BASE}/announcement/add`, { ...announcementForm })
      ElMessage.success('公告已发布')
    }
    showAnnouncementForm.value = false
    editingAnnouncement.value = null
    loadAnnouncements()
  } catch (e) { ElMessage.error('操作失败') }
}

const deleteAnnouncement = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该公告？', '确认')
    await axios.post(`${API_BASE}/announcement/delete/${id}`)
    ElMessage.success('已删除')
    loadAnnouncements()
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

// ========== 管理员端：预约规则 ==========
const loadRule = async () => {
  try {
    let r = await axios.get(`${API_BASE}/rule/get`)
    if (r.data.code === 1 && r.data.data) {
      const d = r.data.data
      ruleForm.ruleId = d.ruleId
      ruleForm.maxPerUser = d.maxPerUser ?? 1
      ruleForm.maxHours = d.maxHours ?? 4
      ruleForm.allowCancelMinutes = d.allowCancelMinutes ?? 30
      ruleForm.autoCheckoutMinutes = d.autoCheckoutMinutes ?? 0
    }
  } catch (e) { /* 静默 */ }
}

const saveRule = async () => {
  ruleSaving.value = true
  try {
    let r = await axios.post(`${API_BASE}/rule/update`, { ...ruleForm })
    if (r.data.code === 1) ElMessage.success('规则已保存')
    else ElMessage.warning(r.data.msg)
  } catch (e) { ElMessage.error('保存失败') }
  finally { ruleSaving.value = false }
}

// ========== 页面挂载时：根据已保存的登录状态自动加载数据 ==========
// 如果 sessionStorage 有 token，直接取对应角色的首页数据
// 学生端：加载自习室列表 + 我的预约 + 公告，并启动拉黑检测定时器
// 管理员端：加载自习室管理列表
onMounted(async () => {
  if (token.value !== null) {
    // 学生端：加载自习室列表 + 我的预约 + 公告
    if (userRole.value === 0) {
      await getRoom()
      await getMy()
      // 加载公告
      try { let r = await axios.get(`${API_BASE}/announcement/list`); announcements.value = r.data.data || [] } catch (e) {}
      // 公告横幅轮播
      let bannerTimer = setInterval(() => {
        if (announcements.value.length > 1)
          currentAnnIndex.value = (currentAnnIndex.value + 1) % announcements.value.length
      }, 4000)
      // 检测是否有正在进行的预约，启动计时器
      const activeRes = myList.value.find(m => m.status === 1)
      if (activeRes) startTimer()
      // 自动刷新
      startAutoRefresh()
      // 定时检测是否被拉黑（每15秒）
      setInterval(async () => {
        try {
          let r = await axios.get(`${API_BASE}/user/check-status/${token.value}`)
          if (r.data.status === 1) {
            ElMessage.error('您的账号已被拉黑，即将退出登录')
            setTimeout(() => logout(), 2000)
          }
        } catch (e) {}
      }, 15000)
    } else {
      await loadAdminRooms()
      // 管理员：默认加载仪表盘
      adminPage.value = 'dashboard'
      await loadDashboard()
      await loadPeakHourData()
    }
  }
  // 滚动监听
  window.addEventListener('scroll', onScroll)
})

// ===== 打字机效果 =====
onMounted(() => {
  const el = document.querySelector('[data-typewriter]')
  if (!el) return
  const text = el.getAttribute('data-typewriter') || ''
  el.textContent = ''
  let i = 0
  const t = setInterval(() => {
    el.textContent += text[i]
    i++
    if (i >= text.length) clearInterval(t)
  }, 50)
})

// ===== 组件卸载时清理 =====
onUnmounted(() => {
  if (timerInterval) clearInterval(timerInterval)
  if (autoRefreshTimer) clearInterval(autoRefreshTimer)
  window.removeEventListener('scroll', onScroll)
})
</script>

<style>
/* ========== 全局样式 ========== */
* { margin:0; padding:0; box-sizing:border-box; }
html, body, #app { height:100%; font-family:-apple-system, BlinkMacSystemFont,'Segoe UI','PingFang SC','Hiragino Sans GB','Microsoft YaHei',sans-serif; }
.app-container { min-height:100vh; background:#f0f2f5; perspective:1200px; transform-style:preserve-3d; }

/* ========== 暗黑模式 ========== */
/* →→ 基础容器 ←← */
html.dark .app-container { background:#14141f !important; }
html.dark .main-page { background:#14141f !important; min-height:100vh; }
html.dark .main-content { background:transparent; }

/* →→ Topbar ←← */
html.dark .topbar { background:rgba(25,25,45,0.92) !important; backdrop-filter:blur(16px); border-bottom-color:rgba(255,255,255,0.06) !important; }
html.dark .topbar-title,
html.dark .topbar-title,
html.dark .tab-item { color:#ccc !important; }
html.dark .tab-item.active { color:#fff !important; background:#2a2a4a !important; box-shadow:0 2px 8px rgba(0,0,0,0.3) !important; }
html.dark .tab-item:hover { color:#ddd !important; background:rgba(255,255,255,0.05) !important; }
html.dark .tab-list { background:rgba(255,255,255,0.04) !important; }

html.dark .user-name { color:#ccc !important; }
html.dark .logout-btn { color:#aaa !important; border-color:rgba(255,255,255,0.12) !important; }
html.dark .logout-btn:hover { color:#fff !important; background:rgba(255,255,255,0.08) !important; }

/* →→ 管理员 Topbar ←← */
html.dark .admin-topbar-title { background:linear-gradient(135deg,#f093fb,#f5576c) !important; -webkit-background-clip:text !important; -webkit-text-fill-color:transparent !important; background-clip:text !important; }

/* →→ 学生端侧边栏卡片（profile / stats / announcements）←← */
html.dark .side-panel .profile-card,
html.dark .side-panel .quick-stats,
html.dark .side-panel .side-announcements { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .side-ann-header { color:#e0e0f0 !important; border-bottom-color:rgba(255,255,255,0.06) !important; }
html.dark .side-ann-title { color:#e0e0f0 !important; }
html.dark .side-ann-content { color:#999 !important; }
html.dark .side-ann-time { color:#666 !important; }
html.dark .side-ann-item { border-bottom-color:rgba(255,255,255,0.04) !important; }

html.dark .profile-name { color:#e0e0f0 !important; }
html.dark .profile-id { color:#999 !important; }
html.dark .profile-logout { background:rgba(255,255,255,0.06) !important; border-color:transparent !important; }
html.dark .qs-num { color:#e0e0f0 !important; }
html.dark .qs-label { color:#999 !important; }

/* →→ 学生端主体内容区域 ←← */
html.dark .room-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .room-card:hover { box-shadow:0 20px 40px rgba(0,0,0,0.4) !important; }
html.dark .room-name { color:#e0e0f0 !important; }
html.dark .room-time { color:#999 !important; }
html.dark .stat-label { color:#999 !important; }

/* →→ 我的预约/历史/留言 ←← */
html.dark .my-reserves { background:transparent !important; }
html.dark .reserve-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .reserve-card:hover { box-shadow:0 12px 32px rgba(0,0,0,0.4) !important; }
html.dark .reserve-room { color:#e0e0f0 !important; }
html.dark .reserve-detail { color:#999 !important; }
html.dark .empty-state { background:transparent !important; }
html.dark .empty-state p { color:#999 !important; }
html.dark .empty-icon { opacity:0.5 !important; }

/* →→ 留言板 ←← */
html.dark .reserve-card-body p[style*="color:#333"] { color:#ccc !important; }
html.dark .reply-item { background:rgba(255,255,255,0.04) !important; border-radius:8px !important; padding:8px 12px !important; }
html.dark .reply-role { color:#667eea !important; font-weight:600 !important; }
html.dark .reply-text { color:#ccc !important; }
html.dark .reply-time { color:#666 !important; }
html.dark .mini-avatar { background:linear-gradient(135deg,#667eea,#764ba2) !important; color:#fff !important; }

/* →→ 管理员导航 ←← */
html.dark .admin-nav { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .admin-nav-item { color:#999 !important; }
html.dark .admin-nav-item:hover { background:rgba(255,255,255,0.05) !important; color:#ddd !important; }
html.dark .admin-nav-item.active { background:linear-gradient(135deg,#667eea,#764ba2) !important; color:#fff !important; box-shadow:0 4px 12px rgba(102,126,234,0.25) !important; }
html.dark .admin-content { background:transparent !important; }

/* →→ 管理员仪表盘 ←← */
html.dark .dash-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .dash-card:hover { box-shadow:0 16px 40px rgba(0,0,0,0.4) !important; }
html.dark .dash-num { color:#e0e0f0 !important; }
html.dark .dash-label { color:#999 !important; }

/* →→ ECharts 图表 ←← */
html.dark .chart-box { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .chart-section { background:transparent !important; }
html.dark .chart-title { color:#e0e0f0 !important; }

/* →→ 自习室/座位管理表格 ←← */
html.dark .admin-section .el-table { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; }
html.dark .admin-section .el-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; }
html.dark .el-table { --el-table-tr-bg-color:#1e1e36 !important; --el-table-header-bg-color:#252545 !important; --el-table-border-color:rgba(255,255,255,0.06) !important; }
html.dark .el-table__header th { color:#e0e0f0 !important; background:#252545 !important; }
html.dark .el-table__body td { color:#bbb !important; background-color:transparent !important; }
html.dark .el-table__empty-text { color:#666 !important; }
html.dark .el-table__body tr.el-table__row:hover td { background-color:rgba(255,255,255,0.03) !important; }
html.dark .el-table--striped .el-table__body tr.el-table__row--striped td { background-color:rgba(255,255,255,0.02) !important; }

/* →→ 座位弹窗 ←← */
html.dark .seat-dialog .el-dialog__title { color:#e0e0f0 !important; }
html.dark .seat-dialog .el-dialog__body { color:#ccc !important; }
html.dark .seat-grid .seat-item .seat-inner { background:rgba(255,255,255,0.04) !important; border-color:transparent !important; }
html.dark .seat-grid .seat-item.available .seat-inner { background:rgba(103,194,58,0.15) !important; border-color:rgba(103,194,58,0.3) !important; }
html.dark .seat-grid .seat-item.available .seat-no { color:#67c23a !important; }
html.dark .seat-grid .seat-item.reserved .seat-inner { background:rgba(255,255,255,0.06) !important; }
html.dark .seat-grid .seat-item.mine .seat-inner { background:rgba(102,126,234,0.2) !important; border-color:rgba(102,126,234,0.4) !important; }
html.dark .seat-grid .seat-item.damaged .seat-inner { background:rgba(245,108,108,0.15) !important; }
html.dark .seat-grid .seat-item.maintain .seat-inner { background:rgba(230,162,60,0.15) !important; }
html.dark .seat-no { color:#ccc !important; }
html.dark .seat-status { color:#999 !important; }
html.dark .seat-legend { color:#bbb !important; }
html.dark .seat-legend .dot { opacity:0.8 !important; }
html.dark .seat-feature-tag { color:#888 !important; }
html.dark .filter-label { color:#ccc !important; }

/* →→ 开放时间卡片 ←← */
html.dark .hour-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .hour-card:hover { box-shadow:0 8px 24px rgba(0,0,0,0.3) !important; }
html.dark .hour-card-header { color:#e0e0f0 !important; border-bottom-color:rgba(255,255,255,0.06) !important; }
html.dark .hour-card-body .form-label,
html.dark .admin-form .form-label { color:#ccc !important; }
html.dark .hour-card-body .el-time-picker .el-input__wrapper { background:rgba(255,255,255,0.06) !important; box-shadow:0 0 0 1px rgba(255,255,255,0.08) inset !important; }
html.dark .hour-card-body .el-time-picker .el-input__inner,
html.dark .hour-card-body .form-item .el-input__inner { color:#e0e0f0 !important; -webkit-text-fill-color:#e0e0f0 !important; }

/* →→ 预约规则卡片 ←← */
html.dark .rule-card { background:#1e1e36 !important; border-color:rgba(255,255,255,0.06) !important; box-shadow:0 2px 12px rgba(0,0,0,0.2) !important; }
html.dark .rule-label { color:#ccc !important; }

/* →→ 重置密码 & 绑定邮箱 弹窗 ←← */
html.dark .auth-dialog .el-dialog__body { color:#ccc !important; }
html.dark .auth-dialog .form-label { color:#ccc !important; }
html.dark .auth-dialog .form-item .el-input__inner { color:#e0e0f0 !important; }
html.dark .auth-dialog .form-item p[style*="color:#666"] { color:#999 !important; }
html.dark .reset-form { color:#ccc !important; }

/* →→ 管理员表单 ←← */
html.dark .admin-form .el-input__inner { color:#e0e0f0 !important; -webkit-text-fill-color:#e0e0f0 !important; }
html.dark .admin-form .el-input__inner::placeholder { color:#666 !important; }
html.dark .admin-form .el-input-number .el-input__inner { color:#e0e0f0 !important; }
html.dark .admin-form .el-time-picker .el-input__inner { color:#e0e0f0 !important; }

/* →→ Element Plus 组件通用覆盖 ←← */
html.dark .el-input__wrapper { background:rgba(255,255,255,0.06) !important; box-shadow:0 0 0 1px rgba(255,255,255,0.08) inset !important; }
html.dark .el-input__inner { color:#e0e0f0 !important; }
html.dark .el-input__inner::placeholder { color:#666 !important; }
html.dark .el-dialog { background:#1e1e36 !important; border:1px solid rgba(255,255,255,0.08) !important; }
html.dark .el-dialog__title { color:#e0e0f0 !important; }
html.dark .el-dialog__headerbtn .el-dialog__close { color:#888 !important; }
html.dark .el-check-tag { background:rgba(255,255,255,0.06) !important; color:#ccc !important; border-color:transparent !important; }
html.dark .el-check-tag.is-checked { background:#667eea !important; color:#fff !important; }
html.dark .el-pagination button, 
html.dark .el-pagination .el-pager li { background:transparent !important; color:#999 !important; }
html.dark .el-pagination .el-pager li.active { color:#667eea !important; font-weight:700 !important; }
html.dark .el-textarea__inner { background:rgba(255,255,255,0.06) !important; color:#e0e0f0 !important; border-color:rgba(255,255,255,0.08) !important; }
html.dark .el-textarea__inner::placeholder { color:#666 !important; }

/* →→ Section 标题 & 通用文本 ←← */
html.dark .section-header h2 { color:#e0e0f0 !important; }
html.dark .section-header { border-bottom-color:rgba(255,255,255,0.04) !important; }
html.dark h2, html.dark h3 { color:#e0e0f0 !important; }

/* →→ 公告栏 ←← */
html.dark .announcement-bar { background:rgba(102,126,234,0.1) !important; border-color:rgba(102,126,234,0.2) !important; }
html.dark .announcement-title { color:#e0e0f0 !important; }
html.dark .announcement-content { color:#aaa !important; }

/* →→ 背景装饰 ←← */
html.dark .main-bg .bg-blob { opacity:0.15 !important; }

/* →→ 暗黑模式切换按钮 ←← */
html.dark .dark-toggle { border-color:rgba(255,255,255,0.15) !important; background:rgba(255,255,255,0.05) !important; }
html.dark .dark-toggle:hover { background:rgba(255,255,255,0.12) !important; }

/* →→ 庆祝弹层 ←← */
html.dark .celebration-overlay { background:rgba(0,0,0,0.7) !important; }

/* →→ 登录页 Element Plus 输入框 ←← */
html.dark .auth-page .el-input__wrapper { background:rgba(255,255,255,0.08) !important; }

/* →→ 分页 ←← */
html.dark .pagination-wrap { background:transparent !important; }

/* →→ 座位过滤器 ←← */
html.dark .seat-feature-filter { color:#ccc !important; }

/* ========== 暗黑模式切换按钮 ========== */
.dark-toggle { width:36px; height:36px; border-radius:50%; border:1px solid #e0e2e8; background:#fff; cursor:pointer; display:flex; align-items:center; justify-content:center; font-size:16px; transition:all .3s; flex-shrink:0; }
.dark-toggle:hover { background:#f0f2f5; border-color:#667eea; box-shadow:0 0 0 3px rgba(102,126,234,0.15); }

/* ========== 登录页 ========== */
.auth-page { position:relative; min-height:100vh; display:flex; align-items:center; justify-content:center; overflow:hidden; }
.auth-bg { position:absolute; inset:0; background:linear-gradient(135deg,#0c0c1d 0%,#1a1a3e 50%,#0d0d2b 100%); z-index:0; }
.auth-bg::before { content:''; position:absolute; top:-50%; left:-50%; width:200%; height:200%; background:radial-gradient(ellipse at 20% 50%,rgba(102,126,234,0.15) 0%,transparent 50%),radial-gradient(ellipse at 80% 20%,rgba(118,75,162,0.15) 0%,transparent 50%),radial-gradient(ellipse at 40% 80%,rgba(79,172,254,0.1) 0%,transparent 50%); animation:bgFloat 20s ease-in-out infinite alternate; }
@keyframes bgFloat { 0%{transform:translate(0,0)rotate(0deg)} 100%{transform:translate(-30px,-20px)rotate(3deg)} }
.auth-card { position:relative; z-index:1; width:420px; padding:40px; background:rgba(255,255,255,0.05); backdrop-filter:blur(20px); -webkit-backdrop-filter:blur(20px); border-radius:24px; border:1px solid rgba(255,255,255,0.1); box-shadow:0 25px 50px rgba(0,0,0,0.5); animation:cardIn .6s ease-out; transform-style:preserve-3d; }
@keyframes cardIn { from{opacity:0;transform:translateY(30px)scale(.95)} to{opacity:1;transform:translateY(0)scale(1)} }
.auth-header { text-align:center; margin-bottom:30px; }
.auth-logo { color:#667eea; margin-bottom:12px; display:inline-block; perspective:800px; }
.auth-logo svg { animation:logo3dSpin 8s ease-in-out infinite; transform-style:preserve-3d; }
@keyframes logo3dSpin {
  0%,100% { transform:rotateY(0deg) scale(1); filter:drop-shadow(0 0 8px rgba(102,126,234,0.3)); }
  25% { transform:rotateY(10deg) rotateX(5deg) scale(1.05); filter:drop-shadow(0 0 20px rgba(102,126,234,0.6)); }
  50% { transform:rotateY(0deg) rotateX(0deg) scale(1); filter:drop-shadow(0 0 8px rgba(102,126,234,0.3)); }
  75% { transform:rotateY(-10deg) rotateX(-5deg) scale(1.05); filter:drop-shadow(0 0 20px rgba(102,126,234,0.6)); }
}
.auth-title { font-size:28px; font-weight:700; color:#fff; letter-spacing:2px; perspective:600px; transform-style:preserve-3d; display:inline-block; animation:title3d 6s ease-in-out infinite; }
.auth-subtitle { font-size:13px; color:rgba(255,255,255,0.4); margin-top:6px; letter-spacing:1px; }

/* 端选择器 */
.role-tabs { display:flex; gap:8px; justify-content:center; margin-bottom:24px; }
.role-tab { padding:8px 24px; border-radius:12px; cursor:pointer; font-size:14px; font-weight:500; color:rgba(255,255,255,0.5); background:rgba(255,255,255,0.06); border:1px solid rgba(255,255,255,0.08); transition:all .3s; user-select:none; }
.role-tab:hover { color:rgba(255,255,255,0.8); background:rgba(255,255,255,0.1); }
.role-tab.active { color:#fff; background:rgba(102,126,234,0.3); border-color:#667eea; }

.auth-form { transition:opacity .3s; }
.form-item { margin-bottom:16px; }
.form-item .el-input__wrapper { background:rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.12); border-radius:12px; box-shadow:none; transition:all .3s; }
.form-item .el-input__wrapper:hover { border-color:rgba(102,126,234,0.5); background:rgba(255,255,255,0.12); }
.form-item .el-input__wrapper.is-focus { border-color:#667eea; background:rgba(255,255,255,0.15); box-shadow:0 0 0 3px rgba(102,126,234,0.15); }
.form-item .el-input__inner { color:#fff!important; height:48px!important; }
.form-item .el-input__inner::placeholder { color:rgba(255,255,255,0.35); }
.form-item .el-input__prefix { color:rgba(255,255,255,0.4); margin-right:8px; }
.auth-btn { width:100%; height:48px; font-size:16px; font-weight:600; letter-spacing:4px; border-radius:12px; background:linear-gradient(135deg,#667eea 0%,#764ba2 100%); border:none; margin-top:4px; transition:all .3s; }
.auth-btn:hover { transform:translateY(-1px); box-shadow:0 8px 25px rgba(102,126,234,0.4); }
.auth-btn:active { transform:translateY(0); }
.auth-footer { text-align:center; margin-top:20px; }
.auth-tip { color:rgba(255,255,255,0.5); font-size:14px; }
.auth-footer .el-link { font-size:14px; }

/* ========== 弹窗高度限制：内容超长时内部滚动，不超出视口 ========== */
.el-dialog__body { max-height:calc(100vh - 160px); overflow-y:auto; }

/* ========== 座位特征徽标 ========== */
.seat-features { display:flex; gap:2px; justify-content:center; margin-top:2px; }
.seat-feature-tag { font-size:11px; line-height:1; cursor:default; }

/* ========== 主界面 ========== */
.main-page { min-height:100vh; background:#f0f2f5; transform-style:preserve-3d; }
.topbar { background:#fff; box-shadow:0 1px 4px rgba(0,0,0,0.08); position:sticky; top:0; z-index:100; }
.topbar-inner { max-width:1100px; margin:0 auto; display:flex; align-items:center; justify-content:space-between; padding:0 24px; height:64px; }
.topbar-left { flex-shrink:0; }
.topbar-logo { display:flex; align-items:center; gap:10px; color:#667eea; }
.topbar-title { font-size:18px; font-weight:700; color:#1a1a2e; }
.topbar-center { flex:1; display:flex; justify-content:center; }
.tab-list { display:flex; gap:4px; background:#f0f2f5; border-radius:10px; padding:4px; }
.tab-item { padding:8px 20px; border-radius:8px; cursor:pointer; font-size:14px; font-weight:500; color:#666; transition:all .25s; user-select:none; white-space:nowrap; }
.tab-item:hover { color:#333; background:rgba(102,126,234,0.06); }
.tab-item.active { background:#fff; color:#667eea; font-weight:600; box-shadow:0 2px 8px rgba(0,0,0,0.06); }
.topbar-right { display:flex; align-items:center; gap:16px; flex-shrink:0; }
.user-info { display:flex; align-items:center; gap:8px; }
.user-avatar { width:32px; height:32px; border-radius:50%; background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; display:flex; align-items:center; justify-content:center; font-size:12px; font-weight:600; }
.admin-avatar { background:linear-gradient(135deg,#f093fb,#f5576c); }
.user-name { font-size:14px; color:#333; font-weight:500; }
.logout-btn { border-radius:8px; border-color:#e0e0e0; color:#666; font-size:13px; display:flex; align-items:center; gap:4px; padding:8px 14px; }
.logout-btn:hover { color:#f56c6c; border-color:#f56c6c; background:rgba(245,108,108,0.04); }

.main-content { max-width:1100px; margin:0 auto; padding:30px 24px; }

/* ========== 学生端两栏布局 ========== */
.content-layout { display:flex; gap:24px; align-items:flex-start; }
.side-panel { width:260px; flex-shrink:0; position:sticky; top:94px; }
.content-main { flex:1; min-width:0; }

/* 侧边用户卡片 */
.profile-card { background:#fff; border-radius:16px; padding:20px; display:flex; align-items:center; gap:14px; box-shadow:0 2px 12px rgba(0,0,0,0.04); margin-bottom:16px; transition:all .3s; }
.profile-card:hover { box-shadow:0 4px 16px rgba(0,0,0,0.06); }
.profile-avatar { width:48px; height:48px; border-radius:50%; background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; display:flex; align-items:center; justify-content:center; font-size:18px; font-weight:600; flex-shrink:0; }
.profile-info { flex:1; min-width:0; }
.profile-name { font-size:15px; font-weight:600; color:#1a1a2e; }
.profile-id { font-size:12px; color:#999; margin-top:2px; }
.profile-logout { flex-shrink:0; font-size:14px; }

/* 侧边快捷统计 */
.quick-stats { display:grid; grid-template-columns:1fr 1fr 1fr; gap:8px; background:#fff; border-radius:16px; padding:16px; box-shadow:0 2px 12px rgba(0,0,0,0.04); margin-bottom:16px; }
.qs-item { text-align:center; }
.qs-num { display:block; font-size:20px; font-weight:700; color:#1a1a2e; line-height:1.2; }
.qs-label { display:block; font-size:11px; color:#999; margin-top:4px; }

/* 侧边公告 */
.side-announcements { background:#fff; border-radius:16px; padding:16px; box-shadow:0 2px 12px rgba(0,0,0,0.04); }
.side-ann-header { display:flex; justify-content:space-between; align-items:center; cursor:pointer; font-weight:600; font-size:14px; color:#1a1a2e; padding-bottom:10px; border-bottom:1px solid #f0f0f0; }
.side-ann-toggle { font-size:10px; color:#999; }
.side-ann-list { max-height:0; overflow:hidden; transition:max-height .3s ease; }
.side-ann-list.expanded { max-height:600px; }
.side-ann-item { padding:10px 0; border-bottom:1px solid #f5f5f5; cursor:pointer; }
.side-ann-item:last-child { border:none; }
.side-ann-title { font-size:13px; font-weight:500; color:#333; }
.side-ann-content { font-size:12px; color:#666; margin-top:4px; line-height:1.6; }
.side-ann-time { font-size:11px; color:#bbb; margin-top:4px; }
.side-ann-more { font-size:12px; color:#667eea; cursor:pointer; text-align:center; padding-top:8px; }

/* ========== 管理员端侧边导航布局 ========== */
.admin-layout { display:flex; gap:24px; align-items:flex-start; }
.admin-nav { width:180px; flex-shrink:0; background:#fff; border-radius:16px; padding:12px; box-shadow:0 2px 12px rgba(0,0,0,0.04); position:sticky; top:94px; }
.admin-nav-item { display:flex; align-items:center; gap:8px; padding:10px 12px; border-radius:10px; cursor:pointer; font-size:13px; color:#666; transition:all .2s; user-select:none; }
.admin-nav-item:hover { background:#f0f2f5; color:#333; }
.admin-nav-item.active { background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; box-shadow:0 4px 12px rgba(102,126,234,0.25); }
.admin-nav-icon { font-size:16px; width:20px; text-align:center; }
.admin-nav-label { font-weight:500; }
.admin-content { flex:1; min-width:0; }
.admin-topbar { }
.admin-topbar-title { font-size:18px; font-weight:700; color:#1a1a2e; background:linear-gradient(135deg,#f093fb,#f5576c); -webkit-background-clip:text; -webkit-text-fill-color:transparent; background-clip:text; }

/* 响应式：窄屏时隐藏侧边栏 */
@media (max-width:900px) {
  .content-layout, .admin-layout { flex-direction:column; }
  .side-panel, .admin-nav { width:100%; position:static; }
  .admin-nav { display:flex; overflow-x:auto; gap:4px; padding:8px 12px; }
  .admin-nav-item { white-space:nowrap; flex-shrink:0; }
}

/* 自习室卡片网格 */
.room-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(300px,1fr)); gap:20px; perspective:1200px; }
.room-card { position:relative; background:#fff; border-radius:20px; padding:28px; cursor:pointer; display:flex; align-items:center; gap:20px; box-shadow:0 2px 12px rgba(0,0,0,0.04); transition:all .3s; overflow:hidden; animation:bounceIn .5s cubic-bezier(0.4,0,0.2,1) both; transform-style:preserve-3d; }
.room-card:hover { transform:translateY(-4px) rotateX(4deg) rotateY(-3deg) scale(1.02); box-shadow:0 20px 40px rgba(0,0,0,0.12); }
.room-card:active { transform:translateY(-1px) rotateX(2deg) rotateY(-1deg); }
.room-card-bg { position:absolute; left:0; top:0; width:100px; height:100%; opacity:0.1; transition:opacity .3s; }
.room-card:hover .room-card-bg { opacity:0.15; }
.room-card-icon { font-size:42px; line-height:1; flex-shrink:0; position:relative; z-index:1; }
.room-card-body { flex:1; position:relative; z-index:1; }
.room-name { font-size:18px; font-weight:700; color:#1a1a2e; margin-bottom:12px; }
.room-stats { display:flex; align-items:center; gap:16px; }
.stat { text-align:center; }
.stat-num { display:block; font-size:22px; font-weight:700; color:#667eea; }
.stat-label { font-size:12px; color:#999; margin-top:2px; }
.stat-divider { width:1px; height:30px; background:#eee; }
.room-time { font-size:12px; color:#999; margin-top:6px; display:flex; align-items:center; gap:4px; }
.room-card-arrow { color:#ccc; transition:all .3s; position:relative; z-index:1; flex-shrink:0; }
.room-card:hover .room-card-arrow { color:#667eea; transform:translateX(4px); }

/* 座位弹窗 */
.seat-dialog .el-dialog__header { padding:20px 24px 0; text-align:center; }
.seat-dialog .el-dialog__title { font-size:18px; font-weight:700; color:#1a1a2e; }
.seat-dialog .el-dialog__body { padding:20px 24px; }
.seat-dialog .el-dialog__footer { padding:0 24px 20px; }
.seat-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(80px,1fr)); gap:10px; }
.seat-item { cursor:pointer; transition:all .25s; animation:bounceSmall .4s cubic-bezier(0.4,0,0.2,1) both; }
.seat-item .seat-inner { display:flex; flex-direction:column; align-items:center; justify-content:center; padding:12px 8px; border-radius:12px; border:2px solid transparent; background:#f0f2f5; transition:all .25s; transform-style:preserve-3d; }
.seat-item.available { perspective:400px; }
.seat-item.available .seat-inner { background:#e8f5e9; border-color:#c8e6c9; transform:rotateX(0deg) rotateY(0deg) translateZ(0); }
.seat-item.available:hover .seat-inner { background:#c8e6c9; border-color:#66bb6a; transform:rotateX(-6deg) rotateY(4deg) translateZ(10px) scale(1.08); box-shadow:0 8px 20px rgba(102,187,106,0.35); }
.seat-item.mine { perspective:400px; }
.seat-item.mine .seat-inner { background:#fff3e0; border-color:#ffcc80; }
.seat-item.mine:hover .seat-inner { background:#ffe0b2; border-color:#ffa726; transform:rotateX(-6deg) rotateY(4deg) translateZ(10px) scale(1.08); box-shadow:0 8px 20px rgba(255,167,38,0.35); }
.seat-item.reserved .seat-inner { background:#fbe9e7; border-color:#ffccbc; cursor:not-allowed; opacity:0.7; }
.seat-item.damaged .seat-inner { background:#ffebee; border-color:#ef9a9a; cursor:not-allowed; opacity:0.6; }
.seat-item.maintain .seat-inner { background:#f3e5f5; border-color:#ce93d8; cursor:not-allowed; opacity:0.6; }
.seat-item.loading .seat-inner { animation:pulse 1s ease-in-out infinite; }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.5} }
.seat-no { font-size:14px; font-weight:700; color:#333; }
.seat-status { font-size:11px; margin-top:2px; font-weight:500; }
.seat-item.available .seat-status{color:#43a047}
.seat-item.mine .seat-status{color:#ff8f00}
.seat-item.reserved .seat-status{color:#e53935}
.seat-item.damaged .seat-status{color:#c62828}
.seat-item.maintain .seat-status{color:#7b1fa2}
.seat-legend { display:flex; justify-content:center; gap:20px; font-size:13px; color:#666; flex-wrap:wrap; }
.seat-legend span { display:flex; align-items:center; gap:6px; }
.dot { display:inline-block; width:12px; height:12px; border-radius:50%; }
.dot-avail{background:#66bb6a}
.dot-mine{background:#ffa726}
.dot-occ{background:#ef5350}
.dot-damaged{background:#c62828}
.dot-maintain{background:#7b1fa2}

/* 我的预约 */
.my-reserves { animation:bounceSmall .5s cubic-bezier(0.4,0,0.2,1) both; }
.empty-state { text-align:center; padding:80px 20px; background:#fff; border-radius:20px; box-shadow:0 2px 12px rgba(0,0,0,0.04); }
.empty-icon { font-size:60px; margin-bottom:16px; }
.empty-state p { font-size:16px; color:#999; margin-bottom:20px; }
.reserve-cards { display:grid; grid-template-columns:repeat(auto-fill,minmax(320px,1fr)); gap:16px; }
.reserve-card { display:flex; align-items:center; gap:16px; background:#fff; border-radius:16px; padding:20px; box-shadow:0 2px 12px rgba(0,0,0,0.04); transition:all .3s; position:relative; overflow:hidden; }
.reserve-card:hover { box-shadow:0 8px 24px rgba(0,0,0,0.08); transform:translateY(-2px); }
.reserve-card-badge { position:absolute; top:12px; right:-28px; transform:rotate(45deg); background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; font-size:11px; font-weight:600; padding:3px 36px; letter-spacing:1px; }
.reserve-card-body { flex:1; }
.reserve-room { font-size:16px; font-weight:700; color:#1a1a2e; margin-bottom:8px; }
.reserve-details { display:flex; flex-wrap:wrap; gap:12px; }
.reserve-detail { display:inline-flex; align-items:center; gap:4px; font-size:13px; color:#888; }
.reserve-detail svg { flex-shrink:0; }
.reserve-card-action { flex-shrink:0; }

/* ========== 管理员端 ========== */
.section-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
.section-header h2 { font-size:20px; font-weight:700; color:#1a1a2e; }
.admin-form { padding: 10px 0; }
.admin-form .form-label { display:block; font-size:13px; font-weight:600; color:#555; margin-bottom:6px; }
.admin-form .el-input__inner { color:#333!important; }
.admin-form .el-input__inner::placeholder { color:#999; }
.admin-form .el-input-number .el-input__inner { color:#333!important; }
.admin-form .el-time-picker .el-input__inner { color:#333!important; }

/* 开放时间卡片 */
.hour-cards { display:grid; grid-template-columns:repeat(auto-fill,minmax(300px,1fr)); gap:20px; }
.hour-card { background:#fff; border-radius:16px; padding:24px; box-shadow:0 2px 12px rgba(0,0,0,0.04); transition:all .3s; }
.hour-card:hover { box-shadow:0 8px 24px rgba(0,0,0,0.08); }
.hour-card-header { font-size:16px; font-weight:700; color:#1a1a2e; margin-bottom:16px; padding-bottom:12px; border-bottom:1px solid #eee; }
.hour-card-body .form-item { margin-bottom:12px; }
.hour-card-body .form-label { display:block; font-size:13px; font-weight:600; color:#555; margin-bottom:6px; }
.hour-card-body .el-time-picker { --el-input-text-color:#333; --el-text-color-regular:#333; }
.hour-card-body .el-time-picker .el-input__wrapper { background:#fff; box-shadow:0 0 0 1px #dcdfe6 inset; }
.hour-card-body .el-time-picker .el-input__inner,
.hour-card-body .form-item .el-input__inner { color:#333!important; -webkit-text-fill-color:#333; }

/* ========== 重置密码 & 绑定邮箱 ========== */
.reset-form { padding:10px 0; }
.auth-dialog .form-item .el-input__inner { color:#333!important; }
.auth-dialog .form-item .el-input__inner::placeholder { color:#999; }
.auth-dialog .form-label { display:block; font-size:13px; font-weight:600; color:#555; margin-bottom:6px; }

/* ========== Element Plus 覆盖 ========== */
.el-dialog { border-radius:20px!important; }
.el-dialog__headerbtn { top:16px; right:16px; }
.el-dialog__headerbtn .el-dialog__close { font-size:18px; color:#999; }
.el-button--primary { --el-button-bg-color:#667eea; --el-button-border-color:#667eea; --el-button-hover-bg-color:#7b93f5; --el-button-hover-border-color:#7b93f5; --el-button-active-bg-color:#5a6fd6; --el-button-active-border-color:#5a6fd6; }
.el-link.el-link--primary { --el-link-text-color:#667eea; --el-link-hover-text-color:#7b93f5; }
.auth-page .el-input__wrapper { background:rgba(255,255,255,0.08)!important; box-shadow:none!important; }
.auth-page .el-input__wrapper .el-input__clear { color:rgba(255,255,255,0.4); }

/* ============================================================
   活泼动画效果 🎉
   ============================================================ */

/* --- 弹跳入场 --- */
@keyframes bounceIn {
  0% { opacity:0; transform:scale(0.6) translateY(40px); }
  100% { opacity:1; transform:scale(1) translateY(0); }
}
.bounce-in { animation:bounceIn .5s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 上滑入场（轻快，不抖） --- */
@keyframes bounceSmall {
  0% { opacity:0; transform:translateY(18px) scale(0.95); }
  100% { opacity:1; transform:translateY(0) scale(1); }
}
.bounce-small { animation:bounceSmall .4s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 从右侧滑入 --- */
@keyframes slideInRight {
  from { opacity:0; transform:translateX(40px); }
  to   { opacity:1; transform:translateX(0); }
}
.slide-in-right { animation:slideInRight .45s cubic-bezier(0.25,0.46,0.45,0.94) both; }

/* --- 从上滑入 --- */
@keyframes slideDown {
  from { opacity:0; transform:translateY(-100%); }
  to   { opacity:1; transform:translateY(0); }
}
.slide-down { animation:slideDown .4s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 脉冲光晕（预约中/加载）--- */
@keyframes pulseGlow {
  0%,100% { box-shadow:0 0 0 0 rgba(102,126,234,0.4); }
  50%     { box-shadow:0 0 0 12px rgba(102,126,234,0); }
}
.pulse-glow { animation:pulseGlow 1.2s ease-in-out infinite; }

/* --- 弹跳按钮 --- */
@keyframes popPress {
  0% { transform:scale(1); }
  50% { transform:scale(0.92); }
  100% { transform:scale(1); }
}

/* --- 弹窗弹跳打开 --- */
@keyframes dialogBounceIn {
  0% { opacity:0; transform:scale(0.85) translateY(20px); }
  100% { opacity:1; transform:scale(1) translateY(0); }
}

/* --- 表单切换动画 --- */
.form-fade-enter-active { animation:formEnter .35s ease-out; }
.form-fade-leave-active { animation:formLeave .25s ease-in; }
@keyframes formEnter {
  from { opacity:0; transform:translateX(30px) scale(0.96); }
  to   { opacity:1; transform:translateX(0) scale(1); }
}
@keyframes formLeave {
  from { opacity:1; transform:translateX(0) scale(1); }
  to   { opacity:0; transform:translateX(-20px) scale(0.96); }
}

/* --- 通知消息滑入（Element Plus 覆盖） --- */
.el-message { animation:msgIn .35s cubic-bezier(0.4,0,0.2,1) !important; }
@keyframes msgIn {
  from { opacity:0; transform:translateY(-20px) scale(0.9); }
  to   { opacity:1; transform:translateY(0) scale(1); }
}

/* --- 座位弹跳选中 --- */
.seat-item.available:active .seat-inner { animation:popPress .25s ease; }

/* --- 弹窗弹跳入场（通过 Element Plus 的 dialog 覆盖） --- */
.seat-dialog,
.auth-dialog,
.el-dialog:not(.seat-dialog):not(.auth-dialog) {
  animation: none !important; /* 让 Element Plus 自己的动画生效 */
}
.seat-dialog .el-overlay-dialog,
.auth-dialog .el-overlay-dialog { animation:dialogBounceIn .4s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 我的预约卡片入场 --- */
.reserve-card { animation:slideInRight .45s cubic-bezier(0.25,0.46,0.45,0.94) both; }

/* --- 管理员 section 淡入上滑 --- */
.admin-section { animation:bounceSmall .5s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 表格行入场动画 --- */
@keyframes fadeIn { from{opacity:0;transform:translateY(8px)} to{opacity:1;transform:translateY(0)} }
.el-table__body tr { animation:fadeIn .3s ease both; }

/* --- 顶部导航条滑入 --- */
.topbar { animation:slideDown .4s cubic-bezier(0.4,0,0.2,1) both; }

/* --- 角色切换标签动画 --- */
.role-tab.active { animation:tabActive .3s cubic-bezier(0.4,0,0.2,1); }
@keyframes tabActive {
  0%   { transform:scale(1); }
  50%  { transform:scale(1.04); }
  100% { transform:scale(1); }
}

/* --- 验证码按钮弹跳 --- */
.el-button:not(.auth-btn):active:not(:disabled) { animation:popPress .2s ease; }

/* ========== 新功能样式 ========== */

/* 仪表盘卡片网格 —— 3D 入场 + 悬停立体 */
.dashboard-grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(200px,1fr)); gap:16px; margin-bottom:24px; perspective:1200px; }
.dash-card {
  background:#fff; border-radius:16px; padding:24px; display:flex; align-items:center; gap:16px;
  box-shadow:0 2px 12px rgba(0,0,0,0.04); transition:all .4s cubic-bezier(0.4,0,0.2,1);
  animation:fadeUp .5s cubic-bezier(0.4,0,0.2,1) both;
  transform-style:preserve-3d; cursor:default;
  position:relative;
}
.dash-card::after {
  content:''; position:absolute; inset:0; border-radius:16px;
  background:linear-gradient(135deg,rgba(255,255,255,0.4) 0%,transparent 50%);
  pointer-events:none; opacity:0; transition:opacity .4s;
}
.dash-card:hover {
  transform:translateY(-6px) rotateX(4deg) rotateY(-3deg) scale(1.03);
  box-shadow:0 20px 40px rgba(0,0,0,0.15);
}
.dash-card:hover::after { opacity:1; }
.dash-icon { font-size:36px; }
.dash-info { flex:1; }
.dash-num { font-size:28px; font-weight:700; color:#1a1a2e; line-height:1.2; }
.dash-label { font-size:13px; color:#999; margin-top:4px; }
.dash-card-blue .dash-icon { color:#409eff; }
.dash-card-green .dash-icon { color:#67c23a; }
.dash-card-orange .dash-icon { color:#e6a23c; }
.dash-card-purple .dash-icon { color:#b37feb; }
.dash-card-pink .dash-icon { color:#f56c6c; }
.dash-card-teal .dash-icon { color:#00b4d8; }

/* 公告栏 */
.announcement-bar { background:linear-gradient(135deg,#667eea15,#764ba215); border-radius:16px; margin-bottom:20px; overflow:hidden; border:1px solid rgba(102,126,234,0.15); }
.announcement-inner { display:flex; align-items:center; gap:10px; padding:14px 20px; cursor:pointer; transition:background .2s; }
.announcement-inner:hover { background:rgba(102,126,234,0.05); }
.announcement-icon { font-size:18px; }
.announcement-text { flex:1; font-size:14px; font-weight:600; color:#667eea; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
.announcement-more { font-size:12px; color:#999; flex-shrink:0; }
.announcement-list { padding:0 20px 14px; border-top:1px solid rgba(102,126,234,0.1); }
.announcement-item { padding:12px 0; border-bottom:1px solid #f0f0f0; }
.announcement-item:last-child { border-bottom:none; }
.announcement-title { font-size:14px; font-weight:600; color:#333; margin-bottom:4px; }
.announcement-content { font-size:13px; color:#666; line-height:1.6; }
.announcement-time { font-size:11px; color:#bbb; margin-top:6px; }

/* 座位特征筛选 */
.seat-feature-filter { margin-bottom:14px; display:flex; align-items:center; flex-wrap:wrap; gap:4px; }
.filter-label { font-size:13px; color:#666; font-weight:500; margin-right:4px; }

/* 留言管理 - 回复列表 */
.reply-list { margin:8px 0 6px; padding:8px 10px; background:#f8f9fa; border-radius:8px; }
.reply-item { padding:4px 0; font-size:13px; border-bottom:1px solid #eee; }
.reply-item:last-child { border-bottom:none; }
.reply-role { color:#409eff; font-weight:500; margin-right:6px; }
.reply-text { color:#333; }
.reply-time { float:right; color:#bbb; font-size:11px; }
.admin-msg-actions { display:flex; gap:8px; margin-top:6px; }

/* 预约卡片状态徽章颜色 */
.status-reserved { background:linear-gradient(135deg,#667eea,#764ba2) !important; }
.status-checked { background:linear-gradient(135deg,#67c23a,#85ce61) !important; }
.status-done { background:linear-gradient(135deg,#909399,#bbb) !important; }
.status-cancelled { background:linear-gradient(135deg,#f56c6c,#f89898) !important; }
.history-badge { background:linear-gradient(135deg,#909399,#bbb) !important; }

/* 规则配置卡片 */
.rule-card { background:#fff; border-radius:16px; padding:24px; box-shadow:0 2px 12px rgba(0,0,0,0.04); }
.rule-item { margin-bottom:20px; }
.rule-item:last-child { margin-bottom:0; }
.rule-label { display:block; font-size:14px; font-weight:600; color:#555; margin-bottom:8px; }

/* ========== ECharts 图表样式 ========== */
.chart-section { display:grid; grid-template-columns:1fr 1fr; gap:16px; margin-bottom:20px; }
.chart-box { background:#fff; border-radius:16px; padding:20px; box-shadow:0 2px 12px rgba(0,0,0,0.04); animation:fadeIn .3s ease both; transform-style:preserve-3d; transition:all 0.4s cubic-bezier(0.34,1.56,0.64,1); }
.chart-box:hover { transform:translateY(-4px) rotateX(3deg) scale(1.02); box-shadow:0 16px 40px rgba(0,0,0,0.12); }
.chart-title { font-size:15px; font-weight:600; color:#1a1a2e; margin-bottom:12px; }
.chart-container { width:100%; }
@media (max-width:768px) { .chart-section { grid-template-columns:1fr; } }

/* ========== 庆祝粒子效果（签到/签退） ========== */
.celebration-overlay { position:fixed; inset:0; z-index:9999; pointer-events:none; display:flex; align-items:center; justify-content:center; background:rgba(0,0,0,0.35); backdrop-filter:blur(2px); }
.celebration-particles { position:absolute; inset:0; pointer-events:none; }
.celeb-particle { position:absolute; width:10px; height:10px; border-radius:50%; background:var(--c); left:var(--x); top:var(--y); animation:celebPop 1.2s var(--d) ease-out both; opacity:0; }
@keyframes celebPop {
  0% { opacity:1; transform:translateY(0) scale(0) rotate(0deg); }
  30% { opacity:1; transform:translateY(-80px) scale(1.5) rotate(180deg); }
  100% { opacity:0; transform:translateY(-200px) scale(0.3) rotate(360deg); }
}
.celebration-text { position:relative; z-index:1; font-size:48px; font-weight:800; color:#fff; text-shadow:0 4px 20px rgba(102,126,234,0.6), 0 0 60px rgba(102,126,234,0.3); animation:celebTextBounce .5s cubic-bezier(0.4,0,0.2,1); text-align:center; }
@keyframes celebTextBounce {
  0% { opacity:0; transform:scale(0.3) rotate(-10deg); }
  50% { transform:scale(1.2) rotate(3deg); }
  100% { opacity:1; transform:scale(1) rotate(0deg); }
}
.celebration-fade-enter-active { transition:opacity .2s; }
.celebration-fade-leave-active { transition:opacity .5s; }
.celebration-fade-enter-from, .celebration-fade-leave-to { opacity:0; }

/* ============================================================
   3D + 视觉特效
   ============================================================ */

/* --- 3D 粒子背景（登录页） --- */
.auth-particles { position:absolute; inset:0; overflow:hidden; perspective:800px; pointer-events:none; }
.particle { position:absolute; border-radius:50%; opacity:0; animation:particleFloat 12s ease-in-out infinite; transform-style:preserve-3d; }
.p1 { width:60px; height:60px; background:radial-gradient(circle,rgba(102,126,234,0.4),transparent); left:10%; top:30%; animation-delay:0s; }
.p2 { width:40px; height:40px; background:radial-gradient(circle,rgba(118,75,162,0.3),transparent); left:70%; top:20%; animation-delay:2s; }
.p3 { width:80px; height:80px; background:radial-gradient(circle,rgba(79,172,254,0.2),transparent); left:50%; top:60%; animation-delay:4s; }
.p4 { width:30px; height:30px; background:radial-gradient(circle,rgba(102,126,234,0.5),transparent); left:20%; top:70%; animation-delay:6s; }
.p5 { width:50px; height:50px; background:radial-gradient(circle,rgba(118,75,162,0.3),transparent); left:85%; top:50%; animation-delay:8s; }
.p6 { width:70px; height:70px; background:radial-gradient(circle,rgba(79,172,254,0.15),transparent); left:30%; top:10%; animation-delay:1s; }
.p7 { width:35px; height:35px; background:radial-gradient(circle,rgba(102,126,234,0.4),transparent); left:60%; top:80%; animation-delay:3s; }
.p8 { width:90px; height:90px; background:radial-gradient(circle,rgba(118,75,162,0.15),transparent); left:40%; top:40%; animation-delay:5s; }
@keyframes particleFloat {
  0% { opacity:0; transform:translateZ(-200px) translateY(0) scale(0.5); }
  10% { opacity:0.6; }
  50% { opacity:0.8; transform:translateZ(50px) translateY(-40px) scale(1.2); }
  90% { opacity:0.4; }
  100% { opacity:0; transform:translateZ(-100px) translateY(20px) scale(0.6); }
}

/* --- 星空背景（满天星闪烁） --- */
.starfield { position:absolute; inset:0; overflow:hidden; pointer-events:none; }
.starfield::before { content:''; position:absolute; width:200%; height:200%; top:-50%; left:-50%;
  background-image:
    radial-gradient(1px 1px at 10% 20%, rgba(255,255,255,0.8), transparent),
    radial-gradient(1px 1px at 20% 50%, rgba(255,255,255,0.6), transparent),
    radial-gradient(1.5px 1.5px at 30% 10%, rgba(255,255,255,0.9), transparent),
    radial-gradient(1px 1px at 40% 70%, rgba(255,255,255,0.5), transparent),
    radial-gradient(1px 1px at 50% 30%, rgba(255,255,255,0.7), transparent),
    radial-gradient(1.5px 1.5px at 60% 80%, rgba(255,255,255,0.8), transparent),
    radial-gradient(1px 1px at 70% 40%, rgba(255,255,255,0.6), transparent),
    radial-gradient(1px 1px at 80% 90%, rgba(255,255,255,0.5), transparent),
    radial-gradient(1.5px 1.5px at 15% 65%, rgba(255,255,255,0.7), transparent),
    radial-gradient(1px 1px at 25% 85%, rgba(255,255,255,0.9), transparent),
    radial-gradient(1px 1px at 35% 15%, rgba(255,255,255,0.5), transparent),
    radial-gradient(1.5px 1.5px at 45% 45%, rgba(255,255,255,0.8), transparent),
    radial-gradient(1px 1px at 55% 5%, rgba(255,255,255,0.6), transparent),
    radial-gradient(1px 1px at 65% 75%, rgba(255,255,255,0.7), transparent),
    radial-gradient(1.5px 1.5px at 75% 55%, rgba(255,255,255,0.9), transparent),
    radial-gradient(1px 1px at 85% 25%, rgba(255,255,255,0.5), transparent),
    radial-gradient(1px 1px at 5% 40%, rgba(255,255,255,0.6), transparent),
    radial-gradient(1.5px 1.5px at 95% 60%, rgba(255,255,255,0.8), transparent);
  animation:starTwinkle 4s ease-in-out infinite alternate;
}
@keyframes starTwinkle {
  0% { opacity:0.3; transform:scale(1) rotate(0deg); }
  50% { opacity:1; transform:scale(1.05) rotate(1deg); }
  100% { opacity:0.4; transform:scale(0.95) rotate(-1deg); }
}

/* --- 3D 几何体装饰（登录背景） --- */
.geo-shapes { position:absolute; inset:0; overflow:hidden; perspective:1000px; pointer-events:none; }

/* 3D 旋转立方体 */
.geo-cube { position:absolute; width:60px; height:60px; top:25%; left:8%; transform-style:preserve-3d; animation:cubeOrbit 20s linear infinite; }
.cube-face { position:absolute; width:60px; height:60px; border:2px solid rgba(102,126,234,0.3); background:rgba(102,126,234,0.05); box-shadow:inset 0 0 20px rgba(102,126,234,0.05); }
.cube-face.front { transform:translateZ(30px); }
.cube-face.back { transform:rotateY(180deg) translateZ(30px); }
.cube-face.right { transform:rotateY(90deg) translateZ(30px); }
.cube-face.left { transform:rotateY(-90deg) translateZ(30px); }
.cube-face.top { transform:rotateX(90deg) translateZ(30px); }
.cube-face.bottom { transform:rotateX(-90deg) translateZ(30px); }
@keyframes cubeOrbit {
  0% { transform:rotateX(0deg) rotateY(0deg) translateX(0) translateY(0); }
  25% { transform:rotateX(90deg) rotateY(90deg) translateX(20px) translateY(-15px); }
  50% { transform:rotateX(180deg) rotateY(180deg) translateX(0) translateY(0); }
  75% { transform:rotateX(270deg) rotateY(270deg) translateX(-20px) translateY(15px); }
  100% { transform:rotateX(360deg) rotateY(360deg) translateX(0) translateY(0); }
}

/* 3D 旋转环（甜甜圈结构） */
.geo-ring { position:absolute; width:80px; height:80px; top:60%; right:12%; transform-style:preserve-3d; animation:ringSpin 15s linear infinite; }
.ring-segment { position:absolute; width:8px; height:8px; background:rgba(118,75,162,0.3); border-radius:50%; border:1px solid rgba(118,75,162,0.4); left:50%; top:50%; margin:-4px 0 0 -4px; transform-style:preserve-3d; }
@keyframes ringSpin {
  0% { transform:rotateX(60deg) rotateY(0deg) rotateZ(0deg); }
  50% { transform:rotateX(60deg) rotateY(180deg) rotateZ(10deg); }
  100% { transform:rotateX(60deg) rotateY(360deg) rotateZ(0deg); }
}

/* ==================================================
   ========== 新增强化功能样式 ==========
   ================================================== */

/* --- 学习计时器 --- */
.study-timer {
  display:flex; align-items:center; gap:8px; background:linear-gradient(135deg,rgba(102,126,234,0.12),rgba(118,75,162,0.12));
  padding:4px 14px 4px 10px; border-radius:24px; cursor:pointer; margin-right:12px; white-space:nowrap;
  border:1px solid rgba(102,126,234,0.2); transition:all 0.3s;
}
.study-timer:hover { background:linear-gradient(135deg,rgba(102,126,234,0.2),rgba(118,75,162,0.2)); transform:scale(1.04); }
.timer-icon { font-size:20px; line-height:1; }
.timer-body { display:flex; flex-direction:column; line-height:1.2; }
.timer-label { font-size:10px; color:#999; }
.timer-display { font-size:16px; font-weight:700; font-variant-numeric:tabular-nums; background:linear-gradient(135deg,#667eea,#764ba2); -webkit-background-clip:text; -webkit-text-fill-color:transparent; background-clip:text; letter-spacing:1px; }
html.dark .study-timer { background:rgba(102,126,234,0.08); border-color:rgba(102,126,234,0.15); }
html.dark .timer-label { color:#888; }

/* --- 返回顶部按钮 --- */
.scroll-top-btn {
  position:fixed; bottom:36px; right:28px; z-index:999; width:44px; height:44px;
  border-radius:50%; border:none; cursor:pointer; display:flex; align-items:center; justify-content:center; color:#fff;
  background:linear-gradient(135deg,#667eea,#764ba2); box-shadow:0 4px 16px rgba(102,126,234,0.4);
  transition:all 0.3s cubic-bezier(0.34,1.56,0.64,1); opacity:0.85;
}
.scroll-top-btn:hover { transform:translateY(-4px) scale(1.08); opacity:1; box-shadow:0 8px 28px rgba(102,126,234,0.5); }
.scroll-top-btn:active { transform:scale(0.92); }
.scroll-fade-enter-active { animation:popIn 0.35s cubic-bezier(0.34,1.56,0.64,1); }
.scroll-fade-leave-active { animation:popIn 0.25s reverse; }
@keyframes popIn {
  0% { opacity:0; transform:scale(0.4) translateY(12px); }
  100% { opacity:0.85; transform:scale(1) translateY(0); }
}

/* --- 快捷操作面板 --- */
.quick-actions {
  margin-top:24px; background:rgba(255,255,255,0.6); backdrop-filter:blur(12px); border-radius:16px;
  padding:20px 24px; border:1px solid rgba(255,255,255,0.7); box-shadow:0 2px 12px rgba(0,0,0,0.04);
}
.qa-title { font-size:16px; font-weight:600; color:#444; margin-bottom:14px; display:flex; align-items:center; gap:6px; }
.qa-grid { display:flex; gap:12px; flex-wrap:wrap; }
.qa-item {
  display:flex; align-items:center; gap:8px; padding:10px 18px; border-radius:12px;
  background:rgba(102,126,234,0.08); cursor:pointer; transition:all 0.3s cubic-bezier(0.34,1.56,0.64,1);
  border:1px solid rgba(102,126,234,0.1); user-select:none;
}
.qa-item:hover { background:linear-gradient(135deg,rgba(102,126,234,0.18),rgba(118,75,162,0.12)); transform:translateY(-3px) scale(1.04); box-shadow:0 8px 20px rgba(102,126,234,0.2); }
.qa-icon { font-size:20px; line-height:1; }
.qa-label { font-size:13px; font-weight:500; color:#555; }
html.dark .quick-actions { background:rgba(30,30,54,0.7); border-color:rgba(255,255,255,0.06); }
html.dark .qa-title { color:#ccc; }
html.dark .qa-item { background:rgba(102,126,234,0.08); border-color:rgba(255,255,255,0.08); }
html.dark .qa-item:hover { background:rgba(102,126,234,0.18); }
html.dark .qa-label { color:#bbb; }

/* --- 热力图区域 --- */
.heatmap-section {
  margin-top:24px; background:rgba(255,255,255,0.6); backdrop-filter:blur(12px); border-radius:16px;
  padding:20px; border:1px solid rgba(255,255,255,0.7); box-shadow:0 2px 12px rgba(0,0,0,0.04);
}
html.dark .heatmap-section { background:rgba(30,30,54,0.7); border-color:rgba(255,255,255,0.06); }
html.dark .heatmap-section .chart-title { color:#e0e0f0; }
html.dark .heatmap-summary { color:#bbb; }
html.dark .heatmap-summary .peak-label { color:#f0c040; }
html.dark .hml-item { color:#aaa; }

/* --- 热力图头部 --- */
.heatmap-header { display:flex; align-items:center; justify-content:space-between; flex-wrap:wrap; gap:8px; margin-bottom:12px; }
.heatmap-header .chart-title { margin:0; }
.heatmap-summary { font-size:13px; color:#666; background:rgba(102,126,234,0.08); padding:6px 14px; border-radius:20px; white-space:nowrap; }
.heatmap-summary .peak-label { color:#667eea; font-weight:700; }
/* --- 图例样式 --- */
.heatmap-legend { display:flex; flex-wrap:wrap; gap:8px 14px; margin-bottom:12px; justify-content:center; }
.hml-item { display:inline-flex; align-items:center; gap:4px; font-size:12px; color:#666; }
.hml-dot { display:inline-block; width:12px; height:12px; border-radius:3px; }

/* --- 骨架屏动画 --- */
@keyframes skeletonShimmer {
  0% { background-position:-200px 0; }
  100% { background-position:calc(200px + 100%) 0; }
}
.skeleton {
  background:#eee; border-radius:8px; background:linear-gradient(90deg,#eee 25%,#f5f5f5 50%,#eee 75%);
  background-size:200px 100%; animation:skeletonShimmer 1.8s ease-in-out infinite;
}
.skeleton-card {
  height:120px; border-radius:16px; margin-bottom:12px;
  background:linear-gradient(90deg,#eee 25%,#f5f5f5 37%,#eee 63%);
  background-size:400px 100%; animation:skeletonShimmer 1.8s ease-in-out infinite;
}
.skeleton-text { height:14px; width:60%; margin:8px 0; border-radius:6px; }
.skeleton-text.short { width:40%; }
html.dark .skeleton,
html.dark .skeleton-card,
html.dark .skeleton-text { background:linear-gradient(90deg,#2a2a4a 25%,#33335a 50%,#2a2a4a 75%); background-size:400px 100%; }

/* --- 页面切换动画 --- */
.page-transition-enter-active { animation:pageFadeIn 0.35s cubic-bezier(0.34,1.56,0.64,1); }
.page-transition-leave-active { animation:pageFadeIn 0.2s reverse; }
@keyframes pageFadeIn {
  0% { opacity:0; transform:translateY(12px) scale(0.97); }
  100% { opacity:1; transform:translateY(0) scale(1); }
}
/* 应用到内容区域 */
.admin-content > .admin-section,
.content-main > .room-page,
.content-main > .reserve-cards,
.content-main > .message-board { animation:pageFadeIn 0.35s ease-out; }

/* 3D 旋转三角锥 */
.geo-triangle { position:absolute; width:50px; height:50px; top:40%; left:75%; transform-style:preserve-3d; animation:triSpin 25s ease-in-out infinite; }
.tri-face { position:absolute; width:0; height:0; border-left:25px solid transparent; border-right:25px solid transparent; border-bottom:43px solid rgba(79,172,254,0.12); transform-origin:50% 100%; }
.tri-face:nth-child(1) { transform:rotateY(0deg) translateZ(20px); }
.tri-face:nth-child(2) { transform:rotateY(120deg) translateZ(20px); }
.tri-face:nth-child(3) { transform:rotateY(240deg) translateZ(20px); }
@keyframes triSpin {
  0% { transform:rotateX(20deg) rotateY(0deg) translateY(0); }
  33% { transform:rotateX(25deg) rotateY(120deg) translateY(-20px); }
  66% { transform:rotateX(15deg) rotateY(240deg) translateY(10px); }
  100% { transform:rotateX(20deg) rotateY(360deg) translateY(0); }
}

/* 发光球体环绕 */
.geo-orb { position:absolute; width:40px; height:40px; top:70%; left:20%; border-radius:50%; background:radial-gradient(circle at 30% 30%, rgba(102,126,234,0.4), rgba(102,126,234,0.1) 60%, transparent); box-shadow:0 0 30px rgba(102,126,234,0.2), 0 0 60px rgba(102,126,234,0.1); animation:orbPulse 4s ease-in-out infinite, orbDrift 12s ease-in-out infinite; }
@keyframes orbPulse {
  0%,100% { transform:scale(1); opacity:0.6; }
  50% { transform:scale(1.4); opacity:1; }
}
@keyframes orbDrift {
  0% { transform:translateX(0) translateY(0); }
  25% { transform:translateX(30px) translateY(-20px); }
  50% { transform:translateX(-10px) translateY(30px); }
  75% { transform:translateX(40px) translateY(10px); }
  100% { transform:translateX(0) translateY(0); }
}

/* --- 登录卡片旋转渐变边框 --- */
.auth-card::before { content:''; position:absolute; inset:-2px; border-radius:26px; background:conic-gradient(from var(--angle,0deg), #667eea, #764ba2, #4facfe, #667eea); z-index:-1; animation:borderSpin 4s linear infinite; }
.auth-card::after { content:''; position:absolute; inset:-2px; border-radius:26px; background:conic-gradient(from var(--angle,0deg), #667eea, #764ba2, #4facfe, #667eea); z-index:-2; animation:borderSpin 4s linear infinite; filter:blur(12px); opacity:0.5; }
@property --angle { syntax:'<angle>'; initial-value:0deg; inherits:false; }
@keyframes borderSpin { to { --angle:360deg; } }

/* 修复 auth-card 原有 border 不冲突 */
.auth-card { border-color:transparent !important; }

/* --- 分段表单进场动画（stagger） --- */
.stagger { animation:staggerIn 0.5s ease-out both; animation-delay:calc(var(--i,0) * 0.12s); }
@keyframes staggerIn {
  from { opacity:0; transform:translateY(20px) scale(0.95); }
  to { opacity:1; transform:translateY(0) scale(1); }
}

/* --- 打字机光标闪烁 --- */
.auth-subtitle::after {
  content:'|'; animation:cursorBlink 0.8s step-end infinite; color:rgba(255,255,255,0.5);
}
@keyframes cursorBlink { 50% { opacity:0; } }

/* --- 3D Topbar --- */
.topbar { transform-style:preserve-3d; }
.topbar:hover { transform:translateZ(5px); box-shadow:0 4px 12px rgba(0,0,0,0.08); }

/* --- 3D 自习室卡片 --- */
.room-card { transform-style:preserve-3d; transition:all 0.4s cubic-bezier(0.34,1.56,0.64,1); animation:fadeInUp 0.5s ease-out both; }
@keyframes fadeInUp { from{opacity:0;transform:translateY(30px) scale(0.9)} to{opacity:1;transform:translateY(0) scale(1)} }
.room-card:hover { transform:translateY(-6px) rotateX(6deg) rotateY(-4deg) translateZ(10px); box-shadow:0 20px 50px rgba(0,0,0,0.15); }

/* 自习室卡片发光边框悬停 */
.room-card::before { content:''; position:absolute; inset:-1px; border-radius:18px; background:linear-gradient(135deg, transparent, rgba(255,255,255,0), transparent); z-index:0; opacity:0; transition:opacity 0.4s; }
.room-card:hover::before { opacity:1; background:linear-gradient(135deg, #667eea20, transparent 40%, #764ba220); }

/* --- 3D 座位卡片 --- */
.seat-item { transform-style:preserve-3d; transition:all 0.3s cubic-bezier(0.34,1.56,0.64,1); animation:seatPop 0.35s ease-out both; transform:translateZ(0); }
@keyframes seatPop { from{opacity:0;transform:scale(0.7) rotateY(20deg);} to{opacity:1;transform:scale(1) rotateY(0deg);} }
.seat-item.available:hover { transform:translateY(-4px) rotateX(-6deg) rotateY(4deg) translateZ(10px); box-shadow:0 10px 30px rgba(102,126,234,0.3); }
.seat-item.mine:hover { transform:translateY(-4px) rotateX(-6deg) rotateY(4deg) translateZ(10px); }
.seat-item:not(.available):not(.mine):hover { transform:translateY(-2px); }
/* 空闲座位呼吸发光 */
.seat-item.available .seat-inner { animation:seatGlow 2.5s ease-in-out infinite; }
@keyframes seatGlow {
  0%,100% { box-shadow:0 0 4px rgba(102,126,234,0.1); }
  50% { box-shadow:0 0 16px rgba(102,126,234,0.25), 0 0 32px rgba(102,126,234,0.08); }
}

/* 空闲座位动态波点背景 */
.seat-item.available .seat-status::after { content:''; display:inline-block; width:6px; height:6px; background:#67c23a; border-radius:50%; margin-left:2px; animation:dotPulse 1.2s ease-in-out infinite; vertical-align:middle; }
@keyframes dotPulse { 0%,100%{transform:scale(1);opacity:0.8;} 50%{transform:scale(1.5);opacity:1;} }

/* --- 预约卡片3D --- */
.reserve-card { transform-style:preserve-3d; transition:all 0.4s cubic-bezier(0.34,1.56,0.64,1); animation:cardSlideIn 0.5s ease-out both; }
@keyframes cardSlideIn { from{opacity:0;transform:translateX(-20px) rotateY(10deg);} to{opacity:1;transform:translateX(0) rotateY(0deg);} }
.reserve-card:hover { transform:translateY(-3px) rotateX(3deg) rotateY(-2deg) scale(1.01); box-shadow:0 12px 32px rgba(0,0,0,0.12); }

/* --- Dash卡片3D --- */
.dash-card { transform-style:preserve-3d; transition:all 0.3s cubic-bezier(0.34,1.56,0.64,1); animation:card3DEnter 0.6s cubic-bezier(0.34,1.56,0.64,1) both; }
.dash-card:nth-child(2) { animation-delay:0.08s; }
.dash-card:nth-child(3) { animation-delay:0.16s; }
.dash-card:nth-child(4) { animation-delay:0.24s; }
.dash-card:hover { transform:translateY(-5px) rotateX(4deg) scale(1.04); box-shadow:0 16px 40px rgba(0,0,0,0.12); }
/* dash 图标悬浮动画 */
.dash-card:hover .dash-icon { animation:iconBounce 0.6s ease-out; }
@keyframes iconBounce { 0%,100%{transform:scale(1) rotateY(0deg);} 30%{transform:scale(1.2) rotateY(15deg);} 60%{transform:scale(0.95) rotateY(-10deg);} }

/* --- 管理端导航栏3D --- */
.admin-nav { transform-style:preserve-3d; perspective:800px; }
.admin-nav:hover { box-shadow:0 8px 24px rgba(0,0,0,0.08); }
.admin-nav-item { position:relative; transform-style:preserve-3d; transition:transform 0.2s ease,box-shadow 0.2s ease,background 0.2s ease,color 0.2s ease; backface-visibility:hidden; will-change:transform; }
.admin-nav-item:hover { transform:translateZ(4px) scale(1.03); box-shadow:0 4px 12px rgba(102,126,234,0.2); }
.admin-nav-item.active { transform:translateZ(4px); }

/* --- 侧边公告栏3D --- */
.side-panel { transform-style:preserve-3d; transition:all 0.4s cubic-bezier(0.34,1.56,0.64,1); }
.side-panel:hover { transform:translateZ(5px) rotateY(-3deg); box-shadow:0 10px 30px rgba(0,0,0,0.08); }
.side-panel .profile-card,
.side-panel .quick-stats,
.side-panel .side-announcements { transform-style:preserve-3d; transition:all 0.3s ease; }
.side-panel .profile-card:hover,
.side-panel .quick-stats:hover,
.side-panel .side-announcements:hover { transform:translateZ(3px); }

/* --- 开放时间卡片3D --- */
.hour-card { transform-style:preserve-3d; transition:all 0.4s cubic-bezier(0.34,1.56,0.64,1); animation:card3DEnter 0.6s cubic-bezier(0.34,1.56,0.64,1) both; }
.hour-card:hover { transform:translateY(-4px) rotateX(3deg); box-shadow:0 12px 28px rgba(0,0,0,0.1); }

/* 统一的3D入场关键帧 */
@keyframes card3DEnter { from{opacity:0;transform:translateY(30px) rotateX(10deg) translateZ(-50px);} to{opacity:1;transform:translateY(0) rotateX(0deg) translateZ(0);} }

/* --- 空状态3D浮动 --- */
.empty-state { transform-style:preserve-3d; }
.empty-state:hover .empty-icon { animation:iconFloat3d 2s ease-in-out infinite; }
@keyframes iconFloat3d {
  0%,100% { transform:translateY(0) rotateX(0deg) rotateZ(0deg); }
  25% { transform:translateY(-12px) rotateX(5deg) rotateZ(-3deg); }
  75% { transform:translateY(-6px) rotateX(-3deg) rotateZ(3deg); }
}

/* --- 公告栏3D --- */
.announcement-bar { transform-style:preserve-3d; }
.announcement-bar:hover { transform:translateZ(5px); box-shadow:0 6px 20px rgba(102,126,234,0.15); }

/* --- 3D 按钮波纹 + 发光 --- */
.auth-btn { position:relative; overflow:hidden; }
.auth-btn::after { content:''; position:absolute; inset:0; background:radial-gradient(circle at var(--x,50%) var(--y,50%), rgba(255,255,255,0.15), transparent 60%); opacity:0; transition:opacity 0.2s; }
.auth-btn:hover::after { opacity:1; }
.auth-btn:active::after { opacity:0; transition:0s; }

/* 波纹按钮基类（用于其他按钮） */
.ripple-btn { position:relative; overflow:hidden; }
.ripple-btn::after { content:''; position:absolute; inset:0; background:radial-gradient(circle at center, rgba(255,255,255,0.1), transparent 60%); opacity:0; transition:opacity 0.2s; }
.ripple-btn:hover::after { opacity:1; }

/* --- 主背景浮动 Blob 动画 --- */
.main-bg { position:fixed; inset:0; overflow:hidden; pointer-events:none; z-index:0; }
.bg-blob { position:absolute; border-radius:50%; filter:blur(60px); opacity:0.15; animation:blobFloat 25s ease-in-out infinite; }
.blob1 { width:400px; height:400px; background:radial-gradient(circle, #667eea, transparent); top:-100px; left:-100px; animation-delay:0s; }
.blob2 { width:300px; height:300px; background:radial-gradient(circle, #764ba2, transparent); bottom:-50px; right:-50px; animation-delay:-8s; }
.blob3 { width:250px; height:250px; background:radial-gradient(circle, #4facfe, transparent); top:50%; left:60%; animation-delay:-16s; }
@keyframes blobFloat {
  0%,100% { transform:translate(0,0) scale(1) rotate(0deg); }
  25% { transform:translate(50px,-30px) scale(1.1) rotate(5deg); }
  50% { transform:translate(-20px,40px) scale(0.9) rotate(-3deg); }
  75% { transform:translate(30px,20px) scale(1.05) rotate(7deg); }
}

/* --- Topbar 渐变文字 --- */
.topbar-title { background:linear-gradient(135deg, #667eea, #764ba2, #4facfe); background-size:200% auto; -webkit-background-clip:text; -webkit-text-fill-color:transparent; background-clip:text; animation:gradientText 4s ease-in-out infinite; font-weight:700; font-size:16px; }
@keyframes gradientText { 0%,100%{background-position:0% center;} 50%{background-position:100% center;} }

/* --- Tab 悬停发光下划线 --- */
.tab-item { position:relative; }
.tab-item::after { content:''; position:absolute; bottom:-2px; left:0%; width:0%; height:2px; background:linear-gradient(90deg, #667eea, #764ba2); border-radius:2px; transition:width 0.3s ease; }
.tab-item:hover::after { width:100%; }
.tab-item.active::after { width:100%; }

/* --- 用户头像发光 --- */
.user-avatar { display:inline-flex; align-items:center; justify-content:center; width:32px; height:32px; border-radius:50%; background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; font-size:13px; font-weight:600; animation:avatarGlow 3s ease-in-out infinite; }
@keyframes avatarGlow {
  0%,100% { box-shadow:0 0 8px rgba(102,126,234,0.3); }
  50% { box-shadow:0 0 20px rgba(102,126,234,0.5), 0 0 40px rgba(102,126,234,0.2); }
}

/* --- 自定义滚动条（发光） --- */
::-webkit-scrollbar { width:8px; height:8px; }
::-webkit-scrollbar-track { background:rgba(0,0,0,0.05); border-radius:4px; }
::-webkit-scrollbar-thumb { background:linear-gradient(180deg, #667eea80, #764ba280); border-radius:4px; transition:all 0.3s; }
::-webkit-scrollbar-thumb:hover { background:linear-gradient(180deg, #667eea, #764ba2); }

/* --- 页面切换过渡动画 --- */
.form-fade-enter-active, .form-fade-leave-active { transition:all 0.35s ease; }
.form-fade-enter-from { opacity:0; transform:translateY(12px) scale(0.97); }
.form-fade-leave-to { opacity:0; transform:translateY(-12px) scale(0.97); }

/* --- 用户徽章发光 --- */
.user-badge { display:inline-block; padding:2px 10px; border-radius:20px; font-size:11px; font-weight:600; background:linear-gradient(135deg,#667eea,#764ba2); color:#fff; animation:badgePulse 2s ease-in-out infinite; }
@keyframes badgePulse { 0%,100%{box-shadow:0 0 4px rgba(102,126,234,0.3);} 50%{box-shadow:0 0 12px rgba(102,126,234,0.5);} }

/* --- 顶部公告横幅 --- */
.announcement-banner { position:relative; z-index:20; margin:0 auto; max-width:1200px; padding:0 20px; }
.announcement-banner-inner { display:flex; align-items:center; gap:10px; background:linear-gradient(135deg, rgba(102,126,234,0.08), rgba(118,75,162,0.08)); border:1px solid rgba(102,126,234,0.15); border-radius:12px; padding:8px 16px; backdrop-filter:blur(8px); margin-top:12px; animation:bannerSlideIn 0.4s ease-out; }
@keyframes bannerSlideIn { from{opacity:0;transform:translateY(-10px);} to{opacity:1;transform:translateY(0);} }
.ann-banner-icon { font-size:18px; flex-shrink:0; }
.ann-banner-scroll { flex:1; overflow:hidden; height:22px; }
.ann-banner-track { display:flex; flex-direction:column; transition:transform 0.5s cubic-bezier(0.34,1.56,0.64,1); }
.ann-banner-item { height:22px; line-height:22px; display:flex; align-items:center; gap:6px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
.ann-banner-title { font-weight:600; font-size:13px; color:#667eea; flex-shrink:0; }
.ann-banner-sep { color:#ccc; flex-shrink:0; }
.ann-banner-text { font-size:13px; color:#555; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.ann-banner-close { cursor:pointer; font-size:16px; color:#999; flex-shrink:0; padding:2px 4px; border-radius:4px; transition:all 0.2s; }
.ann-banner-close:hover { background:rgba(0,0,0,0.05); color:#333; }
.dark .announcement-banner-inner { background:linear-gradient(135deg, rgba(102,126,234,0.15), rgba(118,75,162,0.12)); border-color:rgba(102,126,234,0.25); }
.dark .ann-banner-title { color:#8b9cf7; }
.dark .ann-banner-text { color:#ccc; }
.dark .ann-banner-close:hover { background:rgba(255,255,255,0.1); color:#fff; }

/* --- 公告 NEW 标记 --- */
.ann-banner-new { display:inline-block; padding:0 6px; height:16px; line-height:16px; font-size:10px; font-weight:700; color:#fff; background:linear-gradient(135deg,#ff6b6b,#ee5a24); border-radius:8px; animation:newPulse 1.5s ease-in-out infinite; flex-shrink:0; }
@keyframes newPulse { 0%,100%{transform:scale(1);} 50%{transform:scale(1.12);} }

/* --- 公告"全部"链接 --- */
.ann-banner-all { cursor:pointer; font-size:12px; color:#667eea; flex-shrink:0; padding:2px 8px; border-radius:6px; transition:all 0.2s; white-space:nowrap; font-weight:500; }
.ann-banner-all:hover { background:rgba(102,126,234,0.1); }
.dark .ann-banner-all { color:#8b9cf7; }
.dark .ann-banner-all:hover { background:rgba(102,126,234,0.2); }

/* --- 公告列表弹窗 --- */
.ann-list-modal { max-height:65vh; overflow-y:auto; padding:4px 0; }
.ann-list-item { padding:14px 12px; border-radius:10px; background:#f8f9fc; margin-bottom:10px; animation:annListItemIn 0.3s ease-out both; }
@keyframes annListItemIn { from{opacity:0;transform:translateY(10px);} to{opacity:1;transform:translateY(0);} }
.ann-list-item-header { display:flex; align-items:center; gap:8px; margin-bottom:6px; }
.ann-list-item-title { font-weight:600; font-size:14px; color:#333; flex:1; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.ann-list-item-time { font-size:11px; color:#999; flex-shrink:0; }
.ann-list-item-content { font-size:13px; color:#555; line-height:1.7; }
.ann-list-item-content p { margin:4px 0; }
.dark .ann-list-item { background:rgba(255,255,255,0.04); }
.dark .ann-list-item-title { color:#e0e0e0; }
.dark .ann-list-item-content { color:#bbb; }
</style>
