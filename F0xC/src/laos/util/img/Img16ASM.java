package laos.util.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Img16ASM {
	static ArrayList<Integer> colorPallete13h;
	static ArrayList<Integer> asmImage;

	static URL palStatic = Img16ASM.class.getResource("tmp.png");
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		asmImage = new ArrayList<Integer>();
		File file = null;
		File out = null;
		if(args.length<2) return;
		file = new File(args[0]);
		out = new File(args[1]);
		if(args.length==3)
			try {
				palStatic = new File(args[2]).toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		initColors();
		try {
			BufferedImage bimg = ImageIO.read(file);
			for(int y=0;y<bimg.getHeight();y++)
				for(int x=0;x<bimg.getWidth();x++) {
					int rgb = bimg.getRGB(x, y);
					int close = 0;
					double closeDis = 100000d;
					
					int r1 = ((rgb>>16)&0xFF);
					int g1 = ((rgb>>8)&0xFF);
					int b1 = (rgb&0xFF);
					int index = 0;
					for(int color:colorPallete13h) {
						int r2 = ((color>>16)&0xFF);
						int g2 =((color>>8)&0xFF);
						int b2 = (color&0xFF);
						double dis = Math.sqrt((r2-r1)*(r2-r1)+(g2-g1)*(g2-g1)+(b2-b1)*(b2-b1));
						if(dis<closeDis) {
							closeDis = dis;
							close = index;
						}
						index++;
					}
					asmImage.add(close);
				}
			out.delete();
			out.createNewFile();
			FileOutputStream fos = new FileOutputStream(out);
			fos.write(0);
			fos.write(bimg.getWidth());
			fos.write(bimg.getHeight());
			int a=0;
			int i=0;
			for(int ind:asmImage) {
				i++;
				if(i==2) {
					i=0;
					int b = ind;
					a = a + (b<<4);
					fos.write((byte)a);
				}else 
				  a = ind;
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initColors() {
		colorPallete13h = new ArrayList<Integer>(16);
		try {
			int pal=0;
			BufferedImage bimg = ImageIO.read(palStatic);
			for(int y=0;y<bimg.getHeight();y++)
				for(int x=0;x<bimg.getWidth();x++) {
					int rgb = bimg.getRGB(x, y);
						pal++;
						if(pal>16)continue;
						colorPallete13h.add(rgb);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
