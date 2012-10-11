package models.jqueryui;

import controllers.Secure;
import controllers.Seguridade;
import java.io.File;
import play.jobs.Job;
import play.libs.Codec;
import play.libs.F;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jobs.InitDb;
import play.Play;
import play.exceptions.UnexpectedException;
import play.libs.F.Promise;
import utils.Tools;

/**
 * Example job that publishes progress as an event stream.
 */
public class InitDBProcess extends Job implements Serializable {

    public static Map<String, InitDBProcess> registry = new HashMap<String, InitDBProcess>();
    public String id = Codec.UUID();
    /**
     * Event stream for events that report job completion percentage.
     */
    public F.EventStream<Integer> percentComplete = new F.EventStream<Integer>();

    public void doJob() {

        InitDb idb = new InitDb();


        long mediaMilesimadeSegundo = 3;

        try {
            // Report completion percentage every 100 ms.
            String scriptPath = Play.configuration.getProperty("initDBScripts.path");
            File f = Tools.getFileFromPath(scriptPath);
            int linesNumber = Tools.countLinesFile(f);
            //Promise pr = idb.now();
            idb.now();
            long sleepTime = (linesNumber * mediaMilesimadeSegundo) / 100;
            int i = 0;
            while (!idb.isJobDone()) {

                try {
                    //Thread.sleep(sleepTime);
                    i++;
                    Thread.sleep(sleepTime);

                    final int percent = i * 1;
                    this.percentComplete.publish(percent);

                } catch (InterruptedException e) {
                    this.percentComplete.publish(102);
                    Logger.getLogger(InitDBProcess.class.getName()).log(Level.SEVERE, null, e);
                } catch (Throwable ex) {
                    this.percentComplete.publish(102);

                    Logger.getLogger(InitDBProcess.class.getName()).log(Level.SEVERE, null, ex);

                }


            }

            if(idb.isJobDoneWithError()){
              this.percentComplete.publish(102);
            }else{
                this.percentComplete.publish(101);
            }



        } catch (UnexpectedException ex) {
            this.percentComplete.publish(102);

            Logger.getLogger(InitDBProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            this.percentComplete.publish(102);

            Logger.getLogger(InitDBProcess.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
