%ifndef PRINT_INT
    %define PRINT_INT
	
;%include "printChar.asm"
;%include "mathPow.asm"

;1 args (dword)

;example push dword 12888 call printInt
;_WriteFile@20 in kernel32
;std_addr = ADDR TEMP STORAGE
;std_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE

printInt: ;print stuff  to the screen as a number in the decimal system
	pop dword [std_addr+4]
	pop esi
	call lengthInt
	.loop:
		call printIntDiv
		push edx
		push ebx
		sub ebx, 1
		mov eax, 10
		call mathPow
		pop ebx
		pop edx
		mov ecx, eax
		mov eax, edx
		xor edx, edx
		div ecx
		add ax, '0'
		push eax
		mov [std_tmp+4], ebx
		call printChar
		mov ebx, [std_tmp+4]
		sub ebx, 1
		cmp ebx, 0
			jle .end
	jmp .loop
	.end:	
	push dword [std_addr+4]
	ret

lengthInt: ;returns length of integer esi into ebx
	mov ebx, 0
	.loop:
	call printIntDiv
	cmp eax, 0
		je .end
	add ebx, 1
	jmp .loop
	.end:
	ret

printIntDiv:
	mov eax, 10
	call mathPow
	mov ecx, eax
	mov eax, esi
	xor edx, edx
	div ecx
	ret

%endif