import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



public class StatSVNClassMetricTest {

	private static final String PATH = "testPath/test.java";
	private static final String CLASS_NAME = "test.java";
	private static final int NUM_REVISIONS = 4;
	private StatSVNClassMetric metric;
	
	/**
	 * Initialize the object before each run
	 */
	@Before
	public void setup() 
	{
		metric = new StatSVNClassMetric(PATH, CLASS_NAME, NUM_REVISIONS);
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
	 * Tests the successful return of the path string
	 */
	@Test
	public void testGetPath() 
	{
		assertEquals(PATH, metric.getPath());
	}

	/**
	 * Tests the successful return of the class name string
	 */
	@Test
	public void testGetClassName() 
	{
		assertEquals(CLASS_NAME, metric.getClassName());

	}

	/**
	 * Test the successful return of the number of revisions int
	 */
	@Test
	public void testGetNumRevisions() 
	{
		assertEquals(NUM_REVISIONS, metric.getNumRevisions());

	}
}



