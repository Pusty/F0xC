%ifndef READ_FILE
    %define READ_FILE
	
	
%ifndef _READFILE
	extern _ReadFile@20
%endif

;3 args (int fileHandle, int bufferAddr, int bufferLen)
;returns the handle

;example push handle push buffer push 1024 call readFile 
;_ReadFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
readFile:
	pop dword [std_addr]
	pop edx
	pop ecx
	pop ebx
	mov [ecx], edx
	add ecx, 4
	push dword 0 
	push std_read
	push edx ;READ SIZE
	push ecx ;BUFFER ADDRESS
	push ebx
	call _ReadFile@20
	push dword [std_addr]
	ret
	
%endif