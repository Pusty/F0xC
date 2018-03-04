package pusty.f0xC.util;

import java.util.regex.Pattern;

import pusty.f0xC.kernel.Loadable;

import laos.util.img.ImgMapASM;

public class TileMap16 extends Loadable {
	
	static Pal16 staticPal = null;
	
	public TileMap16(String link) {
		super(link);
	}
	public String getKey(String key) {
		return key.replaceAll("_", "");
	}
	public String getPath() {
		return this.getString().split(Pattern.quote("."))[0]+".bmap";
	}
	@Override
	public void load() {
		if(staticPal==null) {
			String[] args = new String[2];
			args[0] = this.getContent().getAbsolutePath();
			args[1] = this.getRes().getAbsolutePath();			
			ImgMapASM.main(args);
		}else {
			String[] args = new String[3];
			args[0] = this.getContent().getAbsolutePath();
			args[1] = this.getRes().getAbsolutePath();		
			args[2] = staticPal.getContent().getAbsolutePath();
			ImgMapASM.main(args);			
		}
		System.out.println("convert tilemap");
	}
}
