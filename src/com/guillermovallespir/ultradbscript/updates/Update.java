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
    
    public void add_repository(String id, String nombre, String descripcion, String url){
        String sql = "INSERT INTO `ser_servidor`\n"
                + "(`ser_servidor_id`, `ser_nombre`, `ser_descripcion`, `ser_url`)\n"
                + "VALUES ('" + id + "', '" + nombre + "', '" + descripcion + "', '" + url + "');";
        
        try {
            Statement st = conn.createStatement();
            
            st.executeQuery(sql);
            
        } catch (SQLException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
                            
                            String sql = "INSERT OR REPLACE INTO `paq_paquete` (`paq_paquete_id`, `paq_ser_servidor_id`, `paq_nombre`, `paq_descripcion`, `paq_version`, `paq_version_str`, `paq_fecha_version`)"
                                    + "VALUES ('" + e.getAttribute("id") + "' , '" + xml_files.get(i)[0] + "', '" + e.getAttribute("name")+ "', '" + e.getAttribute("desc")+ "');";
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
