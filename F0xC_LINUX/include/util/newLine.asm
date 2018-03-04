%ifndef PRINT_NLINE
    %define PRINT_NLINE
	

;0 args ()

;example call newLine
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
newLine:
	pop dword [std_addr]

	push eax
	push ebx
	push ecx
	push edx
	
	mov [std_tmp], byte 0x0D
	mov [std_tmp+1], byte 0x0A
	mov edx, 2
	mov ecx, std_tmp
	mov ebx, 1
	mov eax, 4
	int 0x80
	
	pop edx
	pop ecx
	pop ebx
	pop eax
	
	push dword [std_addr]
	ret

%endif