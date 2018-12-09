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
        
        FlaggedOption opt_add_server = new FlaggedOption("add_server")
                .setAllowMultipleDeclarations(false)
                .setList(false)
                .setRequired(false)
                .setLongFlag("add_server")
                .setShortFlag(JSAP.NO_SHORTFLAG);
        
        Switch opt_update = new Switch("update")
                .setLongFlag("update")
                .setShortFlag(JSAP.NO_SHORTFLAG);
        
        
        Switch opt_upgrade = new Switch("upgrade")
                .setLongFlag("upgrade")
                .setShortFlag(JSAP.NO_SHORTFLAG);


        // Se registran las entradas
        try {
            jsap.registerParameter(opt_configs);
            jsap.registerParameter(opt_files);
            jsap.registerParameter(opt_update);
            jsap.registerParameter(opt_upgrade);
            jsap.registerParameter(opt_add_server);
        } catch (JSAPException ex) {
            System.out.println("");
        }
        
        
        
        result = jsap.parse(args);
        
        if(!result.success()){
            System.err.println("UltraDBScript. Error Fatal. Error de parseo de parámetros por línea de comandos");
            System.err.println("UltraDBScript uso: " + jsap.getUsage());
            System.err.println();
            System.err.println(jsap.getHelp());
            System.exit(1);
        }
    }
    
    public boolean isAddServer(){
        return result.userSpecified("add_server");
    }
    
    public String[] getAddServer(){
        StringBuilder argumentos = new StringBuilder();
        for(int i = 0; i < this.args.length; i++){
            argumentos.append(this.args[i]);
            argumentos.append(" ");
        }
        String args1 = argumentos.toString();
        args1 = args1.substring(args1.indexOf("--add_server") + 13);
        System.out.println(args1);
        return args1.split(" ");
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
