package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import static pusty.f0xC.b32.InterUtil.*;

public class Test2 {
	
	static Pointer argc = new Pointer("std_argc");
	static Pointer argv0 = new Pointer("std_argv+0");
	static Pointer argv1 = new Pointer("std_argv+4");
	static Pointer pointer = new Pointer("std_memory");
    public static void mmain()   {
//    	String test = InterUtil.readConsoleLine();
//    	String test = InterUtil.readConsoleLine();
    	int argcount = argc.read();
    	printInt(argcount);
    	newLine();
    	printInt(argv0.getValueString().length());
    	newLine();
    	printString(argv0.getValueString(), argv0.getValueString().length());
    	newLine();
    	
    }
    	
//    	for(int i=-4;i<15;i++)
//    	printChar(test[i]);
//    	printString(test, test.length());
//    	pointer.setValue(test);
//    	printInt(pointer.read());
//    	printString(test, test.length());
//    	printInt(pointer.readAddr());
//    	newLine();
//    	func();
//    	newLine();
//    	printInt(pointer.readAddr());
    
   
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test2.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createOBJ();
	    	CompilerF0xC.createEXE();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
