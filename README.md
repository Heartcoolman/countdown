# 倒计时应用

一个基于 Compose Multiplatform 和 Miuix UI 的倒计时应用。

## 功能特性

- 📅 **日期倒计时**：倒数到指定的未来日期
- ⏱️ **定时器**：设置时长并开始倒计时
- ⏲️ **秒表**：正向计时和记圈功能
- 💾 **数据持久化**：重启应用后保留所有倒计时项目
- 🔔 **通知提醒**：倒计时结束时发送通知
- 📱 **小米设备优化**：特别适配 MIUI 的通知和后台运行

## 技术栈

- Kotlin Multiplatform
- Compose Multiplatform
- Miuix UI Library
- Kotlinx Serialization
- Kotlinx DateTime
- Android DataStore
- Navigation Compose

## 系统要求

- Android 7.0 (API 24) 及以上
- 支持 Android 16 (API 35)

## 构建项目

```bash
# 构建 Android 应用
./gradlew :composeApp:assembleDebug

# 安装到设备
./gradlew :composeApp:installDebug
```

## 小米/澎湃OS 设备优化 🎯

本应用已针对小米设备深度优化，参考[小米开发者平台](https://dev.mi.com/xiaomihyperos/app-develop)开发规范。

### ✨ 特色适配

- ✅ **智能权限引导** - 首次启动自动引导设置
- ✅ **多窗口支持** - 支持分屏和自由窗口
- ✅ **优化通知** - 高优先级通知，不被拦截
- ✅ **后台保活** - 确保倒计时准确运行

### 📋 首次使用必做

应用会在首次启动时显示引导对话框，请按提示完成：

1. **开启自启动权限** ⭐ 重要
   - 设置 → 应用设置 → 应用管理 → 倒计时 → 自启动

2. **关闭电池优化** ⭐ 推荐
   - 设置 → 电池与性能 → 应用电池管理 → 倒计时 → 无限制

3. **精确闹钟权限** (Android 12+)
   - 设置 → 应用设置 → 倒计时 → 闹钟和提醒

4. **允许通知**
   - 设置 → 通知 → 应用通知 → 倒计时 → 允许通知

详见：[小米澎湃OS适配文档](XIAOMI_HYPEROS_ADAPTATION.md)

## 许可证

本项目基于 Apache-2.0 许可证开源。

