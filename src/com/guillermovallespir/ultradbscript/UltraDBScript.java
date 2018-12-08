package com.guillermovallespir.ultradbscript;

import com.guillermovallespir.ultradbscript.Process.XML_Parse;
import com.guillermovallespir.ultradbscript.Structures.Parameters;
import com.guillermovallespir.ultradbscript.core.CommandLineParser;
import com.guillermovallespir.ultradbscript.core.Config;
import com.guillermovallespir.ultradbscript.core.Errors;
import java.io.File;
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
    public static Parameters PARAMS;
    
    public static void main(String[] args) {
        // Se le asigna la máxima prioridad de ejecución
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        
        
        // Se crea el objeto para el parse de datos por línea de comando
        CommandLineParser clp = new CommandLineParser(args);
        
        
        // Se carga la configuración
        Config config = new Config(clp.getConfigs());
        
        
        // Se carga la librería de manipulación de errores
        Errors errors = new Errors(config);
        
        
        // Se carga la librería para la manipulación de parámetros
        PARAMS = new Parameters();
        
        
        // Se carga la librería de salidas por pantalla Out
        Out out = new Out(config);
        
        
        // Inicia la ejecución de UltraDBScript
        out.Write("\t\t\t\t******************************", true);
        out.Write("\t\t\t\t**    UltraDBScript v1.0    **", true);
        out.Write("\t\t\t\t******************************", true);
        out.Write("Ultra DBScript (r) - Todos los derechos reservados", false);
        out.Write("Ultra DBScript corriendo sobre " + System.getProperty("os.name") + " versión " + System.getProperty("os.version") + " arquitectura " + System.getProperty("os.arch"), false);
        out.Write("---------------------------------------------- [ Ultra DBScript ] ----------------------------------------------", false);
        out.Write("UltraDBScript iniciando el " + Calendar.getInstance().getTime().toLocaleString().replace(" ", " a las ") + " horas", false);
        out.Write("Se inicia la lectura estructurada de los archivos XML / UDBSXML", false);
        
        
        // Se obtiene la lista de archivos XML / UDBSXML que serán procesados
        String[] archivos = clp.getFiles();
        
        
        // Verifica si existen archivos que leer
        if(archivos.length == 0){
            out.Write(Out.Type.E_ERROR, "", "ULTRADBSCRIPT", "No existen archivos XML / UDBSXML que procesar", false);
        }
        out.Write("- Lista de los archivos XML / UDBSXML que serán procesados", false);
        
        
        // Se recorren y procesan los archivos XML / UDBSXML
        for(int i = 0; i < archivos.length; i++){
            out.Write("[XML FILE] - Se inicia el procesamiento del archivo " + archivos[i], false);
            
            new XML_Parse(out, new File(archivos[i]));
        }
        
    }
    
}
