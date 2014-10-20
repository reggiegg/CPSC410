/** 
 * Mock class to be used for testing until parsers are complete.
 * @author Susannah
 *
 */
public class FakeStatSVNParser {

	private Integer s; 
	
	public FakeStatSVNParser() {
		s = new Integer(7);
	}
	
	public Integer getNumberOfRevisions() {
		return s;
	}
	
	public Integer getChurn() {
		return s;
	}
	
}
