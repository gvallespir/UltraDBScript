/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.core;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guille
 */
public class CommandLineParser {
    private JSAPResult result;
    private String[] args;
    
    public CommandLineParser(String[] args){
        JSAP jsap = new JSAP();
        this.args = args;


        UnflaggedOption opt_files = new UnflaggedOption("files")
                .setRequired(false)
                .setList(true)
                .setListSeparator(',')
                .setGreedy(true);
        opt_files.setHelp("Lista de archivos UDBSXML a ser ejecutados por UltraDBScript.");
        
        FlaggedOption opt_configs = new FlaggedOption("configs")
                .setAllowMultipleDeclarations(true)
                .setList(true)
                .setListSeparator(',')
                .setRequired(false)
                .setLongFlag("Config")
                .setShortFlag('c')
                .setUsageName("config");
        opt_configs.setHelp("Setea configuración manual a través de líneas de comando");
        
        FlaggedOption opt_add_servers = new FlaggedOption("add-server")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(false)
                .setLongFlag("add-server")
                .setShortFlag(JSAP.NO_SHORTFLAG);
        
         Switch opt_list_servers = new Switch("list-servers")
                .setLongFlag("list-servers");
         
         Switch opt_repo_cache = new Switch("repo-cache")
                 .setLongFlag("repo-cache");
        
        Switch opt_update = new Switch("update")
                .setLongFlag("update")
                .setShortFlag(JSAP.NO_SHORTFLAG);
        
        
        Switch opt_upgrade = new Switch("upgrade")
                .setLongFlag("upgrade")
                .setShortFlag(JSAP.NO_SHORTFLAG);
        
        FlaggedOption opt_install = new FlaggedOption("install")
                .setAllowMultipleDeclarations(false)
                .setList(true)
                .setRequired(false)
                .setListSeparator(' ')
                .setLongFlag("install")
                .setShortFlag(JSAP.NO_SHORTFLAG);
                


        // Se registran las entradas
        try {
            jsap.registerParameter(opt_configs);
            jsap.registerParameter(opt_files);
            jsap.registerParameter(opt_update);
            jsap.registerParameter(opt_upgrade);
            jsap.registerParameter(opt_add_servers);
            jsap.registerParameter(opt_list_servers);
            jsap.registerParameter(opt_repo_cache);
            jsap.registerParameter(opt_install);
        } catch (JSAPException ex) {
            System.out.println("");
        }
        
        
        
        result = jsap.parse(args);
        if((!result.userSpecified("add-server")) && (!result.userSpecified("list-servers")))
            if(!result.success()){
                System.err.println("UltraDBScript. Error Fatal. Error de parseo de parámetros por línea de comandos");
                System.err.println("UltraDBScript uso: " + jsap.getUsage());
                System.err.println();
                System.err.println(jsap.getHelp());
                System.exit(1);
        }
    }
    
    public boolean isInstall(){
        return result.userSpecified("install");
    }
    
    public String[] getInstallList(){
        return result.getStringArray("install");
    }
    
    public boolean isRepoCache(){
        return result.userSpecified("repo-cache");
    }
    
    public boolean isListServers(){
        return result.userSpecified("list-servers");
    }
    
    public boolean isAddServer(){
        return result.userSpecified("add-server");
    }
    
    public JSAPResult getAddServer(){
        JSAP jsap = new JSAP();
        
        Switch opt_add_server = new Switch("add-server")
                .setLongFlag("add-server");
        
        FlaggedOption opt_id = new FlaggedOption("id")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(true)
                .setLongFlag("id")
                .setShortFlag('i');
        
        FlaggedOption opt_nombre = new FlaggedOption("name")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(true)
                .setLongFlag("name")
                .setShortFlag('n');
        
        FlaggedOption opt_desc = new FlaggedOption("desc")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(false)
                .setLongFlag("desc")
                .setShortFlag('d');
        
        FlaggedOption opt_url = new FlaggedOption("url")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(true)
                .setLongFlag("url")
                .setShortFlag('u');
        
        try {
            jsap.registerParameter(opt_add_server);
            jsap.registerParameter(opt_id);
            jsap.registerParameter(opt_nombre);
            jsap.registerParameter(opt_desc);
            jsap.registerParameter(opt_url);
        } catch (JSAPException ex) {
            Logger.getLogger(CommandLineParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JSAPResult jres = jsap.parse(this.args);
        
        if(!jres.success()){
            System.err.println("[ERROR FATAL] - Add Server Error - Parseo de línea de comandos error");
            System.err.println("Add Server Uso: " + jsap.getUsage());
            System.exit(1);
        }
                
        return jres;
    }
    
    public boolean isUpdate(){
        return result.getBoolean("update");
    }
    
    public boolean isUpgrade(){
        return result.getBoolean("upgrade");
    }
    
    public String[] getConfigs(){
        return result.getStringArray("configs");
    }
    
    public String[] getFiles(){
        return result.getStringArray("files");
    }
}
