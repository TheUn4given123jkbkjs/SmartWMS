@echo off
setlocal EnableDelayedExpansion
title SmartWMS - Spring Boot Server (XAMPP MySQL Bridge)
echo =========================================================================
echo   HE THONG QUAN LY KHO HANG THONG MINH (SMARTWMS) - XAMPP MYSQL BRIDGE
echo =========================================================================
echo.

cd /d "%~dp0"

REM 1. Tu dong tim Maven Wrapper hoac Maven trong he thong
if exist "%~dp0mvnw.cmd" (
    set "MVN_CMD=%~dp0mvnw.cmd"
) else (
    where mvn >nul 2>&1
    if errorlevel 1 (
        echo [LOI] Khong tim thay mvnw.cmd hoac Maven trong PATH he thong!
        pause
        exit /b 1
    )
    set "MVN_CMD=mvn"
)

REM 2. Quet tu dong tren tat ca cac o dia (C, D, E, F, G, H) de tim thu muc XAMPP
set "XAMPP_DIR="
for %%D in (C D E F G H) do (
    if not defined XAMPP_DIR (
        if exist "%%D:\xampp\xampp-control.exe" (
            set "XAMPP_DIR=%%D:\xampp"
        )
    )
)

if defined XAMPP_DIR (
    echo [*] Phat hien XAMPP tai: !XAMPP_DIR!
    set "XAMPP_HINT=!XAMPP_DIR!\xampp-control.exe"
) else (
    echo [*] Khong tim thay thu muc mac dinh \xampp tren cac o dia, kiem tra truc tiep theo cong dich vu...
    set "XAMPP_HINT=XAMPP Control Panel"
)
echo.

REM 3. Kiem tra ket noi MySQL tren cong 3306 (khong tu dong bat)
echo [1/3] Kiem tra ket noi MySQL XAMPP tren cong 3306...
netstat -an | findstr /R /C:":3306 .*LISTENING" >nul 2>&1
if errorlevel 1 (
    echo [LOI] Khong phat hien MySQL dang chay tren cong 3306!
    echo       Vui long mo !XAMPP_HINT!
    echo       va bam nut [Start] tai dong MySQL truoc khi chay file nay.
    echo.
    pause
    exit /b 1
)
echo       -^> OK: MySQL XAMPP dang hoat dong ^(Cong 3306^).

REM 4. Kiem tra ket noi Apache / phpMyAdmin tren cong 8080 hoac cong 80 mac dinh
echo [2/3] Kiem tra ket noi Apache / phpMyAdmin XAMPP...
set "PMA_URL="
netstat -an | findstr /R /C:":8080 .*LISTENING" >nul 2>&1
if not errorlevel 1 (
    set "PMA_URL=http://127.0.0.1:8080/phpmyadmin"
    set "APACHE_PORT=8080"
) else (
    netstat -an | findstr /R /C:":80 .*LISTENING" >nul 2>&1
    if not errorlevel 1 (
        set "PMA_URL=http://127.0.0.1/phpmyadmin"
        set "APACHE_PORT=80"
    )
)

if not defined PMA_URL (
    echo [LOI] Khong phat hien Apache ^(phpMyAdmin^) dang chay tren cong 8080 hoac 80!
    echo       Vui long mo !XAMPP_HINT!
    echo       va bam nut [Start] tai dong Apache truoc khi chay file nay.
    echo.
    pause
    exit /b 1
)
echo       -^> OK: Apache / phpMyAdmin XAMPP dang hoat dong ^(Cong !APACHE_PORT!^).
echo.

REM 5. Cau hinh ket noi truc tiep vao XAMPP MySQL (khong anh huong file Docker cua nhom)
set "SMARTWMS_WEB_PORT=8088"
set "SPRING_PROFILES_ACTIVE=mysql"
set "SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3306/smartwms?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8"
set "SPRING_DATASOURCE_USERNAME=root"

echo [3/3] Dang khoi dong SmartWMS ket noi truc tiep voi XAMPP MySQL...
echo -------------------------------------------------------------------------
echo   * Thu muc XAMPP          : !XAMPP_DIR!
echo   * Giao dien Web SmartWMS : http://127.0.0.1:8088/auth/login
echo   * Quan ly DB phpMyAdmin  : !PMA_URL! ^(DB: smartwms^)
echo   * Ket noi CSDL MySQL     : 127.0.0.1:3306 ^(User: root, No Password^)
echo   * Nhan Ctrl + C de dung server.
echo -------------------------------------------------------------------------
echo.

call "%MVN_CMD%" spring-boot:run "-Dspring-boot.run.arguments=--spring.datasource.username=root --spring.datasource.password="

pause
