package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.b32.InterUtil;
import pusty.f0xC.kernel.Pointer;
import static pusty.f0xC.b32.InterUtil.*;

public class Test2 {
	
	static Pointer pointer = new Pointer("std_memory");
    public static void mmain()   {
//    	String test = InterUtil.readConsoleLine();
//    	String test = InterUtil.readConsoleLine();
    	String input = InterUtil.readConsoleLine();
    	char[] ch = new char[input.length()];
    	for(int i=0;i<ch.length;i++)
    		ch[i] = input.charAt(i);
    	int argcount = argc(ch);
    	printInt(argcount);
    	newLine();
    	
    	printInt(argl(ch, 1));
    	newLine();
    	printString(input, ch.length);
    	newLine();
    	
    	char[] arg0 = argv(ch, 0);
    	if(arg0 != null)
    		printString(new String(arg0), arg0.length);
    	newLine();
    	char[] arg1 = argv(ch, 1);
    	if(arg1 != null)
    		printString(new String(arg1), arg1.length);
    	
    	
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
    
//    public static void func() {
//    	printString("Written Content: ","Written Content: ".length());
//    	InterUtil.newLine();
//    	InterUtil.newLine();
//    	int handle = openFile("newFile.txt", InterUtil.FILE_CREATE);
//    		setOutput(handle);
//    		printString("Content:", 8);
//    		newLine();
//        	printString("Sum: ",5);
//        	printInt(quersumme(133));
//        	newLine();
//    		printInt(1337);
//    	closeFile(handle);
//    	handle = openFile("newFile.txt", InterUtil.FILE_READ);
//    		int length = lengthFile(handle);
//    		char[] chars = new char[length];
//			readFile(handle, chars, chars.length);
//    		String text = new String(chars);
//		closeFile(handle);
//    	setOutput(0);
//    	printString(text, text.length());
//    	newLine();
//    }
//    
//    public static int summe(int m) {
//    	return m==1?1:m+summe(m-1);
//    }
//    
//    public static int quersumme(int m) {
//    	return m<10?m:m%10+quersumme(m/10);
//    }
//  
    
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
