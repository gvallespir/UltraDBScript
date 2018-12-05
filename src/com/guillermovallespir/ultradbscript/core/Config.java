/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ini4j.Wini;

/**
 *
 * @author guille
 */
public class Config {
    private static Wini CONFIG;
    
    private String user_ini_filename = ".user.ini";
    private int user_ini_cachettl = 300;
    
    private boolean engine = true;
    private boolean short_open_tag = false;
    private int precision = 14;
    private int output_buffering = 4096;
    private String output_handler = "";
    private String open_basedir = "";
    private String disable_functions = "config,globals";
    private String disable_classes = "";
    
    private boolean expose_ultradbscript = false;
    
    private int max_execution_time = 30;
    private int max_input_time = 60;
    private int max_params = 1000;
    private String memory_limit = "128M";
    
    private String error_reporting = "E_ALL ~E_NOTICE ~E_STRICT ~E_DEPRECATED";
    private boolean display_errors = false;
    private boolean display_startup_errors = false;
    private boolean log_errors = true;
    private int log_errors_max_len = 1024;
    private boolean ignore_repeated_errors = false;
    private boolean ignore_repeated_source = false;
    private boolean report_memleaks = true;
    private boolean track_error = false;
    private boolean xmlrpc_errors = false;
    private int xmlrpc_error_number = 0;
    private boolean html_errors = true;
    private String docref_root = "";
    private String docref_ext = ".html";
    private String error_prepend_string = "";
    private String error_append_string = "";
    private String error_log = "syslog";
    
    private String arg_separator_output = "&amp;";
    private String arg_separator_input = "&amp;";
    private String variables_order = "";
    private String super_global_order = "GC";
    private boolean register_args = false;
    private boolean auto_register_system_globals = true;
    private boolean enable_analyze_interface_data = false;
    private String max_analyze_interface_size = "10M";
    private String max_interface_size = "";
    private String auto_prepend_file = "";
    private String auto_append_file = "";
    private String default_content_type = "text/xml";
    private String default_charset = "UTF-8";
    private String internal_encoding = "";
    
    private String doc_root = "";
    private String user_dir = "";
    private String extension_dir = "./include/";
    private String sys_temp_dir = System.getProperty("java.io.tmpdir");
    
    private boolean file_downloads = true;
    private String download_tmp_dir = System.getProperty("java.io.tmpdir");
    private String download_max_filesize = "25M";
    private int max_file_download = 5;
    
    private final static String MATCHES_NONE = "[Nn]one";
    private final static String MATCHES_TRUE = "[Oo]n|[Tt]rue|[Yy]es|1";
    private final static String MATCHES_FALSE = "[Oo]ff|[Ff]alse|[Nn]o|0";
    private final static String ROOT = "ULTRA_DBSCRIPT";
    
    private String ARGS;
    
    private String evalString(String value){
        if(value.matches(MATCHES_NONE))
            return "";
        return value;
    }
    
    private int evalInt(String value){
        if(value.matches(MATCHES_NONE))
            return 0;
        return Integer.valueOf(value);
    }
    
    private Object eval(Object value){
        if(value instanceof String){
            if(((String) value).matches(MATCHES_NONE))
                return "";
            if(((String) value).matches(MATCHES_TRUE))
                return true;
            if(((String) value).matches(MATCHES_FALSE))
                return false;
            try{
                return Integer.valueOf((String) value);
            }catch(Exception e){
                
            }
            return (String) value;
        }
        
        if(value instanceof Boolean){
            return (boolean) value;
        }
        
        if(value instanceof Integer){
            return (int) value;
        }
        
        return value;
    }
    
    private boolean evalBool(String value){
        return value.matches(MATCHES_TRUE);
    }
    
    public String get_user_ini_filename(){
        return user_ini_filename;
    }
    
    public void set_user_ini_filename(String value){
        user_ini_filename = value;
    }
    
    public int get_user_ini_cachettl(){
        return user_ini_cachettl;
    }
    
    public void set_user_ini_cachettl(String value){
        user_ini_cachettl = (int) this.eval(value);
    }
    
    public boolean get_engine(){
        return engine;
    }
    
    public boolean get_short_open_tag(){
        return short_open_tag;
    }
    
    public int get_precision(){
        return precision;
    }
    
    public int get_output_buffering(){
        return output_buffering;
    }
    
