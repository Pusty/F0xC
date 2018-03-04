%ifndef UPDATE_WINDOW
    %define UPDATE_WINDOW
	
	
%ifndef _WINDOWUTIL_3
	extern _UpdateWindow@4
%endif
;1 args (int addr)

;example push string\ call setTitle
;_SetConsoleTitleA@4 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
updateWindow:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
		call _UpdateWindow@4
	push dword [std_addr]
	ret
	
%endif