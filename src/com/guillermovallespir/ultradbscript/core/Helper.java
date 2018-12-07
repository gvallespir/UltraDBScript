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
public class Helper {
    private static String DEPRECATED_FUNCTIONS = "";
    
    public static boolean isDeprecatedFunction(String function){
        return DEPRECATED_FUNCTIONS.toLowerCase().contains(function.toLowerCase());
    }
}