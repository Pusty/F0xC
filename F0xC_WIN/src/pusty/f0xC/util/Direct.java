package pusty.f0xC.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import pusty.f0xC.kernel.Loadable;

public class Direct extends Loadable {
	public Direct(String link) {
		super(link);
	}
	public String getPath() {
		return getString();
	}
	@Override
	public void load() {
		try {
			copy(getContent(), getRes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    try {
	        OutputStream out = new FileOutputStream(dst);
	        try {
	            // Transfer bytes from in to out
	            byte[] buf = new byte[1024];
	            int len;
	            int mlen = (int) src.length();
	            byte[] t = new byte[4];
	            t[0] = (byte) (mlen&0xFF);
	            t[1] = (byte) ((mlen&0xFF00)>>8);
	            t[2] = (byte) ((mlen&0xFF0000)>>16);
	            t[3] = (byte) ((mlen&0xFF000000)>>24);
	            out.write(t,0,4);
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
//	                mlen = mlen + len;
	            }
	            System.out.println(mlen+"MLEN");
//	            byte[] t = new byte[4];
//	            t[0] = (byte) (mlen&0xFF);
//	            t[1] = (byte) ((mlen&0xFF)>>8);
//	            t[2] = (byte) ((mlen&0xFF)>>16);
//	            t[3] = (byte) ((mlen&0xFF)>>24);
//	            out.write(t,0,4);
	        } finally {
	            out.close();
	        }
	    } finally {
	        in.close();
	    }
	}
}
