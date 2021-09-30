package com.grupocastores.mcc.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadFileXmlDomParserFactura {
	private static final String FILENAME = "src/main/resources/factura.xml";

	public static void main(String[] args) {
		// Instantiate the Factory
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// optional, but recommended
			// process XML securely, avoid attacks like XML External Entities (XXE)
			 dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// parse XML file
			 DocumentBuilder db = dbf.newDocumentBuilder();

			 Document doc = db.parse(new File(FILENAME));

			// optional, but recommended
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
			System.out.println("------");
			
			NodeList nList = doc.getElementsByTagName("cfdi:Comprobante");

			org.w3c.dom.Node nNode = nList.item(0);

			System.out.println("Moneda: " + ((Element) nNode).getAttribute("Moneda"));
			System.out.println("TipoCambio: " + ((Element) nNode).getAttribute("TipoCambio"));
			System.out.println("Subtotal: " + ((Element) nNode).getAttribute("SubTotal"));
			System.out.println("Total: " + ((Element) nNode).getAttribute("Total"));

			// uuid example
			/*
			 * NodeList list = doc.getElementsByTagName("cfdi:Complemento"); for (int i = 0;
			 * i < 1; i++) { NodeList child = list.item(i).getChildNodes(); for (int j = 0;
			 * j < child.getLength(); j++) { if
			 * (child.item(j).getNodeName().equals("tfd:TimbreFiscalDigital")) { String uuid
			 * = ((Element) (child.item(j))).getAttribute("UUID");
			 * System.out.println("UUID : " + uuid); } } }
			 */
	
			NodeList list = doc.getElementsByTagName("cfdi:Impuestos");
			for (int i = 0; i < 1; i++) {
				if (list.item(i) != null && !list.item(i).getTextContent().equals("")) {
						
					NodeList child = list.item(i).getChildNodes();
					for (int j = 0; j < child.getLength(); j++) {
						if (child.item(j).getNodeName().equals("cfdi:Traslados")) {
					
							NodeList child2 = child.item(j).getChildNodes();
							for (int k = 0; k < child2.getLength(); k++) {
								if (child2.item(k).getNodeName().equals("cfdi:Traslado")) {
									
									String impuesto = ((Element) (child2.item(k))).getAttribute("Impuesto");
									System.out.println("Impuesto : " + impuesto);
									String importe = ((Element) (child2.item(k))).getAttribute("Importe");
									System.out.println("IVA : " + importe);
									String tasaOCuota = ((Element) (child2.item(k))).getAttribute("TasaOCuota");
									System.out.println("TasaOCuota : " + tasaOCuota);
									
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Method para convertir Documento a String
	 * @param doc
	 * @return
	 */

	private static String convertDocumentToString(Document doc) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			// below code to remove XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Method para convertir String a Documento
	 * @param xmlStr
	 * @return
	 */
	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			// optional, but recommended
			// process XML securely, avoid attacks like XML External Entities (XXE)
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			builder = factory.newDocumentBuilder();
			// parse XML file
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
