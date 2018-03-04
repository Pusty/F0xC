CPU 8086
%ifndef MATH_POW
    %define MATH_POW
pow16: ; ax ^ bx = ax (bx>=0)
	mov cx, ax
	mov dx, bx
	cmp dx, word 0
		jle .end1
	.loop:
		sub dx, word 1
		cmp dx, word 0
		je .end0
		
			;imul ax, cx
		push dx
		imul cx ;same as above but for 8086
		pop dx
		
	jmp .loop
	.end1:
		mov ax, word 1
		ret
	.end0:
		ret
		
pow8: ; al ^ bl = al (bl>=0)
	mov cl, al
	cmp bl, byte 0
		jle .end1
	.loop:
		sub bl, byte 1
		cmp bl, byte 0
		je .end0
		mov dl, cl
		mul dl
	jmp .loop
	.end1:
		mov al, byte 1
		ret
	.end0:
		ret
%endif