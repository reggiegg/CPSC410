

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StatSVNMetricsTest {

	private StatSVNMetrics metric;

	private List<StatSVNClassMetric> classMetrics = new ArrayList<StatSVNClassMetric>();
	private static final int CHURN = 1;
	private static final int NUM_DEVELOPERS = 2;
	
	private static final String PATH = "testPath/test.java";
	private static final String CLASS_NAME = "test.java";
	private static final int NUM_REVISIONS = 4;

	/**
	 * Initialize the object before each run
	 */
	@Before
	public void setup() 
	{
		classMetrics.add(new StatSVNClassMetric(PATH, CLASS_NAME, NUM_REVISIONS));
		metric = new StatSVNMetrics(classMetrics, CHURN, NUM_DEVELOPERS);
	}

	/**
	 * Tests that the constructor properly creates a metric
	 */
	@Test
	public void testConstructor() 
	{
		assertNotNull(metric);
	}

	/**
	 * Tests the successful return of the class metrics list
	 */
	@Test
	public void testGetClassMetricsList() 
	{
		assertEquals(classMetrics, metric.getClassMetricsList());
	}

	/**
	 * Tests the successful return of the churn int
	 */
	@Test
	public void testGetChurn() 
	{
		assertEquals(CHURN, metric.getChurn());

	}

	/**
	 * Test the successful return of the number of developers int
	 */
	@Test
	public void testGetDevelopers() 
	{
		assertEquals(NUM_DEVELOPERS, metric.getNumDevelopers());

	}
	
	
}

