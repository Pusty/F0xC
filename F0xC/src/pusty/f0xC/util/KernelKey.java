package pusty.f0xC.util;

import pusty.f0xC.Forge;
import pusty.f0xC.kernel.ImplClass;

public class KernelKey extends ImplClass {
	String saveVar = "";
	String boolVar = "";
	String func = "";
	public KernelKey(String sv,String bv,String fu) {
		super();
		saveVar = sv;
		boolVar = bv;
		func = fu;
	}
	public KernelKey(String sv,String bv) {
		this(sv,bv,null);
	}
	
	public void pre(Forge forge) {
		forge.getContent().add("IMPL_KEYLISTENER:");
		forge.getContent().add("mov eax,0x0024");
		forge.getContent().add("mov ebx,dword [eax]");
		forge.getContent().add("mov [oldINT24],ebx");
		forge.getContent().add("mov [eax],dword keyboardINTListener");
	}
	
	//	I think this fails because of a overwrite in interrupts
	//	Keyboard 09h 0000:0024h Hardware Keyboard - IRQ 1 
	//	CPU 09h 0000:0024h Hardware Math Coprocessor Segment Overrun (80286, 80386) (AT Only)
	//	An XT computer had 8 IRQ's, while the AT provides 16. -> :|
	// todo: learn how to handle PIC remapping
	public void impl(Forge forge) {
		forge.getContent().add("KEYLISTENER:");
		forge.getContent().add("oldINT24 dd 0");
		forge.getContent().add("keyboardINTListener:");
		forge.getContent().add("pushad"); //push dword registers
		// \ | / this line triggers errors
		forge.getContent().add("in al, 0x60"); //read scancode (not ASCII) into al
		forge.getContent().add("mov bl,al");
		forge.getContent().add("and bl, 01111111b");
		forge.getContent().add("mov bh, al");		
		forge.getContent().add("shr bh, 7");	
		forge.getContent().add("and bh, 00000001b");
		forge.getContent().add("xor bh, 00000001b");
		forge.getContent().add("mov ["+saveVar+"], bl");
		forge.getContent().add("mov ["+boolVar+"], bh");
		if(func != null)
		forge.getContent().add("call "+func);
		forge.getContent().add("mov al, 0x20"); //verify that a interrupt has been triggered
		forge.getContent().add("out 20h, al");
//		forge.getContent().add("mov al, 0x20"); //verify that a interrupt has been triggered
//		forge.getContent().add("out 0xA0, al");
		forge.getContent().add("popad");
		forge.getContent().add("iret");
//		forge.getContent().add("leave"); //dead code but whatever
//		forge.getContent().add("ret");
	}
	
	public void post(Forge forge) {
		
	}
}
