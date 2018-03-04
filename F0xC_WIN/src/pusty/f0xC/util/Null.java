package pusty.f0xC.util;

import pusty.f0xC.kernel.Loadable;

//Null Class for CLINIT
public class Null extends Loadable {

	public Null() {
		super(null);
	}

	@Override
	public void load() {
	}

	@Override
	public String getPath() {
		return null;
	}

}
