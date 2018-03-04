%ifndef SET_OUTPUT
    %define SET_OUTPUT
	
	
%ifndef _STDHANDLE
	extern _GetStdHandle@4
%endif

;1 arg (handle[0 = STD HANDLE])

;example push 0 call setOutput
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
setOutput:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
	pop ebx
	cmp ebx, 0
	je .zero
	.notzero:
		mov [std_output], eax
	jmp .end
	.zero:
		push dword -11
		call _GetStdHandle@4
		mov [std_output], eax
	.end:	
	push dword [std_addr]
	ret
	
%endif