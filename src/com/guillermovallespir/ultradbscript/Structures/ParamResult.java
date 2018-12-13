/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Structures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gvallespir
 */
public class ParamResult {
    public static String id;
    public static ResultSet result;
    
    public ParamResult(String id, ResultSet rs){
        this.id = id;
        this.result = rs;
    }
    
    public boolean isMe(String id){
        return this.equals(id);
    }
    
    public ResultSet getValue(){
        try {
            this.result.beforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(ParamResult.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.result;
    }
    
    public void close(){
        try {
            this.result.close();
        } catch (SQLException ex) {
            Logger.getLogger(ParamResult.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
