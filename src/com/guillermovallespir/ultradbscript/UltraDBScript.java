package com.guillermovallespir.ultradbscript;

import com.guillermovallespir.ultradbscript.core.CommandLineParser;
import com.guillermovallespir.ultradbscript.core.Config;
import com.guillermovallespir.ultradbscript.core.Errors;
import java.util.Calendar;
import out.Out;

/**
 *
 * @author Guillermo Vallespir Wood
 * @date 03-12-2018
 * @version 1.0
 * 
 * Historial de Cambios
 * ====================
 * 1. 03-05-2018. Se inicia la creación de la clase
 */
public class UltraDBScript {
    
    public static void main(String[] args) {
        // Se le asigna la máxima prioridad de ejecución
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        
        
        // Se crea el objeto para el parse de datos por línea de comando
        CommandLineParser clp = new CommandLineParser(args);
        
        
        // Se carga la configuración
        Config config = new Config(clp.getConfigs());
        
        
        // Se carga la librería de manipulación de errores
        Errors errors = new Errors(config);
        
        
        // Se carga la librería de salidas por pantalla Out
        Out out = new Out(config);
        
        
        // Inicia la ejecución de UltraDBScript
        out.Write("******************************", true);
        out.Write("**    UltraDBScript v1.0    **", true);
        out.Write("******************************", true);
        out.Write("Ultra DBScript (r) - Todos los derechos reservados", false);
        out.Write("Ultra DBScript corriendo sobre " + System.getProperty("os.name") + " versión " + System.getProperty("os.version") + " arquitectura " + System.getProperty("os.arch"), false);
        out.Write("------------------------------------------- [ Ultra DBScript ] -------------------------------------------", false);
        out.Write("UltraDBScript iniciando " + Calendar.getInstance().getTime().toString(), false);
    }
    
}
