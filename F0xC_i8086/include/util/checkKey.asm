%ifndef CHECK_KEY
    %define CHECK_KEY

checkKey:
	cmp ax, 0
	jne .next
	in al, 0x60
	mov [.last], al
	and al, 01111111b
	mov ah, 0
	ret
	.next:
	mov al, [.last]
		push cx
		mov cl, 7
		shr al, cl
		pop cx
	and al, 00000001b
	xor al, 00000001b
	mov ah, 0
	ret
	mov ax, 0
	mov ah, 1
	int 16h
	jz .nokey
	mov ax, 0
	int 16h
	ret
	.nokey:
		mov ax, 0
		ret
	.last dw 0

%endif
