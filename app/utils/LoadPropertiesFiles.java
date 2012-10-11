/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Idioma;
import models.MensaxesIdioma;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import play.Play;
import play.vfs.VirtualFile;


/**
 *
 * @author t00159
 */
public class LoadPropertiesFiles {
    
   PropertiesConfiguration config;
   String confPath=Play.configuration.getProperty("conf.path");
   
    public  void loadMenasxeIdiomaProperties(MensaxesIdioma menidi)throws ConfigurationException{

        try {
            
            String nomeFile="messages."+menidi.idioma.sufixo;
            config = new PropertiesConfiguration(VirtualFile.fromRelativePath(confPath).getRealFile()+"/"+nomeFile);
            config.setProperty(menidi.clave,menidi.valor);
            config.save();             
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }              
}
    
  public  void loadAplicatonProperties(String clave,String value)throws ConfigurationException{

        try {
            
            String nomeFile="application.conf";
            config = new PropertiesConfiguration(VirtualFile.fromRelativePath(confPath).getRealFile()+"/"+nomeFile);
            config.setProperty(clave,value);
            config.save();             
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }              
}
  
    public  void loadMessagesIdioma(Idioma idioma){

        try {
       
            String nomeFile="messages."+idioma.sufixo;
            config = new PropertiesConfiguration(nomeFile);
            config.clear();
            List<MensaxesIdioma> menidiList=MensaxesIdioma.findAll();
            
            for (MensaxesIdioma menidi:menidiList){
                if(menidi.idioma.id==idioma.id){
                    
                    if(config.containsKey(menidi.clave)){
                        config.setProperty(menidi.clave,menidi.valor);
                    }else{
                        config.addProperty(menidi.clave,menidi.valor);
                    }
                    
                    config.save();    
                }
            }
                     
            config.setReloadingStrategy(new FileChangedReloadingStrategy());                

        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }              
}   
    
     public  void addMessagesIdioma(Idioma idioma){

        try {
            
            File f=null;      
            String nomeFile="messages."+idioma.sufixo;
            boolean fileExists=VirtualFile.fromRelativePath(confPath+nomeFile).getRealFile().exists();
                     
            
            if(!fileExists){
                f=new File (VirtualFile.fromRelativePath(confPath).getRealFile()+"/"+nomeFile);
                f.createNewFile();               
            }     
         
            config = new PropertiesConfiguration(VirtualFile.fromRelativePath(confPath).getRealFile()+"/"+nomeFile);            
            config.setEncoding("UTF-8");
            config.clear();
            List<MensaxesIdioma> menidiList=MensaxesIdioma.findAll();
            
            for (MensaxesIdioma menidi:menidiList){
                if(menidi.idioma.id==idioma.id){
                                       
                    if(config.containsKey(menidi.clave)){
                        config.setProperty(menidi.clave,menidi.valor);
                    }else{
                        config.addProperty(menidi.clave,menidi.valor);
                    }
                    
                    config.save();    
                }
            }
                     
            config.setReloadingStrategy(new FileChangedReloadingStrategy());                

        } catch (IOException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }  
     }
}
