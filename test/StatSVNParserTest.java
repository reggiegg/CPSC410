import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;


public class StatSVNParserTest {
	private File path = new File("src/repo-statistics.xml");
	private StatSVNParser parser;
	private StatSVNMetrics ssvnm;
	private int TEST_CHURN = 19;
	private int TEST_DEVELOPERS = 9;
	
	@Before
	public void setup() 
	{
		this.parser = new StatSVNParser();
		this.ssvnm = parser.getStatSVNMetrics(path);
	}
	
	@Test
	public void testConstructor() 
	{
		assertNotNull(parser.getDocument());
	}
	
	@Test
	public void testParseChurn() 
	{
		int churn = ssvnm.getChurn();
		assertEquals(churn, TEST_CHURN);
	}

	
	@Test
	public void testParseNumRevisions() 
	{
		int numDevelopers = ssvnm.getNumDevelopers();
		assertEquals(TEST_DEVELOPERS, numDevelopers);
	}
	
	@Test
	public void testParseRevisionsPerFile()
	{
		//TODO: Fill this in.
	}
	
}
