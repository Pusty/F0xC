%ifndef CREATE_WINDOW_CLASS
    %define CREATE_WINDOW_CLASS
	
	
%ifndef _WINDOWUTIL_0
	extern _LoadIconA@8
	extern _LoadCursorA@8
	extern _RegisterClassExA@4
	extern _PostQuitMessage@4
	extern _DefWindowProcA@16
%endif
;3 args (name addr)

;example push string \call createWindowClass
;user32
;std_addr = ADDR TEMP STORAGE
;str_tmp = 16 byte reserved
;std_handle = STD OUTPUT HANDLE
createWindowClass:
	pop dword [std_addr] ;this is apparently the address, so let's just save it and push it back later :)

	pop dword [std_tmp] ;string (class name)
	
	;push dword 10 	;;SW_SHOWDEFAULT
	;push dword [std_cmd] ;;COMMAND LINE HANDLE
	;push dword 0 	;;NULL?
	;push dword [std_handle]  ;;MODULE HANDLER
	call windowMain
	push dword [std_addr]
	ret
	
windowMain:
	;mov eax, dword [std_tmp+4] ;class structure
    enter 48, 0 
    
    ;; We need to fill out the WNDCLASSEX structure, now. 
    lea ebx, [ebp-48]  
	;lea ebx, [eax] ;;WNDCLASSEX structure.
	;mov ebx, eax
	
	mov dword [ebx+00], 48      ;; Offset 00 is the size of the structure. 
	mov dword [ebx+04], 3       ;; Offset 04 is the style for the window. 3 is equal to CS_HREDRAW | CS_VREDRAW 
	;mov eax, dword [std_tmp+4] ;pointer to function
	mov eax, WindowProcedure
	mov dword [ebx+08], eax        ;; Offset 08 is the address of our window procedure. 
	mov dword [ebx+12], 0       ;; I'm not sure what offset 12 and offset 16 are for.
	mov dword [ebx+16], 0       ;; But I do know that they're supposed to be NULL, at least for now. 
	
	mov esi, dword [std_handle]      ;; We load the hInstance value. 
	mov dword [ebx+20], esi     ;; Offset 20 is the hInstance value. 
	
	mov dword [ebx+32], 5 + 1   ;; Offset 32 is the handle to the background brush. We set that to COLOR_WINDOW + 1. 
	mov dword [ebx+36], 0       ;; Offset 36 is the menu name, what we set to NULL, because we don't have a menu.
	
	mov eax, dword [std_tmp] ;string (name for the class)
	mov dword [ebx+40], eax              ;; Offset 40 is the class name for our window class. 
	;; Note that when we're trying to pass a string, we pass the memory address of the string, and the 
	;; function to which we pass that address takes care of the rest. 
	
	;; LoadIcon(0, IDI_APPLICATION) where IDI_APPLICATION is equal to 32512. 
	push dword 32512 
	push dword 0 
	call _LoadIconA@8
	
	;; All Win32 API functions preserve the EBP, EBX, ESI, and EDI registers, so it's 
	;; okay if we use EBX to store the address of the WNDCLASSEX structure, for now. 
	
	mov dword [ebx+24], eax     ;; Offset 24 is the handle to the icon for our window. 
	mov dword [ebx+44], eax     ;; Offset 44 is the handle to the small icon for our window. 
	
	;; LoadCursor(0, IDC_ARROW) where IDC_ARROW is equal to 32512. 
	push dword 32512 
	push dword 0 
	call _LoadCursorA@8
	
	mov dword [ebx+28], eax     ;; Offset 28 is the handle to the cursor for our window.
	
	push ebx
	call _RegisterClassExA@4
	leave
ret


