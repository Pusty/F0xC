%ifndef LENGTH_FILE
    %define LENGTH_FILE_FILE
	
	
%ifndef _LENGTH_FILEFILE
	extern _GetFileSize@8
%endif

;1 argument (int filehandle)
;returns length in bytes

;example push handle call lengthFile
;_GetFileSize@8 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
lengthFile:
	pop dword [std_addr]
	pop eax
	push dword 0 
	push eax
	call _GetFileSize@8
	push dword [std_addr]
	ret
	
%endif