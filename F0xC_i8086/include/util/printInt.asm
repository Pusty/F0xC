%ifndef PRINT_INT
    %define PRINT_INT
	
%include "printChar.asm"
%include "mathPow.asm"
	
printInt: ;print stuff in si to the screen as a number in the decimal system
	;al -> printChar
	call lengthInt
	.loop:
		call printIntDiv
		push dx
		push bx
		sub bx, 1
		mov ax, 10
		call pow16
		pop bx
		pop dx
		mov cx, ax
		mov ax, dx
		xor dx, dx
		div cx
		add al, '0'
		push bx
		call printChar
		pop bx
		sub bx, 1
		cmp bx, 0
			jle .end
	jmp .loop
	.end:	
	ret

lengthInt: ;returns length of integer si into bx
	mov bx, 0
	.loop:
	call printIntDiv
	cmp ax, 0
		je .end
	add bx, 1
	jmp .loop
	.end:
	ret

printIntDiv:
	mov ax, 10
	call pow16
	mov cx, ax
	mov ax, si
	xor dx, dx
	div cx
	ret

%endif