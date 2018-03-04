package laos.util.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Img16PASM {
	static ArrayList<Integer> colorPallete13h;
	public static void main(String[] args) {
		File file = null;
		File out = null;
		if(args.length<2) {
			return;
		}else {
			file = new File(args[0]);
			if(args.length>=2)
				out = new File(args[1]);
		}
		try {
			BufferedImage bimg = ImageIO.read(file);
			colorPallete13h = new ArrayList<Integer>(16);
				int pal=0;
				for(int y=0;y<bimg.getHeight();y++)
					for(int x=0;x<bimg.getWidth();x++) {
						int rgb = bimg.getRGB(x, y);
							pal++;
							if(pal>16)continue;
							colorPallete13h.add(rgb);
					}
			
			out.delete();
			out.createNewFile();
			FileOutputStream fos = new FileOutputStream(out);
//			fos.write(0);

			for(int rgb:colorPallete13h) {
				float redF = ((rgb>>16)&0xFF);
				float greenF = ((rgb>>8)&0xFF);
				float blueF = (rgb&0xFF);
				redF = redF/255f*64f;
				greenF = greenF/255f*64f;
				blueF = blueF/255f*64f;
				int red = (int)redF;
				int green = (int)greenF;
				int blue = (int)blueF;
				fos.write((byte)red);
				fos.write((byte)green);
				fos.write((byte)blue);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
