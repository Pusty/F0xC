package test;

import pusty.f0xC.CompilerF0xC;
import static pusty.f0xC.b32.InterUtil.*;
import pusty.f0xC.kernel.Pointer;

public class Test3 {
	static Pointer function = new Pointer("std_handle");
    public static void mmain()   {
//    	String text = "test";
//    	InterUtil.setConsoleTitle(text);
//    	InterUtil.printString(text, text.length());
    	
    	String name = "SimpleWindowClass";
    	createWindowClass(name);
    	String link = "Simple Window Example";
    	int handle = createWindow(name, link);
    	showWindow(handle);
    	updateWindow(handle);
    	drawText(handle);
    	updateWindow(handle);
    	printInt(handle);
    	byte[] msg = new byte[28];
    	while(handleMsg(msg)) {
        	drawText(handle);
    	}
//    	handleMsg(msg);
    
//    	InterUtil.printInt(handle);
//    	char c[] = new char[10];
//    	InterUtil.readConsole(c, c.length);
    }
    
    public static void update() {
    	printInt(25);
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test3.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.addNoMask("update");
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createOBJ();
	    	CompilerF0xC.createEXE();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
