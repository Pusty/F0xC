package pusty.f0xC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import pusty.f0xC.kernel.INCLUDER;
import pusty.f0xC.kernel.ImplClass;
import pusty.f0xC.kernel.Loadable;
import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.kernel.Storable;
import pusty.f0xC.util.Null;


public class Forge {
	private List<String> realString;
	private List<String> pseudoString;
	
	HashMap<String, Object> vars;
	List<Real> realList;
	public Forge() {
		vars = new HashMap<String, Object>();
		realList = new ArrayList<Real>();
		realString = new ArrayList<String>();
		pseudoString = new ArrayList<String>();
	}
	public List<Real> getReals() {
		return realList;
	}
	public  HashMap<String, Object> getVars() {
		return vars;
	}
	public void setVars(HashMap<String, Object> h) {
		vars = h;
	}
	public void add(Real r) {
		realList.add(r);
	}
	public void forge() throws IOException {
		
		INCLUDER.deleteTemp();
		
		
		String mainFunction = "";
		
		realString.add("global _main"); //Exe start
		realString.add("section .text ");
		
		for(Real real:realList) {
			String name = real.getName();
			if(name.contains("main")) {
				mainFunction = real.getLabel();
				break;
			}
		}
		
		
		realString.add("_main:");
			realString.add("	extern _GetStdHandle@4");
		
			realString.add("	push dword -11");
			realString.add("	call _GetStdHandle@4");
			realString.add("	mov [std_output], eax");
		
			realString.add("	push dword -10");
			realString.add("	call _GetStdHandle@4");
			realString.add("	mov [std_input], eax");
		
			realString.add("	extern _GetModuleHandleA@4");
			realString.add("	push dword 0");
			realString.add("	call _GetModuleHandleA@4");
			realString.add("	mov [std_handle], eax");
			
			realString.add("	extern _GetCommandLineA@0");
			realString.add("	call _GetCommandLineA@0");
			realString.add("	mov [std_cmd], eax");			
			
			realString.add("	call "+mainFunction);
			realString.add("	extern _ExitProcess@4");
			realString.add("	push dword [std_error]");
			realString.add("	call _ExitProcess@4");
		realString.add("ret");
			
		
		
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.pre(this);
		}
		

		for(Real real:realList) {
			String name = real.getName();
			for(String string:real.getP().get())
				pseudoString.add(string);
			if(name.startsWith("<")) continue;
			for(String string:real.get()) 
				realString.add(string);

		}
//		realString.add("jmp $"); //SLEEP
		
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.impl(this);
		}
		
		//Imports
		INCLUDER.startInclude(this);
		
	    
		realString.add("section .data");
		realString.add("std_output dd 0"); //number for default output
		realString.add("std_input dd 0"); //number for default input
		realString.add("std_error dd 0"); //error level
		realString.add("std_handle dd 0"); //module handler
		realString.add("std_cmd dd 0"); //command line
		realString.add("std_memory dd memory"); //current position in memory
		for(Entry<String, Object> e:vars.entrySet()) {
			String line = "";
			if(e.getValue()!=null && e.getValue().getClass().isArray()) {
				
				if(e.getValue().getClass().getComponentType().hashCode() == Integer.class.hashCode()) {
					line = e.getKey()+" "+RegSize.getInt("dw")+" ";
					Object[] os = (Object[]) e.getValue();
					for(int i=0;i<os.length;i++) {
						line = line+ (int)((os[i]==null)?0:os[i]);
						if(i<os.length-1) 
							line = line+ ", ";
					}
				}else if(e.getValue().getClass().getComponentType().hashCode() == Long.class.hashCode()) {
					line = e.getKey()+" "+RegSize.getInt("dd")+" ";
					Object[] os = (Object[]) e.getValue();
					for(int i=0;i<os.length;i++) {
						line = line+ (long)((os[i]==null)?0L:os[i]);
						if(i<os.length-1) 
							line = line+ ", ";
					}
				}else throw new Error("Unknown Array Type: "+e.getValue());
				
				
				
			}else  {
				if(e.getValue() != null && e.getValue() instanceof String) {
					line = e.getKey()+" db '"+e.getValue().toString().replaceAll("§42§", " ")+"', 0";
				}else if(e.getValue() == null || e.getValue() instanceof Integer)
					line = e.getKey()+" "+RegSize.getInt("dw")+" "+(int)((e.getValue()==null)?0:e.getValue());
				else if(e.getValue() instanceof Long)
					line = e.getKey()+" "+RegSize.getInt("dd")+" "+(long)((e.getValue()==null)?0L:e.getValue());
				else if(e.getValue() instanceof Null) {
					line = ";set "+e.getKey()+" to "+e.getValue();
				}
				else if(e.getValue() instanceof Loadable) {
					Loadable load = (Loadable) e.getValue();
					try {
						load.testContent();
						load.load();
						load.testRes();
						line = load.getKey(e.getKey())+" incbin '"+load.getPath()+"'";
					} catch (Exception e1) {
						e1.printStackTrace();
						line = e.getKey()+" dw 0";
					}
					
				}
				else if(e.getValue() instanceof Pointer) {
					Pointer pointer = (Pointer) e.getValue();
					String str = "0";
					if(!pointer.getPointer().trim().equalsIgnoreCase(""))
						str = pointer.getPointer();
					line = e.getKey()+" "+RegSize.getMem("dw")+" "+str;
				}
				else if(e.getValue() instanceof Storable) {
					Storable storable = (Storable) e.getValue();
					String str = "";
					for(int a=0;a<storable.getArray().length;a++)
						if(a==storable.getArray().length-1)
							str = str+storable.get(a)+"";
						else
							str = str+storable.get(a)+",";
					line = e.getKey()+" "+RegSize.getInt("dw")+" "+str;
				}
				else throw new Error("Unknown Variable Type: "+e.getValue());
			}
			realString.add(line);
		}
	
		
		//PREDEFINED FIELDS
		realString.add("section .bss");
		realString.add("std_tmp resb 16"); //temp data for functions
		realString.add("std_addr resb 16"); //temp address to get back to main function
		realString.add("std_read resb 4"); //amount of read bytes (to block overflow)
		realString.add("memory resb "+BUFFER_SIZE); //memory for random data (w/o garbage collector)
		
		
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.post(this);
		}
	}
	
	public static int BUFFER_SIZE = 1024;
	
	  public void printFile(String path) {
	       try 
	       {
	    	   System.out.println(path);
	           BufferedWriter writer = new BufferedWriter(new FileWriter(path));
	           for (String string : realString)
	           {
	               writer.write(string);
	               writer.newLine();
	           }
	           writer.close();
	       }
	       catch (IOException e)
	       {
	           throw new RuntimeException("Unable to write output file " + path, e);
	       }
	   }
	
	public void print(String pre) {
		for(String s:realString)
			System.out.println(pre+s);
	}
	public List<String> getContent() {
		return realString;
	}
}
