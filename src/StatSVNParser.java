import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class StatSVNParser {

	private Document xmlDoc;
	private int numRevisions;
	private int churn;

	public StatSVNParser(File xmlFile)
	{
		this.churn = 0;
		this.numRevisions = 0;
		this.xmlDoc = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			this.xmlDoc = docBuilder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.parseChurn();
		this.parseNumRevisions();
	}

	public Document getDocument() 
	{
		return this.xmlDoc;
	}

	public Integer getNumberOfRevisions() 
	{
		return this.numRevisions;
	}

	public Integer getChurn() 
	{
		return this.churn;
	}

	private void parseChurn()
	{
		// TODO Determine proper metric for churn.
		// Currently returning avg number of revisions per file. 
		Document doc = this.xmlDoc;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == "FileStats"){
					NodeList statNodes = node.getChildNodes();
					Node summaryNode = statNodes.item(0);
					Node revisionsNode = summaryNode.getLastChild();
					String churn = revisionsNode.getTextContent();
					this.churn = (int) Double.parseDouble(churn);
				}
			}
		}
	}

	private void parseNumRevisions()
	{ 
		Document doc = this.xmlDoc;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				if (node.getNodeName() == "Commits"){
					Node commit = node.getLastChild();
					int numRevisions =  (int) Double.parseDouble(commit.getAttributes().getNamedItem("revision").getNodeValue());
					this.numRevisions = numRevisions;
				}
			}
		}		
	}
}
