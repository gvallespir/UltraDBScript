/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo Vallespir Wood
 * @date 05-12-2018
 * @version 1.0
 */
public class Errors {
    private static List<Error> errores;
    
    public static enum Type{
        E_ERROR,
        E_WARNING,
        E_NOTICE,
        E_STRICT,
        E_CORE_ERROR,
        E_CORE_WARNING,
        E_PARSE_ERROR,
        E_PARSE_WARNING,
        E_USER_ERROR,
        E_USER_WARNING,
        E_USER_NOTICE,
        E_DEPRECATED,
        E_USER_DEPRECATED
    }
    
    public Errors(){
        errores = new ArrayList<>();
    }
    
    public void addError(Type type, String file, String nodo, String error){
        
    }
}
