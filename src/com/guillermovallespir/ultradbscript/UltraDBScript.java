/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript;

import com.guillermovallespir.ultradbscript.core.Config;
import org.ini4j.Wini;
import out.Out;
import static out.Out.Type.*;

/**
 *
 * @author guille
 */
public class UltraDBScript {
    public static Wini CONFIG;
    
    public static void main(String[] args) {
        // Se carga la configuración
        Config config = new Config(args);
        
        Out out = new Out(config);
        out.Write("******************************", true);
        out.Write("**    UltraDBScript v1.0    **", true);
        out.Write("******************************", true);
        out.Write("Ultra DBScript (r) - Todos los derechos reservados", false);
        out.Write("Ultra DBScript corriendo sobre " + System.getProperty("os.name") + " versión " + System.getProperty("os.version") + " arquitectura " + System.getProperty("os.arch"), false);
        System.out.println();
        out.Write("Iniciando UltraDBScript v1.0", false);
        out.Write("============================", false);
        out.Write(E_ERROR, "Hola mundo error", true);
    }
    
}
