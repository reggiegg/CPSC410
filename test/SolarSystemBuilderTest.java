import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * This class contains tests for methods in the SolarSystemBuilder class.
 * @author Susannah Kirby
 *
 */
public class SolarSystemBuilderTest {
	
	private Integer testPurpleLow = new Integer(1);
	private Integer testPurpleHigh = SolarSystemConstants.DEVS_1;
	private Integer testBlueLow = SolarSystemConstants.DEVS_1+1;
	private Integer testBlueHigh = SolarSystemConstants.DEVS_2;
	private Integer testGreenLow = SolarSystemConstants.DEVS_2+1;
	private Integer testGreenHigh = SolarSystemConstants.DEVS_3;
	private Integer testYellowLow = SolarSystemConstants.DEVS_3+1;
	private Integer testYellowHigh = SolarSystemConstants.DEVS_4;
	private Integer testOrangeLow = SolarSystemConstants.DEVS_4+1;
	private Integer testOrangeHigh = SolarSystemConstants.DEVS_5;
	private Integer testRedLow = SolarSystemConstants.DEVS_5+1;
	private Integer testRedHigh = new Integer(1000);
	
	private Integer testChurnFirstLevelLow = new Integer(1);
	private Integer testChurnFirstLevelHigh = new Integer(SolarSystemConstants.CHURN_1);
	private Integer testChurnSecondLevelLow = new Integer(SolarSystemConstants.CHURN_1+1);
	private Integer testChurnSecondLevelHigh = new Integer(SolarSystemConstants.CHURN_2);
	private Integer testChurnThirdLevelLow = new Integer(SolarSystemConstants.CHURN_2+1);
	private Integer testChurnThirdLevelHigh = new Integer(SolarSystemConstants.CHURN_3);
	private Integer testChurnFourthLevelLow = new Integer(SolarSystemConstants.CHURN_3+1);
	private Integer testChurnFourthLevelHigh = new Integer(SolarSystemConstants.CHURN_4);
	private Integer testChurnFifthLevelLow = new Integer(SolarSystemConstants.CHURN_4+1);
	private Integer testChurnFifthLevelHigh = new Integer(SolarSystemConstants.CHURN_5);
	private Integer testChurnSixthLevelLow = new Integer(SolarSystemConstants.CHURN_5+1);
	private Integer testChurnSixthLevelHigh = new Integer(10000);
	
	private SolarSystemBuilder builder;
	
	@Before
	public void setup() {
		builder = new SolarSystemBuilder();
	}
	
