/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Structures;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guillermo Vallespir Wood
 * @date 07-12-2018
 * @version 1.0
 */
public class Parameters {
    private static List<DataBase> DATABASE = new ArrayList<>();
    
    private static List<DataBase> FLASH_DATABASE = new ArrayList<>();
    
    private static List<Param> PARAM = new ArrayList<>();
    
    private static List<Param> FLASH_PARAM = new ArrayList<>();
    
    private static List<File> FILE = new ArrayList<>();
    
    private static List<ParamResult> RESULT = new ArrayList<>();
    
    
    public void addFile(String id, java.io.File file){
        FILE.add(new File(id, file));
    }
    
    
    public java.io.File getFile(String id){
        for(int i = 0; i < FILE.size(); i++){
            File file = (File) FILE.get(i);
            
            if(file.isMe(id)){
                return file.getFile();
            }
        }
        
        return null;
    }
    
    
    public void addResultParam(String id, ResultSet rs){
        RESULT.add(new ParamResult(id, rs));
    }
    
    
    public ResultSet getResultParam(String id){
        for(int i = 0; i < RESULT.size(); i++){
            ParamResult pr = (ParamResult) RESULT.get(i);
            
            if(pr.isMe(id))
                return pr.getValue();
        }
        
        return null;
    }
    
    
    public boolean removeResultParam(String id){
        for(int i = 0; i < RESULT.size(); i++){
            ParamResult pr = (ParamResult) RESULT.get(i);
            
            if(pr.isMe(id)){
                pr.close();
                RESULT.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    
    public void addFlashParam(String id, String value){
        FLASH_PARAM.add(new Param(id, value));
    }
    
    
    public String getFlashParam(String id){
        for(int i = 0; i < FLASH_PARAM.size(); i++){
            Param param = (Param) FLASH_PARAM.get(i);
            
            if(param.isMe(id)){
                String retorno = param.getValue();
                
                FLASH_PARAM.remove(i);
                
                return retorno;
            }
        }
        
        return null;
    }
    
    
    public void addFlashDataBase(String id, String type, String host, String port, String database, String user, String password, Connection connection){
        FLASH_DATABASE.add(new DataBase(id, type, host, port, database, user, password, connection));
    }
    
    
    public Statement getFlashDataBase(String id){
        for(int i = 0; i < FLASH_DATABASE.size(); i++){
            DataBase db = (DataBase) FLASH_DATABASE.get(i);
            
            if(db.isMe(id)){
                Statement retorno = db.getStatement();
                
                FLASH_PARAM.remove(i);
                
                return retorno;
            }
        }
        
        return null;
    }
    
    
    public void addParam(String id, String value){
        PARAM.add(new Param(id, value));
    }
    
    
    public String getValue(String id){
        for(int i = 0; i < PARAM.size(); i++){
            Param param = (Param) PARAM.get(i);
            
            if(param.isMe(id)){
                return param.getValue();
            }
        }
        
        return null;
    }
    
    
    public void addDataBase(String id, String type, String host, String port, String database, String user, String password, Connection connection){
        DATABASE.add(new DataBase(id, type, host, port, database, user, password, connection));
    }
    
    
    public static Statement getDataBase(String id){
        for(int i = 0; i < DATABASE.size(); i++){
            DataBase db = (DataBase) DATABASE.get(i);
            
            if(db.isMe(id)){
                return db.getStatement();
            }
        }
        
        return null;
    }
    
    
    public void closeAllDataBases(){
        int i;
        
        for(i = 0; i < DATABASE.size(); i++){
            DataBase db = (DataBase) DATABASE.get(i);
            Statement st = db.getStatement();
            
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(i = 0; i < FLASH_DATABASE.size(); i++){
            DataBase db = (DataBase) FLASH_DATABASE.get(i);
            Statement st = db.getStatement();
            
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
