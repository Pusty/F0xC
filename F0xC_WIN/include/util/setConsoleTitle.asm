%ifndef SET_CONSOLE_TITLE
    %define SET_CONSOLE_TITLE
	
	
%ifndef _SETCONSOLETITLE
	extern _SetConsoleTitleA@4
%endif
;1 args (string addr)

;example push string\ call setTitle
;_SetConsoleTitleA@4 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
setConsoleTitle:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop eax
	push eax
    call    _SetConsoleTitleA@4
	push dword [std_addr]
	ret
	
%endif