    public String get_output_handler(){
        return output_handler;
    }
    
    public String get_open_basedir(){
        return open_basedir;
    }
    
    public String get_disable_functions(){
        return disable_functions;
    }
    
    public boolean is_disable_functions(String function){
        return disable_functions.contains(function);
    }
    
    public String get_disable_classes(){
        return disable_classes;
    }
    
    public boolean is_disable_classes(String class_name){
        return disable_classes.contains(class_name);
    }
    
    public boolean get_expose_ultradbscript(){
        return expose_ultradbscript;
    }
    
    public int get_max_execution_time(){
        return max_execution_time;
    }
    
    public int get_max_input_time(){
        return max_input_time;
    }
    
    public int get_max_params(){
        return max_params;
    }
    
    public Config(String[] args){
        // Primero se carga el archivo de configuración INI, o se intenta cargarlo
        
        // Primero verifica si existe el archivo en la carpeta etc del programa
        File f = new File("./etc/udbs.ini");
        if(f.exists()){
            this.cargarConfiguracion(args, f);
            return;
        }
        
        // Segundo verifica si existe el archivo en la carpeta del programa
        f = new File("./udbs.ini");
        if(f.exists()){
            this.cargarConfiguracion(args, f);
            return;
        }
        
        // Segundo verifica si existe el archivo en la carpeta del programa
        f = new File("./udbs.ini");
        if(f.exists()){
            this.cargarConfiguracion(args, f);
            return;
        }
    }
    
