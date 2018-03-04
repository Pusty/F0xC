package pusty.f0xC.kernel;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import pusty.f0xC.CompilerF0xC;
import pusty.f0xC.Forge;

public class INCLUDER {
	
	static List<Function> functionList = null;
	
	public static void initIncluder() {
		if(functionList!=null) return;
		functionList =  new ArrayList<Function>();
		//UTIL
		functionList.add(new Function("checkKey"	,"util/checkKey.asm").addInput("ax").addOutput("ax"));
		functionList.add(new Function("resetCursor"	,"util/resetCursor.asm"));
		functionList.add(new Function("halt"		,"util/halt.asm"));
		functionList.add(new Function("newLine"		,"util/newLine.asm"));
		functionList.add(new Function("reboot"		,"util/reboot.asm"));
		functionList.add(new Function("printChar"	,"util/printChar.asm").addInput("al"));
		functionList.add(new Function("sleep"		,"util/sleep.asm").addInput("si"));
		functionList.add(new Function("printBin"	,"util/printBin.asm").addInput("si").addDependency("util/printChar.asm"));
		functionList.add(new Function("printHex"	,"util/printHex.asm").addInput("si").addDependency("util/printChar.asm"));
		functionList.add(new Function("printString"	,"util/printString.asm").addInput("si").addDependency("util/printChar.asm"));
		functionList.add(new Function("printInt"	,"util/printInt.asm").addInput("si").addDependency("util/printChar.asm").addDependency("util/mathPow.asm"));
		functionList.add(new Function("lengthInt"	,"util/printInt.asm").addInput("si").addOutput("bx").addDependency("util/printChar.asm").addDependency("util/mathPow.asm"));
		
		functionList.add(new Function("pow16"		,"util/mathPow.asm").addInput("ax","bx").addOutput("ax"));
		functionList.add(new Function("pow8"		,"util/mathPow.asm").addInput("al","bl").addOutput("al"));
		
		//GRAPHICS
		functionList.add(new Function("initGraphics","graphics/initGraphics.asm"));
		functionList.add(new Function("memoryDump"	,"graphics/memoryDump.asm").addDependency("graphics/initGraphics.asm"));
		functionList.add(new Function("loadPal"		,"graphics/loadPal.asm").addInput("si"));
		
		//BUFFER
		functionList.add(new Function("flip"		,"graphics/buffer.asm"));
		functionList.add(new Function("resetBuffer"	,"graphics/buffer.asm"));
		functionList.add(new Function("drawBoxBuffer","graphics/buffer.asm").addInput("di","ax","bx","cx","dx"));
		functionList.add(new Function("drawBox"		,"graphics/buffer.asm").addInput("di","ax","bx","cx","dx"));
		functionList.add(new Function("drawImage"	,"graphics/buffer.asm").addInput("di","ax","bx"));
	}
	
	public static void addFunction(Function f) {
		functionList.add(f);
	}
	
	public static List<Function> getFunctions() {
		return functionList;
	}

	static List<String> includeThis = new ArrayList<String>();
	static List<String> included = new ArrayList<String>();
	
	public static boolean include(String s) {
		boolean include=false;
		if(!includeThis.contains(s)) {
			for(Function func:functionList) {
				if(include && func.getName().equalsIgnoreCase(s))
					throw new Error(s+" imported twice?");
				if(func.getName().equalsIgnoreCase(s))
					include=true;
			}
			if(include) {
				
				includeThis.add(s);
			}
		}
		return include;
	}
	public static Function getFunc(String name) {
			for(Function func:functionList) 
				if(func.getName().equalsIgnoreCase(name))
					return func;		
			return null;
	}
	public static void startInclude(Forge forge) throws IOException {
		forge.getContent().add("resw 0");
		forge.getContent().add("INCLUDES:");
		for(String in:includeThis) {
			Function funct = null;
			for(Function func:functionList) {
				if(func.getName().equalsIgnoreCase(in))
					funct = func;
			}
			List<String> inc = new ArrayList<String>();
			inc.add(funct.getFile());
			inc.addAll(funct.getDependency());
			for(String i:inc) {
				File f = inInclude(i);
				if(!included.contains(f.getName())) {
					File to = inTemp(f);
					OutputStream os = new FileOutputStream(to);
					Files.copy(f.toPath(), os);
					forge.getContent().add("%include '"+f.getName()+"'");
					included.add(f.getName());
					os.close();
				}
			}
		}
	}
	public static File tempFile() {
		File temp = new File(CompilerF0xC.outFile.getParentFile().getAbsoluteFile()+"/temp");
		if(!temp.exists()) temp.mkdir();
		return temp;
	}
	public static File inTemp(File f) {
		return new File(tempFile().getAbsoluteFile()+"/"+f.getName());
	}
	public static File inTemp(String s) {
		return new File(tempFile().getAbsoluteFile()+"/"+s);
	}
	public static File resFile() {
		File res = new File(CompilerF0xC.outFile.getParentFile().getAbsoluteFile()+"/res");
		if(!res.exists()) res.mkdir();
		return res;
	}
	public static File inRes(File f) {
		return new File(resFile().getAbsoluteFile()+"/"+f.getName());
	}
	public static File inRes(String s) {
		return new File(resFile().getAbsoluteFile()+"/"+s);
	}
	public static File inInclude(String i) {
		return new File(CompilerF0xC.outFile.getParentFile().getAbsoluteFile()+"/include/"+i);
	}
	public static void moveOToT() throws IOException {
		File f = CompilerF0xC.outFile;
		File t =  inTemp(f);
		OutputStream os = new FileOutputStream(t);
		Files.copy(f.toPath(), os);
		os.close();
	}
	
	public static void deleteTemp() {
		File temp = tempFile();
		if(temp.exists() && temp.isDirectory()) {
			for(File f:temp.listFiles())
				f.delete();
			temp.delete();
		}
	}
	public static void stopInclude() {
		deleteTemp();
	}
}
