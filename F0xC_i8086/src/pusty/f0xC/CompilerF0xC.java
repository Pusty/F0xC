package pusty.f0xC;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Textifier;

import pusty.f0xC.kernel.INCLUDER;
import pusty.f0xC.kernel.ImplClass;
import pusty.f0xC.kernel.Interrupt;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.io.*;

public class CompilerF0xC {
	
	public static Pseudo curPseudo = new Pseudo();
	public static Forge forge = new Forge();
	
	public static List<ImplClass> implClasses = new ArrayList<ImplClass>();
	
	public static void addImpl(ImplClass impl) {
		implClasses.add(impl);
	}
	
	
	public static boolean DEBUG = false;
	public static boolean CUTTER = true;
	
	static HashMap<MethodNode, String> methodList = new HashMap<MethodNode, String>();
	static HashMap<String,Object> hash = new HashMap<String,Object>();
	
	public static void setCutter(boolean b) {
		CUTTER = b;
	}
	
	public static File outFile;
	public static File inFile;
	public static File dir;
	
	//Highest stored or loaded index to make save no memory errors happen
	
	public static void main(String args[]) throws Exception
    {
		if(args.length<1) throw new Exception("Not enough arguments. At least 1 needed. (InputFile, OutputFile)");
		inFile = new File(args[0]);
		String newFileName =  inFile.getName();
		if(inFile.getName().split(Pattern.quote(".")).length!=0)
			newFileName = newFileName.split(Pattern.quote("."))[0];
		outFile = new File(inFile.getParentFile()+"/"+newFileName+".asm");
		if(args.length>1)
			outFile = new File(args[1]);
		dir = inFile.getParentFile();
			
		INCLUDER.initIncluder();
		//Compile input file
		compile(inFile);
		
    }
	
