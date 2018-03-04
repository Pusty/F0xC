package pusty.f0xC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import pusty.f0xC.kernel.Function;
import pusty.f0xC.kernel.INCLUDER;
import pusty.f0xC.kernel.Storable;

//Am I real or am I not?
public class Real {
	private List<String> real;
	private Pseudo ps;
	private String name;
	private String label;
	public Real(String n, Pseudo pseudo, String l) {
		real = new ArrayList<String>();
		ps = pseudo;
		name=n;
		label=l;
	}
	public String getName() {return name;  }
	public List<String> get() { return real; }
	public Pseudo getP() { return ps; }
	
	private String replace(String s, String key) {
		for(int a=0;a<s.length();a++) {
			if(a+key.length() > s.length()) break;
			String cont = s.substring(a, a+key.length());
			if(cont.equalsIgnoreCase(key)) {
				char a1 = ' ';
				char a2 = ' ';
				if(a>0)
					a1 = s.charAt(a-1);
				if(a+key.length() <s.length())
					a2 = s.charAt(a+key.length());
				if((a1 == ' ' || a1 == ',' ) && (a2 == ' ' || a2 == ',')) {
					String be = s.substring(0,a);
					String af = s.substring(a+key.length(), s.length());
					int type = RegSize.getType(key);
					s = be+"$"+RegSize.get(type, key)+"$"+af;
					return s;
				}
			}
		}
		return null;
	}
	
	private String loopString(String s, String key) {
		String text2 = replace(s, key);
		if(text2 != null) {
			s = text2;
			return s;
		}else
			return s;
	}
	
	String offSet = "";
	public void write(String s) {		
		
		for(String str:RegSize.getKeyWords()) {	
			while(!loopString(s,str).equalsIgnoreCase(s)) {
				s = loopString(s,str);
			}
		}	
		s = s.replaceAll(Pattern.quote("$"), "");
			

		
		//System.out.println(s);
		
		writeRaw(s);
	}
	public void writeRaw(String s) {
		get().add(offSet+s);
	}
	
