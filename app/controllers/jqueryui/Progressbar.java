package controllers.jqueryui;

import controllers.jqueryui.JQueryUI;
import models.jqueryui.InitDBProcess;

/**
 * Progressbar example.
 */
public class Progressbar extends JQueryUI {

   /**
    * Start a job, cache it and re-render the page
    */
   public static void startJob() {
      final InitDBProcess process = new InitDBProcess();
      process.now();
      final String processId = process.id;
      InitDBProcess.registry.put(processId, process);
      renderTemplate("jqueryui/Progressbar/index.html", processId);
   }
}
