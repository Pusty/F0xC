%ifndef PRINT_STR
    %define PRINT_STR
	
;2 args (string addr, len)

;example push string \ push 8 \ call printString
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
printString:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop ebx
	pop eax
	push edx
	push ecx
	

	mov edx, ebx
	mov ecx, eax
	mov ebx, 1
	mov eax, 4
	int 0x80
	
	pop ecx
	pop edx
	push dword [std_addr]
	ret
	
%endif