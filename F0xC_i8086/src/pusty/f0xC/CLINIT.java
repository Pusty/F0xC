package pusty.f0xC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import pusty.f0xC.kernel.Loadable;
import pusty.f0xC.kernel.Pointer;
import pusty.f0xC.kernel.Storable;
import pusty.f0xC.util.Null;

public class CLINIT {

	static int NONE = -1;
	static int ZERO = 0;
	static int INTEGER = 1;
	static int ARRAY_INT = 2;
	static int STRING = 3;
	static int LONG = 4;
	//static int ARRAY_LONG = 5;
	static int LOADABLE = 6;
	static int POINTER = 7;
	static int STORABLE = 8;
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> run(Pseudo p) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		int lastType = NONE;
		List<Integer> lastNums = new ArrayList<Integer>();
		List<Long> lastLongs = new ArrayList<Long>();
		Integer[] lastArray = null;
		Class<?> lastClass = null;
		String lastString = null;
		for(String s:p.get()) {
			String cmd = "";
			String[] args = null;
			if(s.split(" ").length>0) {
				cmd = s.split(" ")[0];
				args = s.split(" ");
			}
			if(cmd.equalsIgnoreCase("reg"))
				hash.put(args[1], null);
			if(cmd.equalsIgnoreCase("new")) {
				try {
					Class<?> cl = Class.forName(args[1].replace('/', '.'));
					if(cl.getSuperclass().equals(Loadable.class)) {
						lastClass = (Class<Loadable>) cl;
						if(lastType==NONE)
							lastType=LOADABLE;
					}else if(cl.equals(Pointer.class)) {
						lastClass = (Class<Pointer>) cl;
						if(lastType==NONE)
							lastType=POINTER;
					}else if(cl.getSuperclass().equals(Storable.class)) {
						lastClass = (Class<Storable>) cl;
						if(lastType==NONE)
							lastType=STORABLE;
					}else
						throw new Error("CLINIT: Wasn't able to handle "+cl.getName()+"(sup"+cl.getSuperclass()+")");
					lastString = null;
				} catch (Exception e) { e.printStackTrace(); }
				
			}
			
			if(cmd.equalsIgnoreCase("ihandle")) {
				lastNums.add(Integer.parseInt(args[1]));
				if(lastType==NONE)
					lastType=INTEGER;
			}
			if(cmd.equalsIgnoreCase("zhandle")) {
				if(lastType==NONE)
					lastType=ZERO;
			}
			if(cmd.equalsIgnoreCase("lhandle")) {
				lastLongs.add(Long.parseLong(args[1]));
				if(lastType==NONE)
					lastType=LONG;
			}
			
			if(cmd.equalsIgnoreCase("dup")) {
				lastNums.add(lastNums.size()-1);
			}		
			if(cmd.equalsIgnoreCase("sta")) {
				lastArray[lastNums.get(lastNums.size()-2)] = lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-2);
			}
			
			if(cmd.equalsIgnoreCase("ca")) {
				lastArray = new Integer[lastNums.get(lastNums.size()-1)];
				lastNums.remove(lastNums.size()-1);
				if(lastType==NONE || lastType==INTEGER)
					lastType=ARRAY_INT;
			}
			
			if(cmd.equalsIgnoreCase("str")) {
				lastString = args[1];
				if(lastType==NONE)
					lastType=STRING;
			}
			
			if(cmd.equalsIgnoreCase("isub")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = newNum - lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			if(cmd.equalsIgnoreCase("iadd")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = newNum + lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			if(cmd.equalsIgnoreCase("imul")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = newNum * lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			if(cmd.equalsIgnoreCase("idiv")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = newNum / lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			if(cmd.equalsIgnoreCase("imod")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = newNum % lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			if(cmd.equalsIgnoreCase("ineg")) {
				int newNum = 0;
				newNum = lastNums.get(lastNums.size()-2);
				newNum = -1 * lastNums.get(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.remove(lastNums.size()-1);
				lastNums.add(newNum);
			}
			
			if(cmd.equalsIgnoreCase("lsub")) {
				long newNum = 0;
				newNum = lastLongs.get(lastLongs.size()-2);
				newNum = newNum - lastLongs.get(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.add(newNum);
			}
			if(cmd.equalsIgnoreCase("ladd")) {
				long newNum = 0;
				newNum = lastLongs.get(lastLongs.size()-2);
				newNum = newNum + lastLongs.get(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.add(newNum);
			}
			if(cmd.equalsIgnoreCase("lmul")) {
				long newNum = 0;
				newNum = lastLongs.get(lastLongs.size()-2);
				newNum = newNum * lastLongs.get(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.add(newNum);
			}
			if(cmd.equalsIgnoreCase("ldiv")) {
				long newNum = 0;
				newNum = lastLongs.get(lastLongs.size()-2);
				newNum = newNum / lastLongs.get(lastNums.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.add(newNum);
			}
			if(cmd.equalsIgnoreCase("lmod")) {
				long newNum = 0;
				newNum = lastLongs.get(lastLongs.size()-2);
				newNum = newNum % lastLongs.get(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.remove(lastLongs.size()-1);
				lastLongs.add(newNum);
			}
			
			if(cmd.equalsIgnoreCase("loadVar")) {
				if(hash.containsKey(args[1])) {
					lastNums.add((int) hash.get(args[1]));
					if(lastType==NONE)
						lastType=INTEGER;
				}
			}
			
			if(cmd.equalsIgnoreCase("saveVar")) {
				if(hash.containsKey(args[1])) {
					if(lastType==ZERO)
						hash.put(args[1],new Null());
					else if(lastType==INTEGER)
						hash.put(args[1],lastNums.get(lastNums.size()-1));
					else if(lastType==LONG)
						hash.put(args[1],lastLongs.get(lastLongs.size()-1));
					else if(lastType==ARRAY_INT)
						hash.put(args[1],lastArray);
					else if(lastType==STRING)
						hash.put(args[1],lastString);
					else if(lastType==LOADABLE) {
						try {
							hash.put(args[1], lastClass.getConstructor(String.class).newInstance(lastString));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(lastType==POINTER) {
						try {
							if(lastString==null)
								hash.put(args[1], lastClass.getConstructor().newInstance());
							else
								hash.put(args[1], lastClass.getConstructor(String.class).newInstance(lastString));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(lastType==STORABLE) {
						try {
							hash.put(args[1], lastClass.getConstructor().newInstance());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else
						throw new Error("CLINIT: Can't resolve type: "+lastType);
					lastType = NONE;
				}
			}
		}
		
		if(CompilerF0xC.DEBUG) {
			System.out.println("CLINIT: ");
			for(Entry<String, Object> e:hash.entrySet()) {
				if(e.getValue()!=null && e.getValue().getClass().isArray()) {
					System.out.print(e.getKey()+": ");
					Object[] os = (Object[]) e.getValue();
					for(Object o:os)
						System.out.print(o+", ");
					System.out.println("Array");
				}else
					System.out.println(e.getKey()+": "+e.getValue());
			}
		}
		return hash;
	}
	
}
