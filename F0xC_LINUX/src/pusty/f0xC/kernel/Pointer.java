package pusty.f0xC.kernel;


public class Pointer {
	String str;
	public Pointer() {
		str = "";
	}
	public Pointer(String s) {
		str = s;
	}
	public String getPointer() {
		return str;
	}
	

	public void set(String string) { }
	public void set(Loadable load) { }
	public int getValue() {
		return 0;
	}
	public String getValueString() {
		return "";
	}
	public char getValueChar() {
		return ' ';
	}
	public void setValue(char[] test) {
	}
	public char[] getValueChars() {
		return null;
	}
	public void setValue(int v) {
		
	}
	public int readAddr() {
		return 0;
	}
	public int read() {
		return 0;
	}
	public void writeAddr(int i) {
	}
	public void write(int i) {
	}
}
