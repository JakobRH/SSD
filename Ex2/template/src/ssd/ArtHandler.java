package ssd;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO: Implement this content handler.
 */
public class ArtHandler extends DefaultHandler {
	/**
	 * Use this xPath variable to create xPath expression that can be
	 * evaluated over the XML document.
	 */
	private static XPath xPath = XPathFactory.newInstance().newXPath();
	
	/**
	 * Store and manipulate the art XML document here.
	 */
	private Document artDoc;
	
	/**
	 * This variable stores the text content of XML Elements.
	 */
	private String eleText;

	/**
	 * Insert local variables here
	 */
	private String title = "";
	private String kind = "";
	private String catalog = "PP-999-z4";
	private String paint = "";
	private String paintOn = "";
	private String labelBy = "";
	private String labelYear = "";
	private String labelContent = "";

	//contains t elements of object/tags/t
	private List<String> tagsOfExhibitionObj = new ArrayList<String>();

	//contains all tags of tags/tag
	private List<String> tagsOfArtXml = new ArrayList<String>();

	
    
	
    public ArtHandler(Document doc) {

    	artDoc = doc;

				NodeList tmpList = artDoc.getElementsByTagName("tag");

				for (int i = 0; i < tmpList.getLength(); i++) {
					Element tagNode = (Element) tmpList.item(i);
					tagsOfArtXml.add(tagNode.getAttribute("tagname"));
				}
    }
    
    @Override
    /**
     * SAX calls this method to pass in character data
     */
  	public void characters(char[] text, int start, int length)
  			    throws SAXException {
  		eleText = new String(text, start, length);
  	}

    /**
     * 
     * Return the current stored art document
     * 
     * @return XML Document
     */
	public Document getDocument() {
		return artDoc;
	}
    
    //***TODO***
	//Specify additional methods to parse the bid document and modify the artDoc
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
			if("type".equals(localName)) {
			paint = atts.getValue("paint");
			paintOn = atts.getValue("surface");
			}
		}


	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if("creator".equals(localName)){
			labelBy = eleText;
		}
		if("title".equals(localName)){
			title = eleText;
		}
		if("type".equals(localName)){
			kind = eleText;
		}
		if("year".equals(localName)){
			labelYear = eleText;
		}
		if("description".equals(localName)){
			labelContent = eleText;
		}
		if("tag".equals(localName)){
			tagsOfExhibitionObj.add(eleText);
		}
		if("object".equals(localName)){
			if(checkElementValues()){
				createObjectElement();
			}
			setValuesBack();
		}
		if("catalog".equals(localName)){
			NodeList catalogs = artDoc.getElementsByTagName("catalog");
			Node objects = artDoc.getElementsByTagName("objects").item(0);

			for(int i = 0; i < catalogs.getLength(); i++){
				if(catalogs.item(i).getTextContent().equals(eleText)){
					objects.removeChild(artDoc.getElementsByTagName("catalog").item(i).getParentNode());
				}
			}
		}
	}

	//checks if element has all needed values
	public boolean checkElementValues(){
		boolean result = true;

		if(title.equals("")){
			result = false;
		}
		if(kind.equals("")){
			result = false;
		}
		if(paintOn != null && paintOn.equals("")){
			result = false;
		}
		if(labelYear.equals("")){
			result = false;
		}
		if(labelBy.equals("")){
			result = false;
		}
		if(labelContent.equals("")){
			result = false;
		}
		if(paint != null && paint.equals("")){
			result = false;
		}
		return result;
	}

	public void createObjectElement(){
		//set kind
		String tmpKind;
		if(kind.equals("painting")) tmpKind = "painting";
		else tmpKind = "sculpture";

		//create nodes
		Element newObjNode = artDoc.createElement("object");
		Element objTitle = artDoc.createElement("title");
		Element objKind = artDoc.createElement("kind");
		Element objKindPainting = artDoc.createElement(tmpKind);
		Element objLabel = artDoc.createElement("label");
		Element objBy = artDoc.createElement("by");
		Element objYear = artDoc.createElement("year");
		Element objYearInteger = artDoc.createElement("yearInteger");
		Element objCatalog = artDoc.createElement("catalog");
		Element objTags = artDoc.createElement("tags");

		//set content and attributes of nodes
		objTitle.setTextContent(title);
		if(kind.equals("painting")) {
			objKindPainting.setAttribute("paint", paint);
			objKindPainting.setAttribute("on", paintOn);
		}
		objLabel.setTextContent(labelContent);
		objBy.setTextContent(labelBy);
		objYearInteger.setTextContent(labelYear);
		objCatalog.setTextContent(catalog);

		//append nodes
		artDoc.getElementsByTagName("objects").item(0).appendChild(newObjNode);
		newObjNode.appendChild(objTitle);
		newObjNode.appendChild(objKind);
		newObjNode.appendChild(objLabel);
		newObjNode.appendChild(objCatalog);
		objKind.appendChild(objKindPainting);
		objLabel.appendChild(objBy);
		objLabel.appendChild(objYear);
		objYear.appendChild(objYearInteger);
		newObjNode.appendChild(objTags);

		//tags
		//remove duplicates
		LinkedHashSet<String> withoutDuplicatesList = new LinkedHashSet<>(tagsOfExhibitionObj);
		tagsOfExhibitionObj = new ArrayList<String>(withoutDuplicatesList);

		//add new tags
		for (String s : tagsOfExhibitionObj) {
			Element objTag = artDoc.createElement("t");
			objTag.setTextContent(s);
			objTags.appendChild(objTag);
			if (!tagsOfArtXml.contains(s)) {
				Element tagsElement = (Element) artDoc.getElementsByTagName("tags").item(artDoc.getElementsByTagName("tags").getLength()-1);
				Element tagElement = artDoc.createElement("tag");
				tagElement.setAttribute("tagname", s);
				tagsElement.appendChild(tagElement);
				tagsOfArtXml.add(s);
			}
		}
	}

	public void setValuesBack(){
		title = "";
		kind = "";
		paint = "";
		paintOn = "";
		labelBy = "";
		labelYear = "";
		labelContent = "";
		tagsOfExhibitionObj = new ArrayList<String>();
	}
	
}

