import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
											
				File statSVNStats = new File("repo-statistics.xml");				
				if (statSVNStats.exists()){
					statSVNStats.delete();
				}
				
				System.out.println("Executing StatSVN...");
				Process statSVNProcess = rt.exec("java -jar statsvn.jar -xml "+logfile.getPath()+" "+modulepath.getPath());
				statSVNProcess.waitFor();				
				if (statSVNProcess.exitValue() != 0){
					System.out.println("StatSVN failed");
					throw new IOException("statSVN did not properly execute.");
					
				}
				statSVNProcess.destroy();
				System.out.println("StatSVN success. New file at "+statSVNStats.getAbsolutePath());
							
				// StatSVNParser statSVNParser = new StatSVNParser();  // something like this for the parsing stage
				// statSVNParser.parse(statSVNStats);
								
				File javaNCSSStats = new File("javancss-statistics.xml");
				if (javaNCSSStats.exists()){
					javaNCSSStats.delete();
				}
				
				System.out.println("Executing JavaNCSS...");
				Process javaNCSSProcess = rt.exec("javancss-32.53/bin/javancss -recursive -xml -all -out "+javaNCSSStats.getAbsolutePath()+" "+modulepath.getAbsolutePath());
				javaNCSSProcess.waitFor();
				
				if (javaNCSSProcess.exitValue() != 0){
					System.out.println("JavaNCSS Failed.");
					throw new IOException("javaNCSS did not properly execute.");
				}
				System.out.println("JavaNCSS success. New file at "+javaNCSSStats.getAbsolutePath());
				javaNCSSProcess.destroy();
				
				// JavaNCSSParser JavaNCSSParser = new JavaNCSSParser();  // something like this for the parsing stage
				// JavaNCSSParser.parse(JavaNCSSStats);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// here's where it will call the data aggregator
			
			// then open default browser pointed at URI for newly created solar system
			try {			
				if (Desktop.isDesktopSupported()){		
					Desktop.getDesktop().browse(new URI("http://www.reggiegillett.com"));				
				}else {
					Runtime rt = Runtime.getRuntime();
					rt.exec("xdg-open "+new URI("http://www.reggiegillett.com"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
									
		}

	}

}