package in.cdac.eraktkosh.utility;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

	private String tempVal;
	private String strResultValue;
	private String strParam;
	// to maintain context

	public SaxHandler() {
	}

	public String getParameter(String _FilePath, String _Parameter) {
		strParam = _Parameter;
		parseDocument(_FilePath);
		return strResultValue;
	}

	private void parseDocument(String _FilePath) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			File file = new File(_FilePath);
			SAXParser sp = spf.newSAXParser();
			sp.parse(file, this);

		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		tempVal = "";
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase(strParam)) {
			strResultValue = tempVal;
		}
	}
}
