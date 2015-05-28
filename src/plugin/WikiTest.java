package plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class WikiTest {
	private static DocumentBuilderFactory dbf;
	static {
	    dbf = DocumentBuilderFactory.newInstance();
	    dbf.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	}
	private static XPathFactory xpathf = XPathFactory.newInstance();
	private static String xexpr = "//html/body//div[@id='bodyContent']/p[1]";


	public static String getPlainSummary(String url) {
		System.out.println("called");
	    try {
	        // OPen Wikipage
	        URL u = new URL(url);
	        URLConnection uc = u.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1) Gecko/20090616 Firefox/3.5");
	        InputStream uio = uc.getInputStream();
	        InputSource src = new InputSource(uio);

	        //Construct Builder
	        DocumentBuilder builder = dbf.newDocumentBuilder();
	        Document docXML = builder.parse(src);

	        //Apply XPath
	        XPath xpath = xpathf.newXPath();
	        XPathExpression xpathe = xpath.compile(xexpr);
	        String s = xpathe.evaluate(docXML);
	        System.out.println("|" + s);
	        //Return Attribute
	        if (s.length() == 0) {
	            return null;
	        } else {
	            return s;
	        }
	    }
	    catch (IOException ioe) {
	        System.out.println("Cant get XML");
	        ioe.printStackTrace();
	        return null;
	    }
	    catch (ParserConfigurationException pce) {
	    	 System.out.println("Cant get DocumentBuilder");
	    	 pce.printStackTrace();
	        return null;
	    }
	    catch (SAXException se) {
	    	 System.out.println("Cant parse XML");
	    	 se.printStackTrace();
	        return null;
	    }
	    catch (XPathExpressionException xpee) {
	    	 System.out.println("Cant parse XPATH");
	    	 xpee.printStackTrace();
	        return null;
	    }
	}

}
