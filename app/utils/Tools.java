/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controllers.CRUD.ObjectType.ObjectField;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author T00159
 */
public class Tools {

    public static String getLocaleDateFormat(Date date) {

        java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        //DateFormat nf=DateFormat.getDateInstance();
        return nf.format(date);

    }

    public static Date getDataBaseDateFormat(String date) {
        try {
            java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return nf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static Date getAppDateFormat(String date) {
        try {
            java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return nf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static String dateToDateDataBaseFormat(String dateString) {

        java.text.SimpleDateFormat dataBaseFormatDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return dataBaseFormatDate.format(getAppDateFormat(dateString));

    }
}
