import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class SolarSystemTest {

	private Integer testInt = new Integer(5);
	private Integer testPurpleLow = new Integer(1);
	private Integer testPurpleHigh = new Integer(3);
	private Integer testBlueLow = new Integer(4);
	private Integer testBlueHigh = new Integer(6);
	private Integer testGreenLow = new Integer(7);
	private Integer testGreenHigh = new Integer(9);
	private Integer testYellowLow = new Integer(10);
	private Integer testYellowHigh = new Integer(12);
	private Integer testOrangeLow = new Integer(13);
	private Integer testOrangeHigh = new Integer(15);
	private Integer testRedLow = new Integer(16);
	private Integer testRedHigh = new Integer(1000);
	private String purple = "800080";
	private String blue = "0000FF";
	private String green = "008000";
	private String yellow = "FFFF00";
	private String orange = "FFA500";
	private String red = "FF0000";
	
	
	private Planet testPlanet = new Planet(testInt, "name", testInt, testInt, testInt, "otherString");
	private Planet testPlanet2 = new Planet(testInt, "name2", testInt, testInt, testInt, "otherString2");
	
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
	public void testSetColourPurple() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testPurpleLow);
		assertEquals(purple, testSystem.getColour());
		testSystem.setColour(testPurpleHigh);
		assertEquals(purple, testSystem.getColour());
	}

	@Test
	public void testSetColourBlue() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testBlueLow);
		assertEquals(blue, testSystem.getColour());
		testSystem.setColour(testBlueHigh);
		assertEquals(blue, testSystem.getColour());
	}
	@Test
	public void testSetColourGreen() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testGreenLow);
		assertEquals(green, testSystem.getColour());
		testSystem.setColour(testGreenHigh);
		assertEquals(green, testSystem.getColour());
	}
	
	@Test
	public void testSetColourYellow() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testYellowLow);
		assertEquals(yellow, testSystem.getColour());
		testSystem.setColour(testYellowHigh);
		assertEquals(yellow, testSystem.getColour());
	}
	
	@Test
	public void testSetColourOrange() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testOrangeLow);
		assertEquals(orange, testSystem.getColour());
		testSystem.setColour(testOrangeHigh);
		assertEquals(orange, testSystem.getColour());
	}
	
	@Test
	public void testSetColourRed() {
		assertNull(testSystem.getColour());
		testSystem.setColour(testRedLow);
		assertEquals(red, testSystem.getColour());
		testSystem.setColour(testRedHigh);
		assertEquals(red, testSystem.getColour());
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
