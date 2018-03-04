@echo off
echo NASM Kernel
echo.

cd /d %~dp0
"%~dp0\..\private\nasm\nasm.exe" -fbin %1 -o %2