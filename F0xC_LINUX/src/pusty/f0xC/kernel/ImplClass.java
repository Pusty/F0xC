package pusty.f0xC.kernel;

import pusty.f0xC.Forge;

public abstract class ImplClass {
	public ImplClass() { }
	public abstract void pre(Forge forge);
	public abstract void impl(Forge forge);
	public abstract void post(Forge forge);
}
