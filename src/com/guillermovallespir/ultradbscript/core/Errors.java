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
    
    private String error_reporting;
    private boolean log_errors;
    private String error_log;
    private boolean display_errors;
    
    private boolean e_error = false;
    private boolean e_warning = false;
    private boolean e_notice = false;
    private boolean e_strict = false;
    private boolean e_core_error = false;
    private boolean e_core_warning = false;
    private boolean e_parse_error = false;
    private boolean e_parse_warning = false;
    private boolean e_user_error = false;
    private boolean e_user_warning = false;
    private boolean e_user_notice = false;
    private boolean e_deprecated = false;
    private boolean e_user_deprecated = false;
    private boolean e_recoverable_error = false;
    private boolean e_compile_error = false;
    private boolean e_compile_warning = false;
    
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
        E_USER_DEPRECATED,
        E_RECOVERABLE_ERROR,
        E_COMPILE_ERROR,
        E_COMPILE_WARNING
    }
    
    public Errors(Config config){
        errores = new ArrayList<>();

        error_reporting = config.get_error_reporting();
        log_errors = config.get_log_errors();
        error_log = config.get_error_log();
        display_errors = config.get_display_errors();
        
        
        if((display_errors == true) || (log_errors == true)){
            
            //**************
            // Establece la configuración para error_reporting
            String[] e_display = error_reporting.split(" ");
            
            for(int i = 0; i < e_display.length; i++){
                if(e_display[i].equals("E_ALL")){
                    e_error = e_warning = e_notice = e_strict = e_core_error = e_core_warning = e_parse_error = e_parse_warning = e_user_error = e_user_warning = e_user_notice = e_deprecated = e_user_deprecated = e_recoverable_error = true;
                }

                if(e_display[i].equals("~E_ALL")){
                    e_error = e_warning = e_notice = e_strict = e_core_error = e_core_warning = e_parse_error = e_parse_warning = e_user_error = e_user_warning = e_user_notice = e_deprecated = e_user_deprecated = e_recoverable_error = false;
                }

                if(e_display[i].equals("E_ERROR")){
                    e_error = e_core_error = e_parse_error = e_user_error = e_recoverable_error = true;
                }

                if(e_display[i].equals("~E_ERROR")){
                    e_error = e_core_error = e_parse_error = e_user_error = e_recoverable_error = false;
                }
                
                if(e_display[i].equals("E_RECOVERABLE_ERROR")){
                    e_recoverable_error = true;
                }
                
                if(e_display[i].equals("~E_RECOVERABLE_ERROR")){
                    e_recoverable_error = true;
                }
                
                if(e_display[i].equals("E_WARNING")){
                    e_warning = e_core_warning = e_parse_warning = e_user_warning = true;
                }
                
                if(e_display[i].equals("~E_WARNING")){
                    e_warning = e_core_warning = e_parse_warning = e_user_warning = false;
                }
                
                if(e_display[i].equals("E_PARSE")){
                    e_parse_error = true;
                }
                
                if(e_display[i].equals("~E_PARSE")){
                    e_parse_error = false;
                }
                
                if(e_display[i].equals("E_NOTICE")){
                    e_notice = e_user_notice = true;
                }
                
                if(e_display[i].equals("~E_NOTICE")){
                    e_notice = e_user_notice = false;
                }
                
                if(e_display[i].equals("E_STRICT")){
                    e_strict = true;
                }
                
                if(e_display[i].equals("~E_STRICT")){
                    e_strict = false;
                }
                
                if(e_display[i].equals("E_CORE_ERROR")){
                    e_core_error = true;
                }
                
                if(e_display[i].equals("~E_CORE_ERROR")){
                    e_core_error = false;
                }
                
                if(e_display[i].equals("E_COMPILE_ERROR")){
                    e_compile_error = true;
                }
                
                if(e_display[i].equals("~E_COMPILE_ERROR")){
                    e_compile_error = false;
                }
                
                if(e_display[i].equals("E_COMPILE_WARNING")){
                    e_compile_warning = true;
                }
                
                if(e_display[i].equals("~E_COMPILE_WARNING")){
                    e_compile_warning = false;
                }
                
                if(e_display[i].equals("E_USER_ERROR")){
                    e_user_error = true;
                }
                
                if(e_display[i].equals("~E_USER_ERROR")){
                    e_user_error = false;
                }
                
                if(e_display[i].equals("E_USER_WARNING")){
                    e_user_warning = true;
                }
                
                if(e_display[i].equals("~E_USER_WARNING")){
                    e_user_warning = false;
                }
            }
        }
    }
    
    public void addError(Type type, String file, String nodo, String error){
        
    }
}
