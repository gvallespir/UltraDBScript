/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Process;

import com.guillermovallespir.ultradbscript.UltraDBScript;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.w3c.dom.NamedNodeMap;
import out.Out;

/**
 *
 * @author gvallespir
 */
public class DataBaseProcess extends Process{
    private static final String TAG = "DATABASE";
    
    public DataBaseProcess(String file, org.w3c.dom.Element element, Out out) {
        super(TAG, file, element, out);
        
        this.Documentacion =
        "El tag <DATABASE> permite efectuar una conexión a una base de datos.";
    }

    @Override
    public void Execute() {
        this.out.Write(Out.Type.NORMAL, this.FILE, TAG, "Conexión a una base de datos");
        
        
        // Verifica si está marcada como deprecada
        this.isDeprecated();
        
        
        String _ID = "id", _DESC = "desc", _TYPE1 = "type", _TYPE2 = "db_type", _HOST = "host", _PORT = "port", _DATABASE = "database",
                _USER = "user", _PASSWORD = "password";
        String id = null, desc = null, type = null, host = null, port = null, database = null, user = null, password = null;
        
        
        // Procesa los parámetros
        NamedNodeMap namedNodeMap = this.Element.getAttributes();
        for(int i = 0; i < namedNodeMap.getLength(); i++){
            String llave = namedNodeMap.item(i).getNodeName();
            String valor = namedNodeMap.item(i).getNodeValue();
            
            if(llave.compareToIgnoreCase(_ID) == 0){
                id = valor;
            }else if(llave.compareToIgnoreCase(_DESC) == 0){
                desc = valor;
            }else if(llave.compareToIgnoreCase(_TYPE1) == 0){
                type = valor;
            }else if(llave.compareToIgnoreCase(_TYPE2) == 0){
                type = valor;
            }else if(llave.compareToIgnoreCase(_HOST) == 0){
                host = valor;
            }else if(llave.compareToIgnoreCase(_PORT) == 0){
                port = valor;
            }else if(llave.compareToIgnoreCase(_DATABASE) == 0){
                database = valor;
            }else if(llave.compareToIgnoreCase(_USER) == 0){
                user = valor;
            }else if(llave.compareToIgnoreCase(_PASSWORD) == 0){
                password = valor;
            }
        }
        
        // Verifica que las etiquetas obligatorias tengan valor
        if(type == null)
            this.out.Write(Out.Type.E_PARSE_ERROR, FILE, TAG, "Falta el parámetro 'type o db_type'");
        if(host == null)
            this.out.Write(Out.Type.E_PARSE_ERROR, FILE, TAG, "Falta el parámetro 'host'");
        if(database == null)
            this.out.Write(Out.Type.E_PARSE_ERROR, FILE, TAG, "Falta el parámetro 'database'");
        if(user == null)
            this.out.Write(Out.Type.E_PARSE_ERROR, FILE, TAG, "Falta el parámetro 'user'");
        if(password == null)
            this.out.Write(Out.Type.E_PARSE_ERROR, FILE, TAG, "Falta el parámetro 'password'");
        
        
        // Se establecen las configuraciones por defecto si son necesarias
        if(port == null)
            port = "3306";
        
        // Todo parece correcto, se pinta la inforamción por pantalla
        this.WriteTable();
        
        
        try {
            // Se inicia la ejecución
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);

            
            UltraDBScript.PARAMS.addDataBase(id, type, host, port, database, user, password, conn);
            
        } catch (ClassNotFoundException ex) {
            this.out.Write(Out.Type.E_ERROR, FILE, TAG, "Ocurrió un error inesperado al realizar la conexión con la base de datos.\n" + ex.getLocalizedMessage());
        } catch (SQLException ex) {
            this.out.Write(Out.Type.E_ERROR, FILE, TAG, "La conexión a la base de datos no puedo realizarse.\n" + ex.getLocalizedMessage());
        }
    }
}
