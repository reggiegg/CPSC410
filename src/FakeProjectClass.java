/** 
 * Mock class to be used for testing until parsers are complete.
 * @author Susannah
 *
 */
public class FakeProjectClass {

	private String name = "name";
	private Integer numMethods = new Integer(5);
	private Integer sLoc = new Integer(13);
	private Integer numRevisions = new Integer(47);
	
	public String getName() {
		return name;
	}
	
	public Integer getNumMethods() {
		return numMethods;
	}
	
	public Integer getSloc() {
		return sLoc;
	}
	
	public Integer getNumRevisions() {
		return numRevisions;
	}
}
