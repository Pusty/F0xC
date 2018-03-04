package pusty.f0xC.b32;

import pusty.f0xC.kernel.Pointer;

public class InterUtil extends Interrupt32 {
	
	public static void printString(String str, int len) { }
	public static void printString(Pointer str, int len) { }
	public static void printString(int str, int len) { }
	public static void readConsole(char[] text, int len) { }
	public static String readConsoleLine() { return ""; }
//	public static char[] readConsoleLine() { return null; }
	
	public static void printChar(int value) { }
	public static void printChar(char c) { }
	public static void printInt(int value) { }
	
	public static int mathPow(int a,int b) { return 0; }

	public static void newLine() { }
	
	public static void setConsoleTitle(String str) { }
	/**
	 * Set's the std handle to <code>handle</code>
	 * @param handle the new handle (0 means STDHANDLE)
	 */
	public static void setOutput(int handle) { }
	
	public static final int FILE_READ = 0x0000;
	public static final int FILE_WRITE = 0x0001;
	public static final int FILE_CREATE = 0x1000;
	public static final int FILE_READWRITE = 0x0002;
	
	public static int openFile(String filename, int mode) { return 0; }
	public static void readFile(int handle, String text, int len) { return;}
	public static void readFile(int handle, char[] text, int len) { return;}
	public static int lengthFile(int handle) { return 0; }
	public static void closeFile(int handle) { }
	
	
	public static void createWindowClass(String name) {}
	
	public static int createWindow(String className, String windowName) { return 0;}
	
	public static void showWindow(int handle) {}
	public static void updateWindow(int handle) {}
	public static void drawText(int handle) {}
	public static boolean handleMsg(byte[] msg) { return false;}
	
}
