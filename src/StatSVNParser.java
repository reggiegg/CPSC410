import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class for extraction of StatSVN metrics
 * Provides: 
 * Overall churn of project
 * Number of Developers
 * Class-by-class number of revisions
 */
public class StatSVNParser {

	private Document xmlDoc;

	/**
	 * Extracts all relevant metrics from the xml file, 
	 * and return in a StatSVNMetrics object
	 * @param File xmlFile
	 * @return StatSVNMetrics
	 */
	public StatSVNMetrics getStatSVNMetrics(File xmlFile) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			this.xmlDoc = docBuilder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		int churn = this.parseChurn();
		HashMap<String, ClassInfo> classesMap = this.parseNumRevisions();
		int numDevelopers = this.parseNumDevelopers();
		
		List<StatSVNClassMetric> metricsList = createClassMetricsList(classesMap);
		return new StatSVNMetrics(metricsList, churn, numDevelopers);
	}

	/**
	 * Get the current xml document being parsed
	 * @return Document xmlDoc
	 */
	public Document getDocument() {
		return this.xmlDoc;
	}

	/**
	 * Extract the overall project churn
	 * - total number of revisions in the project history
	 * @return
	 */
	private int parseChurn() {
		Document doc = this.xmlDoc;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		int churnInt = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == "FileStats"){
					NodeList statNodes = node.getChildNodes();
					Node summaryNode = statNodes.item(0);
					Node revisionsNode = summaryNode.getLastChild();
					String churn = revisionsNode.getTextContent();
					churnInt = (int) Double.parseDouble(churn);
					break;
				}
			}
		}
		return churnInt;
	}

	/**
	 * Extract the number of revisions per class in a ClassInfo map
	 * @return HashMap<String, ClassInfo> classesMap
	 */
	private HashMap<String, ClassInfo> parseNumRevisions() { 
		Document doc = this.xmlDoc;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		HashMap<String, ClassInfo> classesMap = new HashMap<String, ClassInfo>();
		
		// Iterate through, extract, and store number of revisions
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == "Commits"){
					NodeList allCommits = node.getChildNodes();
					
					// Iterate through each commit
					for (int j = 0; j < allCommits.getLength(); j++) {
						Node commitNode = allCommits.item(j);

						// Extract list of changed files for that commit
						Node changedFiles = commitNode.getLastChild();
						NodeList fileList = changedFiles.getChildNodes();
						
						// Iterate through each changed file
						for (int k = 0; k < fileList.getLength(); k++) {
									
							// Extract path name and class name
							String path = fileList.item(k).getFirstChild().getTextContent();
							if(!path.contains(".java")) {
								continue;
							}
							
							String className = "";
							if(path.contains("/")) {
								className = path.substring(path.lastIndexOf("/") + 1, path.length());
							} else {
								className = path;
							}
							
							className = className.substring(0, className.lastIndexOf("."));
							System.out.println(className);
							// If we've seen this file before, then update it, if not, create it.
							ClassInfo classInfo = classesMap.get(path);
							
							if (classInfo == null) {
								classInfo = new ClassInfo(path, className);
							} 
							// Add one to the revision count for the file
							classInfo.addRevision();
							classesMap.put(path, classInfo);
						}
					}
					break;
				}
			}
		}
		
		return classesMap;
	}
	
	/**
	 * Extracts the number of developers from the file
	 * @return int numDevelopers
	 */
	private int parseNumDevelopers() {
		Document doc = this.xmlDoc;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		int numDevelopers = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				// Find "Developer" node and return the number of children
				if (node.getNodeName() == "Developers"){
					NodeList developers = node.getChildNodes();
					numDevelopers =  developers.getLength();
				}
			}
		}
		return numDevelopers;
	}
	
	/**
	 * Convert the classesInfo HashMap into a list of StatSVNClassMetrics
	 * @param classesMap a map of ClassInfo objects identified by class path
	 * @return a list of StatSVNClassMetrics
	 */
	private List<StatSVNClassMetric> createClassMetricsList (HashMap<String, ClassInfo> classesMap) {
		
		List<StatSVNClassMetric> classesList = new ArrayList<StatSVNClassMetric>();
		Iterator<Entry<String, ClassInfo>> classInfoIter = classesMap.entrySet().iterator();
		
		while (classInfoIter.hasNext()) {
			Map.Entry classInfoEntry = (Map.Entry) classInfoIter.next();
			String path = classInfoEntry.getKey().toString();
			ClassInfo classInfo = (ClassInfo)classInfoEntry.getValue();
			String className = classInfo.getClassName();
			int numRevisions = classInfo.getNumRevisions();
			
			StatSVNClassMetric classMetric = new StatSVNClassMetric(path, className, numRevisions);
			classesList.add(classMetric);
		}
		
		return classesList;
	}
	
	/**
	 * Internal class used by the JavaNCSSParser. ClassInfo objects are used in a hash table, and are
	 * associated with a particular class name as the key.
	 * Contains class metrics for:
	 * - Number of revisions on the class
	 * - Path name
	 * - Class name
	 * @author Jeremy/Jon
	 */
	private class ClassInfo {
		private String className;
		private String path;
		private int numRevisions;
		
		public ClassInfo (String path, String className) {
			this.path = path;
			this.className = className;
			numRevisions = 0;
		}
		
		public void addRevision() {
			this.numRevisions++;
		}
		
		public String getPath() {
			return path;
		}
		
		public String getClassName() {
			return className;
		}
		
		public int getNumRevisions() {
			return numRevisions;
		}
	}
	
}
