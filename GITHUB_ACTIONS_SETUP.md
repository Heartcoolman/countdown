# GitHub Actions 自动化打包配置

## ✅ 已配置的工作流

项目已包含两个 GitHub Actions 工作流：

### 1. `android-build.yml` - 持续集成

**触发条件：**
- 推送到 `main`、`master` 或 `develop` 分支
- Pull Request 到上述分支
- 手动触发

**功能：**
- ✅ 自动构建 Debug APK
- ✅ 尝试构建 Release APK（如果已配置签名）
- ✅ 上传 APK 到 Actions Artifacts
- ✅ 保存 30 天

### 2. `release-build.yml` - 发布构建

**触发条件：**
- 推送标签（如 `v1.0.0`）
- 创建 GitHub Release
- 手动触发

**功能：**
- ✅ 自动构建 APK
- ✅ 重命名为版本号
- ✅ 上传到 GitHub Release
- ✅ 自动生成 Release 说明

## 🚀 使用方法

### 方式一：推送代码自动构建

```bash
# 推送到主分支
git add .
git commit -m "你的提交信息"
git push origin main

# GitHub Actions 会自动开始构建
# 等待 3-5 分钟
# 在 Actions 页面查看进度
```

**下载 APK：**
1. 进入 GitHub 仓库
2. 点击 `Actions` 标签
3. 选择最新的工作流运行
4. 在 `Artifacts` 部分下载 APK

### 方式二：创建 Release 发布

```bash
# 1. 创建标签
git tag v1.0.0
git push origin v1.0.0

# 2. GitHub Actions 自动构建
# 3. APK 会自动附加到 Release 页面
```

**或在 GitHub 网页上：**
1. 进入仓库的 `Releases` 页面
2. 点击 `Create a new release`
3. 输入标签（如 `v1.0.0`）
4. 填写发布说明
5. 点击 `Publish release`
6. 等待自动构建完成
7. APK 会自动添加到 Release 下载区

### 方式三：手动触发构建

1. 进入仓库的 `Actions` 页面
2. 选择工作流（`Android CI` 或 `Release Build`）
3. 点击 `Run workflow`
4. 选择分支
5. 点击绿色的 `Run workflow` 按钮
6. 等待构建完成，下载 APK

## 📝 首次设置步骤

### 1. 初始化 Git 仓库（如果还没有）

```bash
cd D:\miuix

# 初始化 Git
git init

# 添加所有文件
git add .

# 首次提交
git commit -m "Initial commit: Countdown App with Miuix UI and Xiaomi HyperOS adaptation"
```

### 2. 创建 GitHub 仓库

1. 访问 https://github.com/new
2. 仓库名称：`countdown-app-miuix` （或其他名称）
3. 描述：`基于 Miuix UI 的倒计时应用，深度适配小米澎湃OS`
4. 选择 Public 或 Private
5. 不要勾选 README、.gitignore（我们已经有了）
6. 点击 `Create repository`

### 3. 推送到 GitHub

```bash
# 添加远程仓库（替换为你的 GitHub 用户名）
git remote add origin https://github.com/你的用户名/countdown-app-miuix.git

# 推送主分支
git branch -M main
git push -u origin main

# GitHub Actions 会自动开始构建！
```

### 4. 查看构建进度

1. 打开你的 GitHub 仓库
2. 点击 `Actions` 标签
3. 可以看到正在运行的工作流
4. 点击进入查看详细日志
5. 构建完成后在 `Artifacts` 下载 APK

## 🔐 配置签名（可选 - 生产环境）

如果想要发布签名的 Release APK，需要配置密钥：

### 1. 生成签名密钥

```bash
# 在本地生成
keytool -genkey -v -keystore countdown-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias countdown
```

### 2. 转换为 Base64

```bash
# Linux/Mac
base64 countdown-release-key.jks > keystore.b64

# Windows PowerShell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("countdown-release-key.jks")) > keystore.b64
```

### 3. 添加 GitHub Secrets

在 GitHub 仓库设置中：

1. Settings → Secrets and variables → Actions
2. 点击 `New repository secret`
3. 添加以下 Secrets：

