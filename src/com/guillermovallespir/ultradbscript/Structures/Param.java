/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guillermovallespir.ultradbscript.Structures;

/**
 *
 * @author guille
 */
public class Param {
    private String id;
    private String value;
    
    public Param(String id, String value){
        this.id = id;
        this.value = value;
    }
    
    public boolean isMe(String id){
        return this.id.equals(id);
    }
    
    public String getValue(){
        return this.value;
    }
}
