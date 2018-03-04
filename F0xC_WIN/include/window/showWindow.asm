%ifndef SHOW_WINDOW
    %define SHOW_WINDOW
	
	
%ifndef _WINDOWUTIL_2
	extern _ShowWindow@8
%endif
;1 args (int addr)

;example push string\ call setTitle
;_SetConsoleTitleA@4 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
showWindow:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
		pop dword [std_tmp]
		push dword 10
		push dword [std_tmp] 
		call _ShowWindow@8
	push dword [std_addr]
	ret
	
%endif