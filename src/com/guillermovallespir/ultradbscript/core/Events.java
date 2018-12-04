/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.core;

/**
 *
 * @author gvallespir
 */
public class Events {
    public enum Type{
        NORMAL,
        E_ERROR,
        E_RECOVERABLE_ERROR,
        E_WARNING,
        E_STRICT,
        E_NOTICE,
        E_CORE_ERROR,
        E_CORE_WARNING,
        E_COMPILE_ERROR,
        E_COMPILE_WARNING,
        E_USER_ERROR,
        E_USER_WARNING,
        E_USER_NOTICE,
        E_DEPRECATED,
        E_USER_DEPRECATED
    }
    
    public void addEvent(Type type, String file, String nodo, String text){
        
    }
}
