package test;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.util.TileMap16;
import static pusty.f0xC.b16.InterUtil.*;

public class Test4 {

    static TileMap16 tilemap_ = new TileMap16("map.png");
    static int[] tilemap = null;
    
    public static void mmain()   {
    	int a = tilemap[0];
    	printInt(a);
    	newLine();
    	a = tilemap[1];
    	printInt(a);
    	newLine();
    	a = tilemap[2];
    	printInt(a);
    	halt();
    }
    
  
    
    public static void main(String[] args) throws Exception {
    	String[] ar = new String[2];
    	ar[0] = System.getProperty("user.dir")+"/bin/test/Test4.class";
    	ar[1] = System.getProperty("user.dir")+"/output.asm";
    	CompilerF0xC.setDebug(false);
    	CompilerF0xC.setCutter(true);
    	CompilerF0xC.main(ar);
    	if(!CompilerF0xC.isDebug()) {
	    	CompilerF0xC.createASM();
	    	CompilerF0xC.createBIN();
	    	CompilerF0xC.createKER();
			System.out.println(CompilerF0xC.isCompiled());
    	}
    }
    
}