	public static void createASM() {
		forge.printFile(outFile.getAbsolutePath());
		try {
			INCLUDER.moveOToT();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createBIN() {
		File fi = INCLUDER.inTemp("output.bin");
		fi.delete();
		ExportHelper.nasmKernel(outFile, forge.getContent());
	}
	
	public static void createKER() {
		try {
			ExportHelper.createKernel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isCompiled() {
		File fi = new File(outFile.getParentFile()+"/output.bin");
		return fi.exists();
	}
	
	public static void setDebug(boolean b) {
		DEBUG = b;
	}
	
	public static boolean isDebug() {
		return DEBUG;
	}
	
	public static byte[] readFile(File f) {
        try  {
            FileInputStream input = new FileInputStream(f);
            byte bytes[] = new byte[(int)f.length()];
            input.read(bytes);
            input.close();
            return bytes;
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }
	
	public static void compile(File input) throws Exception {
		ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(readFile(input));
       
        visitClass(reader, node);
        //hash.put("lstack", new Long[] {0L,0L,0L,0L}); //Needed for the support of long variables
        //hash.put("stackIndex", 0);
        forge.setVars(hash);
        
        for(Real r:forge.getReals())
            r.runThrough(forge.getVars());
        
        forge.forge();
        if(CUTTER) {
	        Cutter cutter = new Cutter(forge);
	        cutter.cut();
        }
	}
	
	public static void visitClass(String file) {
		try {
			String clName = Class.forName(file.replace('/', '.')).getName();
			ClassReader reader = new ClassReader(clName);
			ClassNode node = new ClassNode();
			visitClass(reader, node);
		} catch (Exception e) {
			System.err.println("WARNING: Skipping init of "+file);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void visitClass(ClassReader reader, ClassNode node) throws Exception {
		 reader.accept(node, 0);
	        startProgram(node);
	        
	        Pseudo varPseudo = new Pseudo();
	        //variables
	        curPseudo = varPseudo;
	        for(FieldNode field: (List<FieldNode>) node.fields) {
	        	if((field.access & Opcodes.ACC_STATIC) == 0) continue;
	        	//only register static stuff
	        	registerField(field);
	        }
	        for (MethodNode method : (List<MethodNode>)node.methods)  {
	            method.visitCode();
	            if(noMaskList.contains(method.name)) {
	            	methodList.put(method, method.name);
	            	System.err.println("Exeption for "+method.name+" registered");
	            }else
	            	methodList.put(method, ((LabelNode)method.instructions.get(0)).getLabel().toString());
	        }
	        //methodes
	        for (MethodNode method : (List<MethodNode>)node.methods)  {
	            method.visitCode();
	            if ((method.access & Opcodes.ACC_NATIVE) != 0) continue;
	            //only register static stuff
	            if ((method.access & Opcodes.ACC_STATIC) == 0) continue;
	            
	            curPseudo = new Pseudo();
	            if(method.name.equalsIgnoreCase("<clinit>"))  {
	            	curPseudo = varPseudo;
	            }
	            if(method.name.equalsIgnoreCase("main")) {
	            	continue;
	            }
	 
	            visitMethod(node, method);
	            
	            if(method.name.equalsIgnoreCase("<clinit>")) {
	            	hash.putAll(CLINIT.run(varPseudo));
	            }
	            if(!DEBUG) {
		            Real r = new Real(method.name,curPseudo,getLabel(method));
		            forge.add(r);
	            }
	        }
	        stopProgram(node);
	}
	
	
	public static void visitMethod(ClassNode clNode, MethodNode method) throws Exception{
		// prologue
        startMethod(method);
        String lastAccecedVariable = null;
        for(AbstractInsnNode ins:method.instructions.toArray()) {
        	printInsn(ins);
        	if (ins instanceof LabelNode){
        		LabelNode insn = (LabelNode) ins;
        		label(insn);
        	}
            else if (ins instanceof LineNumberNode) {      	
            }
            else if (ins instanceof VarInsnNode) { 
                VarInsnNode varinsn = (VarInsnNode) ins;
                switch(varinsn.getOpcode())  {
                    case Opcodes.ILOAD:
                    case Opcodes.ALOAD:
                    	loadFrom(varinsn.var, method);
                        break;

                    case Opcodes.ISTORE:
                    case Opcodes.ASTORE:
                    	storeTo(varinsn.var, method);
                        break;
                    case Opcodes.LLOAD:
                    	loadLong(varinsn.var, method);
                    	 break;
                    case Opcodes.LSTORE:
                    	storeLong(varinsn.var, method);
                    	 break;

                    default:
                  	  throw new Exception("Can't deal with varinsnnode: " + varinsn.getOpcode());
                }
            }
            else if (ins instanceof MethodInsnNode) {  
            	MethodInsnNode insn = (MethodInsnNode)ins;
            	if(insn.getOpcode() == Opcodes.INVOKESPECIAL) {
            		//super invokes I think?
            		if(insn.name.equalsIgnoreCase("<init>")){
//            			visitClass(insn.owner);
            			System.err.println("WARNING: Skipping init of "+insn.owner);
            		}else throw new Error("Can't handle invoke special "+insn.name+", "+insn.owner+":"+insn.desc);      	
            	}else
            		if(insn.getOpcode() == Opcodes.INVOKESTATIC) {
            		invokeStatic(insn, methodList);
            	}else if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            		invokeVirtual(insn);
            	}else throw new Error("Can't handle MethodNode "+ins.getOpcode());      	
            }
            else if (ins instanceof IntInsnNode) {  
            	IntInsnNode intInsn = (IntInsnNode)ins;
            	if(intInsn.getOpcode()==Opcodes.BIPUSH)
            		handleInt(intInsn.operand);
            	else if(intInsn.getOpcode()==Opcodes.SIPUSH)
            		handleInt(intInsn.operand);
            	else if(intInsn.getOpcode()==Opcodes.NEWARRAY)
            		createArray(intInsn.operand);
            	else throw new Error("Can't handle the Int Node "+intInsn.getOpcode());
            }
            else if (ins instanceof JumpInsnNode) {
            	JumpInsnNode insn = (JumpInsnNode) ins;
            	  switch(insn.getOpcode())
                  {
	                   	case Opcodes.IF_ICMPEQ:		jump("je", insn.label); 	break;
	                  	case Opcodes.IF_ICMPNE:		jump( "jne", insn.label); 	break;                  	
	                  	case Opcodes.IF_ICMPLT:		jump( "jg", insn.label); 	break;
	                  	case Opcodes.IF_ICMPGE:		jump( "jle", insn.label); 	break;
	                  	case Opcodes.IF_ICMPGT:		jump( "jl", insn.label); 	break; 
	                  	case Opcodes.IF_ICMPLE:		jump( "jge", insn.label);	break;    
	                  	
	                   	case Opcodes.IFEQ:		jumpZero("je", insn.label); 	break;
	                  	case Opcodes.IFNE:		jumpZero( "jne", insn.label); 	break;                  	
	                  	case Opcodes.IFLT:		jumpZero( "jg", insn.label); 	break;
	                  	case Opcodes.IFGE:		jumpZero( "jle", insn.label); 	break;
	                  	case Opcodes.IFGT:		jumpZero( "jl", insn.label); 	break; 
	                  	case Opcodes.IFLE:		jumpZero( "jge", insn.label);	break;    
	                  	
                      case Opcodes.GOTO:
                    	  jump("jmp", insn.label);
                          break; 
                      default:
                    	  throw new Exception("Can't deal with jumpinsnnode: " + insn.getOpcode());
                  }
            }else if (ins instanceof FrameNode) { 
            	//FrameNode insn = (FrameNode)ins;
            	//System.out.println("FrameNode: "+insn.type);
            }
            else if (ins instanceof LdcInsnNode) { 
            	LdcInsnNode insn = (LdcInsnNode)ins;
            	if(insn.getType()==9 && insn.cst instanceof String)
            		setString(insn);
            	else if(insn.getType()==9 && insn.cst instanceof Long)
            		handleLong((Long)insn.cst);
            	else
            		throw new Error("Unknown LDCINSN: "+insn.getType()+", "+insn.cst);
            }             
            else if (ins instanceof IincInsnNode)  {
            	IincInsnNode insn = (IincInsnNode) ins;
            	inc(insn, method);
            }
            else if (ins instanceof TypeInsnNode)  {
            	TypeInsnNode insn = (TypeInsnNode) ins;
            	if(insn.getOpcode()==Opcodes.NEW) {
            		createObject(insn.desc);
            	}else throw new Exception("Can't handle TypeInsnNode: "+insn.getOpcode());
            }
            else if (ins instanceof FieldInsnNode)  {
            	FieldInsnNode insn = (FieldInsnNode) ins;
            	lastAccecedVariable = insn.name;
            	if(ins.getOpcode() == Opcodes.GETSTATIC) 
            		getStatic(insn);    	
            	else if(ins.getOpcode() == Opcodes.PUTSTATIC)
            		putStatic(insn);    
            }
            else {
            	int op = ins.getOpcode();
            	
            	// iconst_m1 to iconst_5
            	if(op == Opcodes.ICONST_0 || op == Opcodes.ICONST_1 
            		|| op == Opcodes.ICONST_2 || op == Opcodes.ICONST_3 
            		|| op == Opcodes.ICONST_4 || op == Opcodes.ICONST_5 || op == Opcodes.ICONST_M1) {
            		switch(op) {
            			case Opcodes.ICONST_M1:
            				handleInt(-1);
            				break;
            			case Opcodes.ICONST_0:
            				handleInt(0);
            				break;
            			case Opcodes.ICONST_1:
            				handleInt(1);
            				break;
            			case Opcodes.ICONST_2:
            				handleInt(2);
            				break;
            			case Opcodes.ICONST_3:
            				handleInt(3);
            				break;
            			case Opcodes.ICONST_4:
            				handleInt(4);
            				break;
            			case Opcodes.ICONST_5:
            				handleInt(5);
            				break;
            			}
            	}
            	//iadd
            	else if(op == Opcodes.IADD)
            		addI();
            	
            	//isub
            	else if(op == Opcodes.ISUB)
            		subI();
            	
            	//imul
            	else if(op == Opcodes.IMUL)
            		mulI();
            	
            	//idiv
            	else if(op == Opcodes.IDIV)
            		divI();
            	
            	//imod
            	else if(op == Opcodes.IREM)
            		modI();
            	
            	//ineg
            	else if(op == Opcodes.INEG)
            		negI();
            	
            	//ladd
            	else if(op == Opcodes.LADD)
            		addL();
            	
            	//lsub
            	else if(op == Opcodes.LSUB)
            		subL();
            	
            	//lmul
            	else if(op == Opcodes.LMUL)
            		mulL();
            	
            	//ldiv
            	else if(op == Opcodes.LDIV)
            		divL();
            	
            	//lmod
            	else if(op == Opcodes.LREM)
            		modL();
            	
            	//lneg
            	else if(op == Opcodes.LNEG)
            		negL();
            	
            	//dup
            	else if(op == Opcodes.DUP)
            		dup();
            	
            	//store to array
            	else if(op == Opcodes.IASTORE)
            		storeToArray();
            	//load from array
            	else if(op == Opcodes.IALOAD)
            		loadFromArray();
            	
            	//RETURN
            	else if(op == Opcodes.RETURN)
            		ret();
            	
            	//RETURN
            	else if(op == Opcodes.IRETURN)
            		retInt(method);
            	
            	//I2L
            	else if(op == Opcodes.I2L)
            		conv("int long");
            	
            	//LCONST_0
            	else if(op == Opcodes.LCONST_0)
            		handleLong(0L);
            	//LCONST_1
            	else if(op == Opcodes.LCONST_1)
            		handleLong(1L);
            	//ARRAYLENGTH
            	else if(op == Opcodes.ARRAYLENGTH) {
            		handleAL(lastAccecedVariable);
            	//	throw new Error("Can't handle Arraylength");
            	}
            	else if(op == Opcodes.POP) {
            		handlePop(1);
            	}else if(op == Opcodes.POP2) {
            		handlePop(2);
            	}else if(op == Opcodes.ACONST_NULL)
            		handleZero();
            	else if(op == -1)
            		System.out.println("-1 Instruction");
            	//UNKNOWN
            	else
            		throw new Exception("Can't handle Instruction "+op);

            	
            	
            //	System.out.println(insn.getOpcode());
        	// System.out.println("; ins: " + ins.getOpcode());
            }
        }
        if(method.maxLocals > 0) {
    		Integer[] stack = new Integer[method.maxLocals];
     		LabelNode insn = (LabelNode)method.instructions.get(0);
    		System.out.println(insn.getLabel());
            hash.put("stack_"+methodList.get(method), stack);
        }
        
        stopMethod(method);
	}

	
	public static String getLabel(MethodNode me) {
		for(Entry<MethodNode, String> m:methodList.entrySet()) {
			MethodNode method = m.getKey();
			if(method.equals(me))
				return m.getValue();
		}
		return null;
	}
	public static String getLabel(MethodInsnNode me) {
		for(Entry<MethodNode, String> m:methodList.entrySet()) {
			MethodNode method = m.getKey();
			if(method.name.equalsIgnoreCase(me.name) 
			&& method.desc.equalsIgnoreCase(me.desc))
				return m.getValue();
		}
		return null;
	}
	
	static String p = "";
	
	public static void startProgram(ClassNode node) {
		
		//System.out.println(p+"Start Method: "+m.name);
	}
	
	public static void stopProgram(ClassNode node) {
		
	}
	private static void handleAL(String la) {
		curPseudo.alength(la);
		if(DEBUG)
			System.out.println(p+"getting length of "+la);	
	}	
	private static void handlePop(int i) {
		curPseudo.pop(i);
		if(DEBUG)
			System.out.println(p+"pop'ing "+i+" times");	
	}
	private static void createObject(String desc) {
		curPseudo.createObject(desc);
		if(DEBUG)
			System.out.println(p+"create new object: "+desc);	
	}	
	
	public static void conv(String str) {
		if(DEBUG)
			System.out.println(p+"conv "+str);	
		curPseudo.conv(str);
	}

	private static void handleLong(long i) {
		if(DEBUG)
			System.out.println(p+"handle long "+i);	
		curPseudo.handleLong(i);
	}

	private static void storeLong(int var, MethodNode methodNode) {
		if(DEBUG)
			System.out.println(p+"store long "+var);	
		curPseudo.storeLong(var, methodNode);
	}

	private static void loadLong(int var, MethodNode methodNode) {
		if(DEBUG)
			System.out.println(p+"load long "+var);	
		curPseudo.loadLong(var, methodNode);
	}
	
	private static void inc(IincInsnNode insn, MethodNode methodNode) {
		if(DEBUG)
			System.out.println(p+"increase "+ insn.var+" by "+insn.incr);
		curPseudo.inc(insn, methodNode);
	}

	private static void jump(String s, LabelNode label) {
		if(DEBUG)
			System.out.println(p+s+": "+label.getLabel());
		curPseudo.jump(s,label);
	}
	
	private static void jumpZero(String s, LabelNode label) {
		if(DEBUG)
			System.out.println(p+s+"(zero): "+label.getLabel());
		curPseudo.jumpZero(s,label);
	}

	public static void label(LabelNode insn) {
		if(DEBUG)
			System.out.println(p+"Lable: "+insn.getLabel());
		curPseudo.lable(insn);
	}
	
	public static void registerField(FieldNode field) {
		if(DEBUG)
			System.out.println(p+"Register Variable "+field.name+" with "+field.value);
		curPseudo.reg(field);
	}
	
	public static void startMethod(MethodNode m) {
		if(DEBUG)
			System.out.println(p+"Start Method: "+m.name);
		curPseudo.start(m);
		p = p + "   ";
	}
	public static void stopMethod(MethodNode m) {
		p = p.substring(0, p.length()-3);
		curPseudo.stop(m);
		if(DEBUG)
			System.out.println(p+"Stop Method: "+m.name);
	}
	
	public static void invokeStatic(MethodInsnNode m, HashMap<MethodNode, String> methodList) {
		boolean found=false;
		try {
			Class<?> cla = Class.forName(m.owner.replace('/', '.'));
			while(cla.getSuperclass() != null) {
				if(cla == Interrupt.class) {
					curPseudo.invokeStatic(m);
					found=true;
				}
				cla = cla.getSuperclass();
			}
			
			
		} catch (Exception e) { e.printStackTrace();}
		if(hasMethod(methodList, m.name)!=null) {
			curPseudo.callStatic(m, hasMethod(methodList, m.name));
			found=true;
		}
		if(m.owner.equals("pusty/f0xC/util/TileMap16") && m.name.equals("create")) {
			found=true;
		}
		if(!found)
			throw new Error("Don't know owner of method: "+m.owner+", "+m.name);
		if(DEBUG)
			System.out.println(p+"Invoke "+m.name);
	}
	
	public static void invokeVirtual(MethodInsnNode insn) {
		curPseudo.invokeVirtual(insn);
		//if(true)
		//	throw new Error("Don't know owner of method: "+insn.owner+", "+insn.name+": "+insn.desc);
		if(DEBUG)
			System.out.println(p+"Invoke Virtual "+insn.name);
	}
	
	
	public static MethodNode hasMethod(HashMap<MethodNode, String> methodList, String m) {
		 for (Entry<MethodNode, String> mep : methodList.entrySet()) {
			 MethodNode me = mep.getKey();
			 me.visitCode();
	         if ((me.access & Opcodes.ACC_NATIVE) != 0) continue;
	         if ((me.access & Opcodes.ACC_STATIC) == 0) continue;
	         if(m.equalsIgnoreCase(me.name))
	        	 return me;
	     }
		 return null;
	}
	
	public static void setString(LdcInsnNode insn) {
		curPseudo.setString(insn);
		if(DEBUG)
			System.out.println(p+"Setting String to "+insn.cst);
	}

	
	public static void retInt(MethodNode n) {
		if(DEBUG)
			System.out.println(p+"return int");
		curPseudo.retInt(n);
	}
	
	public static void ret() {
		if(DEBUG)
			System.out.println(p+"return");
		curPseudo.ret();
	}
	public static void addI() {
		if(DEBUG)
		System.out.println(p+"add integer");
		curPseudo.addInt();
	}
	public static void subI() {
		if(DEBUG)
		System.out.println(p+"sub integer");
		curPseudo.subInt();
	}
	public static void mulI() {
		if(DEBUG)
		System.out.println(p+"mul integer");
		curPseudo.mulInt();
	}
	public static void divI() {
		if(DEBUG)
		System.out.println(p+"div integer");
		curPseudo.divInt();
	}
	public static void modI() {
		if(DEBUG)
		System.out.println(p+"mod integer");
		curPseudo.modInt();
	}
	private static void negI() {
		if(DEBUG)
		System.out.println(p+"neg integer");
		curPseudo.negInt();
	}
	public static void addL() {
		if(DEBUG)
		System.out.println(p+"add long");
		curPseudo.addLong();
	}
	public static void subL() {
		if(DEBUG)
		System.out.println(p+"sub long");
		curPseudo.subLong();
	}
	public static void mulL() {
		if(DEBUG)
		System.out.println(p+"mul long");
		curPseudo.mulLong();
	}
	public static void divL() {
		if(DEBUG)
		System.out.println(p+"div long");
		curPseudo.divLong();
	}
	public static void modL() {
		if(DEBUG)
		System.out.println(p+"mod long");
		curPseudo.modLong();
	}
	private static void negL() {
		if(DEBUG)
		System.out.println(p+"neg long");
		curPseudo.negLong();
	}
	public static void dup() {
		if(DEBUG)
		System.out.println(p+"duplicates last entry");
		curPseudo.dup();
	}
	public static void storeToArray() {
		if(DEBUG)
		System.out.println(p+"store array");
		curPseudo.storeToArray();
	}
	public static void loadFromArray() {
		if(DEBUG)
		System.out.println(p+"load array");
		curPseudo.loadFromArray();
	}
	private static void createArray(int c) {
		if(DEBUG)
		System.out.println(p+"create new array with type "+c);
		curPseudo.createArray();
	}
	public static void handleInt(int num) {
		if(DEBUG)
    	System.out.println(p+"load integer "+num);
    	curPseudo.handleInt(num);
	}
	
	public static void handleZero() {
		if(DEBUG)
    	System.out.println(p+"load integer "+0);
    	curPseudo.handleZero();
	}
	public static void storeTo(int index, MethodNode methodNode) {
		if(DEBUG)
    	System.out.println(p+"save index "+index+" from method "+methodNode.name);
    	curPseudo.storeTo(index, methodNode);
	}
	
	public static void loadFrom(int index, MethodNode methodNode) {
		if(DEBUG)
    	System.out.println(p+"load index "+index+" from method "+methodNode.name);
    	curPseudo.loadFrom(index, methodNode);
	}
	
	public static void getStatic(FieldInsnNode field) {
		if(DEBUG)
		System.out.println(p+"load static field "+field.name+" from "+field.owner+"("+field.desc+")");
		curPseudo.loadStatic(field);
	}
	
	public static void putStatic(FieldInsnNode field) {
		if(DEBUG)
		System.out.println(p+"save static field "+field.name+" from "+field.owner+"("+field.desc+")");
		curPseudo.saveStatic(field);
	}
	
	
	public static void printInsn(AbstractInsnNode insn) {
		if(!DEBUG) return;
		if(insn.getOpcode()!=-1)
			System.out.println(Textifier.TYPES[insn.getType()]+" "+Textifier.OPCODES[insn.getOpcode()]+": "+insn.toString()); 
//		else if(insn.getType()!=-1)
//			System.out.println(insn.toString()); 
	}
	static ArrayList<String> noMaskList = new ArrayList<String>();
	public static void addNoMask(String str) {
		noMaskList.add(str);
	}
}