;; We also need a procedure to handle the events that our window sends us. 
;; We call that procedure WindowProcedure(). 
;; It also has to take 4 arguments, which are as follows: 
;;    hWnd             The handle to the window that sent us that event. 
;;                     This would be the handle to the window that uses 
;;                     our window class. 
;;    uMsg             This is the message that the window sent us. It 
;;                     describes the event that has happened. 
;;    wParam           This is a parameter that goes along with the 
;;                     event message. 
;;    lParam           This is an additional parameter for the message. 
;; If we process the message, we have to return 0. 
;; Otherwise, we have to return whatever the DefWindowProc() function 
;; returns. DefWindowProc() is kind of like the "default window procedure" 
;; function. It takes the default action, based on the message. 
;; For now, we only care about the WM_DESTROY message, which tells us 
;; that the window has been closed. If we don't take care of the 
;; WM_DESTROY message, who knows what will happen. 
;; Later on, of course, we can expand our window to process other 
;; messages too. 
WindowProcedure: 
    ;; We don't really need any local variables, for now, besides the function arguments. 
    enter 0, 0 
    
    ;; We need to retrieve the uMsg value. 
    mov eax, dword [ebp+12]           ;; We get the value of the second argument. 
    
    ;; Now here comes the new instruction. We need to compare the value we just 
    ;; retrieved to WM_DESTROY to see if the message is a WM_DESTROY message. 
    ;; If so, we'll jump to the .window_destroy label. 
    cmp eax, 2                      ;; Compare EAX to WM_DESTROY, which is equal to 2. 
    jz .window_destroy               ;; If it's equal to what we compared it to, jump to 
                                    ;; the .window_destroy label. 
    ;; If the processor doesn't jump to the .window_destroy label, it means that 
    ;; the result of the comparison is not equal. In that case, the message 
    ;; must be something else. 
    ;; In cases like this we can either take care of the message right now, or 
    ;; we can jump to another location in the code that would take care of the 
    ;; message. 
    ;; We'll just jump to the window_default label. 
    jmp .window_default 
    
    ;; We need to define the .window_destroy label, now. 
    .window_destroy: 
        ;; If uMsg is equal to WM_DESTROY (2), then the processor will execute this 
        ;; code next. 
        
        ;; We pass 0 as an argument to the PostQuitMessage() function, to tell it 
        ;; to pass 0 as the value of wParam for the next message. At that point, 
        ;; GetMessage() will return 0, and the message loop will terminate. 
        push dword 0 
        ;; Now we call the PostQuitMessage() function. 
        call _PostQuitMessage@4
        
        ;; When we're done doing what we need to upon the WM_DESTROY condition, 
        ;; we need to jump over to the end of this area, or else we'd end up 
        ;; in the .window_default code, which won't be very good. 
        jmp .window_finish 
    ;; And we define the .window_default label. 
    .window_default: 
        ;; Right now we don't care about what uMsg is; we just use the default 
        ;; window procedure. 
        
        ;; In order for use to call the DefWindowProc() function, we need to 
        ;; pass the arguments to it. 
        ;; It's arguments are the same as WindowProcedure()'s arguments. 
        ;; We push the arguments to the stack, in backwards order. 
        push dword [ebp+20] 
        push dword [ebp+16] 
        push dword [ebp+12] 
        push dword [ebp+08] 
        ;; And we call the DefWindowProc() function. 
        call _DefWindowProcA@16
        
        ;; At this point, we need to return. The return value must 
        ;; be equal to whatever DefWindowProc() returned, so we 
        ;; can't change EAX. 
        
        ;; But we need to leave before we return. 
        leave 
        
        ;; Then, we can return. WindowProcedure() has 4 arguments, 4 bytes each, 
        ;; so we free 4 * 4 = 16 bytes from the stack, after returning. 
        ret 16 
        
        ;; Any code after the RET instruction will not be executed. 
        ;; But we'll put code there anyway, just for consistency. 
        jmp .window_finish 
    ;; This is where the we want to jump to after doing everything we need to. 
    .window_finish: 
    
    ;; Unless we use the DefWindowProc() function, we need to return 0. 
    xor eax, eax                  ;; XOR EAX, EAX is a way to clear EAX. 
                                  ;; Same applies for any other register. 
    ;; Then we need to leave. 
    leave 
;; And, as said earlier, we free 16 bytes, after returning. 
ret 16 
	
%endif