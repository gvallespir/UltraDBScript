/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package out;

import com.bethecoder.ascii_table.ASCIITable;
import com.guillermovallespir.ultradbscript.core.Config;
import java.util.ArrayList;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

/**
 *
 * @author guille
 */
public class Out extends OutBase{
    
    public enum Type{
        NORMAL,
        E_ERROR,
        E_RECOVERABLE_ERROR,
        E_WARNING,
        E_STRICT,
        E_NOTICE,
        E_CORE_ERROR,
        E_CORE_WARNING,
        E_COMPILE_ERROR,
        E_COMPILE_WARNING,
        E_PARSE_ERROR,
        E_PARSE_WARNING,
        E_USER_ERROR,
        E_USER_WARNING,
        E_USER_NOTICE,
        E_DEPRECATED,
        E_USER_DEPRECATED
    }
    
    public Out(Config config) {
        super(config);
    }
    
    public void WriteTable(String[] headers, ArrayList<String[]> data) {
        ASCIITable.getInstance().printTable(headers, (String[][]) data.toArray(new String[0][0]));
    }
    
    public void Write(String text, boolean tab){
        if(tab)
            System.out.print("\t");
        System.out.println(text);
    }
    

    
    
    public void Write(Type type, String file, String tag, String text, boolean tab){
        if(tab)
            System.out.print("\t");
        
        if(terminal_color)
            switch(type){
                case NORMAL:
                    System.out.println(text);
                    break;
                case E_ERROR:
                    System.out.println(ansi().fg(Ansi.Color.RED).a("[ERROR FATAL] - " + text).reset());
                    System.out.println(ansi().fg(Ansi.Color.RED).a("La ejecución de UltraDBScript ha sido detenida a causa de un error fatal").reset());
                    System.exit(1);
                    break;
                case E_RECOVERABLE_ERROR:
                    System.out.println(ansi().fg(Ansi.Color.RED).a("[ERROR] - " + text).reset());
                    break;
                case E_WARNING:
                    System.out.println(ansi().fg(Ansi.Color.YELLOW).a("[WARNING] - " + text).reset());
                    break;
                case E_STRICT:
                    System.out.println(ansi().fg(Ansi.Color.BLUE).a("[STRICT] - " + text).reset());
                    break;
                case E_NOTICE:
                    System.out.println(ansi().fg(Ansi.Color.CYAN).a("[NOTICE] - " + text).reset());
                    break;
                case E_CORE_ERROR:
                    
                    break;
                case E_CORE_WARNING:
                    
                    break;
            }
        else
            switch(type){
                case NORMAL:
                    System.out.println(text);
                    break;
                case E_ERROR:
                    System.out.println("[ERROR FATAL] - " + text);
                    System.out.println("La ejecución de UltraDBScript ha sido detenida a causa de un error fatal");
                    System.exit(1);
                    break;
                case E_RECOVERABLE_ERROR:
                    System.out.println("[ERROR] - " + text);
                    break;
                case E_WARNING:
                    System.out.println("[WARNING] - " + text);
                    break;
                case E_STRICT:
                    System.out.println("[STRICT] - " + text);
                    break;
                case E_NOTICE:
                    System.out.println("[NOTICE] - " + text);
                    break;
            }
    }
}
