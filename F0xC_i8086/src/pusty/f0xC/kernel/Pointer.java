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
}
