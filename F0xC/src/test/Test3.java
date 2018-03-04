package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.RegSize;
import static pusty.f0xC.b16.InterUtil.*;

public class Test3 {

    public static void mmain()   {
    	
    	halt();
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test3.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	RegSize.setSize("INT", RegSize.b16);
    	RegSize.setSize("LONG", RegSize.b32);
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.setCutter(true);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createBIN();
	    	CompilerF0xC.createKER();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
