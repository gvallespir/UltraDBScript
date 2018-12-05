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
        
        FlaggedOption opt_configs = new FlaggedOption("configs")
                .setAllowMultipleDeclarations(true)
                .setList(true)
                .setListSeparator(',')
                .setRequired(false)
                .setLongFlag("Config")
                .setShortFlag('c');


        // Se registran las entradas
        try {
            jsap.registerParameter(opt_files);
            jsap.registerParameter(opt_configs);
        } catch (JSAPException ex) {
            System.out.println("");
        }
        
        
        
        result = jsap.parse(args);
    }
    
    public String[] getConfigs(){
        return result.getStringArray("configs");
    }
}
