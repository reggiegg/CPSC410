import java.util.ArrayList;
import java.util.Random;

public class CodeBaseDataAggregator {

	private SolarSystemBuilder builder;
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
		builder = new SolarSystemBuilder();
		builder.buildColour(jParser.getNumberOfDevelopers());
		builder.buildSpeed(sParser.getChurn());
		
		// TODO
		// builder.buildPlanets();
	
		return builder.getResult();
	}

	
}
