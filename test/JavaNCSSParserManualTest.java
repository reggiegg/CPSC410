import java.io.File;
import java.util.List;

import org.junit.Test;

/**
 * A Manual test that shows sample output from the JavaNCSS parser
 * given a typical sample input xml file that would be generated
 * by JavaNCSS.
 * @author Jeremy
 *
 */
public class JavaNCSSParserManualTest {
	@Test
	public void manualTest() {
		JavaNCSSParser javaNCSSParser = new JavaNCSSParser();
		JavaNCSSMetrics javaNCSSMetrics = javaNCSSParser.getJavaNCSSMetrics(new File("test/javaNCSSSampleOutput.xml"));
		List<JavaNCSSClassMetric> javaNCSSMetricsList = javaNCSSMetrics.getClassMetricsList();
		
		for (JavaNCSSClassMetric classMetric : javaNCSSMetricsList) {
			System.out.println("Classname: " + classMetric.getClassName());
			System.out.println("Fully qualified class name: " + classMetric.getQualifiedClassName());
			System.out.println("Number of methods: " + classMetric.getNumMethods());
			System.out.println("Average method length: " + classMetric.getAverageMethodLength());
			System.out.println("Code Complexity Number: " + classMetric.getComplexityNumber());
			System.out.println("Complexity Density (CCN / Total NCSS): " + classMetric.getComplexityDensity());
			System.out.println();
		}
	}
}
