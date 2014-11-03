import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;


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
	
	private String purple = SolarSystemConstants.PURPLE;
	private String blue = SolarSystemConstants.BLUE;
	private String green = SolarSystemConstants.GREEN;
	private String yellow = SolarSystemConstants.YELLOW;
	private String orange = SolarSystemConstants.ORANGE;
	private String red = SolarSystemConstants.RED;
	
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

	private Integer speed1 = SolarSystemConstants.SPEED_1;
	private Integer speed2 = SolarSystemConstants.SPEED_2;
	private Integer speed3 = SolarSystemConstants.SPEED_3;
	private Integer speed4 = SolarSystemConstants.SPEED_4;
	private Integer speed5 = SolarSystemConstants.SPEED_5;
	private Integer speed6 = SolarSystemConstants.SPEED_6;
	
	private Integer period1 = SolarSystemConstants.PERIOD_1;
	private Integer period2 = SolarSystemConstants.PERIOD_2;
	private Integer period3 = SolarSystemConstants.PERIOD_3;
	private Integer period4 = SolarSystemConstants.PERIOD_4;
	private Integer period5 = SolarSystemConstants.PERIOD_5;
	private Integer period6 = SolarSystemConstants.PERIOD_6;

	private Integer radius1 = SolarSystemConstants.RADIUS_1;
	private Integer radius2 = SolarSystemConstants.RADIUS_2;
	private Integer radius3 = SolarSystemConstants.RADIUS_3;
	private Integer radius4 = SolarSystemConstants.RADIUS_4;
	private Integer radius5 = SolarSystemConstants.RADIUS_5;
	private Integer radius6 = SolarSystemConstants.RADIUS_6;

	private Float axis1 = SolarSystemConstants.AXIS_1;
	private Float axis2 = SolarSystemConstants.AXIS_2;
	private Float axis3 = SolarSystemConstants.AXIS_3;
	private Float axis4 = SolarSystemConstants.AXIS_4;
	private Float axis5 = SolarSystemConstants.AXIS_5;
	private Float axis6 = SolarSystemConstants.AXIS_6;
	
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
	public void testDeterminePlanetPeriod() {
		assertEquals(period6, builder.determinePlanetPeriod(1));
		assertEquals(period6, builder.determinePlanetPeriod(speed1));
		assertEquals(period5, builder.determinePlanetPeriod(speed1+1));
		assertEquals(period5, builder.determinePlanetPeriod(speed2));
		assertEquals(period4, builder.determinePlanetPeriod(speed2+1));
		assertEquals(period4, builder.determinePlanetPeriod(speed3));
		assertEquals(period3, builder.determinePlanetPeriod(speed3+1));
		assertEquals(period3, builder.determinePlanetPeriod(speed4));
		assertEquals(period2, builder.determinePlanetPeriod(speed4+1));
		assertEquals(period2, builder.determinePlanetPeriod(speed5));
		assertEquals(period1, builder.determinePlanetPeriod(speed5+1));
		assertEquals(period1, builder.determinePlanetPeriod(speed6));
		assertEquals(period1, builder.determinePlanetPeriod(speed6+10000));
	}
	
	@Test
	public void testDeterminePlanetRadius() {
		assertEquals(radius1, builder.determinePlanetRadius(1));
		assertEquals(radius1, builder.determinePlanetRadius(3));
		assertEquals(radius2, builder.determinePlanetRadius(4));
		assertEquals(radius2, builder.determinePlanetRadius(6));
		assertEquals(radius3, builder.determinePlanetRadius(7));
		assertEquals(radius3, builder.determinePlanetRadius(9));
		assertEquals(radius4, builder.determinePlanetRadius(10));
		assertEquals(radius4, builder.determinePlanetRadius(12));
		assertEquals(radius5, builder.determinePlanetRadius(13));
		assertEquals(radius5, builder.determinePlanetRadius(15));
		assertEquals(radius6, builder.determinePlanetRadius(16));
		assertEquals(radius6, builder.determinePlanetRadius(10000));
	}

	@Test
	public void testDeterminePlanetAxis() {
		assertEquals(axis1, builder.determinePlanetAxis(1));
		assertEquals(axis1, builder.determinePlanetAxis(1000));
		assertEquals(axis2, builder.determinePlanetAxis(1001));
		assertEquals(axis2, builder.determinePlanetAxis(2000));
		assertEquals(axis3, builder.determinePlanetAxis(2001));
		assertEquals(axis3, builder.determinePlanetAxis(3000));
		assertEquals(axis4, builder.determinePlanetAxis(3001));
		assertEquals(axis4, builder.determinePlanetAxis(4000));
		assertEquals(axis5, builder.determinePlanetAxis(4001));
		assertEquals(axis5, builder.determinePlanetAxis(5000));
		assertEquals(axis6, builder.determinePlanetAxis(5001));
		assertEquals(axis6, builder.determinePlanetAxis(10000));
	}

}
