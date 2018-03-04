%ifndef INIT_GRAPHICS
    %define INIT_GRAPHICS
	
initGraphics:
	mov ah, 0 ;set display mode
	mov al, 13h ;13h = 320x200
	int  0x10
	ret
	
%endif