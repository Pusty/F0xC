%ifndef PRINT_HEX
    %define PRINT_HEX
	
%include "printChar.asm"

printHex: ;args si = full hex num with \n
	mov bh, 0x00 ;page 0
	mov bl, 0x07 ;text attribute
	mov al, '0'
	call printChar
	mov al, 'x'
	call printChar
	call printHexHigher
	call printHexLower
	mov al, ' '
	call printChar
	;call newLine
	ret
printHexLower: ;args si = hexnumber (least significant byte only) ;it's colored because cx is the color register
	push ax
	push bx
	push cx
	push dx
	jmp printHexLower.end ;jumps to the end
	.func: ;actual stuff (sub function)
	mov cx, si ;makes a working copy of si into cx
	and cl, 11110000b ;sets 4 least significant bit  to 0
	shr cl, 4d ;shifts bytes to the left by 4 to get a number between 1 and 16
	cmp cl, 10d ;compares the number with the number 10
	jl printHexLower.case1 ;ifs the number is lower than 10 print out a number char
	;case2: ;if the number is 10 or higher make it a letter
	add cl, 'A'-10 ;sets 10 to a 11 to b 12 to c and so on
	mov al, cl ;saves cx into ax for the print function
	call printChar ;prints the char
	ret ;returns this subfunction
	.case1: ;if the number is lower than 10 make it a number
	add cl, '0' ;sets cl when dec 1 to ascii '1' and so on
	mov al, cl ;moves cx into ax for the print function 
	call printChar ;prints the char
	ret ;returns this subfunction
	.end: ;end of the subfunction
	call .func ;calls the subfunctions
	mov di ,si
	shl si, 4 ;shifts bytes of si by 4 to the left to get the secound part
	call .func ;calls the subfunction again for the secound part
	mov si, di
	
	pop dx
	pop cx
	pop bx
	pop ax
	ret
printHexHigher: ;args si = hexnumber (most significant byte only) ;it's colored because bx is the color register
	push ax
	push bx
	push cx
	push dx
	jmp printHexHigher.end ;jumps to the end
	.func: ;actual stuff (sub function)
	mov cx, si ;makes a working copy of si into cx
	and ch, 11110000b ;sets 4 least significant bit  to 0
	shr ch, 4d ;shifts bytes to the left by 4 to get a number between 1 and 16
	cmp ch, 10d ;compares the number with the number 10
	jl printHexHigher.case1 ;ifs the number is lower than 10 print out a number char
	;case2: ;if the number is 10 or higher make it a letter
	add ch, 'A'-10 ;sets 10 to a 11 to b 12 to c and so on
	mov al, ch ;saves cx into ax for the print function
	call printChar ;prints the char
	ret ;returns this subfunction
	.case1: ;if the number is lower than 10 make it a number
	add ch, '0' ;sets ch when dec 1 to ascii '1' and so on
	mov al, ch ;moves cx into ax for the print function 
	call printChar ;prints the char
	ret ;returns this subfunction
	.end: ;end of the subfunction
	mov di, si
	call .func ;calls the subfunction
	shl si, 4 ;shifts bytes of si by 4 to the left to get the secound part
	call .func ;calls the subfunction again for the secound part
	mov si, di
	
	pop dx
	pop cx
	pop bx
	pop ax
	ret
%endif