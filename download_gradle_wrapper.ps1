# PowerShell 脚本：下载 Gradle Wrapper JAR
# 用法: .\download_gradle_wrapper.ps1

$wrapperUrl = "https://raw.githubusercontent.com/gradle/gradle/v8.7/gradle/wrapper/gradle-wrapper.jar"
$wrapperDir = "gradle\wrapper"
$wrapperJar = "$wrapperDir\gradle-wrapper.jar"

Write-Host "正在创建目录..." -ForegroundColor Green
New-Item -ItemType Directory -Force -Path $wrapperDir | Out-Null

Write-Host "正在下载 Gradle Wrapper JAR..." -ForegroundColor Green
try {
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperJar
    Write-Host "✅ Gradle Wrapper JAR 下载成功！" -ForegroundColor Green
    Write-Host ""
    Write-Host "现在可以运行：" -ForegroundColor Yellow
    Write-Host "  .\gradlew build" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 下载失败：$_" -ForegroundColor Red
    Write-Host ""
    Write-Host "备选方案：" -ForegroundColor Yellow
    Write-Host "1. 用 Android Studio 打开项目（会自动下载）" -ForegroundColor Cyan
    Write-Host "2. 手动访问 $wrapperUrl 下载并放到 gradle\wrapper\ 目录" -ForegroundColor Cyan
}

