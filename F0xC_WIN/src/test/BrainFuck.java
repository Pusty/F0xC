package test;

import static pusty.f0xC.b32.InterUtil.*;
import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.Forge;
import pusty.f0xC.b32.InterUtil;
import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.util.Direct;

public class BrainFuck {
	static Direct file = new Direct("wiki.bf");
	static Pointer pointer = new Pointer("file");
	static Pointer output = new Pointer("std_error");
	static boolean IS_RUNNING = false;
    public static void mmain()   {
    	String consoleInput = InterUtil.readConsoleLine();
    	char[] parsedInput = new char[consoleInput.length()];
    	for(int i=0;i<parsedInput.length;i++)
    		parsedInput[i] = consoleInput.charAt(i);
    	useConsoleInput(parsedInput);
    	
//    	readFromFile("wiki.bf"); //Read file with name wiki.bf
    	if(!IS_RUNNING) {
    		IS_RUNNING = true;
//    		readFromExe(); //Read file within exe (Direct file "wiki.bf")
//    		printString("Empty File", 10);
    	}
    	
    	if(FILE_OUTPUT_HANDLE!=0)
    		closeFile(FILE_OUTPUT_HANDLE);
    	
//    	readChar(2);
//    	readChar(3);
//    	readChar(4);
    }
    
    public static int FILE_OUTPUT_HANDLE = 0;
    static Pointer FILE_INPUT_HANDLE = new Pointer();
    static int FILE_INPUT_INDEX = 0;
    public static void useConsoleInput(char[] ch) {
    	int argcount = argc(ch);
    	if(argcount <= 1) return;
    	
    	char[] arg2 = argv(ch, 2); // INPUT FROM FILE
	    if(arg2 != null) {
	    	if(arg2[0] == '"') {
	    		char[] parse = new char[arg2.length-2];
	    		for(int i=1;i<arg2.length-1;i++)
	    			parse[i-1] = arg2[i];
	    		String fileName = new String(parse);
	        	int handle = openFile(fileName, InterUtil.FILE_READ);
	    		int length = lengthFile(handle);
	        	char[] data = new char[length+1];
	        	readFile(handle, data, length);
	        	closeFile(handle);
	        	FILE_INPUT_HANDLE.setValue(data);
	        	FILE_INPUT_INDEX = 0;
//	        	printChar(FILE_INPUT_HANDLE.getValueChars()[1]);
	    	}
	    }
	    
    	char[] arg3 = argv(ch, 3); //OUTPUT TO FILE
	    if(arg3 != null) {
	    	if(arg3[0] == '"') {
	    		char[] parse = new char[arg3.length-2];
	    		for(int i=1;i<arg3.length-1;i++)
	    			parse[i-1] = arg3[i];
	    		String fileName = new String(parse);
//	    		readFromFile(fileName);
	    		FILE_OUTPUT_HANDLE = openFile(fileName, InterUtil.FILE_CREATE);
	    		closeFile(FILE_OUTPUT_HANDLE);
	    		FILE_OUTPUT_HANDLE = openFile(fileName, InterUtil.FILE_WRITE);
	    		setOutput(FILE_OUTPUT_HANDLE);
	    	}
	    }

    	char[] arg1 = argv(ch, 1);
    	if(arg1 != null) {
    		if(arg1[0] == '"') {
    			IS_RUNNING = true;
    			char[] parse = new char[arg1.length-2];
    			for(int i=1;i<arg1.length-1;i++)
    				parse[i-1] = arg1[i];
    			String fileName = new String(parse);
    			readFromFile(fileName);
    		}
    	}
    	
    }
    
    public static void readFromFile(String input) {
    	int handle = openFile(input, InterUtil.FILE_READ);
		int length = lengthFile(handle);
    	char[] chars = new char[length];
		readFile(handle, chars, length);
		closeFile(handle);
		interpret(chars, length);
    }
    public static void readFromExe() {
    	int length = pointer.getValueChars().length;
		char[] chars = pointer.getValueChars();
		interpret(chars, length);
    }
    public static void interpret(char[] chars, int length) { 
		char[] byteArray = new char[1024]; //amount of cells
		int byteIndex = 0;
		char t = 0;
		for(int i=0;i<length;i++) {
			t = chars[i];
			if(t=='<') {
				byteIndex--;
				continue;
			}else if(t=='>') {
				byteIndex++;
				continue;
			}else if(t=='+') {
				byteArray[byteIndex] = (char) (byteArray[byteIndex] + 1);
				continue;
			}else if(t=='-') {
				byteArray[byteIndex] = (char) (byteArray[byteIndex] - 1);
				continue;
			}else if(t=='[') {
				if(byteArray[byteIndex]==0)
					i = nextChar(i, chars);
				continue;
			}else if(t==']') {
				if(byteArray[byteIndex]!=0)
					i = lastChar(i, chars);
				continue;
			}else if(t=='.') {
				printChar(byteArray[byteIndex]);
				output.write(byteArray[byteIndex]-'0');
//				printInt(byteIndex);
//				printChar(':');
//				printInt(byteArray[byteIndex]);
//				newLine();
				continue;
			}else if(t==',') {
				if(FILE_INPUT_HANDLE.getValue()==0)
					byteArray[byteIndex] = readChar(4);
				else {
					byteArray[byteIndex] = FILE_INPUT_HANDLE.getValueChars()[FILE_INPUT_INDEX];
					FILE_INPUT_INDEX++;
					if(FILE_INPUT_HANDLE.getValueChars().length < FILE_INPUT_INDEX) {
						break;
					}
				}
					
				continue;
			}
		}
    }
    public static char readChar(int am) {
    	char[] array = new char[am];
    	InterUtil.readConsole(array, am);
    	return array[0];
    }
    public static int lastChar(int current, char[] array) {
    	int t = 0;
    	for(int a=current;a>=0;a--) {
    		if(array[a]==']') {
    			t++;
    			continue;
    		}
    		if(array[a]=='[') {
    			t--;
    			if(t==0) return a;
    			continue;
    		}
    	}
    	return current;
    }
    
    public static int nextChar(int current, char[] array) {
    	int t = 0;
    	for(int a=current;a>=0;a--) {
    		if(array[a]=='[') {
    			t++;
    			continue;
    		}
    		if(array[a]==']') {
    			t--;
    			if(t==0) return a;
    			continue;
    		}
    	}
    	return current;
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
    	ar[0] = System.getProperty("user.dir")+"/bin/test/BrainFuck.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	Forge.BUFFER_SIZE = 1024 * 100;
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
