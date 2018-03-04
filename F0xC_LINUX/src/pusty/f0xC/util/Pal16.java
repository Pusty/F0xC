package pusty.f0xC.util;

import java.util.regex.Pattern;

import pusty.f0xC.kernel.Loadable;

import laos.util.img.Img16PASM;

public class Pal16 extends Loadable {
	public Pal16(String link) {
		super(link);
		Image16.staticPal=this;
		TileMap16.staticPal=this;
	}
	public String getPath() {
		return this.getString().split(Pattern.quote("."))[0]+".bpal";
	}
	@Override
	public void load() {
		String[] args = new String[2];
		args[0] = this.getContent().getAbsolutePath();
		args[1] = this.getRes().getAbsolutePath();
		Img16PASM.main(args);
		System.out.println("convert palette");
	}
}
