# 小米澎湃OS / MIUI 适配说明

本应用已针对小米设备进行深度优化，参考[小米开发者平台](https://dev.mi.com/xiaomihyperos/app-develop)的开发规范。

## ✅ 已完成的适配

### 1. 通知与状态栏适配

#### 通知渠道优化
- ✅ **高优先级通知渠道**
  - 设置为 `IMPORTANCE_HIGH` 确保倒计时提醒不被忽略
  - 启用震动和LED灯提示
  - 配置自定义通知声音
  - 锁屏显示（`VISIBILITY_PUBLIC`）
  - 允许显示角标（Badge）

#### 前台服务通知
- ✅ **低优先级前台服务渠道**
  - 静默通知，不打扰用户
  - 明确标注为倒计时服务
  - 符合小米对前台服务的规范

**代码位置**: `CountdownApplication.kt`

```kotlin
// 小米设备：允许通知浮现
setShowBadge(true)
// 小米设备：锁屏显示
lockscreenVisibility = Notification.VISIBILITY_PUBLIC
// 设置声音（小米设备重视）
setSound(defaultSoundUri, audioAttributes)
```

### 2. 权限管理适配

#### 自动权限检测与引导
- ✅ **小米设备检测**
  - 识别 Xiaomi、Redmi、Mi 品牌
  - 获取 MIUI/HyperOS 版本信息

- ✅ **自启动权限引导**
  - 支持 HyperOS / MIUI 14+
  - 支持 MIUI 12/13
  - 支持旧版 MIUI
  - 多个路径尝试，确保能够打开设置

- ✅ **电池优化豁免**
  - 检测当前电池优化状态
  - 引导用户关闭电池优化
  - 防止系统杀掉后台进程

- ✅ **精确闹钟权限**（Android 12+）
  - 检测精确闹钟权限状态
  - 引导用户授予权限
  - 确保倒计时准时触发

**代码位置**: `MiuiPermissionHelper.kt`

#### 首次启动引导
- ✅ **智能权限引导对话框**
  - 仅在小米设备上显示
  - 只在首次启动时显示一次
  - 列表展示所有需要的权限
  - 一键跳转到对应设置页面

**代码位置**: `MainActivity.kt`, `PermissionGuideDialog.kt`

### 3. 多任务与多窗口适配

#### 多窗口支持
- ✅ **分屏模式**
  - `android:resizeableActivity="true"`
  - 支持自由窗口调整大小
  - 设置最小窗口尺寸（400x300dp）
  - 设置默认窗口尺寸（600x500dp）

#### 多任务优化
- ✅ **单任务模式**
  - `android:launchMode="singleTask"`
  - 避免重复创建Activity
  - 优化多任务切换体验

**代码位置**: `AndroidManifest.xml`

```xml
<!-- 多窗口和分屏支持 -->
<activity
    android:resizeableActivity="true"
    android:launchMode="singleTask">
    <layout
        android:defaultHeight="500dp"
        android:defaultWidth="600dp"
        android:gravity="center"
        android:minHeight="300dp"
        android:minWidth="400dp" />
</activity>
```

### 4. 后台保活策略

#### 前台服务
- ✅ **前台服务声明**
  - 类型：`specialUse` - 特殊用途
  - 说明：倒计时定时器
  - 符合小米对前台服务的要求

#### AlarmManager 精确闹钟
- ✅ **适配不同 Android 版本**
  - Android 12+：检查 `canScheduleExactAlarms()`
  - 使用 `setExactAndAllowWhileIdle()`
  - 确保在省电模式下也能触发

**代码位置**: `NotificationHelper.kt`

### 5. 系统适配

#### 配置变更处理
- ✅ **完整的 configChanges**
  - 屏幕旋转、尺寸变化
  - 深色模式切换
  - 字体大小调整
  - 等等（避免重建Activity）

#### 主题适配
- ✅ **Miuix UI 集成**
  - 小米风格的UI组件
  - 自动适配深色/浅色模式
  - 符合小米设计规范

#### 国际化
- ✅ **简体中文支持**
  - 完整的中文界面
  - 符合国内用户习惯

## 📱 小米设备特别优化

### HyperOS / MIUI 特性

| 功能 | 状态 | 说明 |
|-----|------|------|
| 自启动权限引导 | ✅ | 自动检测并引导 |
| 电池优化豁免 | ✅ | 智能提示 |
| 通知优先级 | ✅ | 高优先级通知 |
| 精确闹钟 | ✅ | Android 12+ 支持 |
| 前台服务 | ✅ | 符合规范 |
| 多窗口支持 | ✅ | 分屏优化 |
| 锁屏通知 | ✅ | 公开可见 |
| 通知角标 | ✅ | 显示未读数 |
| 声音震动 | ✅ | 完整配置 |

### 权限清单

应用使用的权限及用途：

| 权限 | 用途 | 必需性 |
|-----|------|--------|
| POST_NOTIFICATIONS | 发送倒计时提醒 | 必需 |
| VIBRATE | 通知震动 | 可选 |
| SCHEDULE_EXACT_ALARM | 精确闹钟 | 必需 |
| USE_EXACT_ALARM | 精确闹钟（备用） | 必需 |
| WAKE_LOCK | 唤醒设备 | 必需 |
| FOREGROUND_SERVICE | 后台运行 | 必需 |
| REQUEST_IGNORE_BATTERY_OPTIMIZATIONS | 电池优化豁免 | 推荐 |

## 🎯 用户使用指南（小米设备）

### 首次安装后必做设置

应用会在首次启动时自动显示引导对话框，包含：

1. **开启自启动**
   - 路径：设置 → 应用设置 → 应用管理 → 倒计时 → 自启动
   - 作用：允许应用在后台运行

2. **关闭电池优化**
   - 路径：设置 → 电池与性能 → 应用电池管理 → 倒计时 → 无限制
   - 作用：防止系统杀掉应用进程

3. **精确闹钟权限**（Android 12+）
   - 路径：设置 → 应用设置 → 倒计时 → 闹钟和提醒
   - 作用：确保倒计时准时触发

4. **允许通知**
   - 路径：设置 → 通知 → 应用通知 → 倒计时
   - 作用：接收倒计时结束提醒

### 如果倒计时不准确

检查以下设置：
- ✅ 自启动已开启
- ✅ 电池优化已关闭
- ✅ 通知权限已允许
- ✅ 精确闹钟权限已授予

### 多窗口使用

小米澎湃OS / MIUI 用户可以：
- 📱 **分屏模式**：与其他应用同时使用
- 🪟 **自由窗口**：调整应用窗口大小
- 🎯 **小窗模式**：悬浮窗口快速查看

## 🔧 开发者说明

### 测试设备要求

建议在以下设备上测试：
- 小米 14 系列（HyperOS）
- 小米 13 系列（MIUI 14/HyperOS）
- 红米 Note 系列
- 其他小米/红米设备

### 关键代码文件

| 文件 | 功能 |
|-----|------|
| `MiuiPermissionHelper.kt` | 权限检测和引导 |
| `PermissionGuideDialog.kt` | 权限引导UI |
| `MainActivity.kt` | 集成权限检查 |
| `CountdownApplication.kt` | 通知渠道配置 |
| `AndroidManifest.xml` | 多窗口配置 |
| `NotificationHelper.kt` | 通知和闹钟管理 |

### API 兼容性

- ✅ Android 8.0 (API 26) ~ Android 16 (API 35)
- ✅ MIUI 10+
- ✅ HyperOS 1.0+
- ✅ 小米澎湃OS

### 注意事项

1. **自启动设置路径**
   - 不同 MIUI 版本路径可能不同
   - 代码已包含多个尝试路径
   - 如果都失败，会打开应用详情页

2. **电池优化**
   - Android 6.0+ 必需
   - 小米设备特别重要

3. **前台服务**
   - Android 8.0+ 必需
   - 需要明确说明用途（`specialUse`）

4. **精确闹钟**
   - Android 12+ 新增权限
   - 小米设备可能需要额外引导

## 📊 适配清单

### 通知系统
- ✅ 通知渠道配置
- ✅ 高优先级设置
- ✅ 声音、震动、LED
- ✅ 锁屏显示
- ✅ 角标显示
- ✅ 前台服务通知

### 权限管理
- ✅ 通知权限检测
- ✅ 自启动引导
- ✅ 电池优化检测
- ✅ 精确闹钟权限
- ✅ 权限设置页面跳转
- ✅ 首次启动引导

### 多窗口
- ✅ 分屏支持
- ✅ 自由窗口
- ✅ 最小尺寸限制
- ✅ 单任务模式

### 后台运行
- ✅ 前台服务
- ✅ WorkManager
- ✅ AlarmManager
- ✅ WakeLock

### UI/UX
- ✅ Miuix UI 组件
- ✅ 深色模式
- ✅ 全中文界面
- ✅ 符合小米设计规范

## 🚀 后续优化建议

### 可选增强功能

- [ ] **桌面小部件**（Widget）
  - 在桌面显示倒计时
  - 符合小米桌面规范
  
- [ ] **负一屏卡片**
  - 在负一屏显示倒计时信息
  - 需要接入小米负一屏SDK

- [ ] **小爱同学语音**
  - "小爱，查看我的倒计时"
  - 需要接入小爱SDK

- [ ] **快捷方式**
  - 长按图标显示快捷操作
  - 快速创建倒计时

- [ ] **分享到小米云**
  - 倒计时数据云同步
  - 跨设备同步

## 📚 参考文档

- [小米澎湃OS应用开发](https://dev.mi.com/xiaomihyperos/app-develop)
- [通知与状态栏](https://dev.mi.com/xiaomihyperos/app-develop) - 通知适配指南
- [权限管理](https://dev.mi.com/xiaomihyperos/app-develop) - 权限申请规范
- [多任务与多窗口](https://dev.mi.com/xiaomihyperos/app-develop) - 多窗口适配
- [功能适配](https://dev.mi.com/xiaomihyperos/app-develop) - 系统功能集成

## ✅ 测试验证

### 已测试场景

- ✅ 小米设备检测
- ✅ 权限引导对话框显示
- ✅ 多窗口模式兼容
- ✅ 通知渠道创建
- ✅ 前台服务启动

### 建议测试

在真实小米设备上测试：
1. 首次安装体验
2. 权限引导流程
3. 倒计时准确性
4. 通知及时性
5. 多窗口/分屏模式
6. 省电模式下的表现
7. 锁屏状态下的通知

## 🎉 总结

本应用已完成小米澎湃OS的核心适配，包括：

- ✅ 完善的权限管理
- ✅ 优化的通知系统
- ✅ 多窗口支持
- ✅ 后台保活策略
- ✅ 符合小米设计规范的UI

所有适配均遵循[小米开发者平台](https://dev.mi.com/xiaomihyperos/app-develop)的最佳实践，确保在小米设备上获得最佳体验！

