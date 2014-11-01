import java.util.ArrayList;
import java.util.List;
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

	protected void buildPlanets(JavaNCSSMetrics jMetrics, StatSVNMetrics sMetrics) {
		ArrayList<Planet> planets = new ArrayList<Planet>();
		Integer id = system.getId();

		for (JavaNCSSClassMetric jcm : jMetrics.getClassMetricsList()) {
			
			Integer period = buildPlanetPeriod(system.getSpeed());

			// JavaNCSS info
			String name = jcm.getClassName();
			Integer radius = buildPlanetRadius(new Integer(jcm.getNumMethods()));
			String hue = buildPlanetHue(new Integer(jcm.getComplexityNumber()));
			
			// StatSVN info
//			Integer numRevisions = new Integer(0);
//			
			// TODO need to loop through class metrics to find matching class for numRevisions
//			for (StatSVNClassMetric scm : sMetrics.getClassMetricsList()) {
//				if (scm.getClassName().equals(name)) {
//					numRevisions = scm.getNumRevisions();
//					break;
//				}
//			}
//			Integer axis = buildPlanetAxis(numRevisions);
			// TODO remove when above works
			Integer axis = new Integer(0);
			
			Planet p = new Planet(id, name, radius, axis, period, hue);  
			planets.add(p);
		}

		system.setPlanets(planets);
	}

	protected String buildPlanetHue(Integer complexity) {
		// TODO construct planet hue from complexity number
		return "FFFFFF";
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
