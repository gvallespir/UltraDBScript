/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import out.Out;

/**
 *
 * @author guille
 */
public class XML_Parse {
    public XML_Parse(Out out, java.io.File file){
        
        String ruta = file.getAbsolutePath();
        
        Document document = null;
        try {    
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(file);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML_Parse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            
        } catch (IOException ex) {
            
        }
        
        document.getDocumentElement().normalize();
        
        if(out != null){
            out.Write(Out.Type.NORMAL, ruta, "XML_FILE", "Se inicia la lectura del archivo XML", false);
            
            NodeList node = document.getDocumentElement().getChildNodes();
            System.out.println("Nodos: " + node.getLength());
            for(int i = 0; i < node.getLength(); i++){
                Node n = (Node) node.item(i);
                
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) n;
                    
                    switch(e.getNodeName().toLowerCase()){
                        case "include":
                            new IncludeProcess(file.getAbsolutePath(), e, out);
                            break;
                        case "database":
                            new DataBaseProcess(file.getAbsolutePath(), e, out);
                            break;
                        case "sql":
                            new SQLProcess(file.getAbsolutePath(), e, out);
                            break;
                    }
                }
            }
        }
    }
}
