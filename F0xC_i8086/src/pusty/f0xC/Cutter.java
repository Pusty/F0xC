package pusty.f0xC;

import java.util.regex.Pattern;

public class Cutter {
	Forge forge;
	public Cutter(Forge f) {
		forge = f;
	}
	public Forge getForge() {
		return forge;
	}
	public void cut() {
		for(int line=0;line<forge.getContent().size();line++) {
			String[] cmd = splitCmd(forge.getContent().get(line));
			if(cmd[0].equalsIgnoreCase("push")) {
				int index = 0;
				boolean breakThis = false;
				for(int l=line;l<forge.getContent().size();l++) {
					String[] cmd2 = splitCmd(forge.getContent().get(l));
					if(cmd2[0].equalsIgnoreCase("pop")) {
						index--;
						if(index>0) continue;
						if(breakThis) continue;
						forge.getContent().set(line, cmd[2]+"mov "+cmd2[1]+", "+cmd[1]);
						forge.getContent().set(l, ";rem");
						break;
					}else if(cmd2[0].equalsIgnoreCase("push")) {
						index++;
					}else {
						breakThis = true;
					}
				}
				if(index>0) {
					if(CompilerF0xC.DEBUG) 
						throw new Error("No pop for push. Line:"+line);
					else
						System.err.println("No pop for push. Line: "+line);
					return;
				}
			}
		}
		while(clearUseless()){}
		while(clearRem()){}
		
		cleanUp();
	}
	
	
	public void cleanUp() {
		for(int line=0;line<forge.getContent().size();line++) {
			String[] cmd = splitCmd(forge.getContent().get(line));
			if(cmd[0].equalsIgnoreCase("mov") || cmd[0].equalsIgnoreCase("add") || cmd[0].equalsIgnoreCase("sub") ) {
				String[] cont = cmd[1].split(Pattern.quote(","));
				String partA = cont[0].trim();
				String partB = cont[1].trim().split(" ")[0];
				String end = "";
				if(cont[1].trim().split(" ").length>1)
					end = cont[1].trim().split(" ")[1];
				if(RegSize.getType(partA)  != RegSize.getType(partB)) {
					forge.getContent().set(line, cmd[2]+cmd[0]+" "+partA+", "+RegSize.get(RegSize.getType(partA), partB)+" "+end);
				}
			}
			if(cmd[0].equalsIgnoreCase("push") || cmd[0].equalsIgnoreCase("pop")) {
				String partA = cmd[1].trim().split(" ")[0];
				String end = "";
				if(cmd[1].trim().split(" ").length>1)
					end = cmd[1].trim().split(" ")[1];
				if(RegSize.getType(partA) == RegSize.b8)
					forge.getContent().set(line, cmd[2]+cmd[0]+" "+RegSize.get(RegSize.b16, partA)+" "+end);
			}
		}
	}
	
	public boolean clearRem() {
		for(int line=0;line<forge.getContent().size();line++) {
			String[] cmd = splitCmd(forge.getContent().get(line));
			if(cmd[0].equalsIgnoreCase(";rem")) {
				forge.getContent().remove(line);
				return true;
			}
		}
		return false;
	}
	
	public boolean clearUseless() {
		for(int line=0;line<forge.getContent().size();line++) {
			String[] cmd = splitCmd(forge.getContent().get(line));
			if(cmd[0].equalsIgnoreCase("mov")) {
				String[] cont = cmd[1].split(Pattern.quote(","));
				if(cont[0].trim().equalsIgnoreCase(cont[1].trim())) {
					forge.getContent().remove(line);
					return true;
				}
			}
		}
		return false;
	}
	
	public static String[] splitCmd(String le) {
		String[] cmd = new String[3];
		String line = le.trim();
		cmd[0] = line.split(" ")[0];
		cmd[1] = line.substring(cmd[0].length()).trim();
		int ws = 0;
		for(char c:le.toCharArray())
			if(c!=' ')
				break;
			else
				ws++;
		cmd[2] = "";
		for(int i=0;i<ws;i++)
			cmd[2] = cmd[2] + " ";
		return cmd;
	}
}
