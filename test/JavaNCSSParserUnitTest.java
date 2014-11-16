import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the JavaNCSSParser.
 * A custom test .xml file has been created to verify behavior for both
 * typical and atypical test cases.
 * Test file located at: "test/javaNCSSTestFile.xml"
 * @author Jeremy
 */
public class JavaNCSSParserUnitTest {
	private final float REQUIRED_PRECISION = Float.MIN_VALUE * 100;
	private HashMap<String, JavaNCSSClassMetric> classMetricsMap;
	private int NUM_TEST_CLASSES = 6;
	
	@Before
	public void initilizeTests() {
		JavaNCSSParser javaNCSSParser = new JavaNCSSParser();
		 // 
		JavaNCSSMetrics javaNCSSMetrics = javaNCSSParser.getJavaNCSSMetrics(new File("test/javaNCSSTestFile.xml"));
		
		List<JavaNCSSClassMetric> javaNCSSMetricsList = javaNCSSMetrics.getClassMetricsList();
		
		classMetricsMap = new HashMap<String, JavaNCSSClassMetric>();
		
		// put all class map metrics into a hash map
		for (JavaNCSSClassMetric metric : javaNCSSMetricsList) {
			classMetricsMap.put(metric.getQualifiedClassName(), metric);
		}
	}
	
	/**
	 * This test verifies that the XML Information that is listed in a 
	 * tag other than a function tag will not be included in the list
	 * of class metrics
	 */
	@Test
	public void allClassesParsedTest() {
		assertEquals(NUM_TEST_CLASSES, classMetricsMap.size());
	}
	
	/**
	 * A typical test case involving a class that contains multiple methods.
	 */
	@Test
	public void basicMetricsTest() {
		final int NUM_METHODS = 2;
		final int method1NCSS = 1;
		final int method1CCN = 2;
		final int method2NCSS = 3;
		final int method2CCN = 4;
		
		// Expected results
		String mapKeyClassName = "package.subPackage.TestClass1";
		String expQualifiedClassName = mapKeyClassName;
		String expShortClassName = "TestClass1";
		int expNumMethods = NUM_METHODS;
		float expAvgMethodLength = (float) (method1NCSS + method2NCSS) / expNumMethods;
		int expComplexityNumber = method1CCN + method2CCN;
		float expComplexityDensity = (float) expComplexityNumber 
				/ (expAvgMethodLength * expNumMethods);
		
		// Actual results
		JavaNCSSClassMetric classMetric = classMetricsMap.get(mapKeyClassName);
		String qualifiedClassName = classMetric.getQualifiedClassName();
		String shortClassName = classMetric.getClassName();
		int numMethods = classMetric.getNumMethods();
		float avgMethodLength = classMetric.getAverageMethodLength();
		int complexityNumber = classMetric.getComplexityNumber();
		float complexityDensity = classMetric.getComplexityDensity();
		
		assertEquals(expQualifiedClassName, qualifiedClassName);
		assertEquals(expShortClassName, shortClassName);
		assertEquals(expNumMethods, numMethods);
		assertTrue(avgMethodLength >= expAvgMethodLength - REQUIRED_PRECISION
				&& avgMethodLength <= expAvgMethodLength + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber, complexityNumber);
		assertTrue(complexityDensity >= expComplexityDensity - REQUIRED_PRECISION
				&& complexityDensity <= expComplexityDensity + REQUIRED_PRECISION);
	}
	
