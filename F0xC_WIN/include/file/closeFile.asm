%ifndef CLOSE_FILE
    %define CLOSE_FILE
	
	
%ifndef _CLOSEFILE
	extern _CloseHandle@4
%endif

;1 argument (int filehandle)

;example push handle call closeFile 
;_CloseHandle@4 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
closeFile:
	pop dword [std_addr]
	;pop eax
	;push eax
	call _CloseHandle@4 
	push dword [std_addr]
	ret
	
%endif