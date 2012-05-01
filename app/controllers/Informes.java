package controllers;

import play.*;
import play.mvc.*;
import sun.text.resources.FormatData;
import sun.text.resources.FormatData_es_ES;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;



import models.*;
import controllers.*;

public class Informes extends  Controller {

	
  public static void informeAfiliados(String data) {
	    Map reportParams = new HashMap();
	    
	    System.out.println(data);
	    	       
	    renderBinary(utils.BaseJasperReports.download_pdf("name2",reportParams),"name2.pdf");		    
            
	  }
  
  
  public static void index() {
      render();
  }  
}
