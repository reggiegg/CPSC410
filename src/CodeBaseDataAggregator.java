import java.util.ArrayList;

public class CodeBaseDataAggregator {

	private FakeJavaNCSSParser jParser;
	private FakeStatSVNParser sParser;
	private SolarSystem solarSystem;
	
	public CodeBaseDataAggregator(FakeJavaNCSSParser jParser, FakeStatSVNParser sParser) {
		this.jParser = jParser;
		this.sParser = sParser;
		solarSystem = constructSolarSystem();
	}
	
	public SolarSystem getSolarSystem(){
		return solarSystem;
	}
	
	private SolarSystem constructSolarSystem(){
		// TODO change this to produce a new random integer?
		SolarSystem system = new SolarSystem(new Integer(17));
		
		// TODO do i need to set these for the system, or should they just go into planets?
		
		// colour of system - number of developers
		system.setColour(jParser.getNumberOfDevelopers());

		// speed of system - StatSVN churn
		system.setSpeed(sParser.getChurn());
		
		
		// TODO how to construct and add planets one by one?
		
		

		// size of each planet class - JavaNCSS number of methods
		// planet hue from average length per class - JavaNCSS #sloc/#methods
		Integer numMethods = jParser.getNumberOfMethods();
		Integer sLoc = jParser.getSloc();
		
		
		// semimajor axis - StatSVN number of revisions 
		sParser.getNumberOfRevisions();
		
		return system;
	}
	
}
