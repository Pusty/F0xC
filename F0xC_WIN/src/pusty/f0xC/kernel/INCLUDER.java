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

		
		functionList.add(new Function("openFile"	,"file/openFile.asm").setPushable().addOutput("eax"));
		functionList.add(new Function("closeFile"	,"file/closeFile.asm").setPushable());
		functionList.add(new Function("lengthFile"	,"file/lengthFile.asm").setPushable().addOutput("eax"));
		functionList.add(new Function("readFile"	,"file/readFile.asm").setPushable());
		
		functionList.add(new Function("setOutput"	,"util/setOutput.asm").setPushable());
		functionList.add(new Function("setConsoleTitle"	,"util/setConsoleTitle.asm").setPushable());
		
		functionList.add(new Function("newLine"		,"util/newLine.asm"));

		functionList.add(new Function("printString"	,"util/printString.asm").setPushable());
		functionList.add(new Function("printChar"	,"util/printChar.asm").setPushable());
		functionList.add(new Function("printInt"	,"util/printInt.asm").setPushable().addDependency("util/printChar.asm", "util/mathPow.asm"));
		functionList.add(new Function("readConsole"	,"util/readConsole.asm").setPushable());
		functionList.add(new Function("readConsoleLine"	,"util/readConsoleLine.asm").addOutput("eax"));
		
		functionList.add(new Function("mathPow"			,"util/mathPow.asm").addInput("eax","ebx").addOutput("eax"));
		
		functionList.add(new Function("createWindowClass"	,"window/createWindowClass.asm").setPushable());
		functionList.add(new Function("createWindow"	,"window/createWindow.asm").setPushable().addOutput("eax"));
		functionList.add(new Function("showWindow"	,"window/showWindow.asm").setPushable());
		functionList.add(new Function("updateWindow"	,"window/updateWindow.asm").setPushable());
		functionList.add(new Function("handleMsg"	,"window/handleMsg.asm").setPushable().addOutput("eax"));
		functionList.add(new Function("drawText"	,"window/drawText.asm").setPushable());
		
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
