import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class CSVResultGeneratorTest {
	
	private ArrayList<Planet> planets = new ArrayList<Planet>();
	private SolarSystem system = new SolarSystem(new Integer(17));
	
	@Before
	public void setup() {
		planets.add(new Planet(new Integer(12), "awesome", new Integer(7), new Integer(10), new Integer(13), "009933"));
		planets.add(new Planet(new Integer(34), "radical", new Integer(8), new Integer(11), new Integer(14), "FFFF00"));
		planets.add(new Planet(new Integer(56), "chill", new Integer(9), new Integer(12), new Integer(15), "0A0B0C"));
		system.setPlanets(planets);
	}
	
	@Test
	public void testSystem() {
        CSVResultGenerator.writeToCSV(system);
	}
	
}
