

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class StatSVNParserTest {
	private File path = new File("test/statSVNTestFile.xml");
	private StatSVNParser parser;
	private StatSVNMetrics ssvnm;
	private static final int TEST_CHURN = 19;
	private static final int TEST_DEVELOPERS = 9;
	private static final int TEST_NUM_CLASSES = 2;

	
	/**
	 * Initialize the parser before each run
	 */
	@Before
	public void setup() 
	{
		this.parser = new StatSVNParser();
		this.ssvnm = parser.getStatSVNMetrics(path);
	}

	/**
	 * Tests that the constructor properly loads the file
	 */
	@Test
	public void testConstructor() 
	{
		assertNotNull(parser.getDocument());
	}

	/**
	 * Tests the successful parsing of the churn metric
	 */
	@Test
	public void testParseChurn() 
	{
		int churn = ssvnm.getChurn();
		assertEquals(churn, TEST_CHURN);
	}

	/**
	 * Tests the successful parsing of the number of contributors
	 */
	@Test
	public void testParseNumDevelopers() 
	{
		int numDevelopers = ssvnm.getNumDevelopers();
		assertEquals(TEST_DEVELOPERS, numDevelopers);
	}

	/**
	 * Integration test for the overall parsing and storing of all the classInfo metrics 
	 */
	@Test
	public void testParseRevisionsPerFile()
	{
		List<StatSVNClassMetric> metricsList = ssvnm.getClassMetricsList();
		assertEquals(TEST_NUM_CLASSES, metricsList.size());

		StatSVNClassMetric secondMetric = metricsList.get(0);
		assertEquals("secondJava", secondMetric.getClassName());
		assertEquals("rulesrc/scores/secondJava.java", secondMetric.getPath());
		assertEquals(1, secondMetric.getNumRevisions());

		StatSVNClassMetric testfile = metricsList.get(1);
		assertEquals("testfile", testfile.getClassName());
		assertEquals("rulesrc/scores/testfile.java", testfile.getPath());
		assertEquals(3, testfile.getNumRevisions());
	}
}
