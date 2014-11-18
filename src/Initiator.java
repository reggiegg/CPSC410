import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
//		// temporary console interface for defining arguments within Eclipse; uncomment to use!
//		Scanner sc = new Scanner(System.in);
//		args = sc.nextLine().split(" +");
//		sc.close();	

		if (args.length != 1){
			printUsageInstructions();
		}else{
			
			File modulepath = new File(args[0]);
			File logfile = new File("logfile.log");
			if (logfile.exists()){
				logfile.delete();
			}
			
								
			try {
				
				Runtime rt = Runtime.getRuntime();
				
				//Generate logfile 	
				System.out.println("Generating StatSVNLogfile...");
				Process statSVNLogfileProcess = rt.exec("svn log -v --xml " +modulepath.getPath());
				BufferedWriter out = new BufferedWriter(new FileWriter(logfile));
				BufferedReader in = new BufferedReader(  
						new InputStreamReader(statSVNLogfileProcess.getInputStream()));  
				String line = null;  
				while ((line = in.readLine()) != null) {  
						out.write(line);  
				}
				in.close();
				out.close();
				
				if (statSVNLogfileProcess.exitValue() != 0){
					System.out.println("Generating log file failed");
					throw new IOException("svn logging did not properly execute.");
					
				};
				
				statSVNLogfileProcess.destroy();
				
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
				
				if (Desktop.isDesktopSupported()){					
					Desktop.getDesktop().browse((new File("site/index.html")).toURI());				
				}else {
					rt.exec("xdg-open "+((new File("site/index.html")).toURI()));
				}
				
				System.out.println("Cleaning up...");
				statSVNStats.delete();
				javaNCSSStats.delete();
				logfile.delete();
				
								
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
												
		}

	}

}