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


public class File {
    private String id;
    private java.io.File file;
    
    public File(String id, java.io.File file){
        this.id = id;
        this.file = file;
    }
    
    public boolean isMe(String id){
        return this.id.equals(id);
    }
    
    public java.io.File getFile(){
        return this.file;
    }
}
