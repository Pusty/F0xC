package pusty.f0xC.util;

import java.util.regex.Pattern;

import pusty.f0xC.kernel.Loadable;

import laos.util.img.Img16ASM;

public class Image16 extends Loadable {
	
	static Pal16 staticPal = null;
	
	public Image16(String link) {
		super(link);
	}
	public String getPath() {
		return this.getString().split(Pattern.quote("."))[0]+".bimg";
	}
	@Override
	public void load() {
		if(staticPal==null) {
			String[] args = new String[2];
			args[0] = this.getContent().getAbsolutePath();
			args[1] = this.getRes().getAbsolutePath();			
			Img16ASM.main(args);
		}else {
			String[] args = new String[3];
			args[0] = this.getContent().getAbsolutePath();
			args[1] = this.getRes().getAbsolutePath();		
			args[2] = staticPal.getContent().getAbsolutePath();
			Img16ASM.main(args);			
		}
		System.out.println("convert image");
	}
}
