/**
 * A JavaNCSSClassMetric contains metrics about a class that were generated by JavaNCSS.
 * @author Jeremy
 */
public class JavaNCSSClassMetric {
	private String qualifiedClassName;
	private String className;
	private int numMethods;
	private float averageMethodLength;
	private int complexityNumber;
	private float complexityDensity;
	
	JavaNCSSClassMetric(String qualifiedClassName, String className, 
			int numMethods, float averageMethodLength, int complexityNumber, 
			float complexityDensity) {
		this.qualifiedClassName = qualifiedClassName;
		this.className = className;
		this.numMethods = numMethods;
		this.averageMethodLength = averageMethodLength;
		this.complexityNumber = complexityNumber;
		this.complexityDensity = complexityDensity;
	}
	
	/**
	 * @return The fully qualified class name which includes the class path.
	 */
	public String getQualifiedClassName() {
		return qualifiedClassName;
	}
	/**
	 * @return The simple (non-qualified) class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * @return The number of methods contained within a class
	 */
	public int getNumMethods() {
		return numMethods;
	}
	
	/**
	 * @return The average method length for the methods in the class. Method
	 * length is calculated using the Non-Commenting Source Statements
	 * metric.
	 */
	public float getAverageMethodLength() {
		return averageMethodLength;
	}
	
	/**
	 * @return The code complexity number  (CCN or McCabe Metric) for the class.
	 */
	public int getComplexityNumber () {
		return complexityNumber;
	}
	
	/**
	 * @return The code complexity density (CCN / Total NCSS Method Sum) for the class
	 */
	public float getComplexityDensity() {
		return complexityDensity;
	}
}

