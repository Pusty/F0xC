@echo off
echo NASM Kernel
echo.

cd /d %~dp0
nasm -fwin32 %1 -o %2