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
		if (devs <= 3)
			colour = "800080"; // purple
		else if (3 < devs && devs <= 6) 
			colour = "0000FF"; // blue
		else if (6 < devs && devs <= 9)
			colour = "008000"; // green
		else if (9 < devs && devs <= 12)
			colour = "FFFF00"; // yellow
		else if (12 < devs && devs <= 15)
			colour = "FFA500"; // orange
		else // (15 < devs)
			colour = "FF0000"; // red
		return colour;
	}

	public void buildSpeed(Integer churn) {
		// speed of system - StatSVN churn
		Integer speed = determineSpeed(churn);
		system.setSpeed(speed);
	}

	private Integer determineSpeed(Integer churn) {
		Integer speed;
		if (churn <= 1000)
			speed = new Integer(3); 
		else if (1000 < churn && churn <= 2000) 
			speed = new Integer(6);
		else if (2000 < churn && churn <= 3000)
			speed = new Integer(9);
		else if (3000 < churn && churn <= 4000)
			speed = new Integer(12);
		else if (4000 < churn && churn <= 5000)
			speed = new Integer(15);
		else 
			speed = new Integer(18);
		return speed;
	}

	public void buildPlanets(ArrayList<FakeProjectClass> classList) {
		// TODO how to construct and add planets one by one?
		for (FakeProjectClass fpc : classList) { 
			Planet p = new Planet(
					system.getId(), 
					fpc.getName(), 
					buildPlanetRadius(fpc.getNumMethods()),
					buildPlanetAxis(fpc.getNumRevisions()),
					buildPlanetPeriod(system.getSpeed()),
					buildPlanetHue(fpc.getSloc() / fpc.getNumMethods()));  
			planets.add(p);
		}

		// TODO 
		// size of each planet - JavaNCSS number of methods
		// Integer numMethods = jParser.getNumberOfMethods();
		// Integer sLoc = jParser.getSloc();
		
		// semimajor axis - StatSVN number of revisions 
		// Integer numRevisions = sParser.getNumberOfRevisions();

		
		system.setPlanets(planets);
	}


	protected String buildPlanetHue(Integer avgMethodLength) {
		// TODO planet hue from 
		// JavaNCSSClassMetric.getAverageMethodLength() => float
		return null;
	}

	protected Integer buildPlanetPeriod(Integer speed) {
		Integer period;
		if (speed <= 3)
			period = new Integer(18); 
		else if (3 < speed && speed <= 6) 
			period = new Integer(15);
		else if (6 < speed && speed <= 9)
			period = new Integer(12);
		else if (9 < speed && speed <= 12)
			period = new Integer(9);
		else if (12 < speed && speed <= 15)
			period = new Integer(6);
		else 
			period = new Integer(3);
		return period;
	}

	protected Float buildPlanetAxis(Integer numRevisions) {
		Float axis;
		if (numRevisions <= 1000)
			axis = new Float(0.2); 
		else if (1000 < numRevisions && numRevisions <= 2000) 
			axis = new Float(0.4);
		else if (2000 < numRevisions && numRevisions <= 3000)
			axis = new Float(0.6);
		else if (3000 < numRevisions && numRevisions <= 4000)
			axis = new Float(0.8);
		else if (4000 < numRevisions && numRevisions <= 5000)
			axis = new Float(1.0);
		else 
			axis = new Float(1.2);
		return axis;
	}

	protected Integer buildPlanetRadius(Integer numMethods) {
		Integer radius;
		if (numMethods <= 3)
			radius = new Integer(10); 
		else if (3 < numMethods && numMethods <= 6) 
			radius = new Integer(20);
		else if (6 < numMethods && numMethods <= 9)
			radius = new Integer(30);
		else if (9 < numMethods && numMethods <= 12)
			radius = new Integer(40);
		else if (12 < numMethods && numMethods <= 15)
			radius = new Integer(50);
		else 
			radius = new Integer(60);
		return radius;
	}

	private Integer getRandomId() {
		Random random = new Random();
		return random.nextInt();
	}



}
