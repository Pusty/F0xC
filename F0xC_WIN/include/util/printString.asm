%ifndef PRINT_STR
    %define PRINT_STR
	
	
%ifndef _WRITEFILE
	extern _WriteFile@20
%endif
;2 args (string addr, len)

;example push string \ push 8 \ call printString
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
printString:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop ebx
	pop eax
	push    0
    push    std_read
    push    ebx
    push    eax
    push    dword [std_output]
    call    _WriteFile@20
	push dword [std_addr]
	ret
	
%endif