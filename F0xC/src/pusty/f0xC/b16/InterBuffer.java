package pusty.f0xC.b16;

import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.util.Image16;
import pusty.f0xC.util.Pal16;


public class InterBuffer extends Interrupt16  {
	
	public static void memoryDump() { }
	public static void flip() { }
	
	public static void drawBox(int color, int x, int y,int sx, int sy) { }
	public static void drawBoxBuffered(int color, int x, int y,int sx, int sy) { }
	
	public static void initGraphics() { }
	public static void resetBuffer() { }
	public static void loadPal(Pal16 p) { }
	public static void drawImage(Image16 i,int x,int y) { }
	public static void drawImage(Pointer p,int x,int y) { }
	public static void drawImage(int pointer,int x,int y) { }
	
}
