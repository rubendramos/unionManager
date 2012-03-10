/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author T00159
 */
public class Tools {
    
    
    public static String getLocaleDateFormat(Date date){
                
        java.text.SimpleDateFormat nf=new java.text.SimpleDateFormat("dd/MM/yyyy");
        //DateFormat nf=DateFormat.getDateInstance();
        return nf.format(date);
        
    }
}
