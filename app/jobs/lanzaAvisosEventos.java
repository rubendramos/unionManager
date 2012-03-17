/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.util.List;
import models.Aviso;
import notificacions.Notificador;
import play.db.jpa.GenericModel;
import play.jobs.Every;
import play.jobs.Job;

/**
 *
 * @author ruben
 **/
 
 @Every("59min")
public class lanzaAvisosEventos extends Job {
     
       @Override
  public void doJob() throws Exception {
   List<Aviso> avisos= Aviso.findAll();
           for(Aviso aviso: avisos){
               if(!"Automático".equals(aviso.tipoAviso)){
               Notificador.notificacionXenerica(aviso);
               }
           }
           
  }
    
}
