import java.util.ArrayList;
import java.util.Random;

/**
 * This Builder constructs a SolarSystem and its Planets given data passed
 * to it by the CodeBaseDataAggregator, which acts as a Director.
 * 
 * @author Susannah
 */
public class SolarSystemBuilder {

	private SolarSystem system;
	private ArrayList<Planet> planets;

	public SolarSystemBuilder() {
		system = new SolarSystem(getRandomId());
		planets = new ArrayList<Planet>();	
	}

	public SolarSystem getResult() {
		return system;
	}

	public void buildColour(Integer devs) {
		// colour of system - number of developers
		String colour = determineColour(devs);
		system.setColour(colour);
	}

	private String determineColour(Integer devs) {
		String colour = "";
		if (devs <= SolarSystemConstants.DEVS_1)
			colour = SolarSystemConstants.PURPLE;
		else if (SolarSystemConstants.DEVS_1 < devs && devs <= SolarSystemConstants.DEVS_2) 
			colour = SolarSystemConstants.BLUE;
		else if (SolarSystemConstants.DEVS_2 < devs && devs <= SolarSystemConstants.DEVS_3)
			colour = SolarSystemConstants.GREEN;
		else if (SolarSystemConstants.DEVS_3 < devs && devs <= SolarSystemConstants.DEVS_4)
			colour = SolarSystemConstants.YELLOW;
		else if (SolarSystemConstants.DEVS_4 < devs && devs <= SolarSystemConstants.DEVS_5)
			colour = SolarSystemConstants.ORANGE;
		else  
			colour = SolarSystemConstants.RED;
		return colour;
	}

	public void buildSpeed(Integer churn) {
		// speed of system - StatSVN churn
		Integer speed = determineSpeed(churn);
		system.setSpeed(speed);
	}

	private Integer determineSpeed(Integer churn) {
		Integer speed;
		if (churn <= SolarSystemConstants.CHURN_1)
			speed = SolarSystemConstants.SPEED_1; 
		else if (SolarSystemConstants.CHURN_1 < churn && churn <= SolarSystemConstants.CHURN_2) 
			speed = SolarSystemConstants.SPEED_2; 
		else if (SolarSystemConstants.CHURN_2 < churn && churn <= SolarSystemConstants.CHURN_3)
			speed = SolarSystemConstants.SPEED_3; 
		else if (SolarSystemConstants.CHURN_3 < churn && churn <= SolarSystemConstants.CHURN_4)
			speed = SolarSystemConstants.SPEED_4; 
		else if (SolarSystemConstants.CHURN_4 < churn && churn <= SolarSystemConstants.CHURN_5)
			speed = SolarSystemConstants.SPEED_5; 
		else 
			speed = SolarSystemConstants.SPEED_6; 
		return speed;
	}

	public void buildPlanets(JavaNCSSMetrics jMetrics, StatSVNMetrics sMetrics) {
		ArrayList<Planet> planets = new ArrayList<Planet>();
		Integer id = system.getId();

		for (JavaNCSSClassMetric jcm : jMetrics.getClassMetricsList()) {
			
			// JavaNCSS info
			String name = jcm.getClassName();
			Integer radius = determinePlanetRadius(new Integer(jcm.getNumMethods()));
			String hue = determinePlanetHue(new Float(jcm.getComplexityDensity()));

			Integer period = determinePlanetPeriod(system.getSpeed());
			Double modperiod = period * (Math.random()*1.1+.99);
			
			// StatSVN info
			Integer numRevisions = new Integer(0);
			
			for (StatSVNClassMetric scm : sMetrics.getClassMetricsList()) {
				
				if (scm.getClassName().equals(name)) {
					numRevisions = scm.getNumRevisions();
					break;
				}
			}

			Float axis = determinePlanetAxis(numRevisions);

			Planet p = new Planet(id, name, radius, axis, modperiod, hue);  
			planets.add(p);
		}

		system.setPlanets(planets);
	}

	protected String determinePlanetHue(Float complexity) {
		String baseColour = system.getColour();
		Integer baseHex = Integer.decode(baseColour);
		Integer multiplier = getMultiplier(baseColour);
		Integer comp = Math.round(complexity * 10);
		Integer addOn = comp * multiplier;
		if (addOn > baseHex) {
			addOn = baseHex;
		}
		Integer newColour = baseHex - addOn;
		String colour = String.format("%06x", newColour);
		return colour;
	}

