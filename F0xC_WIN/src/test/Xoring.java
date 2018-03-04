package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.b32.InterUtil;
import static pusty.f0xC.b32.InterUtil.*;

public class Xoring {
	
	static int keyXor = 0xF0;
    public static void mmain()   {
    	String input = InterUtil.readConsoleLine(); //Read Console Line
    	char[] ch = new char[input.length()];
    	for(int i=0;i<ch.length;i++)
    		ch[i] = input.charAt(i); //Save it into an array to work with, as no string modules are implemented in this version
    	int argcount = argc(ch); //outputs amount of arguments
    	if(argcount != 2) { //Program needs exactly 2 arguments (1 user supplied)
    		printString("./crackme password", 18);
    		return; //Exit if not 2 arguments
    	}
    	char[] arg1 = argv(ch, 1); //read first argument into arg1
    	int arg1_length = argl(ch, 1); //save the length of arg1
    	//printInt(arg1_length);
    	//newLine();
    	
    	int[] key = { 67 ,68 ,102 ,75 ,9 ,15 ,12 ,25 ,235 ,245 ,255 ,211 ,168 ,181 ,133 ,115 ,117 ,67 ,81 ,43 ,53 ,29 };
    	if(arg1_length != key.length) {
    		printString("Wrong!", 6);
    		return;
    	}
    	for(int i=0;i<key.length;i++) {
    		if(((arg1[i]^((i*16)%keyXor))&0xFF) == key[i]) {
    			//printString("Correct!", 8);
    			//newLine();
    			//printInt(((arg1[i]^((i*16)%keyXor))&0xFF));
    			//newLine();    			
    		}else {
    			printString("Wrong!", 6);
    			//newLine();
    			//printInt(((arg1[i]^((i*16)%keyXor))&0xFF));
    			//newLine();
    			return;
    		}
    	}
    	printString("Correct!", 8);
    	newLine();
    }
    public static int argl(char[] c, int n) {
    	int argN = 0;
    	boolean space = false;
    	int chars = 0;
    	for(int i=0;i<c.length;i++) {
    		if(!space && (c[i] == ' '  || c[i] == '\t')) {
    			space=true;
    		}else if(space && !(c[i] == ' '  || c[i] == '\t')) {
    			space = false;
    			if(argN == n) break;
    			argN++;
    			if(argN == n) chars++;
    		}else if(!space)
    			if(argN == n)
    				chars++;
    	}
    	return chars;
    }
    public static int args(char[] c, int n) {
    	int argN = 0;
    	boolean space = false;
    	for(int i=0;i<c.length;i++) {
    		if(!space && (c[i] == ' '  || c[i] == '\t')) {
    			space=true;
    		}else if(space && !(c[i] == ' '  || c[i] == '\t')) {
    			space = false;
    			if(argN == n) break;
    			argN++;
    			if(argN == n) return i;
    		}else if(!space)
    			if(argN == n)
    				return i;
    	}
    	return 0;
    }
    public static char[] argv(char[] c, int n) {
    	int length = argl(c, n);
    	int start = args(c, n);
    	if(length == 0) return null;
    	char[] out = new char[length];
    	for(int i=start;i<start+length;i++) {
    		out[i-start] = c[i];
    	}
    	return out;
    }
    public static int argc(char[] c) {
    	int count = 1;
    	boolean space = false;
    	for(int i=0;i<c.length;i++) {
    		if(!space && (c[i] == ' '  || c[i] == '\t')) {
    			space=true;
    		}else if(space && !(c[i] == ' '  || c[i] == '\t')) {
    			space = false;
    			count++;
    		}
    	}
    	return count;
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
