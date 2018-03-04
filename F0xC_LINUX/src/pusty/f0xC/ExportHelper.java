package pusty.f0xC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import pusty.f0xC.kernel.INCLUDER;


public class ExportHelper {
	
	 public static void createKernel() throws IOException {
		   ProcessBuilder builder = new ProcessBuilder();
		   copyBat();
		   builder.command(INCLUDER.inTemp("createExe.bat").getAbsolutePath());
		   try {   
			   exec(builder);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		 //  copyOutput();
	   }
	 
	 public static void copyBat() throws IOException {
		   File in = INCLUDER.inInclude("createExe.bat");
		   File out = INCLUDER.inTemp("createExe.bat");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 public static void copyNasm() throws IOException {
		   File in = INCLUDER.inInclude("nasmExe.bat");
		   File out = INCLUDER.inTemp("nasmExe.bat");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 public static void copyOutput() throws IOException {
		   File in = INCLUDER.inTemp("output.exe");
		   File out = new File(CompilerF0xC.outFile.getParentFile().getAbsolutePath()+"/output.exe");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 
	 
		public static void exec(ProcessBuilder builder) throws IOException {

			Process proc = builder.start();
			
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new 
				     InputStreamReader(proc.getErrorStream()));
				System.out.println(builder.command());
				// read the output from the command
				System.out.println("Output:\n");
				String s = null;
				while ((s = stdInput.readLine()) != null) {
					if(!s.trim().equalsIgnoreCase(""))
						System.out.println(s);
				}
				// read any errors from the attempted command
				System.out.println("Errors:\n");
				while ((s = stdError.readLine()) != null) {
					if(!s.trim().equalsIgnoreCase(""))
						System.out.println(s);
				}
	    }
	 
	  public static void nasmKernel(File outputFile, List<String> writeContent) {
		   try {
			   copyNasm();
		   } catch (IOException e2) {
			   e2.printStackTrace();
		   }
		   ProcessBuilder builder = new ProcessBuilder();
		   builder.command(INCLUDER.inTemp("nasmExe.bat").getAbsolutePath(),outputFile.getName(),"output.o");
		   try {
			   exec(builder);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   
	   }
	  
	   public static void print(File outputFile, List<String> writeContent) {
	       try 
	       {
	           BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
	           for (String string : writeContent)
	           {
	               writer.write(string);
	               writer.newLine();
	           }
	           writer.close();
	       }
	       catch (IOException e)
	       {
	           throw new RuntimeException("Unable to write output file " + outputFile, e);
	       }
	   }
	   
	  
}
