package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.util.Int3Object;
import static pusty.f0xC.b16.InterUtil.*;

public class Test2 {

	static int[] iamarray = {28, 9 , 5};
	static Int3Object store = new Int3Object();
	

	static Pointer pointer = new Pointer();
    public static void mmain()   {
    	store.set(0, 17);
    	for(int i=1;i<store.getArray().length;i++)
    		store.set(i, iamarray[i]);
    	for(int a:store.getArray())
    		printHex(a);
    	halt();
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test2.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
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
