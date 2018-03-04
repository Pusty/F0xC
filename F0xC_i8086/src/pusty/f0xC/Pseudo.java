package pusty.f0xC;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.kernel.Storable;

public class Pseudo {
	private List<String> pseudo;
	public Pseudo() {
		pseudo = new ArrayList<String>();
	}
	public List<String> get() { return pseudo; }
	public void write(String s) {
		get().add(s);
	}
	public void reg(FieldNode field) {
		write("reg "+field.name);
	}
	public void start(MethodNode m) {
		write("start "+CompilerF0xC.methodList.get(m));
	}
	public void stop(MethodNode m) {
		write("stop "+CompilerF0xC.methodList.get(m));
	}
	public void ret() {
		write("ret");
	}
	public void addInt() {
		write("iadd");
	}
	public void subInt() {
		write("isub");
	}
	public void mulInt() {
		write("imul");
	}
	public void divInt() {
		write("idiv");
	}
	public void modInt() {
		write("imod");
	}
	public void negInt() {
		write("ineg");
	}
	public void addLong() {
		write("ladd");
	}
	public void subLong() {
		write("lsub");
	}
	public void mulLong() {
		write("lmul");
	}
	public void divLong() {
		write("ldiv");
	}
	public void modLong() {
		write("lmod");
	}
	public void negLong() {
		write("lneg");
	}
	public void dup() {
		write("dup");
	}
	public void storeToArray() {
		write("sta"); //STore Array
	}
	public void loadFromArray() {
		write("loa"); //LOad Array
	}
	public void createArray() {
		write("ca");
	}
	public void handleInt(int num) {
		if(num>=RegSize.highest(RegSize.INT))
			throw new Error("Loading Int is too big "+num);
		write("ihandle "+num);
	}
	public void handleZero() {
		write("zhandle");
	}
	public void storeTo(int index, MethodNode methodNode) {
		write("istore "+index+" "+CompilerF0xC.getLabel(methodNode));
	}
	public void loadFrom(int index, MethodNode methodNode) {
		write("iload "+index+" "+CompilerF0xC.getLabel(methodNode));
	}
	public void retInt(MethodNode methodNode) {
		write("istore 0 "+CompilerF0xC.getLabel(methodNode));
		write("ret");
	}
	
	public void handleLong(long n) {
		if(n>=RegSize.highest(RegSize.LONG))
			throw new Error("Loading Long is too big "+n);
		write("lhandle "+n);
	}
	public void storeLong(int index, MethodNode m) {
		write("lstore "+index+" "+CompilerF0xC.getLabel(m));
	}
	public void loadLong(int index, MethodNode m) {
		write("lload "+index+" "+CompilerF0xC.getLabel(m));
	}
	
	public void saveStatic(FieldInsnNode field) {
		write("saveVar "+field.name);
	}
	public void loadStatic(FieldInsnNode field) {
		write("loadVar "+field.name);
	}
	
	public void invokeStatic(MethodInsnNode m) {
		write("int "+m.name);
	}
	
	public void invokeVirtual(MethodInsnNode m) {
		boolean invoke=false;
		if(m.name.equalsIgnoreCase("getAddr")) {
			write("addr "+m.name);
			invoke=true;
		}
		
		try {
			Class<?> clas = Class.forName(m.owner.replace('/', '.'));
			Class<?> cla = clas;
			if(CompilerF0xC.DEBUG)
				System.out.println("invokeVirtual: "+cla);
			Class<?> dec = null;
			for(Method met:clas.getMethods()) 
				if(met.getName().equalsIgnoreCase(m.name)) {
					Type[] params = Type.getArgumentTypes(m.desc);
					Class<?>[] para = met.getParameterTypes();
					if(params.length == para.length) {
						boolean ok = true;
						for(int a=0;a<para.length;a++) {
							if(!params[a].getClassName().equalsIgnoreCase(para[a].getName()))
								ok = false;
						}
						if(ok)
							dec = met.getDeclaringClass();
					}
				}
			while(cla.getSuperclass() != null) {
				if(cla == Storable.class && dec == Storable.class) {
					invoke=true;
					write("storable "+m.name);
				}
				if(cla == Pointer.class && dec == Pointer.class) {
					invoke=true;
					write("pointer "+m.name);
				}
				cla = cla.getSuperclass();
			}
			
		} catch (Exception e) { }
		
		if(!invoke)
			throw new Error("Can't Virtual Invoke "+m.owner+", "+m.name);
	}
	
	public void conv(String str) {
		write("conv "+str);
	}
	
	public void lable(LabelNode insn) {
		write("lable "+insn.getLabel());
	}
	
	public void setString(LdcInsnNode insn) {
		write("str "+insn.cst.toString().replaceAll(" ", "§42§"));
	}
	
	public void print(String pre) {
		for(String s:get())
			System.out.println(pre+s);
	}
	public void jump(String s, LabelNode label) {
		write("jmp "+s+" "+label.getLabel());
	}
	public void inc(IincInsnNode insn, MethodNode m ) {
		write("inc "+insn.var+" "+insn.incr+" "+CompilerF0xC.getLabel(m));
	}
	public void jumpZero(String s, LabelNode label) {
		write("jmpz "+s+" "+label.getLabel());
	}
	public void createObject(String desc) {
		write("new "+desc);
	}
	public void callStatic(MethodInsnNode m, MethodNode methodNode) {
        Type[] params = Type.getArgumentTypes(methodNode.desc);
        if(params.length>0) {
        	for(int i=params.length-1;i>=0;i--) 
        		write("istore "+i+" "+CompilerF0xC.getLabel(methodNode));
        }
        String label = CompilerF0xC.getLabel(m);
        if(label==null)
        	label = m.name;
		write("call "+label);
		if(Type.getReturnType(methodNode.desc).getSort() != Type.VOID) {
			if(Type.getReturnType(methodNode.desc).getSort() == Type.OBJECT) {
				throw new Error("Can't return Object type");
			}else if(Type.getReturnType(methodNode.desc).getSort() == Type.LONG) {
				throw new Error("Can't return Long type");
			}else {
				write("iload 0 "+CompilerF0xC.methodList.get(methodNode));
			}
		}
	}
	public void pop(int i) {
		write("pop "+i);
	}
	public void alength(String la) {
		write("alength "+la);
	}
}
