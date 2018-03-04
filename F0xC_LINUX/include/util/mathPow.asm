%ifndef MATH_POW
    %define MATH_POW
mathPow: ; eax ^ ebx = eax (ebx>=0)
	mov ecx, eax
	mov edx, ebx
	cmp edx, dword 0
		jle .end1
	.loop:
		sub edx, dword 1
		cmp edx, dword 0
		je .end0
		imul eax, ecx
	jmp .loop
	.end1:
		mov eax, dword 1
		ret
	.end0:
		ret
%endif