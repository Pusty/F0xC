package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import static pusty.f0xC.b32.InterUtil.*;

public class Test1 {
	static Pointer pointer = new Pointer("std_argv");
    public static void mmain()   {
    	printString("test\n", 6);
    	printInt(16);
    	newLine();
    	String argv = pointer.getValueString();
    	//for(int i=0;i<10;i++)
    	//	printChar(argv[i]);
    	printString(argv, 4);
    	printInt(argv.length());
    	printChar(argv.charAt(2));
    	newLine();
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test1.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.setCutter(false);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createOBJ();
	    	CompilerF0xC.createEXE();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    	
    	//ld output.o -melf_i386 --oformat=elf32-i386 -o output
    }
    
}
