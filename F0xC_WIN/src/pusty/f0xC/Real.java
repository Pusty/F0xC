package pusty.f0xC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import pusty.f0xC.kernel.Function;
import pusty.f0xC.kernel.INCLUDER;

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
			int size = 0;
			int effective_size = 0;
			switch(cmd) {
				case "start":
					write(args[0]+":");
//					offSet="   ";
					write("push dword [std_memory]");
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
				case "jmpnull":
					write("pop eax");
					write("mov ebx, 0");
					write("cmp ebx, eax");
					write("je .l"+args[1]);
					break;
				case "jmpnotnull":
					write("pop eax");
					write("mov ebx, 0");
					write("cmp ebx, eax");
					write("jne .l"+args[1]);
					break;
				case "inc":
					write("add [stack_"+args[2]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"], word "+args[1]);
					break;
				case "stop":
//					offSet=offSet.substring(0, offSet.length()-3);
					break;
				case "ret":
					write("pop dword [std_memory]");
					write("ret");
					break;
				case "retArray":
//					write("pop dword [std_memory]");
					write("pop eax");
					write("ret");
					break;
				case "ihandle":
					write("push word "+args[0]);
					break;
				case "zhandle":
					write("push word 0");
					break;
				case "lhandle":
					write("push dword "+args[0]);
					break;
				case "loadVar":
					Object obj = vars.get(args[0]);
					if(obj == null || obj instanceof Integer || obj instanceof Character || obj instanceof Short || obj instanceof Boolean || obj instanceof Byte)
						write("push word ["+args[0]+"]");
					else
						write("push word "+args[0]);
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
				case "iand":
					write("pop ax");
					write("pop bx");
					write("and ax, bx");
					write("push ax");
					break;
				case "ixor":
					write("pop ax");
					write("pop bx");
					write("xor ax, bx");
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
					if(args[0].startsWith("set")) {
						write("pop ebx");
						write("pop esi");
						write("mov [esi], ebx");
					}else if(args[0].startsWith("getValue")) {
						write("pop esi");
						write("push dword [esi]");
					}else if(args[0].startsWith("read")) {
						write("pop esi");
						write("mov esi, [esi]");
						write("push word [esi]");
					}else if(args[0].startsWith("write")) {
						write("pop ebx");
						write("pop esi");
						write("mov esi, [esi]");
						write("mov [esi], ebx");
					}else
						throw new Error("Pointer error, unknown cmd "+args[0]);
					break;
				case "storable":
					if(args[0].equalsIgnoreCase("set")) {
						storeToArray(10);
					}else if(args[0].startsWith("get") && !args[0].startsWith("getArray")) {
						loadFromArray(10);
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
				case "bstore":
					write("pop dx");
					write("mov [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.BYTE)+"], dx");
					break;
				case "iload":
					write("push word [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.INT)+"]");
					break;
				case "lstore":
					write("pop edx");
					write("mov [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.LONG)+"], edx");
					break;
				case "lload":
					write("push dword [stack_"+args[1]+"+"+Integer.parseInt(args[0])*+RegSize.size(RegSize.LONG)+"]");
					break;
				case "conv":
					if(args[0].equalsIgnoreCase("int")) {
						if(args[1].equalsIgnoreCase("long")) {
							//if(RegSize.INT != RegSize.b8) { //reg8 registers are the same as reg16 which makes these lines unnecessary for INT SIZE 8 BIT
//								write("pop bx");
//								write("and ebx, 0xFF");
//								write("push ebx");
						//	}
						}else if(args[1].equalsIgnoreCase("byte")) {
							write("pop ebx");
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
					if(funct.getInput().size()!=0 && !funct.isPushable()) {
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
					storeToArray(Integer.parseInt(args[0]));
					break;
				case "loa":
					loadFromArray(Integer.parseInt(args[0]));
					break;
				case "alength":
					//As no internal arrays nor static array redeclarations are possible it's OK to use the fixed value
					write("pop ax");
					{
						write("mov eax, [eax]");
						write("push eax");
					}
					break;
				case "pop":
					for(int i=0;i<Integer.parseInt(args[0]);i++)
						write("pop ax");
					break;
				case "reg":
					break;
				case "str":
					//args[0] string content -> input
					// ouput -> add
					args[0] = args[0].replaceAll("§42§", " ");
					write("mov edi, [std_memory]");
					for(int i=0;i<args[0].length();i++) {
						write("mov [edi], $byte$ "+(int)args[0].charAt(i));
						write("inc edi");
//						write("mov $al$, "+(int)args[0].charAt(i));
//						write("stosb");
					}
					write("mov [edi], $byte$ 0");
					write("add edi, 1");
					write("push dword [std_memory]");
					write("mov [std_memory], edi");
					break;
				case "str_length":
					String label = createLabel();
					write("pop esi");
					write("mov eax, esi");
					write(label+": inc esi");
					write("cmp [esi], $byte$ 0");
					write("jne "+label);
					write("sub esi, eax");
					write("push dword esi");
					break;
				case "str_chars":
					write("pop esi"); //char array
					write("pop edi"); //string position
					
					write("mov ecx, [esi]"); //char length
					
					/*
					for(int i=0;i<args[0].length();i++) {
						write("mov [edi], $byte$ "+(int)args[0].charAt(i));
						write("inc edi");
					}*/
					write("add esi, 4");
					String label3 = createLabel();
						
						write(label3+":");
						//write("mov $al$, [esi]");
						write("mov $al$, [esi]");
						write("mov [edi], $byte$ $al$");
						write("add edi, 1");
						write("add esi, 1");
						write("loop "+label3);
					
					write("mov [edi], $byte$ 0");
					write("inc edi");
					//write("push dword [std_memory]");
					write("mov [std_memory], edi");
					break;
				case "ca":
					size = Integer.parseInt(args[0]);
					if(size == 5)
						effective_size = 1;
					else if(size == 8)
						effective_size = 1;
					else if(size == 10)
						effective_size = 4;
					// ouput -> address
					write("pop eax");
					write("mov edi, [std_memory]");
					
					write("mov [edi], eax");
					write("add edi, 4");
					
					write("mov ecx, eax");
					String label2 = createLabel();
					if(effective_size == 1)
						write(label2+": mov [edi], $byte$ 0");
					else if(effective_size == 2)
						write(label2+": mov [edi], $word$ 0");
					else if(effective_size == 4)
						write(label2+": mov [edi], $dword$ 0");
					write("add edi, "+effective_size);
					write("loop "+label2);
					
					//write("add edi, eax*4");
					write("push dword [std_memory]");
					write("mov [std_memory], edi");
					break;
				case "new":
					write("push dword [std_memory]");
					break;
				default:
					throw new Error("Unknown cmd "+cmd);
			}
		}
	}
	

	private void loadFromArray(int i) {
		int size = 0;
		if(i == 5)
			size = 1;
		else if(i == 8)
			size = 1;
		else if(i == 10)
			size = 4;
		else if(i == -5)
			size = 1;
		write("pop ax");
		write("pop si");
		
		if(i>=0)
			write("inc eax");
		else //Stringy things and stuff
			write("sub eax, 3");
		
		write("mov bx, "+size); //SIZE OF A WORD (2bytes)
		write("mul bx");
		write("add si, ax");
		//write("mov cx, [si]");
		if(size == 1) {
			write("mov eax, [esi]");
			write("and eax, 0xFF000000");
			write("shr eax, 24");
			write("push eax");
		}else if(size == 4) {
			write("push dword [esi]");
		}
			
	}
	private void storeToArray(int i) {
		int size = 0;
		if(i == 5)
			size = 1;
		if(i == 8)
			size = 1;
		if(i == 10)
			size = 4;
		write("pop ecx");
		write("pop eax");
		write("pop esi");
//		write("inc eax");
		write("mov ebx, "+size); //SIZE OF A WORD (2bytes)
		write("mul ebx");
		write("add eax, 4");
		write("add esi, eax");
		if(size == 1)
			write("mov [esi], $cl$");
		else if(size == 2)
			write("mov [esi], $cx$");
		else if(size == 4)
			write("mov [esi], $ecx$");
	}
	
	Random random = new Random();
	public String createLabel() {
		return ".lT"+random.nextInt(Integer.MAX_VALUE);
	}
	
	public void print(String pre) {
		for(String s:get())
			System.out.println(pre+s);
	}
	public String getLabel() {
		return label;
	}
	
}
