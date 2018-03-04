%ifndef PRINT_CHAR
    %define PRINT_CHAR
printChar:
	mov ah, 0x0E ;tells bios to print 1 char
	mov bh, 0x00 ;page 0
	mov bl, 0x07 ;text attribute
	int 0x10 ;do stuff!
	ret
%endif