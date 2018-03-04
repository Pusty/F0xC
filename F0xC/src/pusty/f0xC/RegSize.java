package pusty.f0xC;

import java.util.ArrayList;

public class RegSize {
	public static int b8 = 8;
	public static int b16 = 16;
	public static int b32 = 32;
	
	public static int MEM = b16;
	public static int BYTE = b8;
	public static int INT = b16;
	public static int LONG = b32;
	public static void setSize(String a, int b) {
		if(a.equalsIgnoreCase("MEM"))
			MEM=b;
		if(a.equalsIgnoreCase("INT"))
			INT=b;
		if(a.equalsIgnoreCase("LONG"))
			LONG=b;
		if(a.equalsIgnoreCase("BYTE"))
			BYTE=b;
	}
	public static void setMode(int mode) {
		if(mode==b8) {
			MEM = b16;
			BYTE = b8;
			INT = b8;
			LONG = b16;
		}else if(mode==b16) {
			MEM = b16;
			BYTE = b16;
			INT = b16;
			LONG = b32;
		}
	}
	static ArrayList<String> keys = null;
	public static ArrayList<String> getKeyWords() {
		if(keys==null) {
			keys = new ArrayList<String>();
			keys.add("byte");
			keys.add("word");
			keys.add("dword");
			keys.add("eax");
			keys.add("ax");
			keys.add("ah");
			keys.add("al");
			keys.add("ebx");
			keys.add("bx");
			keys.add("bh");
			keys.add("bl");
			keys.add("ecx");
			keys.add("cx");
			keys.add("ch");
			keys.add("cl");
			keys.add("edx");
			keys.add("dx");
			keys.add("dh");
			keys.add("dl");
			keys.add("db");
			keys.add("dw");
			keys.add("dd");
		}
		return keys;
	}
	
	public static int getType(String t) {
		if(t.equalsIgnoreCase("db") || t.equalsIgnoreCase("al") || t.equalsIgnoreCase("bl") || t.equalsIgnoreCase("cl") || t.equalsIgnoreCase("dl") 
		|| t.equalsIgnoreCase("ah") || t.equalsIgnoreCase("bh") || t.equalsIgnoreCase("ch") || t.equalsIgnoreCase("dh") || t.equalsIgnoreCase("byte"))
			return BYTE;
		if(t.equalsIgnoreCase("dw") || t.equalsIgnoreCase("ax") || t.equalsIgnoreCase("bx") || t.equalsIgnoreCase("cx") || t.equalsIgnoreCase("dx") || t.equalsIgnoreCase("word"))
			return INT;
		if(t.equalsIgnoreCase("dd") || t.equalsIgnoreCase("eax") || t.equalsIgnoreCase("ebx") || t.equalsIgnoreCase("ecx") || t.equalsIgnoreCase("edx") || t.equalsIgnoreCase("dword"))
			return LONG;
		if(t.equalsIgnoreCase("si"))
			return b16;
		if(t.equalsIgnoreCase("di"))
			return b16;
		return 0;
	}
	
	public static String get(int t, String v) {
		if(v.equalsIgnoreCase("byte") || v.equalsIgnoreCase("word") || v.equalsIgnoreCase("dword")) {
			if(t == b8)
				return "byte";
			else if(t == b16)
				return "word";
			else if(t == b32)
				return "dword";
		}else if(v.equalsIgnoreCase("db") || v.equalsIgnoreCase("dw") || v.equalsIgnoreCase("dd")) {
			if(t == b8)
				return "db";
			else if(t == b16)
				return "dw";
			else if(t == b32)
				return "dd";
		}else if(v.equalsIgnoreCase("ah") || v.equalsIgnoreCase("al") || v.equalsIgnoreCase("ax") || v.equalsIgnoreCase("eax")) {
			if(t == b8)
				return "al";
			else if(t == b16)
				return "ax";
			else if(t == b32)
				return "eax";
		}else if(v.equalsIgnoreCase("bh") || v.equalsIgnoreCase("bl") || v.equalsIgnoreCase("bx") || v.equalsIgnoreCase("ebx")) {
			if(t == b8)
				return "bl";
			else if(t == b16)
				return "bx";
			else if(t == b32)
				return "ebx";
		}else if(v.equalsIgnoreCase("ch") || v.equalsIgnoreCase("cl") || v.equalsIgnoreCase("cx") || v.equalsIgnoreCase("ecx")) {
			if(t == b8)
				return "cl";
			else if(t == b16)
				return "cx";
			else if(t == b32)
				return "ecx";
		}else if(v.equalsIgnoreCase("dh") || v.equalsIgnoreCase("dl") || v.equalsIgnoreCase("dx") || v.equalsIgnoreCase("edx")) {
			if(t == b8)
				return "dl";
			else if(t == b16)
				return "dx";
			else if(t == b32)
				return "edx";
		}
		
		return v;
	}
	public static String getByte(String v) {
		return get(BYTE, v);
	}
	public static String getInt(String v) {
		return get(INT, v);
	}
	public static String getLong(String v) {
		return get(LONG, v);
	}
	public static String getMem(String v) {
		return get(MEM, v);
	}
	public static int size(int i) {
		i = INT;
		if(i == b8)
			return 1;
		if(i == b16)
			return 2;
		if(i == b32)
			return 4;
		return 0;
	}

	public static double highest(int i) {
		if(i == b8)
			return Math.pow(2, 8);
		if(i == b16)
			return Math.pow(2, 16);
		if(i == b32)
			return Math.pow(2, 32);
		return 0;
	}
	public static String getFunctionInstance(String s) {
		if(s.equalsIgnoreCase("al") || s.equalsIgnoreCase("ah"))
			return "ax";
		if(s.equalsIgnoreCase("bl") || s.equalsIgnoreCase("bh"))
			return "bx";
		if(s.equalsIgnoreCase("cl") || s.equalsIgnoreCase("ch"))
			return "cx";
		if(s.equalsIgnoreCase("dl") || s.equalsIgnoreCase("dh"))
			return "dx";
		return s;
	}
}
