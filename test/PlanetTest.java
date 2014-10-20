import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class PlanetTest {
	
	private Planet planet;
	private Integer testId = new Integer(13);
	private Integer testRadius = new Integer(7);
	private Integer testAxis = new Integer(43);
	private Integer testPeriod = new Integer(3);
	private String testHue = "FF0000";
	private Integer testStarRadius = new Integer(1);
	private Integer testTeff = new Integer(1);
	

	@Before
	public void setup() {
		planet = new Planet(testId, "outrageousCool", testRadius, testAxis, testPeriod, testHue);
	}
	
	@Test
	public void testConstructor() {
		Planet pla = new Planet(new Integer(123), "planetName", new Integer(5), new Integer(10), new Integer(15), "009933");
		assertEquals(new Integer(123), pla.getSolarSystemId());
		assertEquals(new Integer(5), pla.getPlanetRadius());
		assertEquals(new Integer(10), pla.getSemimajorAxis());
		assertEquals(new Integer(15), pla.getPeriod());
		assertEquals("planetName", pla.getPlanetName());
		assertEquals("009933", pla.getHue());
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
	public void testSetPeriod() {
		assertEquals(testPeriod, planet.getPeriod());
		planet.setPeriod(new Integer(31));
		assertEquals(new Integer(31), planet.getPeriod());
	}
	
	@Test
	public void testSetColour(){
		assertEquals(testHue, planet.getHue());
		planet.setHue("0066FF");
		assertEquals("0066FF", planet.getHue());
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
	
	@Test
	public void testEqualsTrue() {
		Planet p2 = new Planet(testId, "outrageousCool", testRadius, testAxis, testPeriod, testHue);
		assertTrue(p2.equals(planet));
	}

	@Test
	public void testEqualsWrongName() {
		Planet p2 = new Planet(testId, "wonkyName", testRadius, testAxis, testPeriod, testHue);
		assertFalse(p2.equals(planet));
	}

	@Test
	public void testEqualsWrongSystem() {
		Planet p2 = new Planet(new Integer(71), "outrageousCool", testRadius, testAxis, testPeriod, testHue);
		assertFalse(p2.equals(planet));
	}
	
	@Test
	public void testHashCodeEquals() {
		Planet p2 = new Planet(testId, "outrageousCool", testRadius, testAxis, testPeriod, testHue);
		assertEquals(p2.hashCode(), planet.hashCode());
	}
	
	@Test
	public void testHashCodeDoesNotEqualName() {
		Planet p2 = new Planet(testId, "wonkyName", testRadius, testAxis, testPeriod, testHue);
		assertFalse(p2.hashCode() == planet.hashCode());
	}

	@Test
	public void testHashCodeDoesNotEqualSystem() {
		Planet p2 = new Planet(new Integer(71), "outrageousCool", testRadius, testAxis, testPeriod, testHue);
		assertFalse(p2.hashCode() == planet.hashCode());
	}
}

