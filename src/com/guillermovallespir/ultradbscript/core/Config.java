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
    
    public String get_user_ini_filename(){
        return user_ini_filename;
    }
    
    public int get_user_ini_cachettl(){
        return user_ini_cachettl;
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
            
            // Se parsea el archivo INI
            user_ini_filename = this.getINIString(CONFIG, ROOT, "user_ini.filename", user_ini_filename, true);
            user_ini_cachettl = this.getINIInt(CONFIG, ROOT, "user_ini.cachettl", user_ini_cachettl, true);
            
            engine = this.getINIBoolean(CONFIG, ROOT, "engine", engine, true);
            short_open_tag = this.getINIBoolean(CONFIG, ROOT, "short_open_tag", short_open_tag, true);
            precision = this.getINIInt(CONFIG, ROOT, "precision", precision, true);
            output_buffering = this.getINIInt(CONFIG, ROOT, "output_buffering", output_buffering, true);
            output_handler = this.getINIString(CONFIG, ROOT, "output_handler", output_handler, true);
            open_basedir = this.getINIString(CONFIG, ROOT, "open_basedir", open_basedir, true);
            disable_functions = this.getINIString(CONFIG, ROOT, "disable_functions", disable_functions, true);
            disable_classes = this.getINIString(CONFIG, ROOT, "disable_classes", disable_classes, true);
            
            expose_ultradbscript = this.getINIBoolean(CONFIG, ROOT, "expose_ultradbscript", expose_ultradbscript, true);
            
            max_execution_time = this.getINIInt(CONFIG, ROOT, "max_execution_time", max_execution_time, true);
            max_input_time = this.getINIInt(CONFIG, ROOT, "max_input_time", max_input_time, true);
            max_params = this.getINIInt(CONFIG, ROOT, "max_params", max_params, true);
            memory_limit = this.getINIString(CONFIG, ROOT, "memory_limit", memory_limit, true);
            
            error_reporting = this.getINIString(CONFIG, ROOT, "error_reporting", error_reporting, true);
            display_errors = this.getINIBoolean(CONFIG, ROOT, "display_errors", display_errors, true);
            display_startup_errors = this.getINIBoolean(CONFIG, ROOT, "display_startup_errors", display_startup_errors, true);
            log_errors = this.getINIBoolean(CONFIG, ROOT, "log_errors", log_errors, true);
            log_errors_max_len = this.getINIInt(CONFIG, ROOT, "log_errors_max_len", log_errors_max_len, true);
            ignore_repeated_errors = this.getINIBoolean(CONFIG, ROOT, "ignore_repeated_errors", ignore_repeated_errors, true);
            ignore_repeated_source = this.getINIBoolean(CONFIG, ROOT, "ignore_repeated_source", ignore_repeated_source, true);
            report_memleaks = this.getINIBoolean(CONFIG, ROOT, "report_memleaks", report_memleaks, true);
            track_error = this.getINIBoolean(CONFIG, ROOT, "track_error", track_error, true);
            xmlrpc_errors = this.getINIBoolean(CONFIG, ROOT, "xmlrpc_errors", xmlrpc_errors, true);
            xmlrpc_error_number = this.getINIInt(CONFIG, ROOT, "xmlrpc_error_number", xmlrpc_error_number, true);
            html_errors = this.getINIBoolean(CONFIG, ROOT, "html_errors", html_errors, true);
            docref_root = this.getINIString(CONFIG, ROOT, "docref_root", docref_root, true);
            docref_ext = this.getINIString(CONFIG, ROOT, "docref_ext", docref_ext, true);
            error_prepend_string = this.getINIString(CONFIG, ROOT, "error_prepend_string", error_prepend_string, true);
            error_append_string = this.getINIString(CONFIG, ROOT, "error_append_string", error_append_string, true);
            error_log = this.getINIString(CONFIG, ROOT, "error_log", error_log, true);
            
            arg_separator_output = this.getINIString(CONFIG, ROOT, "arg_separator.output", arg_separator_output, true);
            arg_separator_input = this.getINIString(CONFIG, ROOT, "arg_separator.input", arg_separator_input, true);
            variables_order = this.getINIString(CONFIG, ROOT, "variables_order", variables_order, true);
            super_global_order = this.getINIString(CONFIG, ROOT, "super_global_order", super_global_order, true);
            register_args = this.getINIBoolean(CONFIG, ROOT, "register_args", register_args, true);
            auto_register_system_globals = this.getINIBoolean(CONFIG, ROOT, "auto_register_system_globals", auto_register_system_globals, true);
            enable_analyze_interface_data = this.getINIBoolean(CONFIG, ROOT, "enable_analyze_interface_data", enable_analyze_interface_data, true);
            max_analyze_interface_size = this.getINIString(CONFIG, ROOT, "max_analyze_interface_size", max_analyze_interface_size, true);
            max_interface_size = this.getINIString(CONFIG, ROOT, "max_interface_size", max_interface_size, true);
            auto_prepend_file = this.getINIString(CONFIG, ROOT, "auto_prepend_file", auto_prepend_file, true);
            auto_append_file = this.getINIString(CONFIG, ROOT, "auto_append_file", auto_append_file, true);
            default_content_type = this.getINIString(CONFIG, ROOT, "default_content_type", default_content_type, true);
            default_charset = this.getINIString(CONFIG, ROOT, "default_charset", default_charset, true);
            internal_encoding = this.getINIString(CONFIG, ROOT, "internal_encoding", internal_encoding, true);
            
            doc_root = this.getINIString(CONFIG, ROOT, "doc_root", doc_root, true);
            user_dir = this.getINIString(CONFIG, ROOT, "user_dir", user_dir, true);
            extension_dir = this.getINIString(CONFIG, ROOT, "extension_dir", extension_dir, true);
            sys_temp_dir = this.getINIString(CONFIG, ROOT, "sys_temp_dir", sys_temp_dir, true);
            
            file_downloads = this.getINIBoolean(CONFIG, ROOT, "file_downloads", file_downloads, true);
            download_tmp_dir = this.getINIString(CONFIG, ROOT, "download_tmp_dir", download_tmp_dir, true);
            download_max_filesize = this.getINIString(CONFIG, ROOT, "download_max_filesize", download_max_filesize, true);
            max_file_download = this.getINIInt(CONFIG, ROOT, "max_file_download", max_file_download, true);
            
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getINIString(Wini f, String seccion, String dato, String valor, boolean def){
        if(f.get(seccion, dato) != null){
            if(f.get(seccion, dato).matches(MATCHES_NONE))
                return "";
            
            return f.get(seccion, dato).replaceAll("\"([^\"]*)\"", "$1");
        }else{
            if(def)
                return valor;
        }
        
        return "";
    }
    
    public String getINIString(String seccion, String dato, String valor, boolean def){
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
    
    public int getINIInt(Wini f, String seccion, String dato, int valor, boolean def){
        if(f.get(seccion, dato) != null){
            if(f.get(seccion, dato).matches(MATCHES_NONE))
                return 0;
            if(f.get(seccion, dato).matches(MATCHES_TRUE))
                return 1;
            if(f.get(seccion, dato).matches(MATCHES_FALSE))
                return 0;
            return Integer.valueOf(f.get(seccion, dato));
        }else{
            if(def)
                return valor;
            return 0;
        }
    }
    
    public int getINIInt(String seccion, String dato, int valor, boolean def){
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
    
    public boolean getINIBoolean(Wini f, String seccion, String dato, boolean valor, boolean def){
        if(f.get(seccion, dato) == null){
            if(def)
                return valor;
            return false;
        }
        return f.get(seccion, dato).matches(MATCHES_TRUE);
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
