import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class StatSVNParser {

	private Document xmlDoc;
	private Integer numRevisions;
	private Integer churn;

	public StatSVNParser(String path) throws Exception 
	{
		this.churn = 0;
		this.numRevisions = 0;
		this.xmlDoc = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			this.xmlDoc = docBuilder.parse(path);
			
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}	
		
		if(this.xmlDoc == null) {
			throw new Exception();
		}
		else {
			this.parseXmlDoc();
		}
	}
	
	public Integer getNumberOfRevisions() 
	{
		return this.numRevisions;
	}
	
	public Integer getChurn() 
	{
		return this.churn;
	}
	
	private void parseXmlDoc() throws Exception 
	{
		if(this.xmlDoc == null) {
			throw new Exception();
		}
		else {
			// TODO Get proper xml format and extract values.
			Integer churnFromXml = 1;
			Integer numRevisionsFromXml = 2;
			this.churn = churnFromXml;
			this.numRevisions = numRevisionsFromXml;			
		}
	}
}
