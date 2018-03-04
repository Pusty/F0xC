package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.kernel.Pointer;
import static pusty.f0xC.b32.InterUtil.*;

public class Xoring {
	
	static Pointer m_argc = new Pointer("std_argc");
	static Pointer m_argv1 = new Pointer("std_argv+4");
	static int keyXor = 0xF0;
    public static void mmain()   {
    	int argc = m_argc.read();
    	if(argc != 2) { //Program needs exactly 2 arguments (1 user supplied)
    		printString("./crackme password", 18);
    		newLine();
    		return; //Exit if not 2 arguments
    	}
    	String argv1 = m_argv1.getValueString();  //read first argument into argv1
    	int arg1_length = argv1.length(); //save the length of arg1
    	
    	int[] key = { 67 ,68 ,102 ,75 ,9 ,15 ,12 ,25 ,235 ,245 ,255 ,211 ,168 ,181 ,133 ,115 ,117 ,67 ,81 ,43 ,53 ,29 };
    	if(arg1_length != key.length) {
    		printString("Wrong!", 6);
    		newLine();
    		return;
    	}
    	for(int i=0;i<key.length;i++) {
    		if(((argv1.charAt(i)^((i*16)%keyXor))&0xFF) == key[i]) {
    			//printString("Correct!", 8);
    			//newLine();
    			//printInt(((arg1[i]^((i*16)%keyXor))&0xFF));
    			//newLine();    			
    		}else {
    			printString("Wrong!", 6);
    			newLine();
    			//printInt(((arg1[i]^((i*16)%keyXor))&0xFF));
    			//newLine();
    			return;
    		}
    	}
    	printString("Correct!", 8);
    	newLine();
    }
   
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Xoring.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createOBJ();
	    	CompilerF0xC.createEXE();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    	
    	String key = "CTF{I_like_cheesecake}";
    	for(int i=0;i<key.length();i++) {
    		System.out.print(((key.charAt(i)^((i*16)%keyXor))&0xFF)+" ,");
    	}System.out.println();
    }
    
}
