import java.util.ArrayList;

/** 
 * Mock class to be used for testing until parsers are complete.
 * @author Susannah
 *
 */
public class FakeJavaNCSSParser {

	private Integer j;
	private ArrayList<FakeProjectClass> classList = new ArrayList<FakeProjectClass>();
	
	public FakeJavaNCSSParser() {
		j = new Integer(3);
	}
	
	public Integer getNumberOfDevelopers() {
		return j;
	}
	
	public Integer getSloc() {
		return j;
	}
	
	public Integer getNumberOfMethods() {
		return j;
	}
	
	public ArrayList<FakeProjectClass> getClassList() {
		return classList;
	}
	
	
}