	protected Integer getMultiplier(String colour) {
		if (colour == SolarSystemConstants.RED)
			return SolarSystemConstants.MULT_RED;
		else if (colour == SolarSystemConstants.ORANGE)
			return SolarSystemConstants.MULT_ORANGE;
		else if (colour == SolarSystemConstants.YELLOW)
			return SolarSystemConstants.MULT_YELLOW;
		else if (colour == SolarSystemConstants.GREEN)
			return SolarSystemConstants.MULT_GREEN;
		else if (colour == SolarSystemConstants.BLUE)
			return SolarSystemConstants.MULT_BLUE;
		else
			return SolarSystemConstants.MULT_PURPLE;
	}

	protected Integer determinePlanetPeriod(Integer speed) {
		Integer base_period;
		if (speed <= SolarSystemConstants.SPEED_1)
			base_period = SolarSystemConstants.PERIOD_6;
		else if (SolarSystemConstants.SPEED_1 < speed && speed <= SolarSystemConstants.SPEED_2) 
			base_period = SolarSystemConstants.PERIOD_5;
		else if (SolarSystemConstants.SPEED_2 < speed && speed <= SolarSystemConstants.SPEED_3)
			base_period = SolarSystemConstants.PERIOD_4;
		else if (SolarSystemConstants.SPEED_3 < speed && speed <= SolarSystemConstants.SPEED_4)
			base_period = SolarSystemConstants.PERIOD_3;
		else if (SolarSystemConstants.SPEED_4 < speed && speed <= SolarSystemConstants.SPEED_5)
			base_period = SolarSystemConstants.PERIOD_2;
		else 
			base_period = SolarSystemConstants.PERIOD_1;
		
		//Integer period = base_period*radius;
		return base_period;
	}

	protected Float determinePlanetAxis(Integer numRevisions) {
		Float axis;
		
		if (numRevisions <= SolarSystemConstants.REVISIONS_1)
			axis = SolarSystemConstants.AXIS_1; 
		else if (SolarSystemConstants.REVISIONS_1 < numRevisions && numRevisions <= SolarSystemConstants.REVISIONS_2) 
			axis = SolarSystemConstants.AXIS_2; 
		else if (SolarSystemConstants.REVISIONS_2 < numRevisions && numRevisions <= SolarSystemConstants.REVISIONS_3)
			axis = SolarSystemConstants.AXIS_3; 
		else if (SolarSystemConstants.REVISIONS_3 < numRevisions && numRevisions <= SolarSystemConstants.REVISIONS_4)
			axis = SolarSystemConstants.AXIS_4; 
		else if (SolarSystemConstants.REVISIONS_4 < numRevisions && numRevisions <= SolarSystemConstants.REVISIONS_5)
			axis = SolarSystemConstants.AXIS_5; 
		else 
			axis = SolarSystemConstants.AXIS_6; 
		return axis;
	}

	protected Integer determinePlanetRadius(Integer numMethods) {
		Integer radius;
		if (numMethods <= SolarSystemConstants.METHODS_1)
			radius = SolarSystemConstants.RADIUS_1;
		else if (SolarSystemConstants.METHODS_1 < numMethods && numMethods <= SolarSystemConstants.METHODS_2) 
			radius = SolarSystemConstants.RADIUS_2;
		else if (SolarSystemConstants.METHODS_2 < numMethods && numMethods <= SolarSystemConstants.METHODS_3)
			radius = SolarSystemConstants.RADIUS_3;
		else if (SolarSystemConstants.METHODS_3 < numMethods && numMethods <= SolarSystemConstants.METHODS_4)
			radius = SolarSystemConstants.RADIUS_4;
		else if (SolarSystemConstants.METHODS_4 < numMethods && numMethods <= SolarSystemConstants.METHODS_5)
			radius = SolarSystemConstants.RADIUS_5;
		else 
			radius = SolarSystemConstants.RADIUS_6;
		return radius;
	}

	private Integer getRandomId() {
		Random random = new Random();
		return random.nextInt();
	}



}
