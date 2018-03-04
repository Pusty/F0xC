package pusty.f0xC.b8;

import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.util.Image16;
import pusty.f0xC.util.Pal16;


public class InterBuffer extends Interrupt8 {
	
	public static void memoryDump() { }
	public static void flip() { }
	
	public static void drawBox(int color, long x, long y,long sx, long sy) { }
	public static void drawBoxBuffered(long color, long x, long y,long sx, long sy) { }
	
	public static void initGraphics() { }
	public static void resetBuffer() { }
	public static void loadPal(Pal16 p) { }
	public static void drawImage(Image16 i,long x,long y) { }
	public static void drawImage(Pointer p,long x,long y) { }
	public static void drawImage(long pointer,long x,long y) { }
	
}
