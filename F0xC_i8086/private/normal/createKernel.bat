@echo off
echo Creating Bootable Kernel
echo.

cd /d %~dp0
taskkill /F /im  qemu-system-i386.exe

nasm -fbin bootloader.asm -o bootloader.bin
copy /b bootloader.bin + output.bin kernel.bin
"C:\Program Files (x86)\qemu\qemu-system-i386.exe" "kernel.bin"