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
import com.martiansoftware.jsap.UnflaggedOption;

/**
 *
 * @author guille
 */
public class CommandLineParser {
    private JSAPResult result;
    
    public CommandLineParser(String[] args){
        JSAP jsap = new JSAP();


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


        // Se registran las entradas
        try {
            jsap.registerParameter(opt_configs);
            jsap.registerParameter(opt_files);
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
    
    public String[] getConfigs(){
        return result.getStringArray("configs");
    }
    
    public String[] getFiles(){
        return result.getStringArray("files");
    }
}
