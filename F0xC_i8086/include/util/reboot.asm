%ifndef REBOOT
    %define REBOOT

reboot: ;sends program to the end of memory which should cause a reboot
	db 0x0ea 
    dw 0x0000 
    dw 0xffff
	ret
	
%endif