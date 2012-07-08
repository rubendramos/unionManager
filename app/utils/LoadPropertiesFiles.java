/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Idioma;
import models.MensaxesIdioma;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;


/**
 *
 * @author t00159
 */
public class LoadPropertiesFiles {
    
   PropertiesConfiguration config;
   
    public  void loadProperties(MensaxesIdioma menidi)throws ConfigurationException{

        try {
       
            config = new PropertiesConfiguration("messages.es");
            config.setProperty(menidi.clave,menidi.valor);
            config.save();             
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }              
}
    
    
    public  void loadMessagesIdioma(Idioma idioma){

        try {
       
            config = new PropertiesConfiguration("messages.es");
            config.clear();
            List<MensaxesIdioma> menidiList=MensaxesIdioma.findAll();
            
            for (MensaxesIdioma menidi:menidiList){
                if(menidi.idioma.id==idioma.id){
                    
                    config.setProperty(menidi.clave,menidi.valor);
                    config.save();    
                }
            }
                     
            config.setReloadingStrategy(new FileChangedReloadingStrategy());                

        } catch (ConfigurationException ex) {
            Logger.getLogger(LoadPropertiesFiles.class.getName()).log(Level.SEVERE, null, ex);
        }              
}    
}