	/**
	 *  A test case with two classes in different packages. Both classes
	 * 	have the same name, but should be distinguishable by
	 * 	the fully qualified class name.
	 */
	@Test
	public void duplicateClassesTest() {
		final int NUM_METHODS_1 = 1;
		final int TOTAL_CLASS_NCSS_1 = 2;
		final int TOTAL_CLASS_CCN_1 = 2;
		
		// Expected results class 1
		String mapKeyClassName1 = "package.differentPackage1.DuplicateClassName";
		String expQualifiedClassName1 = mapKeyClassName1;
		String expShortClassName1 = "DuplicateClassName";
		int expNumMethods1 = NUM_METHODS_1;
		float expAvgMethodLength1 = (float) TOTAL_CLASS_NCSS_1 / expNumMethods1;
		int expComplexityNumber1 = TOTAL_CLASS_CCN_1;
		float expComplexityDensity1 = (float) expComplexityNumber1 
				/ (expAvgMethodLength1 * expNumMethods1);
		
		// Actual results class 1
		JavaNCSSClassMetric classMetric1 = classMetricsMap.get(mapKeyClassName1);
		String qualifiedClassName1 = classMetric1.getQualifiedClassName();
		String shortClassName1 = classMetric1.getClassName();
		int numMethods1 = classMetric1.getNumMethods();
		float avgMethodLength1 = classMetric1.getAverageMethodLength();
		int complexityNumber1 = classMetric1.getComplexityNumber();
		float complexityDensity1 = classMetric1.getComplexityDensity();
		
		assertEquals(expQualifiedClassName1, qualifiedClassName1);
		assertEquals(expShortClassName1, shortClassName1);
		assertEquals(expNumMethods1, numMethods1);
		assertTrue(avgMethodLength1 >= expAvgMethodLength1 - REQUIRED_PRECISION
				&& avgMethodLength1 <= expAvgMethodLength1 + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber1, complexityNumber1);
		assertTrue(complexityDensity1 >= expComplexityDensity1 - REQUIRED_PRECISION
				&& complexityDensity1 <= expComplexityDensity1 + REQUIRED_PRECISION);
		
		final int NUM_METHODS_2 = 1;
		final int TOTAL_CLASS_NCSS_2 = 7;
		final int TOTAL_CLASS_CCN_2 = 7;
		
		// Expected results 2
		String mapKeyClassName2 = "package.differentPackage2.DuplicateClassName";
		String expQualifiedClassName2 = mapKeyClassName2;
		String expShortClassName2 = "DuplicateClassName";
		int expNumMethods2 = NUM_METHODS_2;
		float expAvgMethodLength2 = (float) TOTAL_CLASS_NCSS_2 / expNumMethods2;
		int expComplexityNumber2 = TOTAL_CLASS_CCN_2;
		float expComplexityDensity2 = (float) expComplexityNumber2 
				/ (expAvgMethodLength2 * expNumMethods2);
		
		// Actual results 2
		JavaNCSSClassMetric classMetric2 = classMetricsMap.get(mapKeyClassName2);
		String qualifiedClassName2 = classMetric2.getQualifiedClassName();
		String shortClassName2 = classMetric2.getClassName();
		int numMethods2 = classMetric2.getNumMethods();
		float avgMethodLength2 = classMetric2.getAverageMethodLength();
		int complexityNumber2 = classMetric2.getComplexityNumber();
		float complexityDensity2 = classMetric2.getComplexityDensity();
		
		// verify that class names are duplicates, but have different
		// fully qualified class names
		assertTrue(shortClassName1.equals(shortClassName2));
		assertTrue(!qualifiedClassName1.equals(qualifiedClassName2));
		
		assertEquals(expQualifiedClassName2, qualifiedClassName2);
		assertEquals(expShortClassName2, shortClassName2);
		assertEquals(expNumMethods2, numMethods2);
		assertTrue(avgMethodLength2 >= expAvgMethodLength2 - REQUIRED_PRECISION
				&& avgMethodLength2 <= expAvgMethodLength2 + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber2, complexityNumber2);
		assertTrue(complexityDensity2 >= expComplexityDensity2 - REQUIRED_PRECISION
				&& complexityDensity2 <= expComplexityDensity2 + REQUIRED_PRECISION);
	}
	
	/**
	 * A test case with a class nested inside of a larger number of subclasses.
	 */
	@Test
	public void nestedSubclassesTest() {
		final int method1NCSS = 3;
		final int method1CCN = 3;
		final int method2NCSS = 11;
		final int method2CCN = 11;
		
		// Expected results
		String mapKeyClassName = "package.subPackage1.subPackage2.subpackage3.PackageClass";
		String expQualifiedClassName = mapKeyClassName;
		String expShortClassName = "PackageClass";
		int expNumMethods = 2;
		float expAvgMethodLength = (float) (method1NCSS + method2NCSS) / expNumMethods;
		int expComplexityNumber = method1CCN + method2CCN;
		float expComplexityDensity = (float) expComplexityNumber 
				/ (expAvgMethodLength * expNumMethods);
		
		// Actual results
		JavaNCSSClassMetric classMetric = classMetricsMap.get(mapKeyClassName);
		String qualifiedClassName = classMetric.getQualifiedClassName();
		String shortClassName = classMetric.getClassName();
		int numMethods = classMetric.getNumMethods();
		float avgMethodLength = classMetric.getAverageMethodLength();
		int complexityNumber = classMetric.getComplexityNumber();
		float complexityDensity = classMetric.getComplexityDensity();
		
		assertEquals(expQualifiedClassName, qualifiedClassName);
		assertEquals(expShortClassName, shortClassName);
		assertEquals(expNumMethods, numMethods);
		assertTrue(avgMethodLength >= expAvgMethodLength - REQUIRED_PRECISION
				&& avgMethodLength <= expAvgMethodLength + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber, complexityNumber);
		assertTrue(complexityDensity >= expComplexityDensity - REQUIRED_PRECISION
				&& complexityDensity <= expComplexityDensity + REQUIRED_PRECISION);
	}
	
