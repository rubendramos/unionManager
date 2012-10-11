/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs;

import java.io.IOException;
import play.exceptions.UnexpectedException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class InitDb extends Job {

    private boolean isDone = false;
    private boolean isDoneWithError = false;

    @Override
    public void doJob() {
        try {
            Tools.initBD();

        } catch (IOException ex) {
            try {
                this.finalize();
                this.onException(ex);
                Logger.getLogger(InitDb.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex1) {
                Logger.getLogger(InitDb.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (SQLException ex) {
            try {
                this.finalize();
                this.onException(ex);
                Logger.getLogger(InitDb.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex1) {
                Logger.getLogger(InitDb.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
    }

    @Override
    public void onSuccess() {
        this.isDone = true;
    }

    @Override
    public void onException(Throwable e) {
        try {
            this.isDone = true;
            this.isDoneWithError = true;
            throw e;
        } catch (Throwable ex) {
            Logger.getLogger(InitDb.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnexpectedException(ex);
        }
    }

    public boolean isJobDone() {
        return isDone;
    }

    public boolean isJobDoneWithError() {
        return isDoneWithError;
    }
}
