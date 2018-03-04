%ifndef CODE_BUFFER
    %define CODE_BUFFER
	
;needs a 80*50 byte buffer called "screenback"

%ifndef BUFFER_CONTAINS
	%define BUFFER_CONTAINS
	screenback times 80*50 db 0
%endif

;NOT OPTIMISED
drawBoxBuffer:
drawBox: ;di = color, ax = xpos, bx = ypos, cx = xsize, dx = ysize
	.yloop: ;start yaxisloop
	sub dx , 1 ;make dx 1 smaller
	push ax ;makes copies of ax,bx,cx and dx
	push bx
	push cx
	push dx
		add bx, dx ;adds the current position within the line to the offset
		call writeLineBuffer ;write Line into buffer
	pop dx ;loads dx,cx,bx and ax
	pop cx
	pop bx
	pop ax
	cmp dx , 0 
	jle .outy
	jmp .yloop	
	.outy:
	ret
	
;Draws a image with has 16 colors, COLOR 0xF -> NO COLOR
drawImage: ;di = position of image, ax = xpos, bx = ypos
	push di ;first byte = empty
	push ax
	push bx
	push cx
	push dx
		mov cx, [di] ; sizex = 2nd byte
		;shr cx, 8
			push ax
			mov ax, cx
			mov cl, 8
			shr ax, cl
			mov cx, ax
			pop ax
		inc di
		mov dx, [di] ; sizey = 3rd byte
		;shr dx, 8
			push cx
			mov cl, 8
			shr dx, cl
			pop cx
		inc di
		inc di
		.yloop: ;start yaxisloop
		sub dx , 1 ;make dx 1 smaller
		push ax ;makes copies of ax,bx,cx and dx
		push bx
		push cx
		push dx
		push di
			add bx, dx ;adds the current position within the line to the offset
			call writeImageLine16
		pop di
		pop dx ;loads dx,cx,bx and ax
		pop cx
		pop bx
		pop ax
		cmp dx , 0 
		jle .outy
		jmp .yloop	
		.outy:
	pop dx
	pop cx
	pop bx
	pop ax
	pop di
	ret

;bx offy, cx sizex
;writes a line from a image into the buffer
writeImageLine16:
	push ax
	push cx
	push dx
	push di
		push dx
		push cx
		push bx
		push ax
			push dx
				mov ax, cx
				mov dx, 0d
				mov bx, 2d
				div bx
				mov cx, ax ;divides sizex by 2 because color is saved in half the amount of bytes than its its size
			pop dx
	
			mov ax, dx
			
			mov dx, cx
			mul dx
			mov	dx, ax	
			add di, dx ;adds y offset inside the image
		pop ax
		pop bx
		pop cx
		pop dx		
		mov dx, bx
		mov bx, screenback  ;sets bx to screenback's memory position
		
		;cmp ax, 0 ;NEEDS TO BE DONE FOR EVERYPIXEL
		;jl .end
		;cmp ax, 50*(320d/5d)
		;jge .end
		push ax
			add bx, ax   ;adds x-offset to the buffer
			mov	ax, dx   
			mov dx, 320d/4d ; applies x scaling  
			mul dx
			mov	dx, ax
			add bx, dx   ;adds y-offset to the buffet
		pop ax

		cmp dx, 0
		jl .end
		cmp dx, 50*80
		jge .end
		
		mov dx, ax		
		.loop:
			cmp dx, 80
			jge .ec1
			cmp dx, 0
			jl  .ec1
			
			
			mov ax, [di]
			and al, 0x0F  ;reads the first 4 bits
			cmp al, 0x0F
			je .ec1
			mov [bx], byte al
			.ec1:
			dec cx
			inc bx	
			inc dx
			
			cmp dx, 80
			jge .ec2
			cmp dx, 0
			jl  .ec2
			
			mov al, [di]
			push cx
			mov cl, 4
			shr al, cl
			pop cx
			and al, 0x0F ;reads the last 4 bits
			cmp al, 0x0F
			je .ec2
			mov [bx], al
			.ec2:
			dec cx
			inc bx
			inc dx
			inc di
			cmp cx, 0
			jg .loop
	.end:
	pop di
	pop dx
	pop cx
	pop ax
	ret


