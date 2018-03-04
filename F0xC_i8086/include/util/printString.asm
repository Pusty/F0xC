%ifndef PRINT_STR
    %define PRINT_STR
	
%include "printChar.asm"
	
printString:
	mov bh, 0x00 ;page 0
	mov bl, 0x07 ;text attribute
	nextChar:
		lodsb  ; mov al, [si] + inc si
		or al, al
		jz nextChar.end ;if the string ends then end this function
		jmp nextChar.printChar;print the current char by defaultter 
		.printChar: ;print a char
			call printChar
		.skipChar: ;skip a char
			jmp nextChar ;repeat
	.end:
	ret
	
%endif