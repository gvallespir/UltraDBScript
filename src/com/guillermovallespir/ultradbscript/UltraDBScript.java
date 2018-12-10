package com.guillermovallespir.ultradbscript;

import com.guillermovallespir.ultradbscript.Process.XML_Parse;
import com.guillermovallespir.ultradbscript.Structures.Parameters;
import com.guillermovallespir.ultradbscript.core.CommandLineParser;
import com.guillermovallespir.ultradbscript.core.Config;
import com.guillermovallespir.ultradbscript.core.Errors;
import com.guillermovallespir.ultradbscript.updates.Update;
import com.martiansoftware.jsap.JSAPResult;
import java.io.File;
import java.util.Calendar;
import java.util.Map;
import static org.fusesource.jansi.Ansi.ansi;
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
        
        
        
        
        
        
        /******************
         * Verficación del tipo de inicio
         */
        if(clp.isUpdate()){
            // El arranque de UltraDBScript es de actualización los repositorios
            
            // Se inicia la conexión con la base de datos
            Update update = new Update();
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se inicia la actualización de UltraDBScript", false);
            out.Write(Out.Type.NORMAL, "", "UPDATE", "- Leyendo base de datos local, obteniendo paquetes instalados", true);
            out.Write(Out.Type.NORMAL, "", "UPDATE", "- Leyendo base de datos local, obteniendo lista de servidores de actualizaciones", true);
            out.Write(Out.Type.NORMAL, "", "UPDATE", "- Intentando conectar con los servidores de actualizaciones . . .", true);
            update.intUpdate();
            System.exit(0);
        }
        
        if(clp.isUpgrade()){
            // El arranque de UltraDBScript es de actualización de los paquetes
        }
        
        if(clp.isAddServer()){
            // El arranque de UltraDBScript es de agregación de repositorio de actualización
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se agrega un nuevo repositorio UltraDBScript", false);
            Update update = new Update();
            JSAPResult result = clp.getAddServer();
            out.WriteTable(new String[]{"ID", "Nombre", "Descripcion", "URL"}, new String[][]{{result.getString("id"), result.getString("name"), result.getString("desc"), result.getString("url")}});
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Escribiendo en la base de datos . . .", false);
            update.add_repository(result.getString("id"), result.getString("name"), result.getString("desc"), result.getString("url"));
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se inicia la actualización de los repositorios", false);
            update.intUpdate();
            System.exit(0);
        }
        
        if(clp.isListServers()){
            // El arranque de UltraDBScript es de agregación de repositorio de actualización
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se listan los repositorios de UltraDBScript", false);
            Update update = new Update();
            out.WriteTable(new String[]{"ID", "Nombre", "URL", "Estado"}, update.getListServers());
            System.exit(0);
        }
        
        if(clp.isRepoCache()){
            // El arranque de UltraDBScript muestra el cache de repositorios
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se lista el caché con los paquetes en cache (no necesariamente instalados)", false);
            Update update = new Update();
            out.WriteTable(new String[]{"ID", "Nombre", "Versión", "Fecha", "Estado"}, update.getListRepos());
            System.exit(0);
        }
        
        if(clp.isInstall()){
            // El arranque de UltraDBScript muestra el cache de repositorios
            out.Write(Out.Type.NORMAL, "", "UPDATE", "Se instalará un nuevo paquete en UltraDBScript", false);
            Update update = new Update();
            String[] installList = clp.getInstallList();
            for(int i = 0; i < installList.length; i++){
                out.Write(Out.Type.NORMAL, "", "UPDATE", "Buscando paquete `" + installList[i] + "´ en la cache de repositorios", false);
                Map<String,String> data = update.getRepoInfo(installList[i]);
                out.Write(Out.Type.NORMAL, "", "UPDATE", "Se encontró el paquete `" + data.get("nombre") + "´ versión " + data.get("version_str") + " con id " + installList[i], false);
                String user_opt = out.inString("¿Estás seguro que quieres instalar este paquete? [S/n]$ ");
                if(user_opt.matches("[S|s|Y|y]")){
                    update.install(data);
                }else{
                    out.Write(Out.Type.NORMAL, "", "UPDATE", "Ha sido cancelada la instalación del paquete " + data.get("nombre") + " v" + data.get("version_str"), false);
                }
            }
            
            System.exit(0);
        }
        
        
        
        
        
        /*********************
         * De todas maneras, aunque UltraDBScript no arranque en modo de actualización,
         * la base de datos se actualiza en segundo plano en modo silencioso
         */
        Thread thread_update = new Thread(){
            @Override
            public void run(){
                Update update = new Update();
                update.setSilence(true);
                update.intUpdate();
            }
        };
        thread_update.setPriority(Thread.MIN_PRIORITY);
        thread_update.start();
        
        
        
        
        
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
