# 如何构建此项目

## 必要步骤

### 1. 安装 Gradle Wrapper JAR

这个项目使用 Gradle Wrapper，但 JAR 文件由于大小限制未包含在仓库中。

**方法 A: 使用 Android Studio（推荐）**

1. 用 Android Studio 打开项目根目录
2. Android Studio 会自动下载并配置 Gradle Wrapper
3. 等待同步完成

**方法 B: 使用已安装的 Gradle**

```bash
# 确保已安装 Gradle 8.x
gradle --version

# 在项目根目录运行
gradle wrapper --gradle-version 8.7
```

**方法 C: 手动下载（高级用户）**

```bash
# 创建目录
mkdir -p gradle/wrapper

# 下载 wrapper jar
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.7.0/gradle/wrapper/gradle-wrapper.jar

# 或者从本地 Gradle 安装复制
# cp ~/.gradle/wrapper/dists/gradle-8.7-bin/*/gradle-8.7/lib/gradle-wrapper.jar gradle/wrapper/
```

### 2. 添加应用图标

项目已包含占位图标配置，但建议添加真实图标以获得更好的视觉效果。

#### 快速方案：使用在线工具

1. 访问 https://icon.kitchen/ 或 https://romannurik.github.io/AndroidAssetStudio/
2. 设计你的图标（建议使用时钟⏰相关图案）
3. 下载生成的资源包
4. 将图标文件复制到以下目录：
   ```
   composeApp/src/androidMain/res/
   ├── mipmap-mdpi/
   ├── mipmap-hdpi/
   ├── mipmap-xhdpi/
   ├── mipmap-xxhdpi/
   └── mipmap-xxxhdpi/
   ```

#### 跳过此步骤

如果暂时不需要自定义图标，项目会使用默认的占位图标（蓝色圆圈），可以正常运行。

### 3. 赋予执行权限（Linux/macOS）

```bash
chmod +x gradlew
```

### 4. 构建项目

```bash
# 清理构建
./gradlew clean

# 构建调试版本
./gradlew :composeApp:assembleDebug

# 或者直接安装到连接的设备
./gradlew :composeApp:installDebug
```

### 5. 运行项目

**使用 Android Studio:**
1. 打开项目
2. 选择设备或模拟器
3. 点击运行 ▶️

**使用命令行:**
```bash
# 确保设备已连接
adb devices

# 安装并运行
./gradlew :composeApp:installDebug
```

## 常见问题排查

### 问题 1: "gradle-wrapper.jar not found"

**解决方案：** 参考上面的"安装 Gradle Wrapper JAR"步骤

### 问题 2: "SDK location not found"

**解决方案：** 创建 `local.properties` 文件：
```properties
sdk.dir=/path/to/your/Android/Sdk
```

或者在 Android Studio 中打开项目，它会自动创建。

### 问题 3: 编译错误

**解决方案：**
```bash
# 清理所有构建文件
./gradlew clean
rm -rf .gradle build */build

# 重新构建
./gradlew build
```

### 问题 4: 依赖下载失败

**解决方案：** 检查网络连接，或配置镜像源（中国用户）

在 `build.gradle.kts` 或 `settings.gradle.kts` 中添加：
```kotlin
repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
    mavenCentral()
    google()
}
```

### 问题 5: JDK 版本问题

**解决方案：** 确保使用 JDK 11 或更高版本
```bash
java -version
# 应该显示 11 或更高
```

## 系统要求

- **JDK**: 11 或更高（推荐 17）
- **Android SDK**: API 24-35
- **Gradle**: 8.7（通过 wrapper）
- **Kotlin**: 2.0.21（自动下载）
- **IDE**: Android Studio Hedgehog (2023.1.1) 或更高

## 构建输出

成功构建后，APK 文件位于：
```
composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

## 发布构建

要构建发布版本（需要配置签名）：

```bash
./gradlew :composeApp:assembleRelease
```

签名配置请参考 Android 官方文档。

## 下一步

- 查看 `QUICKSTART.md` 了解功能使用
- 查看 `PROJECT_SUMMARY.md` 了解项目架构
- 开始开发你的功能！

## 获取帮助

如果遇到问题：
1. 检查 JDK 和 Android SDK 是否正确安装
2. 确保网络连接正常（下载依赖需要）
3. 查看 Android Studio 的 Build 窗口错误信息
4. 搜索错误信息或提 Issue

祝构建成功！🎉

