/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript;

import com.guillermovallespir.ultradbscript.core.CommandLineParser;
import com.guillermovallespir.ultradbscript.core.Config;
import java.util.Calendar;
import out.Out;

/**
 *
 * @author guille
 */
public class UltraDBScript {
    
    public static void main(String[] args) {
        // Se le asigna la máxima prioridad de ejecución
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        
        
        // Se crea la sentencia para el parse de línea de comando
        CommandLineParser clp = new CommandLineParser(args);
        
        
        
        // Se carga la configuración
        Config config = new Config(clp.getConfigs());
        
        
        
        Out out = new Out(config);
        out.Write("******************************", true);
        out.Write("**    UltraDBScript v1.0    **", true);
        out.Write("******************************", true);
        out.Write("Ultra DBScript (r) - Todos los derechos reservados", false);
        out.Write("Ultra DBScript corriendo sobre " + System.getProperty("os.name") + " versión " + System.getProperty("os.version") + " arquitectura " + System.getProperty("os.arch"), false);
        out.Write("------------------------------------------- [ Ultra DBScript ] -------------------------------------------", false);
        out.Write("UltraDBScript iniciando " + Calendar.getInstance().getTime().toString(), false);
    }
    
}
