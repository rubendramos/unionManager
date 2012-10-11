/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import models.*;
import notificacions.Notificador;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.h2.util.IOUtils;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import play.Play;
import play.db.jpa.GenericModel;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import utils.CreateZipDirectoryRecursively;
import utils.Tools;
import utils.createTarFile;

/**
 *
 * @author ruben
 *
 */
@On("00 10 15 ? * MON-FRI")
//@On("0 0 12 * * ?") 
//@Every("1min")
public class backup extends Job {

    @Override
    public void doJob() throws Exception {
        
        try{
        String pathDocumentacion = Play.configuration.getProperty("backup.path");        

        String outputPath = Tools.getFileFromPath(pathDocumentacion).getPath();
        
        File output=new File(outputPath+"/"+getNomeFicheiroBackup());
        File folderData = Tools.getFileFromPath("/data");
        File folderDataBase = Tools.getFileFromPath("/db");

        ArrayList<File> folders = new ArrayList<File>();
        folders.add(folderData);
        folders.add(folderDataBase);

        createTarFile.compressFiles(folders, output);
        
        }catch(IOException e){
            sendNotificacionAdministrador(play.i18n.Messages.get("asunto.errorBackup"),
            play.i18n.Messages.get("contido.errorBackup"));
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
    }
        int diaDoMes=getDayOfMounth();
        if(diaDoMes==2 || diaDoMes==15){
            sendNotificacionAdministrador(play.i18n.Messages.get("asunto.exitoBackup"),
            play.i18n.Messages.get("contido.exitoBackup"));
        }
        
    }

    private String getNomeFicheiroBackup() {
        String nome = Play.configuration.getProperty("application.name");
        return nome + Tools.getDateNameFormat(Tools.getCurrentDate())+".tar.gz";
    }
    
    private void sendNotificacionAdministrador(String asunto,String contido){
        TipoPrioridade tp=TipoPrioridade.findById(Long.parseLong("1"));                
        Date data=Tools.getCurrentDate();
        User u= User.findById(Long.parseLong("1"));
        HashSet<User> hsu=new HashSet<User>();
        hsu.add(u);
        NotificacionInterna nt=new NotificacionInterna(asunto,contido,hsu,data,u,tp);
        nt._save();                
    }
    
    private int getDayOfMounth(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}

