/** 
 * This class aggregates the data from two parsers (JavaNCSSParser and StatSVNParser)
 * and then uses it to direct the SolarSystemBuilder to build a SolarSystem from that
 * data. I.e. the CodeBaseDataAggregator acts as the Director in the Builder pattern.
 * 
 * @author Susannah
 *
 */
public class CodeBaseDataAggregator {

	private SolarSystemBuilder builder;
	private JavaNCSSParser jParser;
	private StatSVNParser sParser;
	private SolarSystem solarSystem;
	
	public CodeBaseDataAggregator(JavaNCSSParser jParser, StatSVNParser sParser) {
		this.jParser = jParser;
		this.sParser = sParser;
		solarSystem = constructSolarSystem();
	}
	
	public SolarSystem getSolarSystem(){
		return solarSystem;
	}
	
	private SolarSystem constructSolarSystem(){
		builder = new SolarSystemBuilder();
		// TODO
		//builder.buildColour(jParser.getNumberOfDevelopers());
		builder.buildSpeed(sParser.getChurn());
		
		// TODO 
		// builder.buildPlanets(jParser, sParser);
	
		return builder.getResult();
	}

	
}
