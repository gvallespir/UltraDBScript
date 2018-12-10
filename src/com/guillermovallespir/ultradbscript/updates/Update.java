/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.updates;

import com.guillermovallespir.ultradbscript.Process.XML_Parse;
import com.guillermovallespir.ultradbscript.core.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
                + "`paq_version_str` VARCHAR(10) NOT NULL,\n"
                + "`paq_url` VARCHAR(255) NOT NULL,\n"
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
    
    public void install(Map<String, String> data){
        // Crea la carpeta temp si no existe
        File file = new File("./include/");
        file.mkdir();
        
        
        System.out.println("Se instalará el paquete " + data.get("nombre") + " v" + data.get("version_str"));
        System.out.println("\t- Se descargará el paquete del servidor `" + data.get("servidor_nombre") + "` (" + data.get("servidor_url") + ")");
        System.out.println("\t" + ansi().fg(Ansi.Color.YELLOW).a("[Descargando Paquete] ").fg(Ansi.Color.DEFAULT).a("Se inicia la descarga desde el servidor de repositorios . . .").reset());
        
        URL url = null;
        File f = null;
        try {
            url = new URL(data.get("servidor_url") + data.get("url"));
            f = new File(url.getFile());
            String nombre = "./include/" + f.getName();

            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(nombre);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("\t" + ansi().fg(Ansi.Color.YELLOW).a("[Instalando Paquete] ").fg(Ansi.Color.DEFAULT).a("UltraDBScript instalará el paquete descargado . . .").reset());
        
        
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream("./include/" + f.getName()));
            ZipEntry zentry = zip.getNextEntry();
            while(zentry != null){
                File newFile = new File("include/" + zentry.getName());
                
                if(zentry.isDirectory())
                    new File(newFile.getAbsolutePath()).mkdirs();
                else{
                    FileOutputStream fos = new FileOutputStream(newFile.getAbsolutePath());
                    int len;
                    while((len = zip.read(buffer)) > 0){
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zentry = zip.getNextEntry();
            }
            zip.closeEntry();
            zip.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new File("./include/" + f.getName()).delete();
        
        System.out.println("\t" + ansi().fg(Ansi.Color.YELLOW).a("[Actualizando Base de Datos] ").fg(Ansi.Color.DEFAULT).a("Se inicia la actualización de base de datos").reset());
        
        String sql = "INSERT INTO `pai_paquete_instalado` (`pai_paquete_instalado_id`, `pai_paq_paquete_id`, `pai_paq_version`, `pai_paq_version_str`, `pai_paq_fecha_version`)\n"
                + "VALUES ('" + data.get("paquete_id") + "', '" + data.get("paquete_id") + "', '" + data.get("version") + "', '" + data.get("version_str") + "', '" + data.get("fecha") + "');";
        
        try {
            Statement st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Map<String, String> getRepoInfo(String id){
        Map<String, String> data = new HashMap<>();
        
        String sql = "SELECT\n"
                + "`paq_paquete_id`,\n"
                + "`paq_ser_servidor_id`,\n"
                + "`paq_nombre`,\n"
                + "`paq_descripcion`,\n"
                + "`paq_version`,\n"
                + "`paq_version_str`,\n"
                + "`paq_url`,\n"
                + "`paq_fecha_version` AS `paq_fecha`,\n"
                + "`ser_servidor_id`,\n"
                + "`ser_nombre`,\n"
                + "`ser_url`\n"
                + "FROM `paq_paquete`\n"
                + "INNER JOIN `ser_servidor` ON `ser_servidor_id`=`paq_ser_servidor_id`"
                + "WHERE `paq_paquete_id`='" + id + "' AND `ser_estado`='1';";
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            rs.next();
            data.put("paquete_id", rs.getString("paq_paquete_id"));
            data.put("servidor_id", rs.getString("paq_ser_servidor_id"));
            data.put("nombre", rs.getString("paq_nombre"));
            data.put("descripcion", rs.getString("paq_descripcion"));
            data.put("version", rs.getString("paq_version"));
            data.put("version_str", rs.getString("paq_version_str"));
            data.put("url", rs.getString("paq_url"));
            data.put("fecha", rs.getString("paq_fecha"));
            data.put("servidor_nombre", rs.getString("ser_nombre"));
            data.put("servidor_url", rs.getString("ser_url"));
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    public void add_repository(String id, String nombre, String descripcion, String url){
        String sql = "INSERT INTO `ser_servidor`\n"
                + "(`ser_servidor_id`, `ser_nombre`, `ser_descripcion`, `ser_url`)\n"
                + "VALUES ('" + id + "', '" + nombre + "', '" + descripcion + "', '" + url + "');";
        
        try {
            Statement st = conn.createStatement();
            
            st.execute(sql);
            
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String[][] getListRepos(){
        String sql = "SELECT\n"
                + "`paq_paquete_id`,\n"
                + "`paq_nombre`,\n"
                + "`paq_version_str` AS `paq_version`,\n"
                + "`paq_fecha_version` AS `paq_fecha`,\n"
                + "CASE WHEN `pai_paq_paquete_id` IS NULL THEN 'No instalado' ELSE CASE WHEN paq_version > pai_paq_version THEN 'Requiere actualizar' ELSE 'Instalado' END END AS `paq_estado`\n"
                + "FROM `paq_paquete`\n"
                + "LEFT JOIN `pai_paquete_instalado` ON `pai_paq_paquete_id`=`paq_paquete_id`\n"
                + "ORDER BY `paq_paquete_id`;";
        
        Statement st;
        ArrayList<String[]> retorno = new ArrayList<>();
        
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            while(rs.next()){
                retorno.add(new String[]{rs.getString("paq_paquete_id"), rs.getString("paq_nombre"), rs.getString("paq_version"), rs.getString("paq_fecha"), rs.getString("paq_estado")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return (String[][]) retorno.toArray(new String[0][0]);
    }
    
    public String[][] getListServers(){
        String sql = "SELECT ser_servidor_id, ser_nombre, ser_url, ser_estado FROM ser_servidor ORDER BY ser_servidor_id";
        
        Statement st;
        ArrayList<String[]> retorno = new ArrayList<>();
        
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
       
            while(rs.next()){
                retorno.add(new String[]{rs.getString("ser_servidor_id"), rs.getString("ser_nombre"), rs.getString("ser_url"), rs.getString("ser_estado").equals("1") ? "On" : "Off"});
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (String[][]) retorno.toArray(new String[0][0]);
    }
    
    
    public void intUpdate(){
        // Primero se obtiene la lista de servidores de actualizaciones.
        
        // Crea la carpeta temp si no existe
        File file = new File("./temp/");
        file.mkdir();
        
        
        try {
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery("SELECT ser_servidor_id, ser_nombre, ser_url FROM ser_servidor WHERE ser_estado='1';");
            ArrayList<String[]> xml_files = new ArrayList<>();
            
            while(rs.next()){
                if(!silence)
                    System.out.print(ansi().fg(Ansi.Color.YELLOW).a("\t[" + rs.getString("ser_nombre") + "]").fg(Ansi.Color.DEFAULT).a(" - Descargando desde: " + rs.getString("ser_url")).reset());
                
                try {
                    URL url = new URL("http://200.74.119.116" + "/repo.xml");
                    String nombre = "./temp/" + Utils.uniqid("", true) + ".xml";
                    
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    FileOutputStream fos = new FileOutputStream(nombre);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    
                    xml_files.add(new String[]{rs.getString("ser_servidor_id"), nombre});
                    
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
                File f = new File(xml_files.get(i)[1]);
                
                Document document = null;
                try {    
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                    document = documentBuilder.parse(f);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(XML_Parse.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {

                } catch (IOException ex) {

                }
                
                document.getDocumentElement().normalize();
                
                Element element = document.getDocumentElement();
                
                NodeList nodos = element.getChildNodes();
                for(int x = 0; x < nodos.getLength(); x++){
                    if(nodos.item(x).getNodeType() == (Node.ELEMENT_NODE)){
                        Element e = (Element) nodos.item(x);
                        
                        if(e.getNodeName().equalsIgnoreCase("paquete")){
                            // Agrega si no existe, actualiza si existe
                            
                            String id = e.getAttribute("id");
                            String nombre = e.getAttribute("name");
                            String desc = e.hasAttribute("desc") ? e.getAttribute("desc") : "";
                            String version = e.getAttribute("version");
                            String version_str = e.getAttribute("version_str");
                            String url = e.getAttribute("url");
                            String date = e.getAttribute("date");
                            
                            String sql = "INSERT OR REPLACE INTO `paq_paquete` (`paq_paquete_id`, `paq_ser_servidor_id`, `paq_nombre`, `paq_descripcion`, `paq_version`, `paq_version_str`, `paq_url`, `paq_fecha_version`)"
                                    + "VALUES ('" + id + "' , '" + xml_files.get(i)[0] + "', '" + nombre + "', '" + desc + "', '" + version + "', '" + version_str + "', '" + url + "', '" + date + "');";
                            
                            st.execute(sql);
                        }
                    }
                }
                //f.delete();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
