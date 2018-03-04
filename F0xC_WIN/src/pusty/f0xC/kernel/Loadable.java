package pusty.f0xC.kernel;

import java.io.File;


public abstract class Loadable {
	String content;
	public Loadable(String s) {
		content=s;
	}
	public String getString() {
		return content;
	}
	public abstract void load();
	public abstract String getPath();
	
	public File getContent() {
		return INCLUDER.inRes(content);
	}
	public File getRes() {
		return INCLUDER.inTemp(getPath());
	}
	public void testContent() throws Exception {
		File file = INCLUDER.inRes(content);
		if(!file.exists())
			throw new Exception("File: "+file+" does not exist in the resource folder");
	}
	public void testRes() throws Exception {
		File file = INCLUDER.inTemp(getPath());
		if(getPath().trim().equalsIgnoreCase(""))
			throw new Exception("Resource: Path is empty");
		if(!file.exists())
			throw new Exception("Resource: "+getPath()+" wasn't created");
	}
	public int getAddr() {
		return 0;
	}
	public String getKey(String key) {
		return key;
	}
}
