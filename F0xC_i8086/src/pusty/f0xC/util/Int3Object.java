package pusty.f0xC.util;

import pusty.f0xC.kernel.Storable;

public class Int3Object extends Storable {

	public Int3Object() {
		super(3);
		this.set(0, 1);
		this.set(1, 2);
		this.set(2, 3);
	}


}
