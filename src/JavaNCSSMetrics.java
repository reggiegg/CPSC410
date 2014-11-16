import java.util.List;

/**
 * Object that contains all parsed JavaNCSSMetrics. 
 * @author Jeremy
 */
public class JavaNCSSMetrics {
	private List<JavaNCSSClassMetric> classMetrics;
	
	public JavaNCSSMetrics (List<JavaNCSSClassMetric> classMetrics) {
		this.classMetrics = classMetrics;
	}
	
	/**
	 * @return a list of parsed class metrics.
	 */
	public List<JavaNCSSClassMetric> getClassMetricsList() {
		return classMetrics;
	}
}
