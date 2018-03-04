%ifndef PRINT_NLINE
    %define PRINT_NLINE
	
newLine:
	;well this was in the code sample and I don't know what it does so I excluded it.
    mov al, 0	; null terminator '\0'
    stosb       ; Store string 
    ;Adds a newline break '\n'
    mov ah, 0x0E ;sets command to draw a char
    mov al, 0x0D ;sets a char to al
    int 0x10	 ;draw the char in al
    mov al, 0x0A ;sets a new char to al
    int 0x10	 ;draws it again
	ret	 		 ;function ends

%endif