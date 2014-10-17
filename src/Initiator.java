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
			File logfile =  new File(args[0]);
			File modulepath = new File(args[1]);
			
			Runtime rt = Runtime.getRuntime();
			
			Process statSVNProcess;
			try {
				statSVNProcess = rt.exec("java -jar statsvn.jar -xml "+logfile.getPath()+" "+modulepath.getPath());
				statSVNProcess.waitFor();
				
				if (statSVNProcess.exitValue() != 0){
					throw new IOException("statSVN did not properly execute.");
				}
			
				File repostats = new File("./repo-statistics.xml");
				
				// StatSVNParser statSVNParser = new StatSVNParser();  // something like this for the parsing stage
				// statSVNParser.parse(repostats);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
									
		}

	}

}