	public void runThrough(HashMap<String,Object> v) {
		HashMap<String,Object> vars = new HashMap<String,Object>();
		if(v!=null)
			vars=v;
		for(String line:ps.get()) {
			String cmd = line;
			String[] args = null;
			if(line.split(" ").length>0) {
				String[] tmp = line.split(" ");
				cmd = tmp[0];
				args = new String[tmp.length-1];
				for(int i=0;i<tmp.length;i++) {
					if(i==0)continue;
					args[i-1]=tmp[i];
				}
			}
			switch(cmd) {
				case "start":
					write(args[0]+":");
					offSet="   ";
					break;
				case "lable":
					write(".l"+args[0]+":");
					break;
				case "jmp":
					if(!args[0].equalsIgnoreCase("jmp")) {
						write("pop ax");
						write("pop bx");
						write("cmp ax, bx");
					}
					write(args[0]+" .l"+args[1]);
					break;
				case "jmpz":
					write("pop ax");
					write("mov bx, 0");
					write("cmp bx, ax");
					write(args[0]+" .l"+args[1]);
					break;
				case "inc":
					write("add [stack_"+args[2]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"], word "+args[1]);
					break;
				case "stop":
					offSet=offSet.substring(0, offSet.length()-3);
					break;
				case "ret":
					write("ret");
					break;
				case "ihandle":
					//write("push word "+args[0]);
					write("mov ax, "+args[0]);
					write("push ax");
					break;
				case "zhandle":
				//	write("push word 0");
					write("mov ax, 0");
					write("push ax");
					break;
				case "lhandle":
				//	write("push dword "+args[0]);
					write("mov eax, "+args[0]);
					write("push eax");
					break;
				case "loadVar":
					Object obj = vars.get(args[0]);
					if(obj == null || obj instanceof Integer || obj instanceof Character || obj instanceof Short || obj instanceof Boolean || obj instanceof Byte)
//						write("push word ["+args[0]+"]");
					{
						write("mov ax, ["+args[0]+"]");
						write("push ax");
					}
					else
					{
						write("mov ax, "+args[0]);
						write("push ax");
					}
					//	write("push word "+args[0]);
					break;
				case "saveVar":
					write("pop ax");
					write("mov ["+args[0]+"], ax");
					break;
				case "lmul":
					write("pop eax");
					write("pop ebx");
					write("imul ebx");
					write("push eax");
					break;
				case "ldiv":
					write("pop ebx");
					write("pop eax");
					write("mov edx, 0");
					write("div ebx");
					write("push eax");
					break;
				case "lmod":
					write("pop ebx");
					write("pop eax");
					write("mov edx, 0");
					write("div ebx");
					write("push edx");
					break;
				case "ladd":
					write("pop eax");
					write("pop ebx");
					write("add eax, ebx");
					write("push eax");
					break;
				case "lsub":
					write("pop ebx");
					write("pop eax");
					write("sub eax, ebx");
					write("push eax");
					break;
				case "lneg":
					write("pop eax");
					write("neg eax");
					write("push eax");
				case "imul":
					write("pop ax");
					write("pop bx");
					write("imul bx");
					write("push ax");
					break;
				case "idiv":
					write("pop bx");
					write("pop ax");
					write("mov dx, 0");
					write("div bx");
					write("push ax");
					break;
				case "imod":
					write("pop bx");
					write("pop ax");
					write("mov dx, 0");
					write("div bx");
					write("push dx");
					break;
				case "iadd":
					write("pop ax");
					write("pop bx");
					write("add ax, bx");
					write("push ax");
					break;
				case "isub":
					write("pop bx");
					write("pop ax");
					write("sub ax, bx");
					write("push ax");
					break;
				case "ineg":
					write("pop ax");
					write("neg ax");
					write("push ax");
					break;
				case "dup":
					write("pop ax");
					write("push ax");
					write("push ax");
					break;
				case "pointer":
					if(args[0].equalsIgnoreCase("set")) {
						write("pop bx");
						write("pop si");
						write("mov [si], bx");
					}else if(args[0].equalsIgnoreCase("getValue")) {
						write("pop si");
						write("mov ax, [si]");
						write("push ax");
						//write("push word [si]");
					}else
						throw new Error("Pointer error, unknown cmd "+args[0]);
					break;
				case "storable":
					if(args[0].equalsIgnoreCase("set")) {
						storeToArray();
					}else if(args[0].startsWith("get") && !args[0].startsWith("getArray")) {
						loadFromArray();
					}else if(args[0].startsWith("getArray")) {
						
					}else
						throw new Error("Storable error, unknown cmd "+args[0]);
					break;
				case "addr":
					//push addr?
					break;
				case "istore":
					write("pop dx");
					write("mov [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"], dx");
					break;
				case "iload":
				//	write("push word [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"]");
					write("mov ax, [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"]");
					write("push ax");
					break;
				case "lstore":
					write("pop edx");
					write("mov [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.LONG)+"], edx");
					break;
				case "lload":
					//write("push dword [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.LONG)+"]");
					write("mov eax, [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.LONG)+"]");
					write("push eax");
					break;
				case "conv":
					if(args[0].equalsIgnoreCase("int")) {
						if(args[1].equalsIgnoreCase("long")) {
							//if(RegSize.INT != RegSize.b8) { //reg8 registers are the same as reg16 which makes these lines unnecessary for INT SIZE 8 BIT
								write("pop bx");
								write("and ebx, 0xFF");
								write("push ebx");
						//	}
						}else throw new Error("Can't convert "+args[0]+" to "+args[1]+" ("+args[1]+" not a found in "+args[0]+")");
					}else throw new Error("Can't convert "+args[0]+" to "+args[1]+" ("+args[0]+" not registered)");
					break;
				case "int":
					String func = args[0];
					INCLUDER.include(args[0]);		
					Function funct = INCLUDER.getFunc(args[0]);
					if(funct==null) throw new Error("Failed to include "+args[0]);
					if(funct.getInput().size()!=0) {
						for(int a=funct.getInput().size()-1;a>=0;a--) 
							writeRaw("pop "+RegSize.getFunctionInstance(funct.getInput().get(a)));
					}
					write("call "+func);
					if(funct!=null || funct.getOutput().size()==0) {
						for(int a=0;a<funct.getOutput().size();a++) 
							writeRaw("push "+RegSize.getFunctionInstance(funct.getOutput().get(a)));	
					}
					break;
				case "call":
					write("call "+args[0]);
					break;
				case "sta":
					storeToArray();
					break;
				case "loa":
					loadFromArray();
					break;
				case "alength":
					//As no internal arrays nor static array redeclarations are possible it's OK to use the fixed value
					write("pop ax");
					try {
						Object lastVar = CompilerF0xC.forge.vars.get(args[0]);
						if(lastVar.getClass().isArray())
							write("push "+((Object[])lastVar).length);
						else if(lastVar.getClass() == Storable.class || lastVar.getClass().getSuperclass() == Storable.class) 
							write("push "+((Storable)lastVar).getArray().length);
						else
							throw new Error("Can't push arraylength of "+lastVar+"("+lastVar.getClass()+")");
							
					}catch(Exception e) {
						e.printStackTrace();
					}
					break;
				case "pop":
					for(int i=0;i<Integer.parseInt(args[0]);i++)
						write("pop ax");
					break;
				case "reg":
					break;
				case "str":
					break;
				case "ca":
					break;
				case "new":
					break;
				default:
					throw new Error("Unknown cmd "+cmd);
			}
		}
	}
	

	private void loadFromArray() {
		write("pop ax");
		write("pop si");
		write("mov bx, "+RegSize.size(RegSize.INT)); //SIZE OF A WORD (2bytes)
		write("mul bx");
		write("add si, ax");
		//write("mov cx, [si]");
		write("mov ax, [si]");
		write("push ax");
		//write("push "+RegSize.getInt("word")+" [si]");
	}
	private void storeToArray() {
		write("pop cx");
		write("pop ax");
		write("pop si");
		write("mov bx, "+RegSize.size(RegSize.INT)); //SIZE OF A WORD (2bytes)
		write("mul bx");
		write("add si, ax");
		write("mov [si], cx");
	}
	
	
	
	public void print(String pre) {
		for(String s:get())
			System.out.println(pre+s);
	}
	public String getLabel() {
		return label;
	}
	
}
