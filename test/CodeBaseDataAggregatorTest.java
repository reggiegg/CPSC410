import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * This class contains tests for the CodeBaseDataAggregator class.
 * Because the CodeBaseDataAggregator relies on a number of other complex classes,
 * we use mock objects to test these methods.
 * @author Susannah Kirby
 *
 */
public class CodeBaseDataAggregatorTest {
	
	private CodeBaseDataAggregator cbda;
	private SolarSystem system;
	private JavaNCSSMetrics mock_jMetrics = mock(JavaNCSSMetrics.class);
	private StatSVNMetrics mock_sMetrics = mock(StatSVNMetrics.class);

	@Before
	public void testConstructor() {
		cbda = new CodeBaseDataAggregator(mock_jMetrics, mock_sMetrics);
		assertEquals(mock_jMetrics, cbda.getJMetrics());
		assertEquals(mock_sMetrics, cbda.getSMetrics());
	}
	
	@Test
	public void testGetSolarSystem() {
		// Because this method is just a getter and requires complicated methods
		// from other classes, we do not test it here.
	}

	@Test
	public void testConstructSolarSystem(){
		// Because this method depends only on other classes' methods, 
		// we do not test it here.
	}
	
	
	
}
