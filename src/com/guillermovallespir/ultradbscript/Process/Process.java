/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import com.guillermovallespir.ultradbscript.core.Helper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author gvallespir
 */
public abstract class Process {
    private String lista_procesos = ""
            + "INCLUDE,"
            + "";
    protected String TAG;
    protected Node NODE;
    
    public Process(String tag, Node node){
        TAG = tag;
        NODE = node;
    }
    
    public abstract void Execute();
    
    
    public boolean isDeprecated(){
        return Helper.isDeprecatedFunction(TAG);
    }
    
    public Object hasAllAttributes(String atributos){
        return true;
    }
}