| Secret 名称 | 值 |
|------------|---|
| `KEYSTORE_BASE64` | keystore.b64 的内容 |
| `KEYSTORE_PASSWORD` | 密钥库密码 |
| `KEY_ALIAS` | 密钥别名（如 countdown） |
| `KEY_PASSWORD` | 密钥密码 |

### 4. 创建签名构建工作流

创建 `.github/workflows/signed-release.yml`:

```yaml
name: Signed Release

on:
  push:
    tags:
      - 'v*'

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: 设置 JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: 解码密钥库
      run: |
        echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > release.jks
        
    - name: 构建签名 APK
      run: ./gradlew assembleRelease
      env:
        SIGNING_KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        SIGNING_KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
        SIGNING_STORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        
    - name: 上传到 Release
      uses: softprops/action-gh-release@v1
      with:
        files: composeApp/build/outputs/apk/release/*.apk
```

## 📊 工作流说明

### android-build.yml

```yaml
触发: 每次推送代码
时间: 3-5 分钟
输出: Debug APK
位置: Actions → Artifacts
保存: 30 天
```

### release-build.yml

```yaml
触发: 推送标签或创建 Release
时间: 3-5 分钟  
输出: Debug APK + 自动 Release
位置: Releases 页面
保存: 永久
```

## 🎯 发布流程示例

### 完整的发布步骤：

```bash
# 1. 确保代码已提交
git add .
git commit -m "准备发布 v1.0.0"
git push

# 2. 创建标签
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# 3. 等待 GitHub Actions 构建（3-5分钟）

# 4. 在 GitHub Releases 页面下载 APK
```

### GitHub 上会自动：

1. ✅ 触发 Release Build 工作流
2. ✅ 构建 APK
3. ✅ 创建 GitHub Release
4. ✅ 上传 APK 文件
5. ✅ 生成版本说明

## 📥 用户下载流程

用户访问你的 GitHub 仓库：

1. 点击 `Releases` 标签
2. 查看最新版本
3. 在 `Assets` 部分找到 APK
4. 点击下载
5. 安装到 Android 设备

## ⚙️ 高级配置

### 自动版本号

如果想自动管理版本号，修改 `composeApp/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        val versionPropsFile = file("version.properties")
        if (versionPropsFile.exists()) {
            val versionProps = Properties()
            versionProps.load(FileInputStream(versionPropsFile))
            versionCode = versionProps["VERSION_CODE"].toString().toInt()
            versionName = versionProps["VERSION_NAME"].toString()
        } else {
            versionCode = 1
            versionName = "1.0.0"
        }
    }
}
```

### 多渠道打包

如果需要不同渠道的 APK（小米商店、华为商店等）:

```kotlin
android {
    flavorDimensions += "store"
    productFlavors {
        create("xiaomi") {
            dimension = "store"
            applicationIdSuffix = ".xiaomi"
        }
        create("huawei") {
            dimension = "store"
            applicationIdSuffix = ".huawei"
        }
    }
}
```

## 🔍 常见问题

### Q: 构建失败怎么办？

A: 查看 Actions 日志：
1. 点击失败的工作流
2. 查看详细错误信息
3. 通常是依赖下载或 Gradle 配置问题

### Q: 如何查看构建日志？

A: 
1. Actions 页面
2. 选择工作流运行
3. 点击任务名称
4. 展开各个步骤查看详细输出

### Q: APK 在哪里下载？

A: 
- **持续集成**: Actions → 工作流运行 → Artifacts
- **发布版本**: Releases 页面

### Q: 如何加速构建？

A: 工作流已包含缓存配置：
- Gradle 依赖缓存
- Gradle Wrapper 缓存
- 第二次构建会快很多

## 📋 工作流文件位置

```
.github/
└── workflows/
    ├── android-build.yml      # 持续集成
    └── release-build.yml      # 发布构建
```

## 🎉 完成！

现在你的项目已配置好自动化打包！

**下次提交代码时**：
- ✅ GitHub Actions 自动构建
- ✅ 自动运行测试
- ✅ 生成 APK 文件
- ✅ 可随时下载

**发布新版本时**：
- ✅ 推送标签即可
- ✅ 自动创建 Release
- ✅ APK 自动上传
- ✅ 用户可直接下载

享受自动化的便利吧！🚀

