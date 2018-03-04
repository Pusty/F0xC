%ifndef PRINT_CHAR
    %define PRINT_CHAR
	
%ifndef _WRITEFILE
	extern _WriteFile@20
%endif
	
;1 args (byte)

;example  push '0' \ call printChar
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
printChar:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop eax
	mov [std_tmp], eax
	push    0
    push    std_read
    push    1
    push    std_tmp
    push    dword [std_output]
    call    _WriteFile@20
	push dword [std_addr]
	ret
%endif