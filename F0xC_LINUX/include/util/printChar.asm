%ifndef PRINT_CHAR
    %define PRINT_CHAR
	
;1 args (byte)

;example  push '0' \ call printChar
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
printChar:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop eax
	push edx
	push ecx
	push ebx
	
	mov [std_tmp], eax
	
	mov edx, 1
	mov ecx, std_tmp
	mov ebx, 1
	mov eax, 4
	int 0x80
	
	pop ebx
	pop ecx
	pop edx
	push dword [std_addr]
	ret
%endif