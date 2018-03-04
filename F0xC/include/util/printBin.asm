%ifndef PRINT_BIN
    %define PRINT_BIN
	
%include "printChar.asm"
	
printBin: ;prints least significant byte of si in binary notation
	mov bh, 0x00 ;page 0
	mov bl, 0x07 ;text attribute
	mov cx, si ;makes a working copy of si into bx
	xor dh,dh;setcounter cl to 0
	.nextDig: ;for (int i=0;i<8;i++)
		mov ch, cl ; copy bl to bh to work with it
		shr ch,7 ;make hsb to lsb
		and ch,00000001b
		add ch, '0' ;make an '0' or '1' out of it
		mov al, ch ;saves bh into al for the print function
		call printChar ;prints the char
		shl cl,1;shift to the next bit
		inc dh ;incement the counter
		cmp dh,8 ;if we havent print all 8 digits...
		jl printBin.nextDig ;...loop
	mov al, 'b' ;print postfix 'b'
	call printChar ;prints the char
	ret
%endif