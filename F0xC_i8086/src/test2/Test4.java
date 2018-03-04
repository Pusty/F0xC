package test2;

import static pusty.f0xC.b8.InterUtil.*;
import static pusty.f0xC.b8.InterBuffer.*;
import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.RegSize;

public class Test4 {

	public static void mmain() {
		int alpha = 222;
		long beta = 23;
		printInt(1189);
		newLine();
		//printHex(TestObject.test());
		newLine();
		//printInt(obj.getTestValue());
		halt();
	}

	public static void main(String[] args) throws Exception {
		String[] ar = new String[2];
		ar[0] = System.getProperty("user.dir") + "/bin/test2/Test4.class";
		ar[1] = System.getProperty("user.dir") + "/output.asm";
		RegSize.setMode(RegSize.b8);
		CompilerF0xC.setDebug(false);
		CompilerF0xC.setCutter(true);
		CompilerF0xC.main(ar);
		if (!CompilerF0xC.isDebug()) {
			CompilerF0xC.createASM();
			CompilerF0xC.createBIN();
			CompilerF0xC.createKER();
			System.out.println(CompilerF0xC.isCompiled());
		}
	}
}
