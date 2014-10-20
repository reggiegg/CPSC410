import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;


public class SolarSystemBuilderTest {
	
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
	
	private Integer testChurnFirstLevelLow = new Integer(1);
	private Integer testChurnFirstLevelHigh = new Integer(1000);
	private Integer testChurnSecondLevelLow = new Integer(1001);
	private Integer testChurnSecondLevelHigh = new Integer(2000);
	private Integer testChurnThirdLevelLow = new Integer(2001);
	private Integer testChurnThirdLevelHigh = new Integer(3000);
	private Integer testChurnFourthLevelLow = new Integer(3001);
	private Integer testChurnFourthLevelHigh = new Integer(4000);
	private Integer testChurnFifthLevelLow = new Integer(4001);
	private Integer testChurnFifthLevelHigh = new Integer(5000);
	private Integer testChurnSixthLevelLow = new Integer(5001);
	private Integer testChurnSixthLevelHigh = new Integer(10000);

	private Integer speed1 = new Integer(3);
	private Integer speed2 = new Integer(6);
	private Integer speed3 = new Integer(9);
	private Integer speed4 = new Integer(12);
	private Integer speed5 = new Integer(15);
	private Integer speed6 = new Integer(18);
	
	private Integer period1 = new Integer(18);
	private Integer period2 = new Integer(15);
	private Integer period3 = new Integer(12);
	private Integer period4 = new Integer(9);
	private Integer period5 = new Integer(6);
	private Integer period6 = new Integer(3);

	private Integer radius1 = new Integer(10);
	private Integer radius2 = new Integer(20);
	private Integer radius3 = new Integer(30);
	private Integer radius4 = new Integer(40);
	private Integer radius5 = new Integer(50);
	private Integer radius6 = new Integer(60);

	private Float axis1 = new Float(0.2);
	private Float axis2 = new Float(0.4);
	private Float axis3 = new Float(0.6);
	private Float axis4 = new Float(0.8);
	private Float axis5 = new Float(1.0);
	private Float axis6 = new Float(1.2);
	
	private SolarSystemBuilder builder;
	
	@Before
	public void setup() {
		builder = new SolarSystemBuilder();
	}
	
	@Test
	public void testBuildColourPurple() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testPurpleLow);
		assertEquals(purple, builder.getResult().getColour());
		builder.buildColour(testPurpleHigh);
		assertEquals(purple, builder.getResult().getColour());
	}

	@Test
	public void testBuildColourBlue() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testBlueLow);
		assertEquals(blue, builder.getResult().getColour());
		builder.buildColour(testBlueHigh);
		assertEquals(blue, builder.getResult().getColour());
	}
	@Test
	public void testBuildColourGreen() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testGreenLow);
		assertEquals(green, builder.getResult().getColour());
		builder.buildColour(testGreenHigh);
		assertEquals(green, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourYellow() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testYellowLow);
		assertEquals(yellow, builder.getResult().getColour());
		builder.buildColour(testYellowHigh);
		assertEquals(yellow, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourOrange() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testOrangeLow);
		assertEquals(orange, builder.getResult().getColour());
		builder.buildColour(testOrangeHigh);
		assertEquals(orange, builder.getResult().getColour());
	}
	
	@Test
	public void testBuildColourRed() {
		assertNull(builder.getResult().getColour());
		builder.buildColour(testRedLow);
		assertEquals(red, builder.getResult().getColour());
		builder.buildColour(testRedHigh);
		assertEquals(red, builder.getResult().getColour());
	}
	
	@Test 
	public void testBuildSpeed1() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFirstLevelLow);
		assertEquals(speed1, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFirstLevelHigh);
		assertEquals(speed1, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed2() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSecondLevelLow);
		assertEquals(speed2, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSecondLevelHigh);
		assertEquals(speed2, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed3() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnThirdLevelLow);
		assertEquals(speed3, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnThirdLevelHigh);
		assertEquals(speed3, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed4() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFourthLevelLow);
		assertEquals(speed4, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFourthLevelHigh);
		assertEquals(speed4, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed5() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFifthLevelLow);
		assertEquals(speed5, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnFifthLevelHigh);
		assertEquals(speed5, builder.getResult().getSpeed());
	}

	@Test 
	public void testBuildSpeed6() {
		assertNull(builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSixthLevelLow);
		assertEquals(speed6, builder.getResult().getSpeed());
		builder.buildSpeed(testChurnSixthLevelHigh);
		assertEquals(speed6, builder.getResult().getSpeed());
	}
	
	@Test 
	public void testBuildPlanetPeriod() {
		assertEquals(period1, builder.buildPlanetPeriod(1));
		assertEquals(period1, builder.buildPlanetPeriod(speed1));
		assertEquals(period2, builder.buildPlanetPeriod(speed1+1));
		assertEquals(period2, builder.buildPlanetPeriod(speed2));
		assertEquals(period3, builder.buildPlanetPeriod(speed2+1));
		assertEquals(period3, builder.buildPlanetPeriod(speed3));
		assertEquals(period4, builder.buildPlanetPeriod(speed3+1));
		assertEquals(period4, builder.buildPlanetPeriod(speed4));
		assertEquals(period5, builder.buildPlanetPeriod(speed4+1));
		assertEquals(period5, builder.buildPlanetPeriod(speed5));
		assertEquals(period6, builder.buildPlanetPeriod(speed5+1));
		assertEquals(period6, builder.buildPlanetPeriod(speed6));
		assertEquals(period6, builder.buildPlanetPeriod(speed6+10000));
	}
	
	@Test
	public void testBuildPlanetRadius() {
		assertEquals(radius1, builder.buildPlanetRadius(1));
		assertEquals(radius1, builder.buildPlanetRadius(3));
		assertEquals(radius2, builder.buildPlanetRadius(4));
		assertEquals(radius2, builder.buildPlanetRadius(6));
		assertEquals(radius3, builder.buildPlanetRadius(7));
		assertEquals(radius3, builder.buildPlanetRadius(9));
		assertEquals(radius4, builder.buildPlanetRadius(10));
		assertEquals(radius4, builder.buildPlanetRadius(12));
		assertEquals(radius5, builder.buildPlanetRadius(13));
		assertEquals(radius5, builder.buildPlanetRadius(15));
		assertEquals(radius6, builder.buildPlanetRadius(16));
		assertEquals(radius6, builder.buildPlanetRadius(10000));
	}

	@Test
	public void testBuildPlanetAxis() {
		assertEquals(axis1, builder.buildPlanetAxis(1));
		assertEquals(axis1, builder.buildPlanetAxis(1000));
		assertEquals(axis2, builder.buildPlanetAxis(1001));
		assertEquals(axis2, builder.buildPlanetAxis(2000));
		assertEquals(axis3, builder.buildPlanetAxis(2001));
		assertEquals(axis3, builder.buildPlanetAxis(3000));
		assertEquals(axis4, builder.buildPlanetAxis(3001));
		assertEquals(axis4, builder.buildPlanetAxis(4000));
		assertEquals(axis5, builder.buildPlanetAxis(4001));
		assertEquals(axis5, builder.buildPlanetAxis(5000));
		assertEquals(axis6, builder.buildPlanetAxis(5001));
		assertEquals(axis6, builder.buildPlanetAxis(10000));
	}

}
