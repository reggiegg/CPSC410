import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class PlanetTest {
	
	private Planet planet;
	private Integer testId = new Integer(13);
	private Integer testRadius = new Integer(7);
	private Integer testAxis = new Integer(43);
	private Integer testStarRadius = new Integer(1);
	private Integer testTeff = new Integer(1);

	@Before
	public void setup() {
		planet = new Planet(testId, "outrageousCool", testRadius, testAxis);
	}
	
	@Test
	public void testConstructor() {
		Planet pla = new Planet(new Integer(123), "planetName", new Integer(5), new Integer(10));
		assertEquals(new Integer(123), pla.getSolarSystemId());
		assertEquals(new Integer(5), pla.getPlanetRadius());
		assertEquals(new Integer(10), pla.getSemimajorAxis());
		assertEquals("planetName", pla.getPlanetName());
		assertEquals(testStarRadius, pla.getStarRadius());
		assertEquals(testTeff, pla.getStarTeff());
	}
	
	@Test	
	public void testSetSolarSystemId() {
		assertEquals(testId, planet.getSolarSystemId());
		planet.setSolarSystemId(new Integer(47));;
		assertEquals(new Integer(47), planet.getSolarSystemId());
	}

	@Test
	public void testSetPlanetName() {
		assertEquals("outrageousCool", planet.getPlanetName());
		planet.setPlanetName("outOfThisWorld");
		assertEquals("outOfThisWorld", planet.getPlanetName());
	}

	@Test
	public void testSetPlanetRadius() {
		assertEquals(testRadius, planet.getPlanetRadius());
		planet.setPlanetRadius(new Integer(11));
		assertEquals(new Integer(11), planet.getPlanetRadius());
	}

	@Test
	public void testSetSemimajorAxis() {
		assertEquals(testAxis, planet.getSemimajorAxis());
		planet.setSemimajorAxis(new Integer(51));
		assertEquals(new Integer(51), planet.getSemimajorAxis());
	}

	@Test
	public void testSetStarRadius() {
		assertEquals(testStarRadius, planet.getStarRadius());
		planet.setStarRadius(new Integer(2));
		assertEquals(new Integer(2), planet.getStarRadius());
	}

	@Test
	public void testSetStarTeff() {
		assertEquals(testTeff, planet.getStarTeff());
		planet.setStarTeff(new Integer(19));
		assertEquals(new Integer(19), planet.getStarTeff());
	}
	
}
