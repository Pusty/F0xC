package test2;

import pusty.f0xC.kernel.Storable;
import static pusty.f0xC.b8.InterUtil.*;

public class TestObject extends Storable {

	public TestObject() {
		super(2);
		this.set(0, 27);
		this.set(1, 12);
	}
	
	public static int test() {
		printInt(1337);
		return 8;
	}
	
	public final int getTestValue() {
		return get(0);
	}


}
