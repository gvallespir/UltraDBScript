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
public class Errors {
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
}
