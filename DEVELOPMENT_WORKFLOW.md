# 🔄 开发工作流程

## 分支策略

```
main (生产分支)
  ├── 稳定代码
  ├── 每次合并自动打包发布
  └── 受保护，只接受 PR

develop (开发分支)
  ├── 最新开发代码
  ├── 只做代码检查，不打包
  └── 功能完成后合并到 main

feature/* (功能分支)
  └── 从 develop 创建，完成后合并回 develop
```

## 📋 标准开发流程

### 1. 日常开发

```bash
# 确保在 develop 分支
git checkout develop
git pull origin develop

# 创建功能分支
git checkout -b feature/new-feature

# 开发功能...
# 修改代码

# 提交
git add .
git commit -m "feat: 添加新功能"

# 推送功能分支
git push -u origin feature/new-feature
```

### 2. 在 GitHub 创建 PR

1. 访问 https://github.com/Heartcoolman/countdown
2. 点击 "Compare & pull request"
3. **Base**: `develop` ← **Compare**: `feature/new-feature`
4. 填写 PR 说明
5. 点击 "Create pull request"
6. 等待 CI 检查通过 ✅
7. 合并到 `develop`

### 3. 准备发布

当 `develop` 分支功能足够稳定时：

```bash
# 从 develop 创建 PR 到 main
```

**在 GitHub 上**:
1. 创建 PR: `develop` → `main`
2. 审查所有更改
3. 合并 PR
4. **自动触发**: 
   - ✅ 构建 Release APK
   - ✅ 自动创建 GitHub Release
   - ✅ 上传 APK 供用户下载

### 4. 创建版本标签（可选）

合并到 main 后，创建版本标签：

```bash
git checkout main
git pull origin main

# 创建标签
git tag -a v1.1.0 -m "Release v1.1.0"
git push origin v1.1.0
```

## ⚙️ GitHub Actions 行为

### develop 分支

**工作流**: `Develop Branch Check`

```yaml
触发: 推送到 develop
行为: 
  - ✅ 编译检查
  - ✅ 运行测试
  - ❌ 不构建 APK
  - ❌ 不发布
目的: 快速验证代码质量
```

### main 分支

**工作流**: `Android CI - Main Branch`

```yaml
触发: 合并到 main
行为:
  - ✅ 编译检查
  - ✅ 构建 Debug APK
  - ✅ 构建 Release APK
  - ✅ 上传 Artifacts
目的: 生成可发布的 APK
```

### 标签触发

**工作流**: `Release Build`

```yaml
触发: 推送 v* 标签
行为:
  - ✅ 构建 APK
  - ✅ 创建 GitHub Release
  - ✅ 自动上传到 Releases
  - ✅ 生成版本说明
目的: 正式发布版本
```

## 📊 完整流程图

```
开发新功能
    ↓
feature/xxx 分支
    ↓
提交 + 推送
    ↓
PR → develop
    ↓
合并（只检查，不打包）✅
    ↓
继续开发...
    ↓
功能稳定
    ↓
PR: develop → main
    ↓
合并到 main
    ↓
自动构建 APK 📦
    ↓
（可选）打标签 v1.x.x
    ↓
自动创建 Release 🚀
    ↓
用户下载 APK 📥
```

## 🎯 分支管理命令

### 查看所有分支

```bash
git branch -a
```

### 切换分支

```bash
git checkout main      # 切换到主分支
git checkout develop   # 切换到开发分支
```

### 同步分支

```bash
# 从远程拉取最新代码
git pull origin develop
git pull origin main
```

### 删除已合并的功能分支

```bash
# 本地删除
git branch -d feature/old-feature

# 远程删除
git push origin --delete feature/old-feature
```

## ✅ 优势

| 优点 | 说明 |
|-----|------|
| 代码质量 | develop 分支持续检查 |
| 主分支稳定 | main 只包含经过测试的代码 |
| 快速迭代 | develop 不打包，检查更快 |
| 正式发布 | main 合并自动打包 |
| 版本管理 | 清晰的版本历史 |

## 📝 Commit 规范

建议使用规范的提交信息：

```bash
feat: 添加新功能
fix: 修复 Bug
docs: 更新文档
style: 代码格式调整
refactor: 重构代码
test: 添加测试
chore: 构建/工具变更
```

## 🎉 总结

现在的工作流程：

1. **日常开发** → `develop` 分支 → 只检查代码
2. **稳定后** → PR 到 `main` → 自动打包发布
3. **正式版本** → 打标签 → 创建 Release

这样既保证了开发效率，又确保了发布质量！

