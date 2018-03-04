package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import static pusty.f0xC.b16.InterUtil.*;

public class Test1 {

	static int[] array = new int[16];

	static Pointer pointer = new Pointer();
    public static void mmain()   {
    	for(int i=0;i<16;i++)
    		array[i] = i*2;
    	for(int i=0;i<16;i++)
    		printHex(array[i]);
    	halt();
    	
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test1.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createBIN();
	    	CompilerF0xC.createKER();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
