/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package out;

import com.guillermovallespir.ultradbscript.core.Config;


/**
 *
 * @author guille
 */
public class OutBase {
    protected boolean terminal_color = true;
    protected boolean terminal_tables = true;
    
    public OutBase(Config config){
        terminal_color = config.getINIBoolean("Terminal", "terminal.color", terminal_color, true);
        terminal_tables = config.getINIBoolean("Terminal", "terminal.tables", terminal_tables, true);
    }
}
