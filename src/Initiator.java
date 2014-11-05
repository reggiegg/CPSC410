import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

/**
 * Initiator acts as a main class. It takes the location of Subversion log file and Subversion local repo as arguments.
 * - Begins analysis of code base by triggering the analysis tools to provide XML reports on the project
 * - It causes the parsers to parse those reports
 * - It receives parsed results and ships them to the Code Base Data Aggregator
 * - It opens the Result in a browser screen
 * @author Reggie
 */

public class Initiator {
	
	private static void printUsageInstructions(){
		System.out.println("Usage: /path/to/svn/logfile/logfile.log /path/to/module");
	}

	public static void main(String[] args) {
		
		// temporary interface for defining arguments
		Scanner sc = new Scanner(System.in);
		args = sc.nextLine().split(" +");
		sc.close();
		

		if (args.length != 2){
			printUsageInstructions();
		}else{
			
			//String paths = "/home/reggie/Documents/CPSC410/spamassassin/trunk/logfile.log /home/reggie/Documents/CPSC410/spamassassin/trunk/";

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
							
								
				File javaNCSSStats = new File("javancss-statistics.xml");
				if (javaNCSSStats.exists()){
					javaNCSSStats.delete();
				}				
				System.out.println("Executing JavaNCSS...");
				
				System.out.println("javancss-32.53/bin/javancss -recursive -xml -all -out "+javaNCSSStats.getAbsolutePath()+" "+modulepath.getAbsolutePath());
				Process javaNCSSProcess = rt.exec("javancss-32.53/bin/javancss -recursive -xml -all -out "+javaNCSSStats.getAbsolutePath()+" "+modulepath.getAbsolutePath());
				javaNCSSProcess.waitFor();
				
				if (javaNCSSProcess.exitValue() != 0){
					System.out.println("JavaNCSS Failed.");
					throw new IOException("javaNCSS did not properly execute.");
				}
				System.out.println("JavaNCSS success. New file at "+javaNCSSStats.getAbsolutePath());
				javaNCSSProcess.destroy();
				
				JavaNCSSParser javaNCSSParser = new JavaNCSSParser();
				JavaNCSSMetrics javaNCSSMetrics = javaNCSSParser.getJavaNCSSMetrics(javaNCSSStats);
				
				StatSVNParser statSVNParser = new StatSVNParser();
				StatSVNMetrics statSVNMetrics = statSVNParser.getStatSVNMetrics(statSVNStats);
				
				CodeBaseDataAggregator CBDA = new CodeBaseDataAggregator(javaNCSSMetrics, statSVNMetrics);
				
				CSVResultGenerator.writeToCSV(CBDA.getSolarSystem());
								
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
									
			try {			
				if (Desktop.isDesktopSupported()){
					System.out.println((new File("../site/index.html")).getAbsolutePath());
					
					Desktop.getDesktop().browse((new File("site/index.html")).toURI());				
				}else {
					Runtime rt = Runtime.getRuntime();
					rt.exec("xdg-open "+((new File("site/index.html")).toURI()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
									
		}

	}

}