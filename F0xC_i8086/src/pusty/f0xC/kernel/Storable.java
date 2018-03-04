package pusty.f0xC.kernel;

public class Storable {
	int size;
	int[] contains;
	public Storable(int ws) {
		size=ws; //size in 16 bits / in words
		contains = new int[ws];
	}
	
	public int getSize() {
		return size;
	}
	public int get(int in) {
		return contains[in];
	}
	public void set(int in, int c) {
		contains[in] = c;
	}
	public void set(int in, char c) {
		contains[in] = c;
	}
	public void set(int in, byte c) {
		contains[in] = c;
	}
	public void set(int in, boolean c) {
		contains[in] = c?1:0;
	}
	public int getInt(int i) {
		return contains[i];
	}
	public byte getByte(int i) {
		return (byte)contains[i];
	}
	public char getChar(int i) {
		return (char)contains[i];
	}
	public boolean getBoolean(int i) {
		return contains[i]==1;
	}
	public int[] getArray() {
		return contains;
	}

}
