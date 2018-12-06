/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import out.Out;
import org.w3c.dom.Node;

/**
 *
 * @author gvallespir
 */
public class IncludeProcess extends Process{
    private Out out;
    
    public IncludeProcess(Out out, Node node) {
        super("INCLUDE", node);
        
        this.out = out;
    }

    @Override
    public void Execute() {
        out.Write(Out.Type.NORMAL, "", TAG, "Se incluye un archivo XML a la lista de ejecución");
        
        // Verifica si está deprecada
        if(this.isDeprecated()){
            out.Write(Out.Type.E_DEPRECATED, "", TAG, "Esta función está marcada como deprecada y pronto podría no estar disponible");
        }
        
        // Verifica que contenga todos los parámetros obligatorios
        String obligatorios = "xml_file";
        String opcionales = "id,desc";
        
    }
    
}
