// Number of methods per class
// Average method length per class (Sum method length) / num methods

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JavaNCSSParser parses metrics from an XML file that are generated by JavaNCSS
 * @param xmlFileName the name of the XML file to be parsed.
 * @return a list of JavaNCSSClassMetrics
 * @author Jeremy
 */
public class JavaNCSSParser {
			
	public JavaNCSSMetrics setJavaNCSSMetrics(File xmlFile) {
		List<JavaNCSSClassMetric> metricsList = null;
		try {
//			File xmlFile = new File(xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// convert the xml file to a format that can be parsed
			Document doc = dBuilder.parse(xmlFile);
			
			// normalize the doc to avoid complications with line breaks
			doc.getDocumentElement().normalize();
			
			// get all method elements within the xml file
			NodeList methodList = doc.getElementsByTagName("function");
			
			HashMap<String, ClassInfo> classesMap = new HashMap<String, ClassInfo>();
			
			// go through all methods, and pull out the relevant metrics
			for (int methodIndex = 0; methodIndex < methodList.getLength(); methodIndex++) {
				Node methodNode = methodList.item(methodIndex);
				
				if (methodNode.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element methodElement = (Element) methodNode;
				
				// get the number "Non Commenting Source Statements" contained within the method
				String methodNCSSText = methodElement.getElementsByTagName("ncss").item(0).getTextContent();
				int methodNCSS = 0;
				try {
					methodNCSS = Integer.valueOf(methodNCSSText);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					break;
				}
				
				// get the full method name
				String methodName = methodElement.getElementsByTagName("name").item(0).getTextContent();
				
				// parse the class name from the full method name, discard the rest
				methodName = methodName.substring(0, methodName.indexOf("("));
				String className = methodName.substring(0, methodName.lastIndexOf("."));
				
				// find the class info object if it already exists in the map
				ClassInfo existingClassInfo = classesMap.get(className);
				
				// if the class does not yet exist in the map then create a new map entry, otherwise add the new method info
				if (existingClassInfo == null) {
					ClassInfo newClass = new ClassInfo();
					newClass.addMethod(methodNCSS);
					classesMap.put(className, newClass);
				} else {
					existingClassInfo.addMethod(methodNCSS);
				}
			}
			// create a new class metrics list from the hash map
			metricsList = createClassMetricsList (classesMap);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return new JavaNCSSMetrics(metricsList);
	}
	
	/**
	 * Convert the classesInfo HashMap into a list of JavaNCSSClassMetrics
	 * @param classesMap a map of ClassInfo objects identified by class name
	 * @return a list of JavaNCSSClassMetrics
	 */
	private List<JavaNCSSClassMetric> createClassMetricsList (HashMap<String, ClassInfo> classesMap) {
		List<JavaNCSSClassMetric> classesList = new ArrayList<JavaNCSSClassMetric>();
		Iterator<Entry<String, ClassInfo>> classInfoIter = classesMap.entrySet().iterator();
		
		while (classInfoIter.hasNext()) {
			Map.Entry classInfoEntry = (Map.Entry) classInfoIter.next();
			String className = classInfoEntry.getKey().toString();
			ClassInfo classInfo = (ClassInfo)classInfoEntry.getValue();
			int numMethods = classInfo.getNumMethods();
			float averageMethodLength = classInfo.getAverageMethodLength();
			
			JavaNCSSClassMetric classMetric = new JavaNCSSClassMetric(className, numMethods, averageMethodLength);
			classesList.add(classMetric);
		}
		
		return classesList;
	}
	
	/**
	 * Internal class used by the JavaNCSSParser. ClassInfo objects are used in a hash table, and are
	 * associated with a particular class name as the key.
	 * Contains class metrics for:
	 * - Number of methods in a class
	 * - Average length of methods in a class
	 * @author Jeremy
	 */
	private class ClassInfo {
		private int numMethods;
		private int methodLengthSum;
		
		public ClassInfo () {
			numMethods = 0;
			methodLengthSum = 0;
		}
		
		public void addMethod(int methodLengthSum) {
			numMethods++;
			this.methodLengthSum += methodLengthSum;
			
		}
		
		public float getAverageMethodLength() {
			return ((float)methodLengthSum)/numMethods;
		}
		
		public int getNumMethods() {
			return numMethods;
		}
	}
}