	/**
	 * A test case with a qualified class name as a parameter into a method.
	 * This has dots in the name and could break the parsing.
	 */ 
	@Test
	public void parametersIgnoredTest() {
		final int NUM_METHODS = 1;
		final int TOTAL_CLASS_NCSS = 4;
		final int TOTAL_CLASS_CCN = 4;
		
		// Expected results
		String mapKeyClassName = "package.subpackage.DotClass";
		String expQualifiedClassName = mapKeyClassName;
		String expShortClassName = "DotClass";
		int expNumMethods = NUM_METHODS;
		float expAvgMethodLength = (float) TOTAL_CLASS_NCSS / expNumMethods;
		int expComplexityNumber = TOTAL_CLASS_CCN;
		float expComplexityDensity = (float) expComplexityNumber 
				/ (expAvgMethodLength * expNumMethods);
		
		// Actual results
		JavaNCSSClassMetric classMetric = classMetricsMap.get(mapKeyClassName);
		String qualifiedClassName = classMetric.getQualifiedClassName();
		String shortClassName = classMetric.getClassName();
		int numMethods = classMetric.getNumMethods();
		float avgMethodLength = classMetric.getAverageMethodLength();
		int complexityNumber = classMetric.getComplexityNumber();
		float complexityDensity = classMetric.getComplexityDensity();
		
		assertEquals(expQualifiedClassName, qualifiedClassName);
		assertEquals(expShortClassName, shortClassName);
		assertEquals(expNumMethods, numMethods);
		assertTrue(avgMethodLength >= expAvgMethodLength - REQUIRED_PRECISION
				&& avgMethodLength <= expAvgMethodLength + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber, complexityNumber);
		assertTrue(complexityDensity >= expComplexityDensity - REQUIRED_PRECISION
				&& complexityDensity <= expComplexityDensity + REQUIRED_PRECISION);
	}
	
	/**
	 * Test case with a method name with a larger number of parameters
	 * that should be ignored.
	 */
	@Test
	public void parametersWithDotsTest() {
		final int NUM_METHODS = 1;
		final int TOTAL_CLASS_NCSS = 5;
		final int TOTAL_CLASS_CCN = 5;
		
		// Expected results
		String mapKeyClassName = "package.subpackage.ParamsTestClass";
		String expQualifiedClassName = mapKeyClassName;
		String expShortClassName = "ParamsTestClass";
		int expNumMethods = NUM_METHODS;
		float expAvgMethodLength = (float) TOTAL_CLASS_NCSS / expNumMethods;
		int expComplexityNumber = TOTAL_CLASS_CCN;
		float expComplexityDensity = (float) expComplexityNumber 
				/ (expAvgMethodLength * expNumMethods);
		
		// Actual results
		JavaNCSSClassMetric classMetric = classMetricsMap.get(mapKeyClassName);
		String qualifiedClassName = classMetric.getQualifiedClassName();
		String shortClassName = classMetric.getClassName();
		int numMethods = classMetric.getNumMethods();
		float avgMethodLength = classMetric.getAverageMethodLength();
		int complexityNumber = classMetric.getComplexityNumber();
		float complexityDensity = classMetric.getComplexityDensity();
		
		assertEquals(expQualifiedClassName, qualifiedClassName);
		assertEquals(expShortClassName, shortClassName);
		assertEquals(expNumMethods, numMethods);
		assertTrue(avgMethodLength >= expAvgMethodLength - REQUIRED_PRECISION
				&& avgMethodLength <= expAvgMethodLength + REQUIRED_PRECISION);
		assertEquals(expComplexityNumber, complexityNumber);
		assertTrue(complexityDensity >= expComplexityDensity - REQUIRED_PRECISION
				&& complexityDensity <= expComplexityDensity + REQUIRED_PRECISION);
	}
}
