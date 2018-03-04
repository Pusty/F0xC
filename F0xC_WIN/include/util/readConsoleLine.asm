%ifndef READ_CONSOLE_LINE
    %define READ_CONSOLE_LINE
	
	
%ifndef _READCONSOLELINE
	extern _GetCommandLineA@16
%endif
;0 args 
;returns pointer to commandline string

;example call readConsoleLine
readConsoleLine:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)
    call _GetCommandLineA@16
	push dword [std_addr]
	ret
	
%endif