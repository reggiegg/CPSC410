/**
 * Class for holding the specific metrics for a given class
 * @author Jon
 *
 */
public class StatSVNClassMetric {
	private String path;
	private String className;
	private int numRevisions;

	/**
	 * Construct a Class metric object containing all relevant information
	 * @param String path
	 * @param String className
	 * @param int numRevisions
	 */
	StatSVNClassMetric(String path, String className, int numRevisions) {
		this.path = path;
		this.className = className;
		this.numRevisions = numRevisions;
	}
		
	public String getPath() {
		return path;
	}
	
	public String getClassName() {
		return className;
	}
	
	public int getNumRevisions() {
		return numRevisions;
	}
}
