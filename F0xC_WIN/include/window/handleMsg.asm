%ifndef HANDLE_MSG
    %define HANDLE_MSG
	
	
%ifndef _WINDOWUTIL_4
	extern _GetMessageA@16
	extern _TranslateMessage@4
	extern _DispatchMessageA@4
%endif
;1 args (byte[28] pointer)
;returns msg

;example push msg / call handleMsg
;user32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
handleMsg:
	pop dword [std_addr]
		pop dword [std_tmp]
        ;; GetMessage(the MSG structure, 0, 0, 0) 
        push dword 0 
        push dword 0 
        push dword 0 
		mov ebx, [std_tmp]
		add ebx, 4
        push ebx 
        call _GetMessageA@16 
        ;; If GetMessage() returns 0, it's time to exit. 
        cmp eax, 0 
        jz .MessageLoopExit 
        
        ;; TranslateMessage(the MSG) 
		mov ebx, [std_tmp]
		add ebx, 4
        push ebx 
        call _TranslateMessage@4 
        
        ;; DispatchMessage(the MSG) 
		mov ebx, [std_tmp]
		add ebx, 4
        push ebx 
        call _DispatchMessageA@4 
		mov eax, 1
        
        ;; And start the loop over again. 
    .MessageLoopExit: 
	push dword [std_addr]
	ret
	
%endif