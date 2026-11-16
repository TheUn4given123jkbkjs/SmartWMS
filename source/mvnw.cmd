@echo off
set "MAVEN_BIN=C:\Users\ACER_LAPTOP\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin"
if exist "%MAVEN_BIN%\mvn.cmd" (
    set "PATH=%MAVEN_BIN%;%PATH%"
    call "%MAVEN_BIN%\mvn.cmd" %*
) else (
    echo [ERROR] Maven not found at %MAVEN_BIN%
    exit /b 1
)
