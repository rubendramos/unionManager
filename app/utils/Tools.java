/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controllers.CRUD.ObjectType.ObjectField;
import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import play.Play;
import play.vfs.VirtualFile;

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
    
    public static String getDateNameFormat(Date date) {

        java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat("yyyyMMdd");
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

    public static String getFormatTimeStamp(Long time) {
        java.text.SimpleDateFormat timeStampFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return timeStampFormat.format(new Date(time));
    }

    public static String getFormatHour(Long time) {
        java.text.SimpleDateFormat timeStampFormat = new java.text.SimpleDateFormat("HH:mm");
        return timeStampFormat.format(new Date(time));
    }

    public static String dateToDateDataBaseFormat(String dateString) {

        java.text.SimpleDateFormat dataBaseFormatDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return dataBaseFormatDate.format(getAppDateFormat(dateString));

    }

    public static String generatePassword() {
        String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String password = "";
        Random r = new Random();
        for (int i = 1; i < 8; i++) {
            int index = r.nextInt(validChars.length());
            password = password + validChars.substring(index, index + 1);
        }
        return password;
    }

    public static String getCurrency(double money) {        
        NumberFormat formatter = NumberFormat.getCurrencyInstance();        
        return formatter.format(money);
    }

    
    
    public static Date getCurrentDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentDate = calendar.getTime();
            String cDate = getLocaleDateFormat(currentDate);
            return getAppDateFormat(cDate);
        } catch (Exception ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static void writeMessageInFile(String filePathName, String fileName, String logmsg) {

        File f = null;
        try {

            boolean fileExists = VirtualFile.fromRelativePath(filePathName + fileName).getRealFile().exists();

            if (fileExists) {
                f = VirtualFile.fromRelativePath(filePathName + fileName).getRealFile();
            } else {

                f = new File(VirtualFile.fromRelativePath(filePathName).getRealFile() + "/" + fileName);
                f.createNewFile();

            }


            FileWriter fstream = new FileWriter(f, true);
            BufferedWriter fbw = new BufferedWriter(fstream);
            fbw.write(logmsg);
            fbw.newLine();
            fbw.close();

        } catch (Exception ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void initBD() throws IOException,SQLException{
        
        File f = null;
        String scriptPath = Play.configuration.getProperty("initDBScripts.path");
        
        boolean fileExists = VirtualFile.fromRelativePath(scriptPath).getRealFile().exists();
        if (fileExists) {
            FileReader fr = null;
            try {


                f = VirtualFile.fromRelativePath(scriptPath).getRealFile();
                fr = new FileReader(f);
                ScriptRunner sr = new ScriptRunner(play.db.DB.getConnection(), true, true);
                sr.runScript(fr);
                


            } catch (IOException ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            } catch (SQLException ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
            }

        }
    }
    
    public static File getFileFromPath(String path){
        File f=null;
        boolean fileExists = VirtualFile.fromRelativePath(path).getRealFile().exists();
        if (fileExists) {          
                f = VirtualFile.fromRelativePath(path).getRealFile();
            }
    
        return f;
    }

    public static int countLinesFile(File file) throws IOException {
        
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    
    public static Date addDaysToDate(Date d,int days){
            return new Date(d.getTime() + (days * 24 * 3600 * 1000));
    }
    


    public static long getDiasEntreDatas(Date date1,Date date2){    
    long millsPerDay = 1000 * 60 * 60 * 24;    
    return ( date1.getTime() - date2.getTime() ) / millsPerDay;   
    }
    
}