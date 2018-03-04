@echo off
echo Creating Bootable Kernel
echo.

cd /d %~dp0
taskkill /F /im  qemu-system-i386.exe

"%~dp0\..\private\nasm\nasm.exe" -fbin bootloader.asm -o bootloader.bin
copy /b bootloader.bin + output.bin kernel.bin
"%~dp0\..\private\qemu\qemu-system-i386.exe" "kernel.bin"