    private void cargarConfiguracion(String[] args, File f){
        try {
            CONFIG = new Wini(f);
            
            // Se concatenan los argumentos
            StringBuilder builder = new StringBuilder();
            for(String s : args) {
                builder.append(s);
            }
            ARGS = builder.toString();
            
            // Se parsea el archivo INI
            user_ini_filename = this.getINIString(ROOT, "user_ini.filename", user_ini_filename, true);
            user_ini_cachettl = this.getINIInt(ROOT, "user_ini.cachettl", user_ini_cachettl, true);
            
            engine = this.getINIBoolean(ROOT, "engine", engine, true);
            short_open_tag = this.getINIBoolean(ROOT, "short_open_tag", short_open_tag, true);
            precision = this.getINIInt(ROOT, "precision", precision, true);
            output_buffering = this.getINIInt(ROOT, "output_buffering", output_buffering, true);
            output_handler = this.getINIString(ROOT, "output_handler", output_handler, true);
            open_basedir = this.getINIString(ROOT, "open_basedir", open_basedir, true);
            disable_functions = this.getINIString(ROOT, "disable_functions", disable_functions, true);
            disable_classes = this.getINIString(ROOT, "disable_classes", disable_classes, true);
            
            expose_ultradbscript = this.getINIBoolean(ROOT, "expose_ultradbscript", expose_ultradbscript, true);
            
            max_execution_time = this.getINIInt(ROOT, "max_execution_time", max_execution_time, true);
            max_input_time = this.getINIInt(ROOT, "max_input_time", max_input_time, true);
            max_params = this.getINIInt(ROOT, "max_params", max_params, true);
            memory_limit = this.getINIString(ROOT, "memory_limit", memory_limit, true);
            
            error_reporting = this.getINIString(ROOT, "error_reporting", error_reporting, true);
            display_errors = this.getINIBoolean(ROOT, "display_errors", display_errors, true);
            display_startup_errors = this.getINIBoolean(ROOT, "display_startup_errors", display_startup_errors, true);
            log_errors = this.getINIBoolean(ROOT, "log_errors", log_errors, true);
            log_errors_max_len = this.getINIInt(ROOT, "log_errors_max_len", log_errors_max_len, true);
            ignore_repeated_errors = this.getINIBoolean(ROOT, "ignore_repeated_errors", ignore_repeated_errors, true);
            ignore_repeated_source = this.getINIBoolean(ROOT, "ignore_repeated_source", ignore_repeated_source, true);
            report_memleaks = this.getINIBoolean(ROOT, "report_memleaks", report_memleaks, true);
            track_error = this.getINIBoolean(ROOT, "track_error", track_error, true);
            xmlrpc_errors = this.getINIBoolean(ROOT, "xmlrpc_errors", xmlrpc_errors, true);
            xmlrpc_error_number = this.getINIInt(ROOT, "xmlrpc_error_number", xmlrpc_error_number, true);
            html_errors = this.getINIBoolean(ROOT, "html_errors", html_errors, true);
            docref_root = this.getINIString(ROOT, "docref_root", docref_root, true);
            docref_ext = this.getINIString(ROOT, "docref_ext", docref_ext, true);
            error_prepend_string = this.getINIString(ROOT, "error_prepend_string", error_prepend_string, true);
            error_append_string = this.getINIString(ROOT, "error_append_string", error_append_string, true);
            error_log = this.getINIString(ROOT, "error_log", error_log, true);
            
            arg_separator_output = this.getINIString(ROOT, "arg_separator.output", arg_separator_output, true);
            arg_separator_input = this.getINIString(ROOT, "arg_separator.input", arg_separator_input, true);
            variables_order = this.getINIString(ROOT, "variables_order", variables_order, true);
            super_global_order = this.getINIString(ROOT, "super_global_order", super_global_order, true);
            register_args = this.getINIBoolean(ROOT, "register_args", register_args, true);
            auto_register_system_globals = this.getINIBoolean(ROOT, "auto_register_system_globals", auto_register_system_globals, true);
            enable_analyze_interface_data = this.getINIBoolean(ROOT, "enable_analyze_interface_data", enable_analyze_interface_data, true);
            max_analyze_interface_size = this.getINIString(ROOT, "max_analyze_interface_size", max_analyze_interface_size, true);
            max_interface_size = this.getINIString(ROOT, "max_interface_size", max_interface_size, true);
            auto_prepend_file = this.getINIString(ROOT, "auto_prepend_file", auto_prepend_file, true);
            auto_append_file = this.getINIString(ROOT, "auto_append_file", auto_append_file, true);
            default_content_type = this.getINIString(ROOT, "default_content_type", default_content_type, true);
            default_charset = this.getINIString(ROOT, "default_charset", default_charset, true);
            internal_encoding = this.getINIString(ROOT, "internal_encoding", internal_encoding, true);
            
            doc_root = this.getINIString(ROOT, "doc_root", doc_root, true);
            user_dir = this.getINIString(ROOT, "user_dir", user_dir, true);
            extension_dir = this.getINIString(ROOT, "extension_dir", extension_dir, true);
            sys_temp_dir = this.getINIString(ROOT, "sys_temp_dir", sys_temp_dir, true);
            
            file_downloads = this.getINIBoolean(ROOT, "file_downloads", file_downloads, true);
            download_tmp_dir = this.getINIString(ROOT, "download_tmp_dir", download_tmp_dir, true);
            download_max_filesize = this.getINIString(ROOT, "download_max_filesize", download_max_filesize, true);
            max_file_download = this.getINIInt(ROOT, "max_file_download", max_file_download, true);
            
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String getINIString(String seccion, String dato, String valor, boolean def){
        // Verifica si viene en los argumentos
        if(ARGS.contains(dato)){
            Pattern p = Pattern.compile("/user_ini_cachettl=[\\s\\S]*?,/");
            Matcher m = p.matcher(ARGS);
            System.out.println(m.group(1));
            if(m != null){
                System.out.println(m.group(0));
                return m.group(1);
            }
        }
        if(CONFIG.get(seccion, dato) != null){
            if(CONFIG.get(seccion, dato).matches(MATCHES_NONE))
                return "";
            
            return CONFIG.get(seccion, dato).replaceAll("\"([^\"]*)\"", "$1");
        }else{
            if(def)
                return valor;
        }
        
        return "";
    }
    
    
    public int getINIInt(String seccion, String dato, int valor, boolean def){
        // Verifica si viene en los argumentos
        if(CONFIG.get(seccion, dato) != null){
            if(CONFIG.get(seccion, dato).matches(MATCHES_NONE))
                return 0;
            if(CONFIG.get(seccion, dato).matches(MATCHES_TRUE))
                return 1;
            if(CONFIG.get(seccion, dato).matches(MATCHES_FALSE))
                return 0;
            return Integer.valueOf(CONFIG.get(seccion, dato));
        }else{
            if(def)
                return valor;
            return 0;
        }
    }
    
    public boolean getINIBoolean(String seccion, String dato, boolean valor, boolean def){
        if(CONFIG.get(seccion, dato) == null){
            if(def)
                return valor;
            return false;
        }
        return CONFIG.get(seccion, dato).matches(MATCHES_TRUE);
    }
}
