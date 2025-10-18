# 快速开始指南

## 前提条件

- JDK 11 或更高版本
- Android Studio Hedgehog (2023.1.1) 或更高版本
- Android SDK (API 24-35)

## 快速启动步骤

### 1. 克隆或打开项目

```bash
cd miuix
```

### 2. 设置 Gradle Wrapper

**如果你已安装 Gradle：**
```bash
gradle wrapper --gradle-version 8.7
```

**或者直接用 Android Studio 打开项目，它会自动处理。**

### 3. 添加图标（可选）

在开发阶段，项目会使用默认的占位图标。如需自定义图标，请参考：
`composeApp/src/androidMain/res/ICONS_README.md`

### 4. 构建并运行

**使用 Android Studio：**
1. 打开项目
2. 等待 Gradle 同步完成
3. 连接 Android 设备或启动模拟器
4. 点击运行按钮 ▶️

**使用命令行：**
```bash
# Linux/Mac
./gradlew :composeApp:installDebug

# Windows
gradlew.bat :composeApp:installDebug
```

## 功能概览

### 首页
- 显示所有正在进行的倒计时
- 实时更新剩余时间
- 点击查看详情

### 倒计时管理
- 创建日期倒计时（倒数到指定日期）
- 创建定时器（设定时长倒计时）
- 编辑和删除倒计时
- 暂停/继续功能

### 秒表
- 正向计时
- 记圈功能
- 精确到毫秒

### 定时器
- 快捷时间选择（1分钟、5分钟、10分钟等）
- 自定义时长
- 倒计时结束通知

## 小米设备特别说明

为确保在小米设备上正常运行，首次使用时请：

1. **允许通知权限**
   - 应用会在启动时请求

2. **开启自启动**（重要！）
   - 设置 → 应用设置 → 应用管理 → 倒计时 → 自启动

3. **关闭电池优化**（推荐）
   - 设置 → 电池与性能 → 应用电池管理 → 倒计时 → 无限制

## 项目特点

✅ 基于 Compose Multiplatform - 跨平台支持  
✅ Miuix UI - 美观的小米风格界面  
✅ 数据持久化 - 重启应用保留数据  
✅ 后台通知 - 倒计时结束及时提醒  
✅ MIUI 优化 - 特别适配小米设备  
✅ 支持 Android 16 - 最新系统版本  

## 技术栈

- Kotlin 2.0.21
- Compose Multiplatform 1.7.0
- Miuix 0.5.2
- Kotlinx Serialization
- Kotlinx DateTime
- Android DataStore
- WorkManager

## 下一步

- 查看 `README.md` 了解完整功能
- 查看 `SETUP_INSTRUCTIONS.md` 了解详细配置
- 参考源码学习 Compose Multiplatform 开发

## 遇到问题？

1. 确保 JDK 版本正确（JDK 11+）
2. 确保 Android SDK 已正确安装
3. 清理并重新构建：`./gradlew clean build`
4. 删除 `.gradle` 和 `build` 目录后重试

享受使用！🎉

