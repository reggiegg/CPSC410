/**
 * A JavaNCSSClassMetric contains metrics about a class that were generated by JavaNCSS.
 * Provides the following information about a class:
 * - The class name
 * - The number of methods contained within a class
 * - The average method length for the methods in the class
 * @author Jeremy
 */
public class JavaNCSSClassMetric {
	private String className;
	private int numMethods;
	private float averageMethodLength;
	
	JavaNCSSClassMetric(String className, int numMethods, float averageMethodLength) {
		this.className = className;
		this.numMethods = numMethods;
		this.averageMethodLength = averageMethodLength;
	}
		
	public String getClassName() {
		return className;
	}
	
	public int getNumMethods() {
		return numMethods;
	}
	
	public float getAverageMethodLength() {
		return averageMethodLength;
	}
}
