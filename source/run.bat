@echo off
title SmartWMS - Spring Boot Server
echo =========================================================================
echo   HE THONG QUAN LY KHO HANG THONG MINH (SMARTWMS) - SPRING BOOT SERVER
echo =========================================================================
echo.

set "MAVEN_BIN=C:\Users\ACER_LAPTOP\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin"
if not exist "%MAVEN_BIN%\mvn.cmd" (
    echo [LOI] Khong tim thay Maven tai: %MAVEN_BIN%
    pause
    exit /b 1
)

set "PATH=%MAVEN_BIN%;%PATH%"
cd /d "%~dp0"

echo [*] Dang bien dich va khoi dong SmartWMS tai http://localhost:8088 ...
echo [*] Nhan Ctrl + C de dung server.
echo.

call "%MAVEN_BIN%\mvn.cmd" spring-boot:run

pause
