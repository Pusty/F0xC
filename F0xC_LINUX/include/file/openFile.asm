%ifndef OPEN_FILE
    %define OPEN_FILE
	
	
%ifndef _OPENFILE
	extern _OpenFile@12
%endif

;2 args (string filename, int mode)
;returns the handle

;example push filename push 0x1001 call openFile 
;_OpenFile@12 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
openFile:
	pop dword [std_addr]
	pop eax
	pop ecx
	enter 136, 0
	lea ebx, [ebp-136]                ;; The address of the OFSTRUCT structure. 
	push eax
	;push dword 0 ;; OF_READ
	;push dword 1 ;; OF_WRITE
	;push dword 0x1000 ;; OF_CREATE
	;push dword 0x0002 ;; OF_READWRITE
	push ebx 
	push ecx
	call _OpenFile@12 
	;mov dword [HANDLE], eax            ;; Save the file handle. 
	leave
	push dword [std_addr]
	ret
	
%endif