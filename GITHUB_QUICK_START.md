# 🚀 GitHub 快速发布指南

## 3 步上传到 GitHub

### 步骤 1: 初始化 Git（如果还没有）

```bash
cd D:\miuix

# 初始化
git init

# 添加所有文件
git add .

# 首次提交
git commit -m "Initial commit: 倒计时应用 v1.0.0"
```

### 步骤 2: 创建 GitHub 仓库

1. 打开 https://github.com/new
2. **仓库名称**: `countdown-app-miuix`
3. **描述**: `基于 Miuix UI 的倒计时应用，深度适配小米澎湃OS`
4. **可见性**: Public（公开）或 Private（私有）
5. ⚠️ **不要**勾选 "Add a README"（我们已经有了）
6. 点击 `Create repository`

### 步骤 3: 推送到 GitHub

复制 GitHub 显示的命令，或使用：

```bash
# 添加远程仓库（替换 YOUR_USERNAME）
git remote add origin https://github.com/YOUR_USERNAME/countdown-app-miuix.git

# 推送
git branch -M main
git push -u origin main
```

**✅ 完成！** GitHub Actions 会自动开始构建 APK！

## 📦 查看自动构建的 APK

### 等待 3-5 分钟后：

1. 打开你的 GitHub 仓库
2. 点击 `Actions` 标签（顶部）
3. 看到绿色 ✅ 表示构建成功
4. 点击工作流名称
5. 滚动到底部 `Artifacts` 部分
6. 点击 `app-debug` 下载 APK

## 🎯 发布正式版本

### 创建第一个 Release：

```bash
# 1. 创建标签
git tag -a v1.0.0 -m "首次发布"

# 2. 推送标签
git push origin v1.0.0

# 3. 等待自动构建（3-5分钟）

# 4. 访问 Releases 页面下载
```

### 在 GitHub 网页上查看：

1. 仓库页面点击 `Releases`（右侧边栏）
2. 看到 `v1.0.0` 版本
3. `Assets` 部分有 APK 文件
4. 点击下载即可！

## 📱 分享给用户

发布后，分享这个链接给用户：

```
https://github.com/YOUR_USERNAME/countdown-app-miuix/releases/latest
```

用户可以直接下载最新版 APK！

## 🔄 更新版本

### 发布新版本：

```bash
# 1. 修改代码...

# 2. 提交
git add .
git commit -m "更新：添加新功能"
git push

# 3. 创建新标签
git tag -a v1.1.0 -m "v1.1.0 更新内容"
git push origin v1.1.0

# 4. 自动构建并发布！
```

## ⚠️ 重要提示

### 不要提交的文件（已在 .gitignore）

- ❌ `*.jks` / `*.keystore` - 签名密钥
- ❌ `local.properties` - 本地配置
- ❌ `.idea/` - IDE 配置
- ❌ `build/` - 构建文件

### 必须提交的文件

- ✅ 所有源代码（`.kt` 文件）
- ✅ Gradle 配置（`*.gradle.kts`, `*.toml`）
- ✅ AndroidManifest.xml
- ✅ 资源文件（`res/`）
- ✅ GitHub Actions 配置（`.github/workflows/`）
- ✅ 文档文件（`*.md`）

## 💡 小技巧

### 查看 GitHub Actions 日志

如果构建失败：
1. Actions 页面
2. 点击失败的运行
3. 点击红色 ❌ 的任务
4. 查看详细错误信息

### 手动触发构建

1. Actions 页面
2. 选择 `Android CI` 或 `Release Build`
3. 点击 `Run workflow`
4. 选择分支
5. 点击 `Run workflow`

### 删除标签（如果发布错了）

```bash
# 删除本地标签
git tag -d v1.0.0

# 删除远程标签
git push origin :refs/tags/v1.0.0
```

## 🎉 完成

现在你的项目已经配置好自动化发布流程！

**工作流程**：
```
代码更改 → Git 提交 → 推送到 GitHub → Actions 自动构建 → 下载 APK
```

**发布流程**：
```
创建标签 → 推送标签 → Actions 自动构建 → 创建 Release → 用户下载
```

就是这么简单！🚀

