%ifndef LOAD_PAL
    %define LOAD_PAL
	
;si = palette
loadPal: ;loads a palette with 16 colors 
	mov ax, 0
	.loop:
    mov bx,ax ;index of color (0-255)
    mov dh,[si];red (0-63)
	inc si
    mov ch,[si];green (0-63)
	inc si
    mov cl,[si];blue (0-63)
	inc si
	push ax
	mov ax,1010h ;interrupt subfunction
	int 0x10
	pop ax
	add ax, 1
	cmp ax, 15
	jge .end
	jmp .loop
	.end:
	ret

%endif