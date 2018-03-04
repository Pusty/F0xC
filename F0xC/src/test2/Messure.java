package test2;

import static pusty.f0xC.b16.InterUtil.*;
import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.RegSize;

public class Messure {
	static String test2 = "Loading...";
	static String test3 = "1sec = ";
	static String spacer = " ";
	public static void mmain() {
		int value = time();
		printString(test2);
		newLine();

		sleep(1000);
		//CODE
		
		value = time()-value;
		printString(test3);
		printInt(value);
		newLine();
		value = time();
		//MESSURE
		//MESSURE
		value = time()-value;
		printInt(value);
		
		halt();
	}

	public static void main(String[] args) throws Exception {
		String[] ar = new String[2];
		ar[0] = System.getProperty("user.dir") + "/bin/test2/Messure.class";
		ar[1] = System.getProperty("user.dir") + "/output.asm";
		RegSize.setMode(RegSize.b16);
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
