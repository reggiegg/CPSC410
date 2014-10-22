import java.io.File;
import java.util.List;

import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the JavaNCSSParser
 * @author Jeremy
 */
public class JavaNCSSParserTest {
	
	@Test
	public void sampleTest() {
		JavaNCSSParser javaNCSSParser = new JavaNCSSParser();
		JavaNCSSMetrics javaNCSSMetrics = javaNCSSParser.getJavaNCSSMetrics(new File("test/javaNCSSTest.xml"));
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
