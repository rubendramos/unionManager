/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.util.List;
import models.Aviso;
import models.TipoEstadoAviso;
import notificacions.Notificador;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import play.db.jpa.GenericModel;
import play.jobs.Every;
import play.jobs.Job;
import utils.Tools;

/**
 *
 * @author ruben
 *
 */
@Every("1min")
public class lanzaAvisosEventos extends Job {

    @Override
    public void doJob() throws Exception {
        TipoEstadoAviso tea;
        Boolean envioCorrecto;
        List<Aviso> avisos = Aviso.findAvisosAEnviar();

        for (Aviso aviso : avisos) {
            
            envioCorrecto = Notificador.notificacionAviso(aviso);            
            
            if (envioCorrecto) {
                tea = (TipoEstadoAviso) TipoEstadoAviso.findById(Long.parseLong("2"));
            } else {
                tea = (TipoEstadoAviso) TipoEstadoAviso.findById(Long.parseLong("3"));
            }
            aviso.setDataRealizacionAviso(Tools.getCurrentDate());
            aviso.setEstadoAviso(tea);
            aviso._save();
        }

    }
}
