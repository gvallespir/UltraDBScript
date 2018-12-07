/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import out.Out;
import org.w3c.dom.Element;

/**
 *
 * @author gvallespir
 */
public class SQLProcess extends Process{
    private static final String TAG = "SQL";
    
    public SQLProcess(String file, Element element, Out out) {
        super(TAG, file, element, out);
    }

    @Override
    public void Execute() {
        this.out.Write(Out.Type.NORMAL, this.FILE, TAG, "Se ejecutará una instrucción SQL");
        
        
        // Verifica si está marcada como deprecada
        this.isDeprecated();
        
        
        String _ID = "id", _DESC = "desc", _DB = "db";
        String id = null, desc = null, db = null;
    }
    
}
