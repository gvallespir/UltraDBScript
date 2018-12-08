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
import org.xml.sax.SAXException;

/**
 *
 * @author guille
 */
public class XML_Parse {
    private java.io.File file;
    private String ruta;
    
    
    public XML_Parse(java.io.File file){
        
        this.file = file;
        this.ruta = this.file.getAbsolutePath();
            
        Document document = null;
        try {    
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(this.file);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML_Parse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            
        } catch (IOException ex) {
            
        }
        
        document.getDocumentElement().normalize();
    }
}
