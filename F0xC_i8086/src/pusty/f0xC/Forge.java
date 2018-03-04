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
		
		realString.add("org 0x7E00"); //Kernel start
		realString.add("bits 16");
		realString.add("CPU 8086");
		
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.pre(this);
		}
		
		for(Real real:realList) {
			String name = real.getName();
			if(name.contains("main")) {
				realString.add("call "+real.getLabel());
				realString.add("jmp $");
				break;
			}
		}
		for(Real real:realList) {
			String name = real.getName();
			for(String string:real.getP().get())
				pseudoString.add(string);
			if(name.startsWith("<")) continue;
			for(String string:real.get())
				realString.add(string);
		}
		realString.add("jmp $"); //SLEEP
		
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.impl(this);
		}
		
		
		realString.add("VARIABLES:");
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
					line = e.getKey()+" dw '"+e.getValue().toString().replaceAll("§42§", " ")+"', 0";
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
		//Imports
		INCLUDER.startInclude(this);
	
		for(ImplClass cl:CompilerF0xC.implClasses) {
			cl.post(this);
		}
	}
	
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
