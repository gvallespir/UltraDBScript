/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Structures;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gvallespir
 */
public class Parameters {
    private static List<DataBase> DATABASE = new ArrayList<>();
    
    
    public void addDataBase(String id, String type, String host, String port, String database, String user, String password, Statement statement){
        DATABASE.add(new DataBase(id, type, host, port, database, user, password, statement));
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
}
