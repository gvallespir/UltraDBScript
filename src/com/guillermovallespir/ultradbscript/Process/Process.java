/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import com.guillermovallespir.ultradbscript.core.Helper;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import out.Out;

/**
 *
 * @author gvallespir
 */
public abstract class Process {
    protected String TAG;
    protected String FILE;
    protected Element Element;
    protected String Documentacion;
    protected Out out;
    
    protected String param_id;
    protected String param_desc;
    
    public Process(String tag, String file, Element element, Out out){
        TAG = tag;
        FILE = file;
        Element = element;
        this.out = out;
        
        this.Execute();
    }
    
    public abstract void Execute();
    
    
    public void isDeprecated(){
        if(Helper.isDeprecatedFunction(TAG)){
            out.Write(Out.Type.E_DEPRECATED, FILE, TAG, "Esta función está marcada como deprecada y pronto podría no estar disponible.");
        }
    }
    
    public Object hasAllAttributes(String atributos){
        return true;
    }
    
    public String getDocumentacion(){
        return this.Documentacion;
    }
    
    public void writeDocumentacion(){
        out.Write(this.Documentacion, false);
    }
    
    public void WriteTable(){
        NamedNodeMap namedNodeMap = this.Element.getAttributes();
        
        ArrayList<String[]> data = new ArrayList<>();
        for(int i = 0; i < namedNodeMap.getLength(); i++){
            data.add(new String[]{namedNodeMap.item(i).getNodeName(), namedNodeMap.item(i).getNodeValue()});
        }
        
        String[] headers = {"Atributo", "Valor"};
        
        out.WriteTable(headers, data);
    }
}