	@Test
	public void testBuildColourPurple() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testPurpleLow);
		assertEquals(SolarSystemConstants.PURPLE, builder.getResult().getColour());
		builder.buildColour(testPurpleHigh);
		assertEquals(SolarSystemConstants.PURPLE, builder.getResult().getColour());
	}

	@Test
	public void testBuildColourBlue() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testBlueLow);
		assertEquals(SolarSystemConstants.BLUE, builder.getResult().getColour());
		builder.buildColour(testBlueHigh);
		assertEquals(SolarSystemConstants.BLUE, builder.getResult().getColour());
	}
	@Test
	public void testBuildColourGreen() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testGreenLow);
		assertEquals(SolarSystemConstants.GREEN, builder.getResult().getColour());
		builder.buildColour(testGreenHigh);
		assertEquals(SolarSystemConstants.GREEN, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourYellow() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testYellowLow);
		assertEquals(SolarSystemConstants.YELLOW, builder.getResult().getColour());
		builder.buildColour(testYellowHigh);
		assertEquals(SolarSystemConstants.YELLOW, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourOrange() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testOrangeLow);
		assertEquals(SolarSystemConstants.ORANGE, builder.getResult().getColour());
		builder.buildColour(testOrangeHigh);
		assertEquals(SolarSystemConstants.ORANGE, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourRed() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testRedLow);
		assertEquals(SolarSystemConstants.RED, builder.getResult().getColour());
		builder.buildColour(testRedHigh);
		assertEquals(SolarSystemConstants.RED, builder.getResult().getColour());
	}
	
	@Test 
	public void testBuildSpeed1() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFirstLevelLow);
		assertEquals(SolarSystemConstants.SPEED_1, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFirstLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_1, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed2() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSecondLevelLow);
		assertEquals(SolarSystemConstants.SPEED_2, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSecondLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_2, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed3() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnThirdLevelLow);
		assertEquals(SolarSystemConstants.SPEED_3, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnThirdLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_3, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed4() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFourthLevelLow);
		assertEquals(SolarSystemConstants.SPEED_4, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFourthLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_4, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed5() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFifthLevelLow);
		assertEquals(SolarSystemConstants.SPEED_5, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFifthLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_5, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed6() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSixthLevelLow);
		assertEquals(SolarSystemConstants.SPEED_6, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSixthLevelHigh);
		assertEquals(SolarSystemConstants.SPEED_6, builder.getResult().getSpeed());
	}
	
	@Test 
	public void testDeterminePlanetPeriod() {
		assertEquals(SolarSystemConstants.PERIOD_6, builder.determinePlanetPeriod(1));
		assertEquals(SolarSystemConstants.PERIOD_6, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_1));
		assertEquals(SolarSystemConstants.PERIOD_5, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_1+1));
		assertEquals(SolarSystemConstants.PERIOD_5, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_2));
		assertEquals(SolarSystemConstants.PERIOD_4, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_2+1));
		assertEquals(SolarSystemConstants.PERIOD_4, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_3));
		assertEquals(SolarSystemConstants.PERIOD_3, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_3+1));
		assertEquals(SolarSystemConstants.PERIOD_3, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_4));
		assertEquals(SolarSystemConstants.PERIOD_2, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_4+1));
		assertEquals(SolarSystemConstants.PERIOD_2, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_5));
		assertEquals(SolarSystemConstants.PERIOD_1, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_5+1));
		assertEquals(SolarSystemConstants.PERIOD_1, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_6));
		assertEquals(SolarSystemConstants.PERIOD_1, builder.determinePlanetPeriod(SolarSystemConstants.SPEED_6+10000));
	}
	
	@Test
	public void testDeterminePlanetRadius() {
		assertEquals(SolarSystemConstants.RADIUS_1, builder.determinePlanetRadius(1));
		assertEquals(SolarSystemConstants.RADIUS_1, builder.determinePlanetRadius(SolarSystemConstants.METHODS_1));
		assertEquals(SolarSystemConstants.RADIUS_2, builder.determinePlanetRadius(SolarSystemConstants.METHODS_1+1));
		assertEquals(SolarSystemConstants.RADIUS_2, builder.determinePlanetRadius(SolarSystemConstants.METHODS_2));
		assertEquals(SolarSystemConstants.RADIUS_3, builder.determinePlanetRadius(SolarSystemConstants.METHODS_2+1));
		assertEquals(SolarSystemConstants.RADIUS_3, builder.determinePlanetRadius(SolarSystemConstants.METHODS_3));
		assertEquals(SolarSystemConstants.RADIUS_4, builder.determinePlanetRadius(SolarSystemConstants.METHODS_3+1));
		assertEquals(SolarSystemConstants.RADIUS_4, builder.determinePlanetRadius(SolarSystemConstants.METHODS_4));
		assertEquals(SolarSystemConstants.RADIUS_5, builder.determinePlanetRadius(SolarSystemConstants.METHODS_4+1));
		assertEquals(SolarSystemConstants.RADIUS_5, builder.determinePlanetRadius(SolarSystemConstants.METHODS_5));
		assertEquals(SolarSystemConstants.RADIUS_6, builder.determinePlanetRadius(SolarSystemConstants.METHODS_5+1));
		assertEquals(SolarSystemConstants.RADIUS_6, builder.determinePlanetRadius(10000));
	}

	@Test
	public void testDeterminePlanetAxis() {
		assertEquals(SolarSystemConstants.AXIS_1, builder.determinePlanetAxis(1));
		assertEquals(SolarSystemConstants.AXIS_1, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_1));
		assertEquals(SolarSystemConstants.AXIS_2, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_1+1));
		assertEquals(SolarSystemConstants.AXIS_2, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_2));
		assertEquals(SolarSystemConstants.AXIS_3, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_2+1));
		assertEquals(SolarSystemConstants.AXIS_3, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_3));
		assertEquals(SolarSystemConstants.AXIS_4, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_3+1));
		assertEquals(SolarSystemConstants.AXIS_4, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_4));
		assertEquals(SolarSystemConstants.AXIS_5, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_4+1));
		assertEquals(SolarSystemConstants.AXIS_5, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_5));
		assertEquals(SolarSystemConstants.AXIS_6, builder.determinePlanetAxis(SolarSystemConstants.REVISIONS_5+1));
		assertEquals(SolarSystemConstants.AXIS_6, builder.determinePlanetAxis(10000));
	}
	
	@Test
	public void testDeterminePlanetHueRed() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.RED, 16);
		Integer mult = SolarSystemConstants.MULT_RED;
		
		Integer comp = 2;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testRedLow);
		assertEquals(SolarSystemConstants.RED, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.21167));
		
		comp = 7;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testRedHigh);
		assertEquals(SolarSystemConstants.RED, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.74530));
		
	}
	
	@Test
	public void testDeterminePlanetHueOrange() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.ORANGE, 16);
		Integer mult = SolarSystemConstants.MULT_ORANGE;
		
		Integer comp = 3;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testOrangeLow);
		assertEquals(SolarSystemConstants.ORANGE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.31167));
		
		comp = 5;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testOrangeHigh);
		assertEquals(SolarSystemConstants.ORANGE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.52993));
	}
	
	@Test
	public void testDeterminePlanetHueYellow() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.YELLOW, 16);
		Integer mult = SolarSystemConstants.MULT_YELLOW;
		
		Integer comp = 1;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testYellowLow);
		assertEquals(SolarSystemConstants.YELLOW, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.11167));
		
		comp = 6;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testYellowHigh);
		assertEquals(SolarSystemConstants.YELLOW, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.59937));
		
	}
	
	
	@Test
	public void testDeterminePlanetHueGreen() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.GREEN, 16);
		Integer mult = SolarSystemConstants.MULT_GREEN;
		
		Integer comp = 4;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testGreenLow);
		assertEquals(SolarSystemConstants.GREEN, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.41697));
		
		comp = 8;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testGreenHigh);
		assertEquals(SolarSystemConstants.GREEN, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.81679));
		
	}
	
	@Test
	public void testDeterminePlanetHueBlue() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.BLUE, 16);
		Integer mult = SolarSystemConstants.MULT_BLUE;
		
		Integer comp = 2;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testBlueLow);
		assertEquals(SolarSystemConstants.BLUE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.19007));
		
		comp = 9;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testBlueHigh);
		assertEquals(SolarSystemConstants.BLUE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.88427));
		
	}
	
	@Test
	public void testDeterminePlanetHuePurple() {
		Integer baseHex = Integer.parseInt(SolarSystemConstants.PURPLE, 16);
		Integer mult = SolarSystemConstants.MULT_PURPLE;
		
		Integer comp = 4;
		Integer addOn = comp * mult;
		Integer newColour = baseHex + addOn;
		
		builder.buildColour(testPurpleLow);
		assertEquals(SolarSystemConstants.PURPLE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.40862));
		
		comp = 5;
		addOn = comp * mult;
		newColour = baseHex + addOn;
		
		builder.buildColour(testPurpleHigh);
		assertEquals(SolarSystemConstants.PURPLE, builder.getResult().getColour());
		
		assertEquals(String.valueOf(newColour), builder.determinePlanetHue((float) 0.52370));

	}
	
	@Test
	public void testGetMultiplierForHueComputation() {
		assertEquals(SolarSystemConstants.MULT_RED, builder.getMultiplier(SolarSystemConstants.RED));
		assertEquals(SolarSystemConstants.MULT_ORANGE, builder.getMultiplier(SolarSystemConstants.ORANGE));
		assertEquals(SolarSystemConstants.MULT_YELLOW, builder.getMultiplier(SolarSystemConstants.YELLOW));
		assertEquals(SolarSystemConstants.MULT_GREEN, builder.getMultiplier(SolarSystemConstants.GREEN));
		assertEquals(SolarSystemConstants.MULT_BLUE, builder.getMultiplier(SolarSystemConstants.BLUE));
		assertEquals(SolarSystemConstants.MULT_PURPLE, builder.getMultiplier(SolarSystemConstants.PURPLE));
	}
	

}
