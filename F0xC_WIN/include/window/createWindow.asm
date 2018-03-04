%ifndef CREATE_WINDOW
    %define CREATE_WINDOW
	
	
%ifndef _WINDOWUTIL_1
	extern _CreateWindowExA@48
%endif
;2 args (string classname, string windowname)

;example push classname \ push windowname \ call createWindow
;user32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
createWindow:
	pop dword [std_addr]
		pop dword [std_tmp+4]
		pop dword [std_tmp]
		 ;; CreateWindowEx(0, ClassName, window title, WS_OVERLAPPEDWINDOW, x, y, width, height, handle to parent window, handle to menu, hInstance, NULL); 
		push dword 0
		push dword [std_handle]
		push dword 0 
		push dword 0 
		push dword 400              ;; 400 pixels high. 
		push dword 500              ;; 500 pixels wide. 
		push dword 0x80000000       ;; CW_USEDEFAULT 
		push dword 0x80000000       ;; CW_USEDEFAULT 
		push dword 0x00 | 0xC00000 | 0x80000 | 0x40000 | 0x20000 | 0x10000    ;; WS_OVERLAPPEDWINDOW 
                                ;; WS_OVERLAPPEDWINDOW = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX 
		push dword [std_tmp+4]  ;ApplicationName
		push dword [std_tmp] ;ClassName
		push dword 0 
		call _CreateWindowExA@48
		;;HANDLE IS NOW IN EAX
		;push eax
	push dword [std_addr]
	ret
	
%endif