flip: ;displays the buffer
	push ds
	push di
	push si
	push dx
	push cx
	push bx
	push ax
	
		mov cx, 320d ;screen x size
		mov dx, 200d ;screen y size
		.yloop: ;start yaxisloop
		sub dx , 1
		push ax
		push cx
		push dx
			call flipLine ;flips one line of the buffer to the mainscreen at the time
		pop dx
		pop cx
		pop ax
			cmp dx , 0 
			jle .outy
			jmp .yloop	
			.outy:
	pop ax
	pop bx
	pop cx
	pop dx
	pop si
	pop di
	pop ds
	ret
	
;cx len, dx posy
;flips one line of the buffer to the screen
;NOT OPTIMIZED AS IT'S NOT WORKING CORECTLY
flipLine:
	push ds ;makes a copy of ds
	push di
	push si
	push ax
	push bx
	push cx
	push dx
		
		mov si, screenback ;load the position of screenback into si
		
		push dx
		push cx
			mov ax, dx ;Divides dx  by the y scaling (4x)
			mov dx, 0d
			mov bx, 4d ;SCALING Y SIDE
			div bx
			mov dx, 320d/4d  ;sets dx into 320d (xsize of display) / (SCALING X)
			mul dx		  ;multiplies dx with ax and saves it into ax (ax = 320d*dx)
			mov	dx, ax	  ;saves ax into dx	
			add si, dx ;adds yoffset to si to set the screenpos to the right y pos within the buffer
		pop cx
		pop dx
		
		mov	ax, dx     ;moves dx into ax
		mov dx, 320d  ;sets dx into 320d (xsize of display)
		mul dx		  ;multiplies dx with ax and saves it into ax (ax = 320d*dx)
		mov	dx, ax	  ;saves ax into dx	

		mov di, ds ;saves current ds into di
		
		mov ax, 0d
		
		.loop:
		push cx
			mov cl, [si]; sets color from the current si pos into al
			mov bx, 0A000h ;sets bx to the position of the graphic memory
			mov ds, bx ;sets ds to the graphic memory position
			mov bx, dx ;sets bx to the (now) general offset
			mov [bx], cl ;sets the color saved in al into the graphic memory on the position dx
		pop cx
		mov ds, di ;sets ds to the state before
		dec cx ;one runthrough of the length of a line is done , repeat till cx == 0
		inc dx ;increase the graphic memory offset by one
		inc ax ;increase scaling index
		cmp ax, 4 ;SCALING X-SIZE (4x)
		jge .incSi
		cmp cx, 0 ;checks cx 
		jg .loop ;if the line hasn't the size cx (at the start) then repeat
		jmp .end
		.incSi:
		mov ax, 0 ;reset scaling index
		inc si ;increase the buffer memory offset by one
		cmp cx, 0 ;checks cx 
		jg .loop ;if the line hasn't the size cx (at the start) then repeat
		jmp .end
		.end:
	pop dx
	pop cx
	pop bx
	pop ax
	pop si
	pop di
	pop ds
	ret
	
resetBuffer: ;resets the buffer
	push di
	push ax
	push bx
	push cx
	push dx
		mov di, 0000b ;color cyan
		mov ax, 0d ;0 x offset
		mov bx, 0d ;0 y offset
		mov cx, 80d ;xsize of the screen
		mov dx, 50d ;ysize of the screen
		call drawBoxBuffer ;draws the reset screen into the buffer
	pop dx
	pop cx
	pop bx
	pop ax
	pop di
	ret
;bx offy, cx sizex
;writes a line into the buffer
writeLineBuffer:
	push ax
	push cx
	push dx
	push di
		
		mov dx, bx
		mov bx, screenback  ;sets bx to screenback's memory position
		
		add bx, ax   ;adds x-offset
		mov	ax, dx   
		mov dx, 320d/4d ; applies x scaling  
		mul dx
		mov	dx, ax	
			
		add bx, dx   ;adds y-offset
		
		.loop:
		mov ax, di
		mov [bx], al ;writes the color from di into the right buffer position
		dec cx
		inc bx
		cmp cx, 0
		jg .loop
		
	pop di
	pop dx
	pop cx
	pop ax
	
	ret

%endif
