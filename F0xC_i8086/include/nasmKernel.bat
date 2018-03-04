@echo off
echo NASM Kernel
echo.

cd /d %~dp0
nasm -fbin %1 -o %2