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
		List<JavaNCSSClassMetric> javaNCSSMetricsList = 
				javaNCSSParser.getJavaNCSSMetrics("test/javaNCSSTest.xml");
		
		for (JavaNCSSClassMetric classMetric : javaNCSSMetricsList) {
			System.out.println("Classname: " + classMetric.getClassName());
			System.out.println("Number of methods: " + classMetric.getNumMethods());
			System.out.println("Average method length: " + classMetric.getAverageMethodLength());
			System.out.println();
		}
	}
}