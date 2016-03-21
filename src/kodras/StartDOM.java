package kodras;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class StartDOM {

	private static Document doc = null ;
	private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	
	public static void main(String[] args) {
		
		String path = "";
		if(args.length >= 2 && args[0].equals("-p")) {
			path = args[1];
		} else {
			path = "customerOrders.xml";
			System.out.println("Keinen Pfad angegeben. Default-XML wird im selben Verzeichnis verwendet.");
		}
		
		System.out.println();
		
		try {
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			doc = builder.parse(new File(path));
			doc.getDocumentElement().normalize();
			questions();
			answers();
			addLanguage();
		} catch(SAXParseException e) {
//			e.printStackTrace();
		} catch(SAXException e) {
//			e.printStackTrace();
		} catch(ParserConfigurationException e) {
//			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("XML-Dokument wurde nicht gefunen!\nMit -p können Sie einen Pfad angeben!");
//			e.printStackTrace();
		} catch (DOMException e) {
//			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Fehler beim parsen des Datums!");
//			e.printStackTrace();
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
	
	public static void questions() {
		System.out.println(
				"1.Wie viele Kunden und wie viele Bestellungen sind gespeichert?\n" + 
				"2.Welche CustomerID besitzt der vierte Kunde?\n" +
				"3.Wie lautet die vollständige Adresse von der Firma Lazy K Kountry Store?\n" +
				"4.Gibt es Kunden, welche dieselbe dreistellige Vorwahl verwenden?\n" +
				"5.Gibt es Kunden, die nicht aus den USA sind?\n" +
				"6.Welche(r) Kunde hatten die meisten Bestellungen?\n" +
				"7.Wann war die letzte Bestellung von LAZYK?\n" +
				"8.Von wie vielen verschiedenen Mitarbeitern wurde GREAL bedient?\n" +
				"9.Welches Gewicht hat LETSS insgesamt verschicken lassen?\n");
	}
	
	public static void answers() throws DOMException, ParseException {
		NodeList nListC = doc.getElementsByTagName("Customer");
		NodeList nListO = doc.getElementsByTagName("Order");
		Node nNodeC, nNodeO;
		Element eElementC, eElementO;
		//1
		System.out.println("1.Kunden: " + nListC.getLength()
				+ ", Bestellungen: " + nListO.getLength());
		//2
		if(nListC.getLength()>=4) {
			System.out.println("2.CustomerID: " + ((Element)(nListC.item(3))).getAttribute("CustomerID"));
		} else {
			System.out.println("2.Es gibt keinen vierten Kunden!");
		}
		//3
		String text = "3.Firma Lazy K Kountry Store ist nicht vorhanden!";
		for(int i=0; i<nListC.getLength(); i++) {
			nNodeC = nListC.item(i);
			if (nNodeC.getNodeType() == Node.ELEMENT_NODE) {
				eElementC = (Element) nNodeC;
				if(eElementC.getElementsByTagName("CompanyName").item(0).getTextContent().equals("Lazy K Kountry Store")) {
					text = "3.FullAddress:" + "\n\tAddress: " + eElementC.getElementsByTagName("Address").item(0).getTextContent()
							+ "\n\tCity: " + eElementC.getElementsByTagName("City").item(0).getTextContent()
							+ "\n\tRegion: " + eElementC.getElementsByTagName("Region").item(0).getTextContent()
							+ "\n\tPostalCode: " + eElementC.getElementsByTagName("PostalCode").item(0).getTextContent()
							+ "\n\tCountry: " + eElementC.getElementsByTagName("Country").item(0).getTextContent();
				}
			}
		}
		System.out.println(text);
		//4
		ArrayList<String> numb = new ArrayList<String>();
		for(int i=0; i<nListC.getLength(); i++) {
			nNodeC = nListC.item(i);
			if (nNodeC.getNodeType() == Node.ELEMENT_NODE) {
				eElementC = (Element) nNodeC;
				text = eElementC.getElementsByTagName("Phone").item(0).getTextContent();
				numb.add("" + text.charAt(1) + text.charAt(2) + text.charAt(3));
			}
		}
		text = "4.Keine Kunden verwenden die selbe Vorwahl";
		int j=0;
		for(int i=0; i<nListC.getLength(); i++) {
			if(numb.get(i).equals(numb.get(j)) && i!=j)
				text = "4.Kunden verwenden die selbe Vorwahl";
		}
		System.out.println(text);
		//5
		text = "5.Es sind nicht alle Kunden aus den USA";
		for(int i=0; i<nListC.getLength(); i++) {
			nNodeC = nListC.item(i);
			if (nNodeC.getNodeType() == Node.ELEMENT_NODE) {
				eElementC = (Element) nNodeC;
				if(eElementC.getElementsByTagName("Country").item(0).getTextContent().equals("USA")) {
					text = "5.Alle Kunden sind aus den USA";
				}
			}
		}
		System.out.println(text);
		//6
		System.out.println("6.");
		//7
		text = "";
		j=0;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null, date2 = null;
		for(int i=0; i<nListO.getLength(); i++) {
			nNodeO = nListO.item(i);
			if (nNodeO.getNodeType() == Node.ELEMENT_NODE) {
				eElementO = (Element) nNodeO;
				if(eElementO.getElementsByTagName("CustomerID").item(0).getTextContent().equals("LAZYK")) {
					if(j==0)
						date1 = format.parse(eElementO.getElementsByTagName("OrderDate").item(0).getTextContent());
					date2 = format.parse(eElementO.getElementsByTagName("OrderDate").item(0).getTextContent());
					if(date1.before(date2))
						date1 = date2;
					text = "7.Die letzte Bestellung war am " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1);
					j++;
				}
			}
		}
		System.out.println(text);
		//8
		j=0;
		for(int i=0; i<nListO.getLength(); i++) {
			nNodeO = nListO.item(i);
			if (nNodeO.getNodeType() == Node.ELEMENT_NODE) {
				eElementO = (Element) nNodeO;
				if(eElementO.getElementsByTagName("CustomerID").item(0).getTextContent().equals("GREAL")) {
					j++;
				}
			}
		}
		System.out.println("8.GREAL wurde von " + j + " Mitarbeitern bedient");
		//9
		double k=0;
		for(int i=0; i<nListO.getLength(); i++) {
			nNodeO = nListO.item(i);
			if (nNodeO.getNodeType() == Node.ELEMENT_NODE) {
				eElementO = (Element) nNodeO;
				if(eElementO.getElementsByTagName("CustomerID").item(0).getTextContent().equals("LETSS")) {
					k += Double.parseDouble(eElementO.getElementsByTagName("Freight").item(0).getTextContent());
				}
			}
		}
		System.out.println("9.LETSS hat insgesamt " + k + " verschickt");
	}
	
	public static void addLanguage() {
		NodeList nListC = doc.getElementsByTagName("Customer");
		Node nNodeC;
		Element eElementC;
		for(int i=0; i<nListC.getLength(); i++) {
			nNodeC = nListC.item(i);
			if (nNodeC.getNodeType() == Node.ELEMENT_NODE) {
				eElementC = (Element) nNodeC;
				if(eElementC.getElementsByTagName("language").item(0).getTextContent()==null) {
					eElementC.appendChild(doc);
					eElementC.setAttribute("value", "en");					
				}
			}
		}
		System.out.println("Kunden wurden durch Element language mit dem Attribut value=eng ergänzt");
	}
	
	public static void xmlAusgeben() {
		System.out.println("Root-Element: " + doc.getDocumentElement().getNodeName());
		NodeList nListC = doc.getElementsByTagName("Customer");
		Node nNodeC;
		Element eElementC;
        for (int i=0; i<nListC.getLength(); i++) {
            nNodeC = nListC.item(i);
            System.out.println("\nCurrent Element :" + nNodeC.getNodeName());
            if (nNodeC.getNodeType() == Node.ELEMENT_NODE) {
				eElementC = (Element) nNodeC;
				
				System.out.println("Companyname: "
						+ eElementC.getElementsByTagName("CompanyName").item(0).getTextContent());
				System.out.println("ContactName: "
						+ eElementC.getElementsByTagName("ContactName").item(0).getTextContent());
				System.out.println("ContactTitle: "
						+ eElementC.getElementsByTagName("ContactTitle").item(0).getTextContent());
                System.out.println("Phone: "
                		+ eElementC.getElementsByTagName("Phone").item(0).getTextContent());
                System.out.println("FullAddress:");
                System.out.println("\tAddress: "
    					+ eElementC.getElementsByTagName("Address").item(0).getTextContent());
                System.out.println("\tCity: "
    					+ eElementC.getElementsByTagName("City").item(0).getTextContent());
                System.out.println("\tRegion: "
    					+ eElementC.getElementsByTagName("Region").item(0).getTextContent());
                System.out.println("\tPostalCode: "
    					+ eElementC.getElementsByTagName("PostalCode").item(0).getTextContent());
                System.out.println("\tCountry: "
    					+ eElementC.getElementsByTagName("Country").item(0).getTextContent());
            }
        }
        NodeList nListO = doc.getElementsByTagName("Order");
        Node nNodeO;
        Element eElementO;
        for (int i=0; i<nListO.getLength(); i++) {
            nNodeO = nListO.item(i);
            System.out.println("\nCurrent Element :" + nNodeO.getNodeName());
            if (nNodeO.getNodeType() == Node.ELEMENT_NODE) {
				eElementO = (Element) nNodeO;
				
				System.out.println("CustomerID: "
						+ eElementO.getElementsByTagName("CustomerID").item(0).getTextContent());
				System.out.println("EmployeeID: "
						+ eElementO.getElementsByTagName("EmployeeID").item(0).getTextContent());
				System.out.println("OrderDate: "
						+ eElementO.getElementsByTagName("OrderDate").item(0).getTextContent());
                System.out.println("RequiredDate: "
                		+ eElementO.getElementsByTagName("RequiredDate").item(0).getTextContent());
                System.out.println("ShipInfo:");
                System.out.println("\tShipVia: "
    					+ eElementO.getElementsByTagName("ShipVia").item(0).getTextContent());
                System.out.println("\tFreight: "
    					+ eElementO.getElementsByTagName("Freight").item(0).getTextContent());
                System.out.println("\tShipName: "
    					+ eElementO.getElementsByTagName("ShipName").item(0).getTextContent());
                System.out.println("\tShipAddress: "
    					+ eElementO.getElementsByTagName("ShipAddress").item(0).getTextContent());
                System.out.println("\tShipCity: "
    					+ eElementO.getElementsByTagName("ShipCity").item(0).getTextContent());
                System.out.println("\tShipRegion: "
    					+ eElementO.getElementsByTagName("ShipRegion").item(0).getTextContent());
                System.out.println("\tShipPostalCode: "
    					+ eElementO.getElementsByTagName("ShipPostalCode").item(0).getTextContent());
                System.out.println("\tShipCountry: "
    					+ eElementO.getElementsByTagName("ShipCountry").item(0).getTextContent());
            }
        }
	}
}