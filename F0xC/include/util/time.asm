%ifndef TIME
    %define TIME
	
time:
	mov eax, 0
	mov ah, 00h
	int 1ah
	;mov ax, dx
	;shl eax, 16
	mov ax, dx
	;mov ax, 0
	;mov al, dl
	ret

%endif
