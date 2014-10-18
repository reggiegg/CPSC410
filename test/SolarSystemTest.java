import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class SolarSystemTest {

	private Integer testInt = new Integer(5);
	private String testColour = "5";
	
	private Planet testPlanet = new Planet(testInt, "name", testInt, testInt, "otherString");
	private Planet testPlanet2 = new Planet(testInt, "name2", testInt, testInt, "otherString2");
	
	private SolarSystem testSystem;
	private SolarSystem testSystem2;
	
	@Before
	public void setup() {
		testSystem = new SolarSystem(testInt);
	}
	
	@Test
	public void testConstructor() {
		SolarSystem test = new SolarSystem(new Integer(13));
		assertEquals(new Integer(13), test.getId());
		assertEquals(0, test.getPlanets().size());
		assertNull(test.getSpeed());
		assertNull(test.getColour());
	}
	
	@Test
	public void testSetColour() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testInt);
		assertEquals(testColour, testSystem.getColour());
	}
	
	@Test
	public void setSpeed() {
		assertNull(testSystem.getSpeed());
		testSystem.setSpeed(testInt);
		assertEquals(testInt, testSystem.getSpeed());
	}
	
	@Test
	public void testAddPlanet() {
		assertEquals(0, testSystem.getPlanets().size());
		testSystem.addPlanet(testPlanet);
		assertEquals(1, testSystem.getPlanets().size());
		assertEquals(testPlanet, testSystem.getPlanets().get(0));
		testSystem.addPlanet(testPlanet2);
		assertEquals(2, testSystem.getPlanets().size());
		assertEquals(testPlanet2, testSystem.getPlanets().get(1));
	}
	
	@Test
	public void testSetPlanets() {
		assertEquals(0, testSystem.getPlanets().size());
		ArrayList<Planet> planets = new ArrayList<Planet>();
		planets.add(testPlanet);
		planets.add(testPlanet2);
		testSystem.setPlanets(planets);
		assertEquals(2, testSystem.getPlanets().size());
		assertEquals(testPlanet, testSystem.getPlanets().get(0));
		assertEquals(testPlanet2, testSystem.getPlanets().get(1));
	}
	
	@Test
	public void testEqualsTrue() {
		testSystem2 = new SolarSystem(testInt);
		assertTrue(testSystem2.equals(testSystem));
	}

	@Test
	public void testEqualsWrongId() {
		testSystem2 = new SolarSystem(new Integer(91));
		assertFalse(testSystem2.equals(testSystem));
	}

	@Test
	public void testEqualsWrongPlanets() {
		testSystem2 = new SolarSystem(testInt);
		testSystem2.addPlanet(testPlanet);
		assertFalse(testSystem2.equals(testSystem));
	}
	
	@Test
	public void testHashCodeEquals() {
		testSystem2 = new SolarSystem(testInt);
		assertEquals(testSystem2.hashCode(), testSystem.hashCode());
	}
	
	@Test
	public void testHashCodeDoesNotEqualId() {
		testSystem2 = new SolarSystem(new Integer(91));
		assertFalse(testSystem2.hashCode() == testSystem.hashCode());
	}

	@Test
	public void testHashCodeDoesNotEqualPlanets() {
		testSystem2 = new SolarSystem(testInt);
		testSystem2.addPlanet(testPlanet);
		assertFalse(testSystem2.equals(testSystem));
		assertFalse(testSystem2.hashCode() == testSystem.hashCode());
	}
}
