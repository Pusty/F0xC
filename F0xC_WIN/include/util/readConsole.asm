%ifndef READ_CONSOLE
    %define READ_CONSOLE
	
	
%ifndef _READCONSOLE
	extern _ReadConsoleA@16
%endif
;2 args (buffer address, input length)

;example push buffer \ push 8 \ call readConsole
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
readConsole:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop eax
	pop ecx
	mov [ecx], eax
	add ecx, 4
	push 0x80000000
    push std_read
    push eax
    push ecx
    push dword [std_input]
    call _ReadConsoleA@16
	push dword [std_addr]
	ret
	
%endif