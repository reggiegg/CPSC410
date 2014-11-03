import java.util.List;

/**
 * Contains all the project metrics, as well as the class metrics.
 * @author Jon
 *
 */
public class StatSVNMetrics {
	private List<StatSVNClassMetric> classMetrics;
	private int churn;
	private int numDevelopers;
	
	public StatSVNMetrics (List<StatSVNClassMetric> classMetrics, int churn, int numDevelopers) {
		this.classMetrics = classMetrics;
		this.churn = churn;
		this.numDevelopers = numDevelopers;
	}
	
	public List<StatSVNClassMetric> getClassMetricsList() {
		return this.classMetrics;
	}
	
	public int getChurn() {
		return this.churn;
	}
	
	public int getNumDevelopers() {
		return this.numDevelopers;
	}
}
