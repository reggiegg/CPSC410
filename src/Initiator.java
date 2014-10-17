import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Initiator {
	
	private static void printUsageInstructions(){
		System.out.println("Usage: /path/to/svn/logfile/logfile.log /path/to/module");
	}

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		args = sc.nextLine().split(" +");
		
		sc.close();
		
		if (args.length != 2){
			printUsageInstructions();
		}else{
			
			String paths = "/home/reggie/Documents/CPSC410/spamassassin/trunk/logfile.log /home/reggie/Documents/CPSC410/spamassassin/trunk/";

			File logfile =  new File(args[0]);
			File modulepath = new File(args[1]);
					
			
			try {
				
				Runtime rt = Runtime.getRuntime();
				Process statSVNProcess = rt.exec("java -jar statsvn.jar -xml "+logfile.getPath()+" "+modulepath.getPath());
				statSVNProcess.waitFor();
				System.out.println("??");
				
				if (statSVNProcess.exitValue() != 0){
					throw new IOException("statSVN did not properly execute.");
				}
				
				statSVNProcess.destroy();
						
				File statSVNStats = new File("./repo-statistics.xml");
							
				// StatSVNParser statSVNParser = new StatSVNParser();  // something like this for the parsing stage
				// statSVNParser.parse(statSVNStats);
								
				File javaNCSSStats = new File("javancss-statistics.xml");
				
				Process javaNCSSProcess = rt.exec("javancss-32.53/bin/javancss -xml -all -out "+javaNCSSStats.getAbsolutePath()+" "+modulepath.getAbsolutePath());
				javaNCSSProcess.waitFor();
				
				if (javaNCSSProcess.exitValue() != 0){
					throw new IOException("javaNCSS did not properly execute.");
				}
				javaNCSSProcess.destroy();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
									
		}

	}

}