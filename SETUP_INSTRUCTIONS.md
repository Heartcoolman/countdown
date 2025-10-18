# 项目设置说明

## 初始设置

### 1. 下载 Gradle Wrapper JAR

项目使用 Gradle Wrapper，但由于文件大小限制，wrapper JAR 文件需要单独下载。

**方法一：使用现有的 Gradle**

如果你的系统已安装 Gradle，运行：

```bash
gradle wrapper
```

**方法二：手动下载**

1. 访问：https://services.gradle.org/distributions/gradle-8.7-bin.zip
2. 下载并解压
3. 复制 `lib/gradle-wrapper.jar` 到项目的 `gradle/wrapper/` 目录

**方法三：使用 Android Studio**

1. 用 Android Studio 打开项目
2. Android Studio 会自动下载所需的 Gradle Wrapper

### 2. 添加应用图标

请参考 `composeApp/src/androidMain/res/ICONS_README.md` 添加应用图标。

临时方案：可以使用在线工具生成图标
- https://romannurik.github.io/AndroidAssetStudio/
- https://icon.kitchen/

### 3. 赋予 gradlew 执行权限（Linux/Mac）

```bash
chmod +x gradlew
```

## 构建项目

### Android

```bash
# 调试版本
./gradlew :composeApp:assembleDebug

# 发布版本
./gradlew :composeApp:assembleRelease

# 安装到设备
./gradlew :composeApp:installDebug
```

### Desktop

```bash
./gradlew :composeApp:run
```

## 项目结构

```
CountdownApp/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/           # 跨平台共享代码
│   │   │   └── kotlin/
│   │   │       ├── data/         # 数据层
│   │   │       ├── ui/           # UI 层
│   │   │       ├── viewmodel/    # ViewModel
│   │   │       └── utils/        # 工具类
│   │   ├── androidMain/          # Android 特定代码
│   │   │   ├── kotlin/
│   │   │   └── res/
│   │   └── desktopMain/          # Desktop 特定代码
│   └── build.gradle.kts
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 依赖库

- **Miuix**: 0.5.2 - UI 组件库
- **Compose Multiplatform**: 1.7.0
- **Kotlin**: 2.0.21
- **Kotlinx Serialization**: 1.7.3
- **Kotlinx DateTime**: 0.6.1
- **Android DataStore**: 1.1.1
- **WorkManager**: 2.9.1

## 常见问题

### Q: 构建失败，提示找不到 Gradle Wrapper

A: 请按照上述步骤 1 下载 Gradle Wrapper JAR 文件。

### Q: 小米设备上通知不工作

A: 请在设置中：
1. 开启应用的自启动权限
2. 关闭电池优化
3. 允许通知权限

### Q: 如何修改应用图标？

A: 参考 `composeApp/src/androidMain/res/ICONS_README.md`

### Q: 支持哪些平台？

A: 当前主要支持 Android，也可以运行在 Desktop (JVM) 上。iOS 和其他平台需要额外配置。

## 开发建议

### 使用 Android Studio

推荐使用 Android Studio Hedgehog (2023.1.1) 或更高版本。

### 使用 IntelliJ IDEA

需要安装以下插件：
- Kotlin Multiplatform Mobile
- Android

## 更多信息

- Miuix 文档: https://compose-miuix-ui.github.io/miuix/
- Compose Multiplatform: https://www.jetbrains.com/lp/compose-multiplatform/

