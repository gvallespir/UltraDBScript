/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import out.Out;
/**
 *
 * @author gvallespir
 */
public class IncludeProcess extends Process{
    
    public IncludeProcess(String file, Element element, Out out) {
        super("INCLUDE", file, element, out);
    }

    @Override
    public void Execute() {
        this.out.Write(Out.Type.NORMAL, "", TAG, "Inclusión de documento XML / UDBSXML", false);
        
        // Verifica si está marcada como deprecada
        this.isDeprecated();
        
        String _XML_FILE = "xml_file", _ID = "id", _DESC = "desc";
        String xml_file = null, id = null, desc = null;
        
        
        // Procesa los parámetros
        NamedNodeMap namedNodeMap = this.Element.getAttributes();
        for(int i = 0; i < namedNodeMap.getLength(); i++){
            String llave = namedNodeMap.item(i).getNodeName();
            String valor = namedNodeMap.item(i).getNodeValue();
            
            if(llave.compareToIgnoreCase(_ID) == 0){
                id = valor;
            }else if(llave.compareToIgnoreCase(_XML_FILE) == 0){
                xml_file = valor;
            }else if(llave.compareToIgnoreCase(_DESC) == 0){
                desc = valor;
            }
        }
        
        
        // Verifica que las etiquetas obligatorias tengan valor
        if(xml_file == null){
            this.out.Write(Out.Type.E_ERROR, "", TAG, "El tag INCLUDE no contiene la instrucción 'xml_file'", false);
        }
        
        
        
        // Se vuelca la información de la instrucción por pantalla
        this.out.Write(Out.Type.NORMAL, "", TAG, "Se pintan los atributos de la operación", false);
        this.WriteTable();
    }
    
}
