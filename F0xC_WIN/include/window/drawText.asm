%ifndef DRAW_TEXT
    %define DRAW_TEXT
	
	
%ifndef _WINDOWUTIL_5
	extern _GetDC@4
	extern _DrawMenuBar@4
	extern _LoadIconA@8
	extern _DrawIcon@4
%endif
;1 args (int addr)

;example push addr / call drawText
;user32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
drawText:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
		call _GetDC@4
	;mov [std_tmp], eax
		push eax
		push 32
		push 32
		
		;; LoadIcon(0, IDI_APPLICATION) where IDI_APPLICATION is equal to 32512. 
	push dword 32512 
	push dword 0 
	call _LoadIconA@8
	
		push eax
		call _DrawIcon@4
	push dword [std_addr]
	ret
	
%endif