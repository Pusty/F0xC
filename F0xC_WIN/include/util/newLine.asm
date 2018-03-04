%ifndef PRINT_NLINE
    %define PRINT_NLINE
	
%ifndef _WRITEFILE
	extern _WriteFile@20
%endif

;0 args ()

;example call newLine
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
newLine:
	pop dword [std_addr]
	mov [std_tmp], byte 0x0D
	mov [std_tmp+1], byte 0x0A
	push    0
    push    std_read
    push    2
    push    std_tmp
    push    dword [std_output]
    call    _WriteFile@20
	push dword [std_addr]
	ret

%endif