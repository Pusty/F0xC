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
		   copyBootloader();
		   copyBat();
		   builder.command(INCLUDER.inTemp("createKernel.bat").getAbsolutePath());
		   try {   
			   exec(builder);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   copyOutput();
	   }
	 
	 public static void copyBat() throws IOException {
		   File in = INCLUDER.inInclude("createKernel.bat");
		   File out = INCLUDER.inTemp("createKernel.bat");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 public static void copyNasm() throws IOException {
		   File in = INCLUDER.inInclude("nasmKernel.bat");
		   File out = INCLUDER.inTemp("nasmKernel.bat");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 public static void copyBootloader() throws IOException {
		   File in = INCLUDER.inInclude("bootloader.asm");
		   File out = INCLUDER.inTemp("bootloader.asm");
		   Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
	 }
	 public static void copyOutput() throws IOException {
		   File in = INCLUDER.inTemp("kernel.bin");
		   File out = new File(CompilerF0xC.outFile.getParentFile().getAbsolutePath()+"/kernel.bin");
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
		   builder.command(INCLUDER.inTemp("nasmKernel.bat").getAbsolutePath(),outputFile.getName(),"output.bin");
		   try {
			   exec(builder);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   
		   File fi = INCLUDER.inTemp("output.bin");
		   double size = fi.length();
		   int sects = (int) Math.ceil(size/512);
		   createMacro(sects);
		   writeContent.add("times 512*"+sects+"- ($-$$) db 0"); //Write Suffix
		   print(outputFile,writeContent);
		   try {
			   INCLUDER.moveOToT();
		   } catch (IOException e1) {
				e1.printStackTrace();
			}
		   builder = new ProcessBuilder();
		   builder.command(INCLUDER.inTemp("nasmKernel.bat").getAbsolutePath(),outputFile.getName(),"output.bin");
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
	   
	  
	   public static void createMacro(int sects) {
		   File fi = INCLUDER.inTemp("SECTS");
		   fi.delete();
	       try {
	           BufferedWriter writer = new BufferedWriter(new FileWriter(fi));
	           writer.write("%define SECTORS "+sects);
	           writer.newLine();
	           writer.close();
	       }catch (IOException e) {
	           throw new RuntimeException("Unable to write macrofile file " + fi, e);
	       }
	   }
}
