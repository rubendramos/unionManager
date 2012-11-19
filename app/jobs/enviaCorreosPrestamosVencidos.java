/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.util.Date;
import java.util.List;
import models.Aviso;
import models.Fondo;
import models.PrestamoFondo;
import models.TipoEstadoAviso;
import notificacions.Notificador;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import play.Logger;
import play.db.jpa.GenericModel;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import utils.Tools;

/**
 *
 * @author ruben
 *
 */
@On("00 30 15 ? * *")
public class enviaCorreosPrestamosVencidos extends Job {

    @Override
    public void doJob() throws Exception {
        List<PrestamoFondo> pretamosVencidos = PrestamoFondo.getPrestamosVencidos();

        for (PrestamoFondo prestamoVencido : pretamosVencidos) {
            if (cumpreDemora(prestamoVencido)) {
                Notificador.notificacionPrestamoVencido(prestamoVencido);
            }
        }
    }

    private boolean cumpreDemora(PrestamoFondo pf) {
        boolean res = false;
        Fondo fondo = pf.entradaFondo.fondo;
        int demoraFondo = fondo.diasDemoraAvisoVencemento;
        if (demoraFondo > 0) {
            Date hoxe = Tools.getCurrentDate();
            Date dataVencemento = pf.getDataVencemento();
            long diasDiferencia = Tools.getDiasEntreDatas(hoxe, dataVencemento);
            long multipo = diasDiferencia % demoraFondo;
            Logger.debug("cupre demora ", Tools.getLocaleDateFormat(hoxe), Tools.getLocaleDateFormat(dataVencemento), diasDiferencia, multipo);
            if (diasDiferencia % demoraFondo == 0) {
                res = true;
            }
        }
        return res;

    }
}
