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
	private JavaNCSSMetrics jMetrics;
	private StatSVNMetrics sMetrics;
	private SolarSystem solarSystem;
	
	public CodeBaseDataAggregator(JavaNCSSMetrics jMetrics, StatSVNMetrics sMetrics) {
		this.jMetrics = jMetrics;
		this.sMetrics = sMetrics;
		solarSystem = constructSolarSystem();
	}
	
	public SolarSystem getSolarSystem(){
		return solarSystem;
	}
	
	private SolarSystem constructSolarSystem(){
		builder = new SolarSystemBuilder();
		// TODO
		//builder.buildColour(jParser.getNumberOfDevelopers());
		//builder.buildSpeed(sMetrics.getChurn());
		
		// TODO 
		// builder.buildPlanets(jParser, sParser);
	
		return builder.getResult();
	}

	
}
