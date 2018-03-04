package pusty.f0xC.kernel;

import java.util.ArrayList;

public class Function {
	ArrayList<String> output;
	ArrayList<String> input;
	String name;
	String file;
	ArrayList<String> depend;
	public Function(String n,String f) {
		name=n;
		file=f;
		output = new ArrayList<String>();
		input = new ArrayList<String>();
		depend = new ArrayList<String>();
	}
	
	public String getName() { return name; }
	public String getFile() { return file; }
	public ArrayList<String> getInput() { return input; }
	public ArrayList<String> getOutput() { return output; }
	public ArrayList<String> getDependency() { return depend; }
	
	public Function addDependency(String... files) {
		for(String file:files)
			depend.add(file);
		return this;
	}
	public Function addOutput(String... regs) {
		for(String reg:regs)
			output.add(reg);
		return this;
	}
	public Function addInput(String... regs) {
		for(String reg:regs)
			input.add(reg);
		return this;
	}
}
