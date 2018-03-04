%ifndef CHECK_KEY
    %define CHECK_KEY

%include "initGraphics.asm"	

memoryDump:
	call initGraphics
	mov si,0
	loo:
		mov al, [si] ;sets color
		mov ah, 0x0C
		mov bh, 0d
		mov cx, si
		mov dx, 0d
		int 0x10
		inc si
		cmp si,320*200
	jne loo
	ret
	
%endif