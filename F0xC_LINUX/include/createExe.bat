@echo off
echo Creating Executable
echo.

cd /d %~dp0
rem taskkill /F /im  output.exe

rem ld -o output.exe output.o -e_main -lkernel32 --enable-stdcall-fixup %SYSTEMROOT%\system32\kernel32.dll -luser32 --enable-stdcall-fixup %SYSTEMROOT%\system32\user32.dll
rem gcc -o output.exe output.o -e_main -lkernel32 --enable-stdcall-fixup %SYSTEMROOT%\system32\kernel32.dll
rem "C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\link.exe" output.o /out:output8.exe /entry:main /defaultlib:"C:\Program Files (x86)\Microsoft SDKs\Windows\v7.1A\Lib\kernel32.lib"
echo ====================
echo.
rem output.exe
echo.
echo ====================