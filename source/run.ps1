# SmartWMS - Spring Boot Runner Script
Write-Host "=========================================================================" -ForegroundColor Cyan
Write-Host "  HE THONG QUAN LY KHO HANG THONG MINH (SMARTWMS) - SPRING BOOT SERVER" -ForegroundColor Yellow
Write-Host "=========================================================================" -ForegroundColor Cyan
Write-Host ""

$mavenBin = "C:\Users\ACER_LAPTOP\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin"

if (-not (Test-Path "$mavenBin\mvn.cmd")) {
    Write-Host "[LỖI] Không tìm thấy Maven tại: $mavenBin" -ForegroundColor Red
    Write-Host "Vui lòng cài đặt Maven hoặc kiểm tra lại đường dẫn." -ForegroundColor Yellow
    exit 1
}

$env:PATH = "$mavenBin;" + $env:PATH
Set-Location $PSScriptRoot

Write-Host "[*] Đang biên dịch và khởi động SmartWMS tại http://localhost:8088 ..." -ForegroundColor Green
Write-Host "[*] Nhấn Ctrl + C để dừng server." -ForegroundColor Gray
Write-Host ""

& "$mavenBin\mvn.cmd" spring-boot:run
