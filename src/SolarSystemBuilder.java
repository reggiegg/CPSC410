import java.util.ArrayList;
import java.util.Random;


public class SolarSystemBuilder {

	private SolarSystem system;
	private ArrayList<Planet> planets;

	public SolarSystemBuilder() {
		system = new SolarSystem(getRandomId());
		planets = new ArrayList<Planet>();	
	}

	public void buildSolarSystem(FakeJavaNCSSParser jParser, FakeStatSVNParser sParser){
		// TODO???
	}

	public void buildColour(Integer devs) {
		// colour of system - number of developers
		system.setColour(devs);
	}

	public void buildSpeed(Integer churn) {
		// speed of system - StatSVN churn
		system.setSpeed(churn);
	}

	public void buildPlanets(ArrayList<FakeProjectClass> classList) {
		// TODO how to construct and add planets one by one?
		for (FakeProjectClass fpc : classList) { 
			Planet p = new Planet(
					system.getId(), 
					fpc.getName(), 
					buildRadius(fpc.getNumMethods()),
					buildAxis(fpc.getNumRevisions()),
					buildPeriod(system.getSpeed()),
					buildHue(fpc.getSloc() / fpc.getNumMethods()));  
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

	public SolarSystem getResult() {
		return system;
	}

	private String buildHue(Integer avgMethodLength) {
		// planet hue from average length per class - JavaNCSS #sloc/#methods
		return null;
	}

	private Integer buildPeriod(Integer speed) {
		// TODO how to turn speed into period???
		return null;
	}

	private Integer buildAxis(Integer numRevisions) {
		// TODO if statements to decide size of axis???
		return null;
	}

	private Integer buildRadius(Integer numMethods) {
		// TODO if statements to decide radius of planet???
		return null;
	}

	private Integer getRandomId() {
		Random random = new Random();
		return random.nextInt();
	}



}
