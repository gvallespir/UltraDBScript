/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Structures;

import java.sql.Statement;

/**
 *
 * @author gvallespir
 */
public class DataBase {
    private String id;
    private String type;
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private Statement statement;
    
    public DataBase(String id, String type, String host, String port, String database, String user, String password, Statement statement){
        this.id = id;
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.statement = statement;
    }
    
    public boolean isMe(String id){
        return id.equals(id);
    }
    
    public String getId(){
        return this.id;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getHost(){
        return this.host;
    }
    
    public String getPort(){
        return this.port;
    }
    
    public String getDatabase(){
        return this.database;
    }
    
    public Statement getStatement(){
        return this.statement;
    }
}
