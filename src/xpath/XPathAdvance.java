package xpath;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathAdvance {
	Document doc;
	XPath xpath;

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		XPathAdvance advance = new XPathAdvance();
		advance.loadXML();

		System.out.println(advance.getWeight("5"));

		System.out.println(advance.getHeight("5"));

		advance.printAll();

		advance.printHealthProfile("5");

		advance.printPersonFromWeight(90, ">");
	}

	public void loadXML() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		doc = builder.parse("database.xml");

		// creating xpath object
		getXPathObj();
	}

	public XPath getXPathObj() {
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		return xpath;
	}

	// returns weight of a person with that id
	public String getWeight(String personID) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile/weight/text()");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node.getTextContent();
	}

	// returns height of a person with that id
	public String getHeight(String personID) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile/height/text()");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		return node.getTextContent();
	}

	// prints all people in the list with detail
	public void printAll() throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person");
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println("All people in the list with detail:");
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getTextContent());
		}
	}

	// prints the HealthProfile of the person with that id
	public void printHealthProfile(String personID) throws XPathExpressionException {
		XPathExpression expr = xpath.compile("/people/person[@id='" + personID + "']/healthprofile");
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		System.out.println("HealthProfile of the person with id = " + personID + ":");
		System.out.println(node.getTextContent());
	}

	// accepts a weight and an operator (=, > , <) as parameters and prints
	// people that fulfill that condition
	public void printPersonFromWeight(int weight, String operator) throws XPathExpressionException {
		XPathExpression expr = xpath
				.compile("/people/person/healthprofile[weight" + operator + "" + weight + "]/parent::person");
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println("People with weight " + operator + " " + weight + "Kg:");
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getTextContent());
		}
	}
}