@echo off
echo NASM Kernel
echo.

cd /d %~dp0
nasm -felf32 %1 -o %2