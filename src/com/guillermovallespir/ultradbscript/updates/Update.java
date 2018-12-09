/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.updates;

import com.guillermovallespir.ultradbscript.Process.XML_Parse;
import com.guillermovallespir.ultradbscript.core.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

/**
 *
 * @author guille
 */
public class Update {
    private static final String URL_UDBS_DB = "jdbc:sqlite:./etc/updates.db";
    
    private boolean silence = false;
    
    private Connection conn = null;

    public Update(){      
        try {
            conn = DriverManager.getConnection(URL_UDBS_DB);
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String sql_servidores = "CREATE TABLE IF NOT EXISTS `ser_servidor` (\n"
                + "`ser_servidor_id` INT PRIMARY KEY,\n"
                + "`ser_nombre` VARCHAR(100) NOT NULL,\n"
                + "`ser_descripcion` TEXT NULL,\n"
                + "`ser_url` VARCHAR(255) NOT NULL,\n"
                + "`ser_estado` INT(2) NOT NULL DEFAULT '1',\n"
                + "`ser_ultima_actualizacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
                + "`ser_fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP\n"
                + ");";
        
        
        String sql_repositorios = "CREATE TABLE IF NOT EXISTS `paq_paquete` (\n"
                + "`paq_paquete_id` INT PRIMARY KEY,\n"
                + "`paq_ser_servidor_id` INT NOT NULL,\n"
                + "`paq_nombre` VARCHAR(100) NOT NULL,\n"
                + "`paq_descripcion` TEXT NULL,\n"
                + "`paq_version` INT NOT NULL,\n"
                + "`paq_version_str` VARCHAR(10) NOT NULL\n,"
                + "`paq_fecha_version` DATETIME NOT NULL\n"
                + ");";
        
        
        String sql_repo_instalados = "CREATE TABLE IF NOT EXISTS `pai_paquete_instalado` (\n"
                + "`pai_paquete_instalado_id` INT PRIMARY KEY,\n"
                + "`pai_paq_paquete_id` INT NOT NULL,\n"
                + "`pai_paq_version` INT NOT NULL,\n"
                + "`pai_paq_version_str` VARCHAR(10) NOT NULL,\n"
                + "`pai_paq_fecha_version` DATETIME NOT NULL,\n"
                + "`pai_fecha_actualizacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        
        if(conn != null){
            try {
                Statement st = conn.createStatement();
                
                st.execute(sql_servidores);
                st.execute(sql_repositorios);
                st.execute(sql_repo_instalados);
            } catch (SQLException ex) {
                Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setSilence(boolean silence){
        this.silence = silence;
    }
    
    public void add_repository(String id, String nombre, String descripcion, String url){
        String sql = "INSERT INTO `ser_servidor`\n"
                + "(`ser_servidor_id`, `ser_nombre`, `ser_descripcion`, `ser_url`)\n"
                + "VALUES ('" + id + "', '" + nombre + "', '" + descripcion + "', '" + url + "');";
        
        try {
            Statement st = conn.createStatement();
            
            //st.executeQuery(sql);
            
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void intUpdate(){
        // Primero se obtiene la lista de servidores de actualizaciones.
        
        // Crea la carpeta temp si no existe
        File file = new File("./temp/");
        file.mkdir();
        
        
        try {
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery("SELECT ser_servidor_id, ser_nombre, ser_url FROM ser_servidor WHERE ser_estado='1';");
            List<String> xml_files = new ArrayList<>();
            
            while(rs.next()){
                if(!silence)
                    System.out.print(ansi().fg(Ansi.Color.YELLOW).a("\t[" + rs.getString("ser_nombre") + "]").fg(Ansi.Color.DEFAULT).a(" - Descargando desde: " + rs.getString("ser_url")).reset());
                
                try {
                    URL url = new URL("http://200.74.119.116" + "/repo.xml");
                    String nombre = "./temp/" + Utils.uniqid("", true) + ".xml";
                    
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = new FileOutputStream(nombre);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    
                    xml_files.add(nombre);
                    
                    if(!silence)
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("\t[OK]").reset());
                    fos.close();
                    rbc.close();
                } catch (MalformedURLException ex) {
                    if(!silence)
                        System.err.println("\tPor alguna razón la URL del repositorio está mal formada\n" + ex);
                } catch (IOException ex) {
                    if(!silence)
                    System.err.println("\tOcurrió un error inesperado al obtener la data del repositorio\n\t" + ex);
                }
            }
            
            if(!silence)
                System.out.println("\tSe inicia la lectura de los repositorios remotos y match con los repositorios locales");
            for(int i = 0; i < xml_files.size(); i++){
                File f = new File(xml_files.get(i));
                XML_Parse xml_parse = new XML_Parse(null, f);
                
                f.delete();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
