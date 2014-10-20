import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class StatSVNParserTest {
	private String path = "src/repo-statistics.xml";
	private StatSVNParser parser;
	private int TEST_CHURN = 19;
	private int TEST_REVISIONS = 1627259;
	
	@Before
	public void setup() 
	{
		parser = new StatSVNParser(path);
	}
	
	@Test
	public void testConstructor() 
	{
		StatSVNParser ssvnp;
		ssvnp = new StatSVNParser(path);
		assertNotNull(ssvnp.getDocument());
	}
	
	@Test
	public void testParseChurn() 
	{
		int churn = parser.getChurn();
		assertEquals(churn, TEST_CHURN);
	}

	
	@Test
	public void testParseNumRevisions() 
	{
		int numRevisions = parser.getNumberOfRevisions();
		System.out.println(numRevisions);
		assertEquals(TEST_REVISIONS, numRevisions);
	}
	
}
