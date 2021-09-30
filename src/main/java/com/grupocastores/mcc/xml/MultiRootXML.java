package com.grupocastores.mcc.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiRootXML{
	
	private static final String FILENAME = "src/main/resources/factura.xml";
	
    public static void main(String[] args) throws Exception{
        List<InputStream> streams = Arrays.asList(
                new ByteArrayInputStream("<cfdi:Comprobante>".getBytes()),
                new FileInputStream(FILENAME),
                new ByteArrayInputStream("</cfdi:Comprobante>".getBytes())
        );
        InputStream is = new SequenceInputStream(Collections.enumeration(streams));
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        NodeList children = doc.getDocumentElement().getChildNodes();
        for(int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            if(child.getNodeType()==Node.ELEMENT_NODE){
                System.out.println("persion: "+child);
            }
        }
    }
}
