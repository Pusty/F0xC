%ifndef RESET_CURSOR
    %define RESET_CURSOR
		
resetCursor:
	mov ah, 0x02
	mov bh, 0x00
	mov dh, 0x00
	mov dl, 0x00
	int 0x10
	ret

%endif