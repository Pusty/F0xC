%include "SECTS"
org 0x7C00
jmp start
nop
;------------------------------------------; 
;  Standard BIOS Parameter Block, "BPB".   ; 
;------------------------------------------; 
bpbOEM db 'F0xC'
bpbSectSize dw 512
bpbClustSize db 1 
bpbReservedSec dw 1 
bpbFats db 2 
bpbRootSize dw 224 
bpbTotalSect dw 2880
bpbMedia db 240 
bpbFatSize dw 9 
bpbTrackSect dw 18 
bpbHeads dw 2  
bpbHiddenSect dd 0
bpbLargeSect dd 0
;-------------------------------------;
; extendet BPB for fat12/fat16        ;
;-------------------------------------;
bpbDriveNo db 0
bpbReserved db 0
bpbSignature db 41 
bpbID dd 0
bpbVolumeLabel db 'F0xC'
bpbFileSystem db 'FAT12'
;**************************** 
; Realmode startup code. 
;**************************** 
start: 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; setup a stack and segment regs ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    xor ax, ax
    mov ds, ax
    mov es, ax
    mov ss, ax
    mov sp, ax

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; read kernel from floppy disk ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    mov [bootdrive], dl ; boot drive stored by BIOS in DL.
                        ; we save it to [bootdrive] to play for safety.

load_kernel:
    xor ax, ax         ; mov ax, 0  => function "reset"
    int 0x13         
    jc load_kernel     ; trouble? try again


    ; set parameters for reading function
    ; 8-bit-wise for better overview
    mov dl,[bootdrive] ; select boot drive

    mov bx, 0x7E00    ; set up start address of kernel
	
	mov al, SECTORS     ;read sectors
    mov ch, 0          ; cylinder = 0
    mov cl, 2          ; sector   = 2
    mov dh, 0          ; head     = 0
    mov ah, 2          ; function "read" 

    int 0x13    							
	jc load_kernel     ; trouble? try again
	
    ; show loading message

	mov dl,[bootdrive]
	
	jmp 0x7E00;jump to kernel
	
	mov si, failmsg
	call pS
		
	ret
pC:
	mov ah, 0x0E ;tells bios to print 1 char
	int 0x10 ;do stuff!
	ret	
pS:
	push ax
	push bx
	push cx
	push dx
	mov bh, 0x00 ;page 0
	mov bl, 0x07 ;text attribute
	nextChar:
		lodsb 
		or al, al
		jz nextChar.end ;if the string ends then end this function
		jmp nextChar.printChar;print the current char by defaultter 
		.printChar: ;print a char
			call pC
		.skipChar: ;skip a char
			jmp nextChar ;repeat
	.end:
	pop dx
	pop cx
	pop bx
	pop ax
	ret
		
failmsg dw "F0xC Error...",0
bootdrive db 0    ; boot drive
;fills rest of sector
times 510-($-$$) db 5
;signes with codes to say that it's a bootloader

db 0x55
db 0xAA
eof: